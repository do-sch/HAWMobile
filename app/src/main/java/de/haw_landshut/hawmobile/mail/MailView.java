package de.haw_landshut.hawmobile.mail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import de.haw_landshut.hawmobile.R;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

public class MailView extends AppCompatActivity {

    public static final int REQUEST_CODE=1;
    public static final String DELETE = "delete";

    private String subject, sender;
    private int adapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_view);

        final Intent intent = getIntent();
        final String encoding = intent.getStringExtra(MailEntryAdapter.ViewHolder.MESSAGE_ENCODING);
        final String text = intent.getStringExtra(MailEntryAdapter.ViewHolder.MESSAGE_TEXT);
        subject = intent.getStringExtra(MailEntryAdapter.ViewHolder.MESSAGE_SUBJECT);
        sender = intent.getStringExtra(MailEntryAdapter.ViewHolder.MESSAGE_SENDER);
        adapterPosition = intent.getIntExtra(MailEntryAdapter.ViewHolder.MESSAGE_ADAPTER_POSITION, -1);

//        final String foldername = intent.getStringExtra(MailEntryAdapter.ViewHolder.MESSAGE_FNA);
//        final EMail indicies = new EMail();
//        indicies.setUid(uid);
//        indicies.setFoldername(foldername);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(subject);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final WebView wv = findViewById(R.id.mailWebView);

        ContentType contentType;
        try {
            contentType = new ContentType(encoding);
        } catch (ParseException e) {
            e.printStackTrace();
            ParameterList pl = new ParameterList();
            pl.set("encoding", "utf-8");
            contentType = new ContentType("text", "html", pl);
        }

        wv.loadDataWithBaseURL(null, text, contentType.getBaseType(), contentType.getParameter("encoding"), null);

        Log.d("encoding", encoding);

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        menu.clear();
        getMenuInflater().inflate(R.menu.mailviewactionbar, menu);

        final MenuItem deleteItem = menu.findItem(R.id.removeMail);
        final MenuItem answerItem = menu.findItem(R.id.answerMail);

        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final Intent i = new Intent();
                i.putExtra(DELETE, true);
                i.putExtra(MailEntryAdapter.ViewHolder.MESSAGE_ADAPTER_POSITION, adapterPosition);
                setResult(RESULT_OK, i);
                finish();
                return true;
            }
        });

        answerItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final Intent it = new Intent(MailView.this, MailCreateActivity.class);
                final Bundle b = new Bundle();

                b.putString("subject", "Re:"+subject);
                b.putString("sender", sender);

                it.putExtras(b);

                startActivity(it);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {

        final Intent i = new Intent();
        i.putExtra(DELETE, false);
        i.putExtra(MailEntryAdapter.ViewHolder.MESSAGE_ADAPTER_POSITION, adapterPosition);
        setResult(RESULT_OK, i);
        finish();

    }

}
