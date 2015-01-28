package com.yorktown.yorktown;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel on 1/22/2015.
 */
public class StepAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    int layoutResourceId;
    JSONObject[] data = null;

    public StepAdapter(Context context, int layoutResourceId, JSONObject[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StepHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new StepHolder();
            holder.stepTitle = (TextView)row.findViewById(R.id.stepAdapter_title);
            holder.stepDetails = (TextView)row.findViewById(R.id.stepAdapter_details);

            row.setTag(holder);


        } else {
            holder = (StepHolder)row.getTag();
        }

        JSONObject step = data[position];
        try {
            holder.stepTitle.setText(step.getString("name"));
            holder.stepDetails.setText(step.getString("details"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return row;
    }

    static class StepHolder {
        TextView stepTitle;
        TextView stepDetails;
    }
}

