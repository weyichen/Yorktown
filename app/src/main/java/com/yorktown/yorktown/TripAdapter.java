package com.yorktown.yorktown;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Daniel on 1/22/2015.
 */
public class TripAdapter extends ArrayAdapter<ParseObject> {
    Context context;
    int layoutResourceId;
    List<ParseObject> data = null;

    public TripAdapter(Context context, int layoutResourceId, List<ParseObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TripHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TripHolder();
            holder.tripCard = (LinearLayout)row.findViewById(R.id.card_layout);
            holder.tripTitle = (TextView)row.findViewById(R.id.trip_title);
            holder.tripDesc = (TextView)row.findViewById(R.id.trip_desc);

            row.setTag(holder);

        } else {
            holder = (TripHolder)row.getTag();
        }

        ParseObject trip = data.get(position);
        String title = ParseHelpers.getString(trip, "title");
        String desc = ParseHelpers.getString(trip, "details");
        String color = ParseHelpers.getString(trip, "color");

        holder.tripTitle.setText(title);
        holder.tripDesc.setText(desc);
        if (!color.equals("")) holder.tripCard.setBackgroundColor((int) Long.parseLong(trip.getString("color"), 16));

        return row;
    }

    static class TripHolder {
        LinearLayout tripCard;
        TextView tripDesc;
        TextView tripTitle;
    }
}
