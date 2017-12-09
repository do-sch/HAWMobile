package de.haw_landshut.hawmobile.mail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MailEntryAdapter extends RecyclerView.Adapter<MailEntryAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView subjectView, senderView, contentView, dateView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.subjectView = itemView.findViewById(R.id.subject);
            this.senderView = itemView.findViewById(R.id.sender);
            this.contentView = itemView.findViewById(R.id.mailcontent);
            this.dateView = itemView.findViewById(R.id.maildate);
        }
    }

    private List<EMail> messages;
    @SuppressLint("SimpleDateFormat")
    private DateFormat df = new SimpleDateFormat("dd.MM.yy");

    public MailEntryAdapter(List<EMail> messages){
        this.messages = messages;
    }

    @Override
    public MailEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = ((View) inflater.inflate(R.layout.adapter_mail_entry, parent, false));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MailEntryAdapter.ViewHolder holder, int position) {
        EMail m = messages.get(position);
        holder.subjectView.setText(m.getSubject());
        holder.senderView.setText(m.getSenderMails());
        holder.dateView.setText(df.format(m.getDate()));
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

}
