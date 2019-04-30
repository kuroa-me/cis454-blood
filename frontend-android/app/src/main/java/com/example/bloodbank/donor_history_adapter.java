package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class donor_history_adapter extends RecyclerView.Adapter<donor_history_adapter.ViewHolder> {

    private List<donor_history_listitem> listItems;
    private Context context;

    public donor_history_adapter(List<donor_history_listitem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_history_listitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final donor_history_listitem listItem = listItems.get(position);

        holder.textView_donor_id.setText(listItem.getDonor_id());
        holder.textView_blood_type.setText(listItem.getBlood_type());
        holder.textView_date_received.setText(listItem.getDate_received());
        holder.textView_used.setText(listItem.getUsed());
        holder.textView_date_used.setText(listItem.getUsed_date());
        holder.textView_used_by.setText(listItem.getUsed_by());


    }

    @Override
    public int getItemCount() {

        return listItems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView textView_donor_id;
        public TextView textView_blood_type;
        public TextView textView_date_received;
        public TextView textView_used;
        public TextView textView_date_used;
        public TextView textView_used_by;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_donor_id = (TextView) itemView.findViewById(R.id.history_listitem_donor_id);
            textView_blood_type = (TextView) itemView.findViewById(R.id.history_listitem_blood_type);
            textView_date_received = (TextView) itemView.findViewById(R.id.history_listitem_date_received);
            textView_used = (TextView) itemView.findViewById(R.id.history_listitem_used);
            textView_date_used = (TextView) itemView.findViewById(R.id.history_listitem_date_used);
            textView_used_by = (TextView) itemView.findViewById(R.id.history_listitem_used_by);
        }
    }
}
