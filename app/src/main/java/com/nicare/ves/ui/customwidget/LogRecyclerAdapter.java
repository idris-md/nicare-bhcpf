package com.nicare.ves.ui.customwidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicare.ves.R;
import com.nicare.ves.persistence.remote.apimodels.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogRecyclerAdapter extends RecyclerView.Adapter<LogRecyclerAdapter.ViewHolder> {

    List<Transaction> mTransactions;
    Context mContext;


    public LogRecyclerAdapter(Context mContext, List<Transaction> mTransactions) {
        this.mTransactions = mTransactions;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_log, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = mTransactions.get(position);

        if (null == transaction.getLastSyncTime()) {
            holder.time.setText("Not Synced");
        } else {
            holder.time.setText("Synced: " + transaction.getLastSyncTime());
        }

        holder.date.setText(transaction.getDate());
        holder.enrollment.setText(transaction.getRegistration() + " Beneficiary Enrolled");
        //holder.idSync.setText(transaction.getSync()+" Uploaded record(s) ");

    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.enrollment)
        TextView enrollment;
        @BindView(R.id.id_sync)
        TextView idSync;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
