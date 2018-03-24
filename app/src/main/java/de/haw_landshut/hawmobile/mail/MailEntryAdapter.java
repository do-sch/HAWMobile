package de.haw_landshut.hawmobile.mail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.EMail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MailEntryAdapter extends RecyclerView.Adapter<MailEntryAdapter.ViewHolder> {

    public interface Selectable{
        void select();
        void deselect();
        int getAdapterPosition();
        EMail getMail();
        boolean isSelected();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, Selectable{

        TextView subjectView, senderView, contentView, dateView;
        RelativeLayout viewBackground, viewForeground;
        private final MailEntryAdapter mea;
        private EMail mail;

        public static final String MESSAGE_TEXT = "de.haw_landshut.hawmobile.MailView.text";
        public static final String MESSAGE_FNA = "de.haw_landshut.hawmobile.MailView.foldername";
        public static final String MESSAGE_UID = "de.haw_landshut.hasmobile.MailView.uid";
        public static final String MESSAGE_SUBJECT = "de.haw_landshut.hawmobile.MailView.subject";
        public static final String MESSAGE_ENCODING = "de.haw_landshut.hawmobile.MailView.encoding";
        public static final String MESSAGE_SENDER = "de.haw_landshut.hawmobile.MailView.sender";
        public static final String MESSAGE_ADAPTER_POSITION = "de.haw_landshut.hawmobile.MailView.adapterPosition";


        ViewHolder(View itemView, MailEntryAdapter mea) {
            super(itemView);
            this.mea = mea;
            this.subjectView = itemView.findViewById(R.id.subject);
            this.senderView = itemView.findViewById(R.id.sender);
            this.contentView = itemView.findViewById(R.id.mailcontent);
            this.dateView = itemView.findViewById(R.id.maildate);
            this.viewBackground = itemView.findViewById(R.id.view_background);
            this.viewForeground = itemView.findViewById(R.id.view_foreground);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void select(){
            mea.select(getAdapterPosition());
            viewForeground.setSelected(true);
        }

        @Override
        public void deselect(){
            mea.deselect(getAdapterPosition());
            viewForeground.setSelected(false);
        }

        @Override
        public boolean isSelected(){
            return viewForeground.isSelected();
        }

        @Override
        public void onClick(View view) {
            mea.getListener().onItemClick(this);
        }

        @Override
        public boolean onLongClick(View view) {
            mea.getListener().onItemLongClick(this);
            return true;
        }

        @Override
        public EMail getMail() {
            return mail;
        }

        public void setMail(final EMail mail) {this.mail = mail; }
    }


    private List<EMail> messages;
    private SparseBooleanArray selectedItems;
    private final MailEntryClickListener mailEntryClickListener;
    @SuppressLint("SimpleDateFormat")
    private DateFormat df = new SimpleDateFormat("dd.MM.yy");

    public void setMessages(List<EMail> messages) {
        this.messages = messages;
    }

    MailEntryAdapter(List<EMail> messages, MailEntryClickListener mailEntryClickListener){
        this.messages = messages;
        this.mailEntryClickListener = mailEntryClickListener;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public MailEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.adapter_mail_entry, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(MailEntryAdapter.ViewHolder holder, int position) {
        final EMail m = messages.get(position);
        holder.setMail(m);
        holder.subjectView.setText(m.getSubject());
        holder.dateView.setText(df.format(m.getDate()));
        holder.contentView.setText(m.getShortText());
        holder.viewForeground.setSelected(selectedItems.get(position));

        String senderName = m.getSenderName();
        if (senderName == null)
            holder.senderView.setText(m.getSenderMail());
        else
            holder.senderView.setText(senderName);

        if(!m.isSeen()){
            holder.subjectView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.senderView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.dateView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.dateView.setTextColor(Color.rgb(0, 150, 255));
            holder.subjectView.setTextColor(Color.BLACK);
            holder.senderView.setTextColor(Color.BLACK);
        } else {
            holder.dateView.setTextAppearance(R.style.AppTheme);
            holder.dateView.setTextColor(R.color.primary);
            holder.senderView.setTextAppearance(R.style.AppTheme);
            holder.subjectView.setTextAppearance(R.style.AppTheme);
        }

    }

    public int getSelectedItemCount(){
        return selectedItems.size();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(EMail m){
        messages.add(m);
    }

    public void removeMessage(int position) {
        messages.remove(position);
    }

    void select(int position){
        selectedItems.put(position, true);
//        this.notifyItemChanged(position);
    }

    void deselect(int position){
        System.out.println("size before: " + selectedItems.size());
        selectedItems.delete(position);
        System.out.println("size after: "+ selectedItems.size());
//        this.notifyItemChanged(position);
    }

    void deselectAll(){
        for(int i = 0; i < selectedItems.size(); i++){
            final int pos = selectedItems.indexOfKey(i);
            selectedItems.put(pos, false);
            notifyItemChanged(pos);
        }
        selectedItems.clear();

        for(int i = 0; i< selectedItems.size(); i++){
            System.out.println(selectedItems.keyAt(i) + ": " + selectedItems.get(i));
        }
    }

    EMail getMail(final int index){
        return messages.get(index);
    }

    Integer[] getAllSelectedMessagePositions(){
        final Integer[] selectedMessages = new Integer[selectedItems.size()];

        for (int i = 0; i < selectedItems.size(); i++){
            if (selectedItems.get(selectedItems.keyAt(i)))
                selectedMessages[i] = selectedItems.keyAt(i);
        }

        return selectedMessages;
    }

    MailEntryClickListener getListener(){
        return mailEntryClickListener;
    }


    interface MailEntryClickListener{
        void onItemClick(Selectable vh);
        void onItemLongClick(Selectable vh);
    }

}
