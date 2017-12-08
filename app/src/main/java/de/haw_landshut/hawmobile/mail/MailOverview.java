package de.haw_landshut.hawmobile.mail;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;
import de.haw_landshut.hawmobile.base.EMailDao;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MailOverview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MailOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MailOverview extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SharedPreferences preferences;
    private RecyclerView mRecyclerView;
    private EMailDao eMailDao;

    public MailOverview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MailOverview.
     */
    public static MailOverview newInstance() {
        return new MailOverview();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

        eMailDao = MainActivity.getHawDatabase().eMailDao();

//        new MailInsertTask().execute();
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(!preferences.getBoolean("mailsFetched", false))
            new Mail2BaseTask().execute();

        new Base2MailEntryAdapter().execute();

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().setTitle(R.string.INBOX);
        menu.clear();
        inflater.inflate(R.menu.mailactionbar_default, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected", item.getTitle().toString());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail_overview, container, false);

        mRecyclerView = view.findViewById(R.id.mailsRecycleView);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), RecyclerView.VERTICAL));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class Mail2BaseTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            preferences.edit().putBoolean("mailsFetched", true).apply();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final EMailDao eMailDao = MainActivity.getHawDatabase().eMailDao();
            final EMail[] mailList = Protocol.loadAllMessages();

            if(mailList == null)
                return null;

            eMailDao.insertAllEMails(mailList);

            Log.d("MailOverview.M2BTask", "doInBackground: inserted");

            return null;
        }
    }

    private class Base2MailEntryAdapter extends AsyncTask<Void, Void, MailEntryAdapter>{
        @Override
        protected void onPostExecute(MailEntryAdapter mea) {
            mRecyclerView.setAdapter(mea);
        }

        @Override
        protected MailEntryAdapter doInBackground(Void... voids) {

            final List<EMail> mailList = eMailDao.getAllEmailsFromFolder("INBOX");

            if (mailList != null) {
                final MailEntry[] mails = MailEntry.getEntriesFromBase(mailList);
                Log.d("MailOverview.B2MEA", "doInBackground: mailsCount: " + mails.length);

                return new MailEntryAdapter(mails);

            }
            return null;
        }
    }
}
