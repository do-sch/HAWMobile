package de.haw_landshut.hawmobile.mail;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.sun.mail.imap.AppendUID;
import com.sun.mail.imap.IMAPFolder;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.OnBackPressedListener;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;
import de.haw_landshut.hawmobile.base.EMailDao;
import de.haw_landshut.hawmobile.base.EMailFolder;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static de.haw_landshut.hawmobile.mail.MailEntryAdapter.ViewHolder.MESSAGE_ADAPTER_POSITION;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MailOverview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MailOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MailOverview extends Fragment implements View.OnClickListener, MailEntryAdapter.MailEntryClickListener, AdapterView.OnItemSelectedListener, OnBackPressedListener {

    static MailOverview instance;

    private int lastMessageNum = -1;
    private String currentFolderName;
    private boolean selectionMode = false;

    static final int MESSAGESAVECOUNT = 20;
    public static final String INBOX = "INBOX", DELETED = "Trash";

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFloatingActionButton;
    private SharedPreferences preferences;
    private RecyclerView mRecyclerView;
    private MailEntryAdapter mMailEntryAdapter;
    private ProgressBar mProgressBar;
//    private EMailDao eMailDao;
    private Snackbar mSnackBar;
    private Spinner mFolderIndicator;
    private TextView mSelectedCount;
    private View actionbarDefault, actionbarSelect;
//    private SearchView searchView;

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
        instance = this;
        this.setRetainInstance(true);
        this.setHasOptionsMenu(true);

//        if(eMailDao == null)
//            eMailDao = MainActivity.getHawDatabase().eMailDao();

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        MailService.schedulePeriodic(PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.currentFolderName = eMailFolders.get(i).getName();
        if (mMailEntryAdapter != null)
            mMailEntryAdapter.deselectAll();
        new Base2MailEntryAdapter().execute(currentFolderName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(MailEntryAdapter.Selectable selectable) {
        if (selectionMode) {
            if (selectable.isSelected()) {
                selectable.deselect();
                if (mMailEntryAdapter.getSelectedItemCount() == 0){

                    final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setCustomView(actionbarDefault);
                    }
                    selectionMode = false;
                    return;
                }
            } else
                selectable.select();

            mSelectedCount.setText(""+mMailEntryAdapter.getSelectedItemCount());

        } else {
            final EMail mail = selectable.getMail();
            mail.setSeen(true);
            new OpenMail().execute(mail);

            Intent intent = new Intent(getActivity(), MailView.class);
            intent.putExtra("mail", mail);
            intent.putExtra(MESSAGE_ADAPTER_POSITION, selectable.getAdapterPosition());
            startActivityForResult(intent, MailView.REQUEST_CODE);
//
//            mMailEntryAdapter.notifyItemChanged(selectable.getAdapterPosition());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MailView.REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                final int position = data.getIntExtra(MESSAGE_ADAPTER_POSITION, -1);
                final boolean delete = data.getBooleanExtra(MailView.DELETE, false);

                if (delete) {
                    new MoveToFolder(DELETED).execute(position);
                } else {
                    mMailEntryAdapter.notifyItemChanged(position);
                }

            }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemLongClick(MailEntryAdapter.Selectable selectable) {
        if(!selectionMode){
//            getActivity().invalidateOptionsMenu();
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setCustomView(actionbarSelect);
        }
        selectionMode = true;
        selectable.select();
        mSelectedCount.setText(""+mMailEntryAdapter.getSelectedItemCount());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("currentFolderName", currentFolderName);
        outState.putBoolean("selectionMode", selectionMode);
        outState.putString("selectedCount", mSelectedCount.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mail_overview, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);

        mRecyclerView = view.findViewById(R.id.mailsRecycleView);
        mProgressBar = view.findViewById(R.id.progressBar);

        if (savedInstanceState == null) {
            currentFolderName = INBOX;
            selectionMode = false;
        } else {
            currentFolderName = savedInstanceState.getString("currentFolderName");
            selectionMode = savedInstanceState.getBoolean("selectionMode");
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), RecyclerView.VERTICAL));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFloatingActionButton.isShown()) {
                    mFloatingActionButton.hide();
                } else if (dy < 0 && !mFloatingActionButton.isShown()) {
                    mFloatingActionButton.show();
                }
                //Lädt mehr E-Mails wenn am Ende des RecyclerViews
                if (dy > 0 && mProgressBar.getVisibility() == View.GONE) {
                    if (layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount()-1) {
                        mProgressBar.setVisibility(View.VISIBLE);

                        new FetchMessagesBefore().execute(currentFolderName);
                        Log.d("onScrolled", "Bottom visible");
                    }
                }
            }
        });

        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getLayoutPosition();
                if (currentFolderName.equals(DELETED))
                    new MoveToFolder(null).execute(pos);
                else
                    new MoveToFolder(DELETED).execute(pos);
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
//                new UpdateBase().execute();
                new Update().execute(currentFolderName);
            }
        });

        mFloatingActionButton = view.findViewById(R.id.createMail);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity(), MailCreateActivity.class);
                startActivity(intent);
            }
        });

        //Fügt E-Mails in die Liste ein

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(ab != null) {
            final LayoutInflater li = LayoutInflater.from(getActivity());

            actionbarDefault = li.inflate(R.layout.mailactionbar_defalut, null);
            actionbarSelect = li.inflate(R.layout.mailactionbar_selection, null);
            mSelectedCount = actionbarSelect.findViewById(R.id.mailSelectionCount);
            if (savedInstanceState != null)
                mSelectedCount.setText(savedInstanceState.getString("selectedCount"));

            if(selectionMode)
                ab.setCustomView(actionbarSelect);
            else
                ab.setCustomView(actionbarDefault);
            ab.setDisplayShowCustomEnabled(true);

//            searchView = actionbarDefault.findViewById(R.id.mailSearch);
//
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String s) {
//                    callSearch(s);
//                    searchView.clearFocus();//TODO: http://droidmentor.com/searchview-animation-like-whatsapp/
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String s) {
//                    callSearch(s);
//                    return true;
//                }
//
//                private void callSearch(final String query){
//
//                }
//            });

            final ImageView selectExitButton = actionbarSelect.findViewById(R.id.mailExitSelection);
            final ImageView deleteMailsButton = actionbarSelect.findViewById(R.id.mailDelete);
            final ImageView markMailsAsUnreadButton = actionbarSelect.findViewById(R.id.mailUnread);
            final ImageView moveMails = actionbarSelect.findViewById(R.id.mailMove);
            final ImageView settingsButton = actionbarDefault.findViewById(R.id.mailSettings);
            selectExitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deselectEverything();
                }
            });
            deleteMailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentFolderName.equals(DELETED))
                        new MoveToFolder(null).execute(mMailEntryAdapter.getAllSelectedMessagePositions());
                    else {
                        new MoveToFolder(DELETED).execute(mMailEntryAdapter.getAllSelectedMessagePositions());
                    }
                }
            });
            markMailsAsUnreadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MarkAsUnread().execute(mMailEntryAdapter.getAllSelectedMessagePositions());
                }
            });
            moveMails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    new MoveToFolder().execute();
                    final PopupMenu popup = new PopupMenu(getContext(), moveMails, Gravity.BOTTOM);
                    final Menu menu = popup.getMenu();
                    for (int i = 0, eMailFoldersSize = eMailFolders.size(); i < eMailFoldersSize; i++) {
                        EMailFolder emf = eMailFolders.get(i);
                        final String name = emf.getName();
                        if (!currentFolderName.equals(name)) {
                            final int stringName = getResources().getIdentifier(emf.getName(), "string", getActivity().getPackageName());
                            menu.add(0, i, 0, getActivity().getString(stringName));
                        }
                    }
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            new MoveToFolder(eMailFolders.get(menuItem.getItemId()).getName()).execute(mMailEntryAdapter.getAllSelectedMessagePositions());
                            return true;
                        }
                    });
                    popup.show();
                }
            });
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(getActivity(), MailSettingsActivity.class);
                    startActivity(intent);
                }
            });
