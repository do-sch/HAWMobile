package de.haw_landshut.hawmobile.mail;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.SettingsActivity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public class MailCreateActivity extends AppCompatActivity {

    private EditText toAddress, subject, messageText;
    private MenuItem actionSend;
    private boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_create);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.write);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toAddress = findViewById(R.id.to_email_editText);
        subject = findViewById(R.id.subject_editText);
        messageText = findViewById(R.id.email_text_editText);

        toAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (edited) {
                    if (isEmailInvalid(toAddress.getText().toString())) {
                        actionSend.setEnabled(false);
                        toAddress.setError(getString(R.string.invalid_mail_address));
                    } else {
                        actionSend.setEnabled(true);
                    }
                }
            }
        });

        toAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edited && !b) {
                    edited = true;
                    if (isEmailInvalid(toAddress.getText().toString()))
                        toAddress.setError(getString(R.string.invalid_mail_address));
                    else
                        actionSend.setEnabled(true);
                    toAddress.setOnFocusChangeListener(null);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mailcreateactionbar, menu);

        actionSend = menu.findItem(R.id.action_send);
        actionSend.setEnabled(false);

        final Bundle b = getIntent().getExtras();
        if (b != null) {
            actionSend.setEnabled(true);
            toAddress.setText(b.getString("sender"));
            subject.setText(b.getString("subject"));
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_send:
                new SendTask().execute();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {

        if (!messageText.getText().toString().isEmpty() || !toAddress.getText().toString().isEmpty()){


                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.warning_abbort_title)
                        .setMessage(R.string.warning_abbort_text)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MailCreateActivity.super.onBackPressed();
                            }
                        })
//                        .setNeutralButton(R.string.add_to_drafts, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();


        } else

            super.onBackPressed();
    }

    private boolean isEmailInvalid(CharSequence email) {
        return email == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private class SendTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            //TODO: Email versenden

            try {
                final Session session = Protocol.getSession();
                final Store store = Protocol.getStore();
                if (session == null || store == null)
                    throw new RuntimeException("store or session is null");
                final MimeMessage message = new MimeMessage(session);
                final Transport transport = session.getTransport("smtp");

                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MailCreateActivity.this);
                final String name = sharedPref.getString("pref_username", Credentials.getUsername());

                try {
                    message.setFrom(new InternetAddress(Credentials.getUsername() + "@haw-landshut.de", name));
                } catch (UnsupportedEncodingException e) {
                    message.setFrom(Credentials.getUsername() + "@haw-landshut.de");
                }


                message.addRecipients(Message.RecipientType.TO, toAddress.getText().toString());

                message.setSubject(subject.getText().toString());

                message.setText(messageText.getText().toString());

//                Transport.send(message, Credentials.getUsername(), Credentials.getPassword());
                transport.connect(Credentials.getUsername(), Credentials.getPassword());
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();

                message.setFlag(Flags.Flag.SEEN, true);
                final Folder sentFolder = store.getDefaultFolder().getFolder("Sent");
                if (!sentFolder.isOpen())
                    sentFolder.open(Folder.READ_WRITE);
                sentFolder.appendMessages(new Message[]{message});

                Log.i("MailCreate.sendMessage", "Message was send");


            } catch (MessagingException e){
                throw new RuntimeException(e);
                //TODO: Fehlermeldung
            }

            return null;
        }
    }
}
