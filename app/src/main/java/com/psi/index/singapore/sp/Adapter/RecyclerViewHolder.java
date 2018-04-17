package com.psi.index.singapore.sp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psi.index.singapore.sp.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView north,south,central, east,west,time,navigate;
    LinearLayout line_items;
    public RecyclerViewHolder(View view) {
        super(view);
        this.north = (TextView) view.findViewById(R.id.north);
        this.south = (TextView) view.findViewById(R.id.south);
        this.central = (TextView) view.findViewById(R.id.central);
        this.east = (TextView) view.findViewById(R.id.east);
        this.west = (TextView) view.findViewById(R.id.west);
        this.time = (TextView) view.findViewById(R.id.time);
        this.navigate = (TextView) view.findViewById(R.id.navigate);

        this.line_items= (LinearLayout) view.findViewById(R.id.line_items);


    }
}
