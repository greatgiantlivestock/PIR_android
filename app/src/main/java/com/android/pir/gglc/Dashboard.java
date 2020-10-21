package com.android.pir.gglc;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.pir.gglc.absen.LoginAbsenActivity;
import com.android.pir.gglc.absen.LoginActivity;
import com.android.pir.mobile.R;

public class Dashboard extends AppCompatActivity{
    private Button absen, canvassing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        absen = (Button)findViewById(R.id.absen);
        canvassing = (Button)findViewById(R.id.canvassing);

        absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAbsen();
            }
        });

        canvassing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCanvassing();
            }
        });
    }

    protected void gotoAbsen(){
        Intent intentActivity = new Intent(this, LoginAbsenActivity.class);
        startActivity(intentActivity);
    }

    protected void gotoCanvassing(){
        Intent intentActivity = new Intent(this, LoginActivity.class);
        startActivity(intentActivity);
    }
}
