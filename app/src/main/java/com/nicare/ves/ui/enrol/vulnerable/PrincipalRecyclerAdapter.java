package com.nicare.ves.ui.enrol.vulnerable;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrincipalRecyclerAdapter extends ListAdapter<Vulnerable, PrincipalRecyclerAdapter.MyViewHolder> {

    private static final DiffUtil.ItemCallback<Vulnerable> DIFF_CALL_BACK = new DiffUtil.ItemCallback<Vulnerable>() {
        @Override
        public boolean areItemsTheSame(@NonNull Vulnerable oldItem, @NonNull Vulnerable newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Vulnerable oldItem, @NonNull Vulnerable newItem) {
            return oldItem.getId() == (newItem.getId());
        }
    };

    public PrincipalRecyclerAdapter() {
        super(DIFF_CALL_BACK);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_enrolled, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {

        final Vulnerable enrollment = getItem(position);

        if (enrollment.getPhoto() != null)
            viewHolder.imageView.setImageBitmap(Util.decodeBase64ToBitmap(enrollment.getPhoto()));
        viewHolder.tvName.setText(enrollment.getSurName() + " " + enrollment.getFirstName() + " " + enrollment.getOtherName());
//        viewHolder.tvType.setText(enrollment.getPinType());
        viewHolder.tvSynced.setText(enrollment.getProvider());
        viewHolder.tvPhone.setText(enrollment.getPhone());

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

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(view.getContext(), EditVulnerableActivity.class);
                    intent.putExtra(EditVulnerableActivity.ID, Long.valueOf(getItem(getAdapterPosition()).getId()));
                    view.getContext().startActivity(intent);

                }
            });
        }


    }

}