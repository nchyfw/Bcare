package com.example.nora.bcarebabymonitor;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nora on 07/09/16.
 */
public class HolderItem extends RecyclerView.ViewHolder {
    TextView txt_tanggaljam, txt_peringatan;
    ImageView img_icon;
    CardView cardview_history;
    public HolderItem(View itemView) {
        super(itemView);
        txt_tanggaljam = (TextView) itemView.findViewById(R.id.txt_tanggaljam);
        txt_peringatan = (TextView) itemView.findViewById(R.id.txt_peringatan);
        img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
        cardview_history = (CardView) itemView.findViewById(R.id.cardview_history);

    }
}
