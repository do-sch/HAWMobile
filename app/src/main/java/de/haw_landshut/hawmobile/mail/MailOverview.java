package de.haw_landshut.hawmobile.mail;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.*;
import android.widget.PopupMenu;
import android.widget.Toast;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;
import de.haw_landshut.hawmobile.base.EMailDao;
import de.haw_landshut.hawmobile.base.EMailFolder;

import javax.mail.MessagingException;
import javax.mail.Store;
import java.util.List;

import static de.haw_landshut.hawmobile.mail.MailEntryAdapter.ViewHolder.MESSAGE_FNA;
import static de.haw_landshut.hawmobile.mail.MailEntryAdapter.ViewHolder.MESSAGE_UID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MailOverview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MailOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MailOverview extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFloatingActionButton;
    private SharedPreferences preferences;
    private RecyclerView mRecyclerView;
    private MailEntryAdapter mMailEntryAdapter;
    private EMailDao eMailDao;

    private Store store;
    private List<EMailFolder> eMailFolders;

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

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        //TODO: Verhindern, dass alle E-Mail heruntergeladen werden
        if(!preferences.getBoolean("mailsFetched", false))
            new Mail2BaseTask().execute();

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
        switch (item.getItemId()){
            case R.id.mailFolder:
                if(eMailFolders == null)
                    break;
                final View folderButton = getActivity().findViewById(R.id.mailFolder);
                final PopupMenu popupMenu = new PopupMenu(getActivity(), folderButton);
                for(EMailFolder ef : eMailFolders){
                    final Intent it = new Intent();
                    final Resources res = getActivity().getResources();
                    final int stringName = res.getIdentifier(ef.getName(), "string", getActivity().getPackageName());
                    it.putExtra("FolderName", ef.getName());
                    it.putExtra("StringName", stringName);
                    popupMenu.getMenu().add(stringName).setIntent(it);
                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        final Intent intent = menuItem.getIntent();
        getActivity().setTitle(intent.getIntExtra("StringName", R.string.undefined));
        new Base2MailEntryAdapter().execute(intent.getStringExtra("FolderName"));
        return false;
    }

    @Override
    public void onClick(View view) {
        Log.d("onClick", view.toString());
    }

    public void onMessageClicked(final EMail mail, int messagePos){
        mail.setSeen(true);
        new MarkAsSeen().execute(mail);

        mMailEntryAdapter.notifyItemChanged(messagePos);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail_overview, container, false);

        mRecyclerView = view.findViewById(R.id.mailsRecycleView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), RecyclerView.VERTICAL));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && mFloatingActionButton.isShown()){
                    mFloatingActionButton.hide();
                } else if (dy < 0 && !mFloatingActionButton.isShown()) {
                    mFloatingActionButton.show();
                }
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO: E-Mail löschen
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((MailEntryAdapter.ViewHolder) viewHolder).viewForeground;

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((MailEntryAdapter.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((MailEntryAdapter.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);
            }
        };
        final ItemTouchHelper ith = new ItemTouchHelper(simpleCallback);
        ith.attachToRecyclerView(mRecyclerView);

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new UpdateBase().execute();
            }
        });

        mFloatingActionButton = view.findViewById(R.id.createMail);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Fügt E-Mails in die Liste ein
        new Base2MailEntryAdapter().execute("INBOX");

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Logout().execute();
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
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Fetches all Mails and writes them into the Database
     */
    public class Mail2BaseTask extends AsyncTask<Void, Integer, Void>{
        private static final int ID = 1;
        private Activity activity;
        private NotificationManager mNotifyManager;
        private NotificationCompat.Builder mBuilder;
        @Override
        protected void onPostExecute(Void aVoid) {
            preferences.edit().putBoolean("mailsFetched", true).apply();
            mBuilder.setContentText(activity.getResources().getString(R.string.downloaded))
                    .setProgress(0, 0, false);
            mNotifyManager.notify(ID, mBuilder.build());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final EMailDao eMailDao = MainActivity.getHawDatabase().eMailDao();
            activity = MailOverview.this.getActivity();
            mNotifyManager = ((NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE));
            mBuilder = new NotificationCompat.Builder(activity);
            mBuilder.setContentTitle(activity.getResources().getString(R.string.downloading))
                    .setSmallIcon(R.drawable.mail_icon);


            try {
                Protocol.login();
                Protocol.loadAllMessagesAndFolders(eMailDao, this/*, mMailEntryAdapter*/);
//                Protocol.logout();
            } catch (MessagingException e){
                final Activity mainActivity = MailOverview.this.getActivity();
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, R.string.login_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            final int curr = values[0], max = values[1];
            mBuilder.setProgress(max, curr, false);
            mBuilder.setContentText(String.valueOf(curr + 1) + "/" + max);
            mNotifyManager.notify(ID, mBuilder.build());
        }

        public void tellProgress(int curr, int max){
            publishProgress(curr, max);
        }
    }

    /**
     * Obtaines all Emails from Database and creates an Adapter where the E-Mails will be inserted into
     */
    private class Base2MailEntryAdapter extends AsyncTask<String, Void, MailEntryAdapter>{
        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final MailEntryAdapter mea) {
            mMailEntryAdapter = mea;
            mRecyclerView.setAdapter(mea);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected MailEntryAdapter doInBackground(String... name) {

            eMailFolders = eMailDao.getAllEmailFolders();
            final List<EMail> mailList = eMailDao.getAllEmailsFromFolder(name[0]);

            return new MailEntryAdapter(mailList, MailOverview.this);
        }
    }

    /**
     * Updates The Database with new E-Mails from the Mail-Server
     */
    private class UpdateBase extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            new Base2MailEntryAdapter().execute("INBOX");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Protocol.login();
                Protocol.updateAllFolders(eMailDao);
//                Protocol.logout();
                eMailFolders = eMailDao.getAllEmailFolders();
            } catch (MessagingException e){
                Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }

    private class MarkAsUnread extends AsyncTask<EMail, Void, Void>{
        @Override
        protected Void doInBackground(final EMail... eMails) {

            try {
                Protocol.login();
                Protocol.markAsUnread(eMailDao, eMails);
//                Protocol.logout();
            } catch (MessagingException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    public class MarkAsSeen extends AsyncTask<EMail, Void, Void>{

        @Override
        protected Void doInBackground(EMail... email) {

            try {
                Protocol.login();

                Protocol.markAsSeen(eMailDao, email[0].getUid(), email[0].getFoldername());


            } catch (MessagingException e) {
                e.printStackTrace();
            }


            Intent intent = new Intent(getActivity(), MailView.class);
            intent.putExtra(MESSAGE_UID, email[0].getUid());
            intent.putExtra(MESSAGE_FNA, email[0].getFoldername());
            startActivity(intent);

            return null;
        }
    }

    private class Logout extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
        try {
            Protocol.logout();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
            return null;
        }
    }

}
