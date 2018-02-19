package de.haw_landshut.hawmobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import de.haw_landshut.hawmobile.base.HAWDatabase;
import de.haw_landshut.hawmobile.mail.MailOverview;
import de.haw_landshut.hawmobile.news.NewsOverview;
import de.haw_landshut.hawmobile.schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements MailOverview.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, NewsOverview.OnFragmentInteractionListener {

    private static HAWDatabase hawDatabase;
    private BottomNavigationView navigation;
    private OnBackPressedListener onBackPressedListener;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.content);
            switch (item.getItemId()) {
                case R.id.action_mail:
                    if (currentFragment.getClass() != MailOverview.class){
                        fragment = MailOverview.newInstance();
                    }
                    break;
                case R.id.action_schedule:
                    if (currentFragment.getClass() != ScheduleFragment.class)
                        fragment = ScheduleFragment.newInstance();
                    break;
                case R.id.action_map:
                    /*
                    if (currentFramgnet.getClass() != MapFragment.class)
                        fragment = MapFragment.newInstance();
                    break;
                     */
                    return true;
                case R.id.action_news:
                    if(currentFragment.getClass() != NewsOverview.class)
                        fragment = NewsOverview.newInstance();
                    break;
                default:
                    return false;
            }
            if(fragment != null)
                getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (savedInstanceState == null){
            ft.replace(R.id.content, MailOverview.newInstance());
            ft.commit();
        } else {
            navigation.setSelectedItemId(savedInstanceState.getInt("selectedNavID"));
        }

        if(hawDatabase == null || !hawDatabase.isOpen())
            hawDatabase = Room.databaseBuilder(getApplicationContext(), HAWDatabase.class, "haw").build();

        handleLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("selectedNavID", navigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        hawDatabase.close();
        super.onDestroy();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            final boolean handled = onBackPressedListener.onBackPressed();
            if (!handled)
                super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void setOnBackPressedListener(final OnBackPressedListener onBackPressedListener){
        this.onBackPressedListener = onBackPressedListener;
    }

    private void handleLogin(){

        //holt sich alle HAWMobile-Accounts auf dem Gerät
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("de.haw_landshut.hawmobile.ACCOUNT");
        if (accounts.length == 0){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {

            final Account a = accounts[0];
            final String username = a.name;
            final String password = accountManager.getPassword(a);
            final Fakultaet fa = Fakultaet.get(accountManager.getUserData(a, "FACULTY"));

            Credentials.setCredentialsForRuntime(username, password, fa);
        }

    }

    public static HAWDatabase getHawDatabase(){
        return hawDatabase;
    }

    public HAWDatabase getDatabase(){
        return hawDatabase;
    }


}
