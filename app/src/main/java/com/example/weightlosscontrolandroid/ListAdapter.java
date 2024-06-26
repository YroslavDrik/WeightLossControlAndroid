package com.example.weightlosscontrolandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private List<String> ListItem;
    private List<String> DateList;

    public ListAdapter(Context context , List<String> listItem , List<String> dateList){
        this.context = context;
        this.ListItem = listItem;
        this.DateList = dateList;
    }

    @Override
    public int getCount() {
        return ListItem.size();
    }

    @Override
    public Object getItem(int i) {
        return ListItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListClassString listClassString;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_listbox, viewGroup, false);
            listClassString = new ListClassString();
            listClassString.text = view.findViewById(R.id.TextList);
            listClassString.Date = view.findViewById(R.id.DateTitele);
            view.setTag(listClassString);
        } else {
            listClassString = (ListClassString) view.getTag();
        }

        String item = ListItem.get(i);
        String date = DateList.get(i);
        listClassString.text.setText(String.valueOf(item));
        listClassString.Date.setText(date);

        return view;
    }
    private static class ListClassString {
         TextView text;
         TextView Date;
    }

}
