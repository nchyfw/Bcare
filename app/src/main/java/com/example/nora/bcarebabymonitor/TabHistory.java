package com.example.nora.bcarebabymonitor;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nora.bcarebabymonitor.model.HistoryObject;
import com.example.nora.bcarebabymonitor.model.ItemObject;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TabHistory extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    // Waktu
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String waktu;

    // Widget
    TextView txt_no_history;

    // TAG
    private static final String TAG = "TabHistory";

    // Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("notif");

    // String
    String fTemp, fHum, fTempDua, fHumDua, fTempTiga, fHumTiga;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tab_history, container, false);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Widget
        txt_no_history = (TextView) view.findViewById(R.id.txt_no_history);

        // Recycler View
        recyclerView = (RecyclerView) view.findViewById(R.id.rvHIstory);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        waktu = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + " " + sdf.format(c.getTime());

        final FirebaseRecyclerAdapter<HistoryObject, HistoryHolder> adapter = new FirebaseRecyclerAdapter<HistoryObject, HistoryHolder>(
                HistoryObject.class,
                R.layout.activity_cardhistory,
                HistoryHolder.class,
                myref.getRef()
        ) {

            @Override
            public void populateViewHolder(final HistoryHolder viewHolder, final HistoryObject model, final int position) {
                if (txt_no_history.getVisibility() == View.VISIBLE) {
                    txt_no_history.setVisibility(View.GONE);
                }
                Log.d(TAG, "Data loaded");
                viewHolder.txt_tanggaljam.setText(model.getDate());
                viewHolder.txt_peringatan.setText(model.getStatus());

                if(model.getStatus().equals("Bayi anda kepanasan")) {
                    viewHolder.img_icon.setImageResource(R.mipmap.ic_ket_panas);
                } else if(model.getStatus().equals("Bayi anda buang air")){
                    viewHolder.img_icon.setImageResource(R.mipmap.ic_ket_pipis);
                } else if(model.getStatus().equals("Bayi anda menangis")){
                    viewHolder.img_icon.setImageResource(R.mipmap.ic_ket_menangis);
                }

                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), DetailHistory.class);
                        i.putExtra("date", model.getDate());
                        i.putExtra("status", model.getStatus());
                        i.putExtra("temp", model.getTemp());
                        i.putExtra("hum", model.getHum());
                        view.getContext().startActivity(i);

                    }
                });


            }
        };
        recyclerView.setAdapter(adapter);


    }

    public static class HistoryHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView txt_tanggaljam, txt_peringatan;
        ImageView img_icon;
        CardView cardView;

        public HistoryHolder(View v) {
            super(v);
            mView = v;
            txt_tanggaljam = (TextView) v.findViewById(R.id.txt_tanggaljam);
            txt_peringatan = (TextView) v.findViewById(R.id.txt_peringatan);
            img_icon = (ImageView) v.findViewById(R.id.img_icon);
            cardView = (CardView) v.findViewById(R.id.cardview_history);
        }
    }
}