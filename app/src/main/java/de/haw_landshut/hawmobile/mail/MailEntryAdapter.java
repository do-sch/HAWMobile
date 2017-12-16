package de.haw_landshut.hawmobile.mail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        private final Activity activity;
        private long pos;
        public static final String MESSAGE = "de.haw_landshut.hawmobile.MailView.uid";
        public ViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            this.subjectView = itemView.findViewById(R.id.subject);
            this.senderView = itemView.findViewById(R.id.sender);
            this.contentView = itemView.findViewById(R.id.mailcontent);
            this.dateView = itemView.findViewById(R.id.maildate);
            itemView.setOnClickListener(this);
        }

        public void setPos(long pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, MailView.class);
            intent.putExtra(MESSAGE, pos);
            activity.startActivity(intent);
        }
    }

    private List<EMail> messages;
    private final Activity activity;
    @SuppressLint("SimpleDateFormat")
    private DateFormat df = new SimpleDateFormat("dd.MM.yy");

    public MailEntryAdapter(List<EMail> messages, Activity activity){
        this.messages = messages;
        this.activity = activity;
    }

    @Override
    public MailEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.adapter_mail_entry, parent, false);
        return new ViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(MailEntryAdapter.ViewHolder holder, int position) {
        EMail m = messages.get(position);
        holder.setPos(m.getUid());
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
