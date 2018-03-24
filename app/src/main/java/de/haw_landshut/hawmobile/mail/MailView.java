package de.haw_landshut.hawmobile.mail;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import com.pchmn.materialchips.ChipView;
import com.sun.mail.imap.IMAPFolder;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MailView extends AppCompatActivity {

    public static final int REQUEST_CODE=1;
    private static final int SAVEPERMISSION = 1;
    public static final String DELETE = "delete";

    private String subject, sender;
    private int adapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_view);

        final Intent intent = getIntent();
        final EMail mail = ((EMail) intent.getSerializableExtra("mail"));
        final String text = mail.getText();
        final String encoding = mail.getEncoding();
        this.subject = mail.getSubject();
        this.sender = mail.getSenderMail();
        adapterPosition = intent.getIntExtra(MailEntryAdapter.ViewHolder.MESSAGE_ADAPTER_POSITION, -1);

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

        final String[] attachmentNames = mail.getAttachmentNames();
        final LinearLayout attachments = findViewById(R.id.attachment_chips);
        for (final String attachmentName : attachmentNames) {
            final ChipView chipView = ((ChipView) View.inflate(this, R.layout.attachment_item_view, null));
            chipView.setLabel(attachmentName);
            attachments.addView(chipView);
            chipView.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    new OpenAttachment(attachmentName).execute(mail);
                    final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), chipView, Gravity.BOTTOM);
                    popupMenu.inflate(R.menu.save_attachment_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.save_attachment:
                                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                                            !ActivityCompat.shouldShowRequestPermissionRationale(MailView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        saveMail = mail;
                                        fileName = attachmentName;
                                        ActivityCompat.requestPermissions(MailView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVEPERMISSION);
                                    } else
                                        new DownloadAttachment(attachmentName, true).execute(mail);
                                    return true;
                                case R.id.open_attachment:
                                    new DownloadAttachment(attachmentName, false).execute(mail);
                                    return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

        }

    }

    private EMail saveMail;
    private String fileName;
    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SAVEPERMISSION)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                new DownloadAttachment(fileName, true).execute(saveMail);
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

    private class DownloadAttachment extends AsyncTask<EMail, Void, Void>{
        private static final String PREFIX = "tmpfile";
        String filename;
        final boolean permSave;


        DownloadAttachment(final String filename, final boolean permSave){
            this.filename = filename;
            this.permSave = permSave;
        }

        @Override
        protected Void doInBackground(EMail... mails) {

            final Store store = Protocol.getStore();
            if (store == null)
                return null; //TODO: Fehlermeldung geben
            final EMail mail = mails[0];

            try {
                final IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder(mail.getFoldername()));
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);
                final Message m = folder.getMessageByUID(mail.getUid());

                final Multipart mp = ((Multipart) m.getContent());
                for (int i = mp.getCount() - 1; i >= 0; i--){
                    final BodyPart bp = mp.getBodyPart(i);
                    if (bp.getFileName() != null && MimeUtility.decodeText(bp.getFileName()).equals(filename)) {

                        final String mimeType = new ContentType(bp.getContentType()).getBaseType();
                        final String[] nameParts = filename.split("\\.");
                        final File file;


                        if (permSave) {
                            final File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            int c = 0;
                            File f;
                            do {
                                f = new File(dir, c == 0 ? filename : filename+'('+c+')');
                                c++;
                            } while (f.exists());
                            c--;
                            file = new File(dir, c == 0 ? filename : filename+'('+c+')');
                        } else {
                            file = File.createTempFile(PREFIX, "." + nameParts[nameParts.length - 1]);
                            file.deleteOnExit();
                        }

                        final InputStream is = bp.getInputStream();

                        if (permSave) {
                            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                                return null;
                        }


                        if (bp instanceof MimeBodyPart)
                            ((MimeBodyPart) bp).saveFile(file);
                        else {
                            int read = 0;
                            byte[] bytes = new byte[1024];
                            try (FileOutputStream out = new FileOutputStream(file)) {
                                while ((read = is.read(bytes)) != -1)
                                    out.write(bytes, 0, read);
                            }
                        }

                        if (permSave) {
                            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            downloadManager.addCompletedDownload(file.getName(), file.getName(), true, mimeType, file.getAbsolutePath(),file.length(),true);
                        } else {
                            final Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".sl.mail.provider", file);
                            final Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, mimeType);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);//TODO: ActivityNotFoundException abfangen, wenn keine Anwendung installiert, die die Datei öffnen kann
                        }
                    }
                }


            } catch (MessagingException|IOException e) {
                throw new RuntimeException(e);
            }


            return null;
        }
    }

}