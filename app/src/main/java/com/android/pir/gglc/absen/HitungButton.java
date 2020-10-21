package com.android.pir.gglc.absen;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.pir.mobile.R;

public class HitungButton extends FragmentActivity{
    final String nilaiBtn1 = "4";
    final String nilaiBtn2 = "2";
    private String nilaiBtn3 = "5";
    private Button btn1,btn2,btn3;
    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hitungbutton);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);

        edt = (EditText)findViewById(R.id.edt);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt.getText().toString().isEmpty()){
                    edt.setText(nilaiBtn1);
                }else{
                    edt.setText(String.valueOf(edt.getText()+nilaiBtn1));
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt.getText().toString().isEmpty()){
                    edt.setText(nilaiBtn2);
                }else{
                    edt.setText(String.valueOf(edt.getText()+nilaiBtn2));
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt.getText().toString().isEmpty()){
                    edt.setText(nilaiBtn3);
                }else{
                    edt.setText(String.valueOf(edt.getText()+nilaiBtn3));
                }
            }
        });
    }
}
