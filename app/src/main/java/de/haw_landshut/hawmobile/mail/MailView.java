package de.haw_landshut.hawmobile.mail;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;
import de.haw_landshut.hawmobile.base.EMailDao;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

public class MailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_view);

        final Intent intent = getIntent();
        final long uid = intent.getLongExtra(MailEntryAdapter.ViewHolder.MESSAGE, -1);

        new InsertEMail().execute(uid);

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private class InsertEMail extends AsyncTask<Long, Void, EMail>{
        @Override
        protected void onPostExecute(EMail mail) {
            final ActionBar actionBar = getSupportActionBar();
            if(actionBar == null)
                Log.d("onPostExecute", "actionBar is null");
            actionBar.setTitle(mail.getSubject());
            actionBar.setDisplayHomeAsUpEnabled(true);

            final WebView wv = findViewById(R.id.mailWebView);

            ContentType contentType;
            try {
                contentType = new ContentType(mail.getEncoding());
            } catch (ParseException e) {
                e.printStackTrace();
                ParameterList pl = new ParameterList();
                pl.set("encoding", "utf-8");
                contentType = new ContentType("text", "html", pl);
            }

            wv.loadDataWithBaseURL(null, mail.getText(), contentType.getBaseType(), contentType.getParameter("encoding"), null);

            Log.d("encoding", mail.getEncoding());
        }

        @Override
        protected EMail doInBackground(Long... uid) {
            final EMailDao dao = MainActivity.getHawDatabase().eMailDao();
            final EMail mail = dao.getEmailFromUid(uid[0]);
            return mail;
        }
    }
}
