//package com.nicare.ves.ui.enrol.vulnerable;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.nicare.ves.R;
//import com.nicare.ves.common.Util;
//import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
//
//import java.util.List;
//
//public class VulnerableAdapter extends ListAdapter<VulnerableAdapter.ViewHolder> {
//
//    Context context;
//    private List<Vulnerable> mData;
//
//    public VulnerableAdapter(Context context, List<Vulnerable> data) {
//        this.mData = data;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public VulnerableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_enrolled, viewGroup, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        Vulnerable enrollment = mData.get(i);
//
//        viewHolder.imageView.setImageBitmap(Util.decodeBase64ToBitmap(enrollment.getPhoto()));
//        viewHolder.tvName.setText(enrollment.getFirstName() + " " + enrollment.getSurName());
//        viewHolder.tvType.setText(enrollment.getMaritalStatus());
//        viewHolder.tvSynced.setText(enrollment.getAddress());
//
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(position);
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView imageView;
//        TextView tvName;
//        TextView tvType;
//        TextView tvSynced;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            imageView = itemView.findViewById(R.id.imageView);
//
//            tvName = itemView.findViewById(R.id.tv_name);
//            tvType = itemView.findViewById(R.id.tv_type);
//            tvSynced = itemView.findViewById(R.id.tv_synced);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), EditVulnerableActivity.class);
//                    intent.putExtra(EditVulnerableActivity.ID, Long.valueOf(getItem(getAdapterPosition()).getId()));
//                    view.getContext().startActivity();
//                }
//            });
//        }
//
//    }
//
//}
