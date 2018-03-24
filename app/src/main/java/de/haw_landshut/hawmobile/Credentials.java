package de.haw_landshut.hawmobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class Credentials {

    private static String username;
    private static String password;
    private static Fakultaet fakultaet;

    public static void loadCredentialsFromAccountManager(final Context context){

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("de.haw_landshut.hawmobile.ACCOUNT");

        if (accounts.length == 0)
            return;

        final Account a = accounts[0];
        final String username = a.name;
        final String password = accountManager.getPassword(a);
        final Fakultaet fa = Fakultaet.get(accountManager.getUserData(a, "FACULTY"));

        Credentials.setCredentialsForRuntime(username, password, fa);
    }

    public static void setCredentialsForRuntime(String username, String password, Fakultaet fakultaet){
        Credentials.username = username;
        Credentials.password = password;
        Credentials.fakultaet = fakultaet;

    }

    public static String getUsername(){
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static Fakultaet getFakultaet(){
        return fakultaet;
    }
}
