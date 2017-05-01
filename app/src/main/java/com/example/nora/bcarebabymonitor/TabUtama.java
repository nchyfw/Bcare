package com.example.nora.bcarebabymonitor;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nora.bcarebabymonitor.model.ItemObject;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;

public class TabUtama extends Fragment {

    // Firebass
    private Firebase mREf;
    // WIDGETS
    WebView video;
    TextView sensorKelembapan, sensorPopok, sensorTangis, sensorSuhu, sensorFace;
    ImageView imgMenangis;
    Button btn_notif;
    CardView cardview_utama;
    Switch pilTidur;

    String data1;
    String data2;

    int tampil_face=0;



    ItemObject itemObject;


    private static final String TAG = "TabUtama";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tab_utama, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Text Sensor
        sensorSuhu = (TextView) view.findViewById(R.id.txt_suhu);
        sensorKelembapan = (TextView) view.findViewById(R.id.txt_kelembaban);
        sensorPopok = (TextView) view.findViewById(R.id.txt_popok);
        sensorTangis = (TextView) view.findViewById(R.id.txt_menangis);
        sensorFace = (TextView) view.findViewById(R.id.txt_face);
        final ColorStateList oldColors =  sensorFace.getTextColors();

        // Img Sensor
        imgMenangis = (ImageView) view.findViewById(R.id.img_menangis);

        // Cardview
        cardview_utama = (CardView) view.findViewById(R.id.cardview_utama);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cardview_utama.getBackground().setAlpha(0);
        } else {
            cardview_utama.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        }



        // Video
        video = (WebView) view.findViewById(R.id.video);
        video.getSettings();
        int default_zoom_level=200;
        video.setInitialScale(default_zoom_level);
        final String stream = "http://192.168.115.5:5000/video_feed";

        video.post(new Runnable() {
            @Override
            public void run() {
                int width = video.getWidth();
                int height = video.getHeight();
                video.loadUrl(stream);
                video.zoomIn();
            }
        });

        // Untuk mengambil data dari firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("readings");
        myref.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
               itemObject = dataSnapshot.getValue(ItemObject.class);
                sensorSuhu.setText(itemObject.getTemp()+"\u00b0C");
                sensorKelembapan.setText(itemObject.getHum()+"%");
                if(itemObject.getPopok().equals("true ")) {
                    sensorPopok.setText("Basah");
                } else {
                    sensorPopok.setText("Kering");
                }
                if (itemObject.getCry().equals("true ")) {
                    sensorTangis.setText("Ya");
                    imgMenangis.setImageResource(R.mipmap.ic_ket_menangis2);
                } else {
                    sensorTangis.setText("Tidak");
                    imgMenangis.setImageResource(R.mipmap.ic_color_senyum);
                }
                if (itemObject.getFace().equals("false") && tampil_face==0) {
                    sensorFace.setText("Bayi tidak terdeteksi");

                    // Anim
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(100); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);

                    // Sensor muka
                    sensorFace.setTextColor(Color.RED);
                    sensorFace.setTypeface(sensorFace.getTypeface(), Typeface.BOLD);
                    sensorFace.setTextSize(20);
                    sensorFace.startAnimation(anim);

                } else {
                    sensorFace.setText("Bayi terdeteksi");
                    sensorFace.setTypeface(null,Typeface.NORMAL);
                    sensorFace.setTextSize(16);
                    sensorFace.setTextColor(oldColors);
                    sensorFace.clearAnimation();
                    tampil_face = 0;
                }
                Log.d(TAG, "Data loaded");

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), "Maaf, terjadi kesalahan",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }


}

