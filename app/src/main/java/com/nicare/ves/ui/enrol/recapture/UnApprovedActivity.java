package com.nicare.ves.ui.enrol.recapture;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nicare.ves.R;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnApprovedActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    public static List<Ward> wardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_approved);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        UnApprovedRecyclerAdapter recyclerAdapter = new UnApprovedRecyclerAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(recyclerAdapter);
        UnApprovedViewModel viewModel = ViewModelProviders.of(this).get(UnApprovedViewModel.class);


        viewModel.getAll().observe(UnApprovedActivity.this, new Observer<List<ReCapture>>() {
            @Override
            public void onChanged(List<ReCapture> reCaptures) {
                recyclerAdapter.submitList(reCaptures);
            }
        });

//
//        viewModel.getAllWards().observe(this, new Observer<List<Ward>>() {
//            @Override
//            public void onChanged(List<Ward> wards) {
//                wardsList = wards;
//
//
//            }
//        });


    }
}