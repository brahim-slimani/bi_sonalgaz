package com.slimani.bi_sonalgaz.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.auth.LoginActivity;
import com.slimani.bi_sonalgaz.paramsSQLite.Db_server;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;

import org.json.JSONException;

public class ServerActivity extends AppCompatActivity {
    Db_server dbm = new Db_server(this);
    public Cursor cursor;

    String response = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_setting);

        final EditText ipAddress = (EditText) findViewById(R.id.ip_address_field);
        final EditText portNumber = (EditText) findViewById(R.id.port_nmuber_field);

        final Button connection_btn = (Button) findViewById(R.id.connectionBtn);

        try {

            if(initServer()){
                DataManager dataManager = new DataManager();
                ipAddress.setText(dataManager.getCurrentIPaddress(getApplicationContext()));
                portNumber.setText(dataManager.getCurrentPortNumber(getApplicationContext()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        connection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipAddress.getText().toString().equals("") || portNumber.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "You should enter the two fields !", Toast.LENGTH_SHORT).show();
                }else{

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Service service = new Service(getApplicationContext());
                            response = service.testConnection(ipAddress.getText().toString(), portNumber.getText().toString());

                            ServerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.equals("connection successful")){

                                        //Toast.makeText(getApplicationContext(), "Connection was established successfully !", Toast.LENGTH_SHORT).show();
                                        successAlert("Connection is established successfully !");
                                        try {
                                            if(initServer()){
                                                dbm.refreshServer(1,ipAddress.getText().toString(), portNumber.getText().toString());
                                            }else{
                                                dbm.initServer(1,ipAddress.getText().toString(), portNumber.getText().toString());

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Connection could not established !", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    });
                    thread.start();


                }
            }
        });


    }

    public boolean initServer() throws JSONException {
        boolean b;
        cursor = dbm.getParamServer(1);
        if(cursor.getCount() == 0){
            b = false;
        }else b = true;

        return b;
    }

    public void successAlert(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Successful !");
        alertDialogBuilder.setIcon(R.drawable.success);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
