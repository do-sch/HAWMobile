package de.haw_landshut.hawmobile.mail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.haw_landshut.hawmobile.R;

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

    private MailEntry[] messages;

    public MailEntryAdapter(MailEntry[] messages){
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
        MailEntry m = messages[position];
        holder.subjectView.setText(m.getSubject());
        holder.senderView.setText(m.getSender());
        holder.dateView.setText(m.getDate());
        holder.contentView.setText(m.getContent());
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

}
