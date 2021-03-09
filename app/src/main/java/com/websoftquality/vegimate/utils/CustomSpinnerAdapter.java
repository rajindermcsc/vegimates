package com.websoftquality.vegimate.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.websoftquality.vegimate.R;

import java.util.ArrayList;



public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter
{
    public final Context activity;
    public ArrayList<String> asr;

    public CustomSpinnerAdapter(Context context, ArrayList<String> asr)
    {
        this.asr=asr;
        activity = context;
    }
    public int getCount()
    {
        return asr.size();
    }
    public Object getItem(int i)
    {
        return asr.get(i);
    }
    public long getItemId(int i)
    {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(10, 20, 10, 20);
        txt.setTextSize(14);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(asr.get(position));
        txt.setTextColor(activity.getResources().getColor(R.color.black));
        return  txt;
    }


    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setPadding(10, 10, 10, 5);
        txt.setTextSize(14);
        txt.setSingleLine();
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown, 0);
        txt.setText(asr.get(i));
        txt.setTextColor(activity.getResources().getColor(R.color.black));
        return  txt;
    }

}
