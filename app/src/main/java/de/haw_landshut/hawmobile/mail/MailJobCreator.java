package de.haw_landshut.hawmobile.mail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class MailJobCreator implements JobCreator{

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case MailService.TAG:
                return new MailService();

            default:
            return null;
        }
    }
}
