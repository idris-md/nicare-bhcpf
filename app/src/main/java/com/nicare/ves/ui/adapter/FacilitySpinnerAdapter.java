package com.nicare.ves.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nicare.ves.R;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;

import java.util.ArrayList;
import java.util.List;

public class FacilitySpinnerAdapter extends BaseAdapter {

    Context context;

    List<Facility> mDrugs = new ArrayList<>();
    LayoutInflater inflter;

    public FacilitySpinnerAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return mDrugs.size();
    }

    @Override
    public Object getItem(int i) {
        return mDrugs.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.layout_spinner, null);
        TextView names = (TextView) view.findViewById(R.id.text1);
        names.setText(mDrugs.get(i).getHcpname());
        return view;
    }

    public void notifyEntryChange(List<Facility> newList) {
        mDrugs = newList;
        notifyDataSetChanged();
    }

    public void  clearEntries(){
        mDrugs.clear();
        notifyDataSetChanged();
    }
    public int getItemPosition(Facility program) {
        return mDrugs.indexOf(program);
    }


}
