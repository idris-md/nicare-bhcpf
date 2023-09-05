package com.nicare.ves.ui.enrol.recapture;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnApprovedRecyclerAdapter extends ListAdapter<ReCapture, UnApprovedRecyclerAdapter.MyViewHolder> {

    private static final DiffUtil.ItemCallback<ReCapture> DIFF_CALL_BACK = new DiffUtil.ItemCallback<ReCapture>() {
        @Override
        public boolean areItemsTheSame(@NonNull ReCapture oldItem, @NonNull ReCapture newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReCapture oldItem, @NonNull ReCapture newItem) {
            return oldItem.getId() == (newItem.getId());
        }
    };

    public UnApprovedRecyclerAdapter() {
        super(DIFF_CALL_BACK);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recapture, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {

        final ReCapture enrollment = getItem(position);

        if (enrollment.getPhoto() != null)
            viewHolder.imageView.setImageBitmap(Util.decodeBase64ToBitmap(enrollment.getPhoto()));
        String name = enrollment.getLastName() + " " + enrollment.getFirstName();
        if (enrollment.getOtherName() != null) {
            name = enrollment.getLastName() + " " + enrollment.getFirstName() + " " + enrollment.getOtherName().trim();
        }
        viewHolder.tvName.setText(name);
//        viewHolder.tvType.setText(enrollment.getPinType());
        viewHolder.tvSynced.setText(Util.toTitleCase(Util.toTitleCase(enrollment.getWard())));

//        for (Ward ward : UnApprovedActivity.wardsList) {al4onsopresa

//            if (ward.getWard_id().equalsIgnoreCase(enrollment.getProvider()))
//                viewHolder.tvSynced.setText(Util.toTitleCase(ward.getName()));
//
//        }


        viewHolder.tvPhone.setText(enrollment.getPhone());
        if (enrollment.isRecaptured()) {
            viewHolder.tvStatus.setText("Re-Captured");
            viewHolder.tvStatus.setTextColor(ContextCompat.getColor(viewHolder.root.getContext(), R.color.white));
            viewHolder.root.setCardBackgroundColor(ContextCompat.getColor(viewHolder.root.getContext(), R.color.colorPrimary));
        } else {
            viewHolder.tvStatus.setText("Not-Captured");
            viewHolder.tvStatus.setTextColor(ContextCompat.getColor(viewHolder.root.getContext(), R.color.deep_orange));
            viewHolder.root.setCardBackgroundColor(ContextCompat.getColor(viewHolder.root.getContext(), R.color.gray));
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView tvName;
        //        @BindView(R.id.tv_type)
//        TextView tvType;
        @BindView(R.id.tv_synced)
        TextView tvSynced;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.status)
        TextView tvStatus;
        @BindView(R.id.root)
        MaterialCardView root;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(view.getContext(), ReCaptureActivity.class);
                    intent.putExtra(ReCaptureActivity.ID, getItem(getAdapterPosition()).getNicareId());
                    view.getContext().startActivity(intent);

                }
            });
        }


    }

}