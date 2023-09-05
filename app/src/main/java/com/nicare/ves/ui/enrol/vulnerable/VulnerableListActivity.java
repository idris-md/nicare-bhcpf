package com.nicare.ves.ui.enrol.vulnerable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nicare.ves.R;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VulnerableListActivity extends AppCompatActivity {

    List<Vulnerable> vulnerableList;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vulnerable_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //getVulnerableRecords();

        PrincipalRecyclerAdapter recyclerAdapter = new PrincipalRecyclerAdapter();

        vulnerableList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);

        VulnerableListViewModel viewModel = ViewModelProviders.of(this).get(VulnerableListViewModel.class);
//      VulnerableAdapter adapter = new VulnerableAdapter()

        viewModel.getAll().observe(this, vulnerables -> {
            recyclerAdapter.submitList(vulnerables);
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(getBaseContext(), SettingActivity.class);
//            startActivity(intent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }

}
