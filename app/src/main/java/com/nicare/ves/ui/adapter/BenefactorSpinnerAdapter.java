package com.nicare.ves.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nicare.ves.R;
import com.nicare.ves.persistence.local.databasemodels.Benefactor;

import java.util.ArrayList;
import java.util.List;

public class BenefactorSpinnerAdapter extends BaseAdapter {

    Context context;

    List<Benefactor> benefactors = new ArrayList<>();
    LayoutInflater inflter;

    public BenefactorSpinnerAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return benefactors.size();
    }

    @Override
    public Object getItem(int i) {
        return benefactors.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.layout_spinner, null);
        TextView names = (TextView) view.findViewById(R.id.text1);
        names.setText(benefactors.get(i).getName());
        return view;
    }

    public void notifyEntryChange(List<Benefactor> newList) {
        benefactors = newList;
        notifyDataSetChanged();
    }

    public int getItemPosition(Benefactor program) {
        return benefactors.indexOf(program);
    }


}
