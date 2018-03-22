package de.haw_landshut.hawmobile.mail;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
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
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.pchmn.materialchips.ChipView;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.R;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MailCreateActivity extends AppCompatActivity {

    private static final int ATTACH_CHOOSE_FILE = 1;

    private EditText toAddress, subject, messageText;
    private MenuItem actionSend;
    private LinearLayout chips;
    private boolean edited = false;
    private List<Uri> attachments = new ArrayList<>();

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

        chips = findViewById(R.id.chips);
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
                new SendTask().execute(getApplicationContext());
                break;
            case R.id.attach_file:
                onAttachFile();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    private void onAttachFile() {
        final Intent chooseFile, intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, getString(R.string.choose_file));
        startActivityForResult(intent, ATTACH_CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == ATTACH_CHOOSE_FILE) {
            final Uri newUri = data.getData();
            attachments.add(newUri);

            final ChipView chipView = ((ChipView) View.inflate(this, R.layout.attachment_item, null));
            chipView.setDeletable(true);
            chipView.setHasAvatarIcon(false);
            chipView.setLabel(getFileName(newUri));
            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attachments.remove(newUri);
                    chips.removeView(chipView);
                }
            });
            chips.addView(chipView);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
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

    private class SendTask extends AsyncTask<Context, Void, Context>{
        @Override
        protected void onPreExecute() {
            finish();
        }

        @Override
        protected Context doInBackground(Context... contexts) {
            final Context context = contexts[0];

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

                if (attachments.isEmpty())
                    message.setText(messageText.getText().toString());
                else {

                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText(messageText.getText().toString());

                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    for (final Uri uri : attachments){

                        final InputStream inputStream;

                        try {
                            inputStream = context.getContentResolver().openInputStream(uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        }

                        BodyPart bp = new MimeBodyPart();

                        DataSource source;
                        try {
                            source = new ByteArrayDataSource(inputStream, getMimeType(uri));
                        } catch (IOException e) {
                            e.printStackTrace();
                            continue;
                        }
                        bp.setDataHandler(new DataHandler(source));
                        bp.setFileName(getFileName(uri));

                        multipart.addBodyPart(bp);
                    }

                    message.setContent(multipart);

                }

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

            return context;
        }

        @Override
        protected void onPostExecute(final Context context) {
            Toast.makeText(context, R.string.sent, Toast.LENGTH_SHORT).show();
        }
    }
}