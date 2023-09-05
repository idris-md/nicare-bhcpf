package com.nicare.ves.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nicare.ves.R;

import java.util.ArrayList;
import java.util.List;

public class StringSpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> mDrugs = new ArrayList<>();
    LayoutInflater inflter;

    public StringSpinnerAdapter(Context applicationContext) {
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
        names.setText(mDrugs.get(i));
        return view;
    }

    public void notifyEntryChange(List<String> newList) {
        mDrugs = newList;
        notifyDataSetChanged();
    }

    public int getItemPosition(String program) {
        return mDrugs.indexOf(program);
    }


}
