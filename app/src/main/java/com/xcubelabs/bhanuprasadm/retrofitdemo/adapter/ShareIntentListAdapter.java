package com.xcubelabs.bhanuprasadm.retrofitdemo.adapter;

import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcubelabs.bhanuprasadm.retrofitdemo.R;

public class ShareIntentListAdapter extends ArrayAdapter{

    private final Activity context;
    Object[] items;

    public ShareIntentListAdapter(Activity context,Object[] items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.social_share, parent, false);
        TextView shareName = (TextView) rowView.findViewById(R.id.shareName);
        ImageView imageShare = (ImageView) rowView.findViewById(R.id.shareImage);

        shareName.setText(((ResolveInfo)items[position]).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
        imageShare.setImageDrawable(((ResolveInfo)items[position]).activityInfo.applicationInfo.loadIcon(context.getPackageManager()));

        return rowView;
    }
}