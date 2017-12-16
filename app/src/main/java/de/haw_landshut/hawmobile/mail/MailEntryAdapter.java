package de.haw_landshut.hawmobile.mail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;
import org.jsoup.Jsoup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MailEntryAdapter extends RecyclerView.Adapter<MailEntryAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView subjectView, senderView, contentView, dateView;
        public RelativeLayout viewBackground, viewForeground;
        private final MailOverview mailOverview;
        private int pos;
        private EMail mail;
        public static final String MESSAGE_UID = "de.haw_landshut.hawmobile.MailView.uid";
        public static final String MESSAGE_FNA = "de.haw_landshut.hawmobile.MailView.foldername";
        public ViewHolder(View itemView, MailOverview mailOverview) {
            super(itemView);
            this.mailOverview = mailOverview;
            this.subjectView = itemView.findViewById(R.id.subject);
            this.senderView = itemView.findViewById(R.id.sender);
            this.contentView = itemView.findViewById(R.id.mailcontent);
            this.dateView = itemView.findViewById(R.id.maildate);
            this.viewBackground = itemView.findViewById(R.id.view_background);
            this.viewForeground = itemView.findViewById(R.id.view_foreground);
            itemView.setOnClickListener(this);
        }

        public void setMail(final EMail mail) {this.mail = mail; }
        public void setPos(int pos) { this.pos = pos; }

        @Override
        public void onClick(View view) {
            mailOverview.onMessageClicked(mail, pos);
        }
    }

    private List<EMail> messages;
    private final MailOverview mailOverview;
    @SuppressLint("SimpleDateFormat")
    private DateFormat df = new SimpleDateFormat("dd.MM.yy");

    public MailEntryAdapter(List<EMail> messages, MailOverview mailOverview){
        this.messages = messages;
        this.mailOverview = mailOverview;
    }

    @Override
    public MailEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.adapter_mail_entry, parent, false);
        return new ViewHolder(v, mailOverview);
    }

    @Override
    public void onBindViewHolder(MailEntryAdapter.ViewHolder holder, int position) {
        EMail m = messages.get(position);
        holder.setMail(m);
        holder.setPos(position);
        holder.subjectView.setText(m.getSubject());
        holder.senderView.setText(m.getSenderMails());
        holder.dateView.setText(df.format(m.getDate()));
        if(m.isHtml())
            holder.contentView.setText(Jsoup.parse(m.getText()).text());
        else
            holder.contentView.setText(m.getText());

        if(!m.isSeen()){
            holder.subjectView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.senderView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.dateView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.dateView.setTextColor(Color.rgb(0, 150, 255));
            holder.subjectView.setTextColor(Color.BLACK);
            holder.senderView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(EMail m){
        messages.add(m);
    }

}
