package de.haw_landshut.hawmobile;

import android.app.Application;
import com.evernote.android.job.JobManager;
import de.haw_landshut.hawmobile.mail.MailJobCreator;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new MailJobCreator());
    }
}
