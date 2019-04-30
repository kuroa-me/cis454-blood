package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class reque_history_adapter extends RecyclerView.Adapter<reque_history_adapter.ViewHolder> {

    private List<reque_history_listitem> listItems;
    private Context context;

    public reque_history_adapter(List<reque_history_listitem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reque_history_listitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final reque_history_listitem listItem = listItems.get(position);

        holder.textView_date_requested.setText(listItem.getDate_requested());
        holder.textView_accpted.setText(listItem.getAccepted());
        holder.textView_date_accpted.setText(listItem.getDate_accepted());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_date_requested;
        public TextView textView_accpted;
        public TextView textView_date_accpted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_date_requested = (TextView) itemView.findViewById(R.id.history_listitem_date_requested);
            textView_accpted = (TextView) itemView.findViewById(R.id.history_listitem_accepted);
            textView_date_accpted = (TextView) itemView.findViewById(R.id.history_listitem_date_accepted);

        }
    }
}
