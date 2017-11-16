package de.haw_landshut.hawmobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import de.haw_landshut.hawmobile.mail.MailOverviewActivity;
import de.haw_landshut.hawmobile.mail.Protocol;
import de.haw_landshut.hawmobile.schedule.ScheduleActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Intent in;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_mail:
                    in = new Intent(getBaseContext(), MailOverviewActivity.class);
                    startActivity(in);
                    return true;
                case R.id.action_schedule:
                    in = new Intent(getBaseContext(), ScheduleActivity.class);
                    startActivity(in);
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        handleLogin();
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

            if (Protocol.testAccount(username, password))
                Credentials.setCredentials(username, password, fa);
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.login_failed), 3000);
            }

        }

    }

}
