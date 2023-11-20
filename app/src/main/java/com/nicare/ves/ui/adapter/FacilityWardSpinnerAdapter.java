package com.nicare.ves.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nicare.ves.R;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;

import java.util.ArrayList;
import java.util.List;

public class FacilityWardSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Ward> mDrugs = new ArrayList<>();
    LayoutInflater inflter;

    public FacilityWardSpinnerAdapter(Context applicationContext) {
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
        names.setText("" + mDrugs.get(i).getName() + " " + mDrugs.get(i).getTotalEnrolled() + mDrugs.get(i).getTotalEnrolledRemotely() + "/ " + mDrugs.get(i).getEnrolmentCap());
        return view;
    }

    public void notifyEntryChange(List<Ward> newList) {
        mDrugs = newList;
        notifyDataSetChanged();
    }

    public void clearEntries() {
        mDrugs.clear();
        notifyDataSetChanged();
    }

    public int getItemPosition(Ward program) {
        return mDrugs.indexOf(program);
    }


}
