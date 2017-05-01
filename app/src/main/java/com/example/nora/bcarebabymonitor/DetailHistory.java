package com.example.nora.bcarebabymonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailHistory extends AppCompatActivity {
    TextView txt_date, txt_status, txt_temp, txt_hum;
    String date, status, temp, hum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        // WIDGET
        txt_date = (TextView) findViewById(R.id.txt_detail_tanggal);
        txt_status = (TextView) findViewById(R.id.txt_detail_status);
        txt_temp = (TextView) findViewById(R.id.txt_detail_sensor_satu);
        txt_hum = (TextView) findViewById(R.id.txt_detail_sensor_dua);


        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        temp = getIntent().getStringExtra("temp");
        hum = getIntent().getStringExtra("hum");


        txt_date.setText(date);
        txt_status.setText(status);
        txt_temp.setText(temp+"\u00b0C");
        txt_hum.setText(hum+"%");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_share:
                //nanti buat ngeshare
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Bcare Baby Monitor");
                i.putExtra(Intent.EXTRA_TEXT,
                        date+ "\n"
                                + "Status: "+status
                                + "\n" +temp+"\u00b0C"
                                + "\n" +hum+"%"); //Untuk share
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Intent untuk memunculkan aplikasi medsos yg ada di hp
                Intent share = Intent.createChooser(i, "Bagikan melalui");
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(share);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
