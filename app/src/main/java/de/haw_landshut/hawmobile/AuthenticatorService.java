package de.haw_landshut.hawmobile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        HAWAuthenticator authenticator = new HAWAuthenticator(this);
        return authenticator.getIBinder();
    }
}
