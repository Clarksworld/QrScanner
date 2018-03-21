package com.example.clarksworld.qrscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity  {
    private Button scan_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan_btn = (Button)findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("SCAN NOW");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "you have cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {
                String res = result.getContents();
                Log.e("Encrypted Message: ",res);

                int key = 200;
                String decmsg = "";

                //Decrypt result data
                for(int i = 0; i < res.length(); i++){
                    decmsg = decmsg + (char) (res.charAt(i) ^ key);
                }

                //create string array of length 6
                String[] dataContainer = new String[8];

                //loop through the lenght of the string
                Log.e("Decrypted Message: ",decmsg);

                String data1 = decmsg;
                String content = "";
                int count = 0;

                    //loop through the String
                    //for each string
                    //if its not / or * save string
                  for(int x = 0; x < data1.length(); x++){

                      //if it is the first slash skip else if it is other slashes then add the
                      //summed up string to the array
                      //if it is a character save else check if it is a slash
                      //if is is a asterix end loop
                      if(x != 0){

                          if (Character.isLetter(data1.charAt(x))){
                              content = content + data1.charAt(x);
                          }else {
                              if (x == (data1.length()-1)){
                                  dataContainer[count] = content;
                                  content = "";
                              }else{
                                  dataContainer[count] = content;
                                  content = "";
                                  count++;
                              }
                          }
                      }
                }

                for(int x = 0; x < dataContainer.length; x++){
                    if (dataContainer[x] == null){
                        break;
                    }else{
                        content = content + dataContainer[x];
                        Log.e("data: ",dataContainer[x]);
                    }

                    if (x == (dataContainer.length-1)){
                        Toast.makeText(MainActivity.this,content,Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
        else {


            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
