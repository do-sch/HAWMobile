package de.haw_landshut.hawmobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import de.haw_landshut.hawmobile.mail.MailOverview;

public class MainActivity extends AppCompatActivity implements MailOverview.OnFragmentInteractionListener {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_mail:
                    changeFragment(MailOverview.newInstance());
                    return true;
                case R.id.action_schedule:

                    return true;
                case R.id.action_map:

                    return true;
                case R.id.action_news:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeFragment(new MailOverview());
        handleLogin();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void changeFragment(Fragment fragment){
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

}
