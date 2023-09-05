package com.nicare.ves.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.remote.apimodels.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionRecyclerAdapter extends ListAdapter<Transaction, TransactionRecyclerAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<Transaction> DIFF_CALL_BACK = new DiffUtil.ItemCallback<Transaction>() {

        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.getId() == (newItem.getId());
        }

    };

    public TransactionRecyclerAdapter() {
        super(DIFF_CALL_BACK);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_log, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = getItem(position);

        if (transaction.getSync() == 0) {
            holder.time.setText("Not Synced");
        } else {
            holder.time.setText("Synced: " + transaction.getSync() + " records");
        }

        String[] date = transaction.getDate().split("-");
        holder.date.setText(Util.formatDatToDisplay(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2])));
        holder.enrollment.setText(transaction.getRegistration() + " Beneficiary Enrolled");

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