//            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//                @Override
//                public boolean onClose() {
//                    settingsButton.setVisibility(View.VISIBLE);
//                    mFolderIndicator.setVisibility(View.VISIBLE);
//                    ab.setDisplayShowTitleEnabled(true);
//
//                    return false;
//                }
//            });
//            searchView.setOnSearchClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    settingsButton.setVisibility(View.GONE);
//                    mFolderIndicator.setVisibility(View.GONE);
//                    ab.setDisplayShowTitleEnabled(false);
//                }
//            });
        }

        mFolderIndicator = actionbarDefault.findViewById(R.id.mailFolder);

        new Base2MailEntryAdapter(savedInstanceState == null).execute(currentFolderName);

        super.onViewCreated(view, savedInstanceState);
    }

    void updateAdapter(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Base2MailEntryAdapter().execute(currentFolderName);
            }
        });
    }


    @Override
    public boolean onBackPressed() {
        if(selectionMode)
            deselectEverything();
//        else if (!searchView.isIconified())
//            searchView.setIconified(true);
        else return false;
        return true;
    }

    private synchronized Store getStore(){
        return Protocol.getStore();
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
    public void onResume() {
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
        }
        super.onResume();
    }

    @Override
    public void onPause() {

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(false);
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        new Logout().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void firstStart(){
//        new Update().execute(currentFolderName);
    }

    private void deselectEverything(){
        mMailEntryAdapter.deselectAll();
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setCustomView(actionbarDefault);
        selectionMode = false;
    }

    private void removeFromRecyclerView(final int id){

        mMailEntryAdapter.removeMessage(id);
        mMailEntryAdapter.notifyItemRemoved(id);
    }

    private boolean hasNetworkConnection(){
        final ConnectivityManager cm = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        final boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        synchronized (this) {
            if (mSnackBar == null)
                mSnackBar = Snackbar.make(mRecyclerView, R.string.offline_mode, Snackbar.LENGTH_INDEFINITE);

            if (!isConnected)
                mSnackBar.show();
            else
                mSnackBar.dismiss();
        }

        return isConnected;
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


//    /**
//     * Fetches all Mails and writes them into the Database
//     */
//    public class Mail2BaseTask extends AsyncTask<Void, Integer, Void>{
//        private static final int ID = 1;
//        private Activity activity;
//        private NotificationManager mNotifyManager;
//        private NotificationCompat.Builder mBuilder;
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            preferences.edit().putBoolean("mailsFetched", true).apply();
//            mBuilder.setContentText(activity.getResources().getString(R.string.downloaded))
//                    .setProgress(0, 0, false);
//            mNotifyManager.notify(ID, mBuilder.build());
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            final EMailDao eMailDao = MainActivity.getHawDatabase().eMailDao();
//            activity = MailOverview.this.getActivity();
//            mNotifyManager = ((NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE));
//            mBuilder = new NotificationCompat.Builder(activity);
//            mBuilder.setContentTitle(activity.getResources().getString(R.string.downloading))
//                    .setSmallIcon(R.drawable.mail_icon);
//
//
//            try {
//                Protocol.login();
//                Protocol.loadAllMessagesAndFolders(eMailDao, this/*, mMailEntryAdapter*/);
////                Protocol.logout();
//            } catch (MessagingException e){
//                final Activity mainActivity = MailOverview.this.getActivity();
//                mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, R.string.login_failed, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(final Integer... values) {
//            final int curr = values[0], max = values[1];
//            mBuilder.setProgress(max, curr, false);
//            mBuilder.setContentText(String.valueOf(curr + 1) + "/" + max);
//            mNotifyManager.notify(ID, mBuilder.build());
//        }
//
//        public void tellProgress(int curr, int max){
//            publishProgress(curr, max);
//        }
//    }

    private EMailDao getEMailDao(){
        return MainActivity.getHawDatabase(getContext()).eMailDao();
    }

    /**
     * Obtains all Emails from Database and creates an Adapter where the E-Mails will be inserted into
     */
    private class Base2MailEntryAdapter extends AsyncTask<String, Void, List<EMail>>{
        final boolean appstart;
        Base2MailEntryAdapter(){this.appstart = false;}
        Base2MailEntryAdapter(final boolean appstart) {this.appstart = appstart; }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final List<EMail> messages) {
            if(mMailEntryAdapter == null) {
                mMailEntryAdapter = new MailEntryAdapter(messages, MailOverview.this);

            }
            mRecyclerView.setAdapter(mMailEntryAdapter);
            mMailEntryAdapter.setMessages(messages);
            mMailEntryAdapter.notifyDataSetChanged();
            if (appstart) {
                new Base2Spinner(true).execute();
            }
        }

        @Override
        protected List<EMail> doInBackground(String... name) {

//            if(eMailFolders == null || eMailFolders.isEmpty())
//                eMailFolders = eMailDao.getAllEmailFolders();
            return getEMailDao().getAllEmailsFromFolder(name[0]);

        }
    }

    private class Base2Spinner extends AsyncTask<Void, Void, List<EMailFolder>>{
        boolean start = false;
        Base2Spinner(){}
        Base2Spinner(boolean start) {this.start = start;}
        @Override
        protected void onPostExecute(final List<EMailFolder> neweMailFolders) {
            if (neweMailFolders != null)
                eMailFolders = neweMailFolders;
            if (mFolderIndicator != null) {
                final List<String> items = new ArrayList<>();
                final Resources res = getActivity().getResources();
                int inboxPos = 0;
                for (int i = 0; i < eMailFolders.size(); i++) {
                    EMailFolder emf = eMailFolders.get(i);
                    if (emf.getName().equals(INBOX))
                        inboxPos = i;
                    final int stringName = res.getIdentifier(emf.getName(), "string", getActivity().getPackageName());
                    items.add(res.getString(stringName));
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.folder_item, items);
                adapter.setDropDownViewResource(R.layout.folder_item_dropdown);
                mFolderIndicator.setAdapter(adapter);
                mFolderIndicator.setSelection(inboxPos, false);
                mFolderIndicator.setOnItemSelectedListener(MailOverview.this);

//                if (start)
//                    new Update().execute(currentFolderName);
            }
        }

        @Override
        protected List<EMailFolder> doInBackground(Void... voids) {
            if (eMailFolders == null || eMailFolders.isEmpty())
                return getEMailDao().getAllEmailFolders();
            return null;
        }
    }

    private class Update extends AsyncTask<String, Void, List<EMail>>{
        boolean foldersChanged = false;

        @Override
        protected void onPreExecute() {
            if(!hasNetworkConnection())
                return;
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(final List<EMail> mails) {
            if(mails == null)
                return;
            mMailEntryAdapter.setMessages(mails);
            mMailEntryAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
            if (foldersChanged)
                new Base2Spinner().execute();
        }

        @Override
        protected List<EMail> doInBackground(String... strings) {

            final Store store = getStore();
            final EMailDao eMailDao = getEMailDao();
//            final List<EMailFolder> folderList = eMailDao.getAllEmailFolders();

            try {

                if(store == null){
                    Log.w("Update", "store is null");
                    return null;
                }

                final IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder(currentFolderName));
                if(!folder.isOpen())
                    folder.open(Folder.READ_ONLY);

                if(eMailFolders == null || eMailFolders.isEmpty()) {
                    foldersChanged = true;
                    eMailFolders = new ArrayList<>();
                    for (final Folder f : store.getDefaultFolder().list()) {
                        final IMAPFolder imapFolder = ((IMAPFolder) f);
                        final EMailFolder eMailFolder = new EMailFolder(imapFolder.getName(), imapFolder.getUIDValidity(), imapFolder.getUIDNext(), imapFolder.getMessageCount());
                        eMailFolders.add(eMailFolder);
                    }
                    eMailDao.insertAllFolders(eMailFolders);
                }
                Log.d("Update", eMailFolders.toString());

                final int messageCount = folder.getMessageCount();
                eMailDao.updateFolderStuff(currentFolderName, folder.getUIDNext(), folder.getUIDValidity());
                if(messageCount == 0) {
                    eMailDao.deleteAllEMailsFromFolder(currentFolderName);
                    return new ArrayList<>();
                }

                lastMessageNum = messageCount - MESSAGESAVECOUNT;
                if (lastMessageNum < 1)
                    lastMessageNum = 1;

                final Message[] messages = folder.getMessages(lastMessageNum, messageCount);

                final List<EMail> mails = new ArrayList<>();
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message m = messages[i];
                    final long uid = folder.getUID(m);
                    final EMail savedMail = eMailDao.getEmailFromUidAndFolder(uid, currentFolderName);
                    if (savedMail == null) {
                        final EMail eMail = new EMail(m, uid, currentFolderName);
                        mails.add(eMail);
                    } else {
                        final boolean mailSeen = m.isSet(Flags.Flag.SEEN);
                        if (mailSeen ^ savedMail.isSeen())
                            eMailDao.setEMailSeen(uid, currentFolderName, mailSeen);

                    }
                }

                eMailDao.insertAllEMails(mails);
                eMailDao.deleteLowestUIDMailsFromFolder(currentFolderName, MESSAGESAVECOUNT);

                folder.close(false);

                return eMailDao.getAllEmailsFromFolder(currentFolderName);

            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class MarkAsUnread extends AsyncTask<Integer, Void, Integer[]>{
        @Override
        protected void onPreExecute() {
            hasNetworkConnection();
        }

        @Override
        protected void onPostExecute(Integer[] integers) {
            if (integers == null)
                return;

            for (final int i : integers){
                mMailEntryAdapter.getMail(i).setSeen(false);
                mMailEntryAdapter.notifyItemChanged(i);
            }
            super.onPostExecute(integers);
        }

        @Override
        protected Integer[] doInBackground(Integer... integers) {

            final EMailDao eMailDao = getEMailDao();
            try{

                final Store store = Protocol.getStore();
                if (store == null)
                    return null;

                final IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder(currentFolderName));
                if (!folder.isOpen())
                    folder.open(Folder.READ_WRITE);

                final long[] uids = new long[integers.length];
                for (int i = 0; i < uids.length; i++){
                    uids[i] = mMailEntryAdapter.getMail(integers[i]).getUid();
                }

                final Message[] messagesToChange = folder.getMessagesByUID(uids);

                for (final Message m : messagesToChange){
                    m.setFlag(Flags.Flag.SEEN, false);
                }

                folder.close(true);

                for (final long uid : uids){

                    eMailDao.setEMailSeen(uid, currentFolderName, false);

                }

                return integers;

            } catch (MessagingException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    private class MoveToFolder extends AsyncTask<Integer, Void, Integer[]>{
        private final String emailFolderName;
        MoveToFolder(final String emailFolderName){
            this.emailFolderName = emailFolderName;
        }

        @Override
        protected void onPostExecute(Integer[] emPositions) {

            if (emPositions == null)
                return;

            for (int i = emPositions.length - 1; i >= 0; i--) {
                removeFromRecyclerView(emPositions[i]);
            }

            deselectEverything();

            super.onPostExecute(emPositions);
        }

        @Override
        protected Integer[] doInBackground(Integer... mailPositions) {

            final EMailDao eMailDao = getEMailDao();
            final Store store = getStore();

            try {
                final IMAPFolder currentFolder = ((IMAPFolder) store.getDefaultFolder().getFolder(currentFolderName));
                if(!currentFolder.isOpen()) {
                    currentFolder.open(Folder.READ_WRITE);
                }


                final long[] uids = new long[mailPositions.length];
                for (int i = 0; i < uids.length; i++){//TODO: nicht auf mMailEntryAdapter zugreifen sonder auf Datenbank
                    uids[i] = mMailEntryAdapter.getMail(mailPositions[i]).getUid();
                }

                final Message[] messages = currentFolder.getMessagesByUID(uids);

                if (emailFolderName != null){

                    final Folder otherFolder = store.getDefaultFolder().getFolder(emailFolderName);
                    if(!otherFolder.isOpen())
                        otherFolder.open(Folder.READ_WRITE);

                    final AppendUID[] appendUIDS = currentFolder.copyUIDMessages(messages, otherFolder);

                    for (int i = 0; i < uids.length; i++){
                        eMailDao.moveEMailToNewFolder(emailFolderName, currentFolderName, uids[i], appendUIDS[i].uid);
                    }

                    otherFolder.close();
                } else {

                    for (final long uid : uids) {
                        eMailDao.deleteEMail(currentFolderName, uid);
                    }

                }

                for (final Message m : messages)
                    m.setFlag(Flags.Flag.DELETED, true);

                currentFolder.close(true);

                return mailPositions;

            } catch (MessagingException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     *
     */
    private class FetchMessagesBefore extends AsyncTask<String, Void, Integer[]>{
        @Override
        protected void onPreExecute() {
            hasNetworkConnection();
        }

        @Override
        protected Integer[] doInBackground(String... string) {
            final Store store = getStore();
            final String foldername = string[0];

            if(store == null){
                return null;
            }

            try{

                final IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder(foldername));
                if(!folder.isOpen())
                    folder.open(Folder.READ_ONLY);

                if (lastMessageNum == -1) {
                    lastMessageNum = getEMailDao().getFolderMessageCount(foldername) - MESSAGESAVECOUNT;
                }

                final int lastMessage = lastMessageNum;
                lastMessageNum -= MESSAGESAVECOUNT;
                if(lastMessageNum < 1)
                    lastMessageNum = 1;


                //keine weiteren EMails
                if(lastMessage == lastMessageNum)
                    return null;

                final Message[] messages = folder.getMessages(lastMessageNum, lastMessage-1);
                final int oldMailAdapterSize = mMailEntryAdapter.getItemCount();
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message m = messages[i];
                    mMailEntryAdapter.addMessage(new EMail(m, folder.getUID(m), foldername));
                }

                folder.close(false);

                return new Integer[]{oldMailAdapterSize, messages.length};

            } catch (MessagingException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer[] integers) {
            mProgressBar.setVisibility(View.GONE);
            if(integers != null)
            mMailEntryAdapter.notifyItemRangeInserted(integers[0], integers[1]);
//                mMailEntryAdapter.notifyDataSetChanged();

        }
    }

    private class OpenMail extends AsyncTask<EMail, Void, Void>{

        @Override
        protected Void doInBackground(EMail... mail) {
            final Store store = getStore();
            final EMail eMail = mail[0];

            try {
                if (store == null) {
                    return null;
                }

                final IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder(currentFolderName));
                if(!folder.isOpen())
                    folder.open(Folder.READ_WRITE);

                final Message m = folder.getMessageByUID(eMail.getUid());

                m.setFlag(Flags.Flag.SEEN, true);
                getEMailDao().setEMailSeen(eMail.getUid(), currentFolderName, true);

                folder.close(true);

            } catch (MessagingException e){
                e.printStackTrace();
            }
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
