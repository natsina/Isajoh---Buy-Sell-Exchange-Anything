package com.isajoh.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.isajoh.app.R;
import com.isajoh.app.modelsList.catSubCatlistModel;
import com.isajoh.app.modelsList.homeCatListModel;

public class LocationCustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<homeCatListModel> arrayList;

    public LocationCustomAdapter(Context context, ArrayList<homeCatListModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_locations_dialog, parent, false);
        }
        TextView name, email;
        name = (TextView) convertView.findViewById(R.id.name);
        name.setText(arrayList.get(position).getTitle());

        return convertView;
    }

}
