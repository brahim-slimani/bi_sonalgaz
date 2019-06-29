package com.slimani.bi_sonalgaz.auth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.home.HomeActivity;
import com.slimani.bi_sonalgaz.paramsSQLite.Db_server;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoUser;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    Db_server dbm = new Db_server(this);
    public Cursor cursor;

    String response = new String();
    public static String token = new String();

    public static String roleUser;
    public static String loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText username_field = (EditText) findViewById(R.id.username);
        final EditText password_field = (EditText) findViewById(R.id.password);
        final Button login_btn = (Button) findViewById(R.id.login_submit_btn);
        final TextView help_login = (TextView) findViewById(R.id.help_login_panel);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(!validate(username_field, password_field)){
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "The fields must not be empty !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Service service = new Service(getApplicationContext());
                            PojoUser user = new PojoUser(username_field.getText().toString(),password_field.getText().toString());
                            token = service.Token(user);
                            roleUser = service.getRoleUser("/role?username="+username_field.getText().toString());


                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(token.equals("login error !")){
                                        Toast.makeText(getApplicationContext(), "Error login, username or password are not incorrect !", Toast.LENGTH_SHORT).show();

                                    }else{
                                        if(validateToken(token)){
                                            loggedUser = username_field.getText().toString();
                                            final ProgressDialog progressdialog = new ProgressDialog(LoginActivity.this);
                                            progressdialog.setMessage("Logging....");
                                            progressdialog.show();
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    progressdialog.dismiss();
                                                    Intent intent =new Intent(LoginActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            }, 2000);
                                        }
                                    }
                                }
                            });

                        }
                    }
                });
                thread.start();



            }


        });

        help_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.popup_help_login);

                final Button connection_btn = (Button) dialog.findViewById(R.id.connection_btn);
                final EditText ip_address = (EditText) dialog.findViewById(R.id.ipAddress_field);
                final EditText port_number = (EditText) dialog.findViewById(R.id.portNumber_field);

                try {

                    if(initServer()){
                        DataManager dataManager = new DataManager();
                        ip_address.setText(dataManager.getCurrentIPaddress(getApplicationContext()));
                        port_number.setText(dataManager.getCurrentPortNumber(getApplicationContext()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.show();

                connection_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ip_address.getText().toString().equals("") || port_number.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "You should enter the two fields !", Toast.LENGTH_SHORT).show();
                        }else{

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Service service = new Service(getApplicationContext());
                                    response = service.testConnection(ip_address.getText().toString(), port_number.getText().toString());

                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(response.equals("connection successful")){
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Connection was established successfully !", Toast.LENGTH_SHORT).show();

                                                try {
                                                    if(initServer()){
                                                        dbm.refreshServer(1,ip_address.getText().toString(), port_number.getText().toString());
                                                    }else{
                                                        dbm.initServer(1,ip_address.getText().toString(), port_number.getText().toString());
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
        });


    }

    public Boolean validate(EditText username, EditText password){
        if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
            return true;
        }else
        return false;
    }

    public Boolean validateToken(String token){
        try {
            if(token.substring(0,5).equals("Token")){
                return true;
            }else
                return false;
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Connection cannot established", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public boolean initServer() throws JSONException {
        boolean b;
        cursor = dbm.getParamServer(1);
        if(cursor.getCount() == 0){
            b = false;
        }else b = true;

        return b;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(intent);
    }



}
