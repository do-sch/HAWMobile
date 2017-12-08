package de.haw_landshut.hawmobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
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
import de.haw_landshut.hawmobile.schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements MailOverview.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener {

    private Fragment currentFragment, mailFragment, scheduleFragment/*, mapFragment, newsFragment*/;
    private static HAWDatabase hawDatabase;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_mail:
                    changeFragment(mailFragment);
                    return true;
                case R.id.action_schedule:
                    if (scheduleFragment == null)
                        scheduleFragment = ScheduleFragment.newInstance();
                    changeFragment(scheduleFragment);
                    return true;
                case R.id.action_map:
                    /*
                    if(mapFragment == null)
                        mapFragment = MapFragment.newInstance();
                    changeFragment(mapFragment)
                     */
                    return true;
                case R.id.action_news:
                    /*
                    if(newsFragment == null)
                        newsFragment = NewsFragment.newInstance();
                    changeFragment(newsFragment)
                     */
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mailFragment = MailOverview.newInstance();
        changeFragment(mailFragment);

        if(hawDatabase == null)
            hawDatabase = Room.databaseBuilder(getApplicationContext(), HAWDatabase.class, "haw").build();
        handleLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        hawDatabase.close();
        super.onDestroy();
    }

    private void changeFragment(Fragment fragment){
        currentFragment = fragment;
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
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

            Credentials.setCredentials(username, password, fa);

        }

    }

    public static HAWDatabase getHawDatabase(){
        return hawDatabase;
    }


}
