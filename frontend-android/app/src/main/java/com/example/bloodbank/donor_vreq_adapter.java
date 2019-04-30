package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class donor_vreq_adapter extends RecyclerView.Adapter<donor_vreq_adapter.ViewHolder> {

    private List<donor_vreq_listitem> listItems;
    private Context context;
    View mParent;

    public donor_vreq_adapter(List<donor_vreq_listitem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_vreq_listitem, parent, false);
        mParent = v;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final donor_vreq_listitem listItem = listItems.get(position);

        holder.textViewName.setText(listItem.getName());
        holder.textViewAge.setText(listItem.getAge());
        holder.textViewSex.setText(listItem.getSex());

        holder.linearLayout.setOnClickListener(new DonorAcceptClickListener(context, mParent, listItem.getId()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName;
        public TextView textViewAge;
        public TextView textViewSex;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.vreq_listitem_requester_name);
            textViewAge = (TextView) itemView.findViewById(R.id.vreq_listitem_requester_age);
            textViewSex = (TextView) itemView.findViewById(R.id.vreq_listitem_requester_sex);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.donor_vreq_linearLayout);
        }
    }


}
