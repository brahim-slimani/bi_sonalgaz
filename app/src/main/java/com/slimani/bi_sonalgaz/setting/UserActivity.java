package com.slimani.bi_sonalgaz.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting);

        final Button addUser_btn = (Button) findViewById(R.id.addingUser_btn);
        final Button listUser_btn = (Button) findViewById(R.id.usersList_btn);



        List<String> roles = new ArrayList<>();
        roles.add(new String("ROLE_MM"));
        roles.add(new String("ROLE_SD"));
        roles.add(new String("ROLE_DD"));
        roles.add(new String("ROLE_AG"));
        roles.add(new String("ROLE_ADMIN"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, roles);
        //Service service = new Service(getApplicationContext());

        addUser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserActivity.this);
                dialog.setContentView(R.layout.popup_adding_user);

                final Button saveUser_btn = (Button) dialog.findViewById(R.id.save_user_btn);
                final Spinner roles_list = (Spinner) dialog.findViewById(R.id.roles_spinner);
                final EditText username = (EditText) dialog.findViewById(R.id.username_text);
                final EditText password = (EditText) dialog.findViewById(R.id.password_text);

                roles_list.setAdapter(adapter);



                dialog.show();
                saveUser_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "You must fill the user's fields !", Toast.LENGTH_SHORT).show();
                        }else{

                            PojoUser user;

                            user = new PojoUser(username.getText().toString(),
                                    password.getText().toString(),roles_list.getSelectedItem().toString());


                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Service service = new Service(getApplicationContext());
                                    String response = service.saveUser(
                                            "/register",user);

                                    if(response.equals("Registration failed, this username already exists !")){


                                        UserActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "This username already exists !", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }else if(response.equals("User registred successfully !")){

                                        UserActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                successAlert(response);
                                            }
                                        });


                                    }

                                }
                            });
                            thread.start();



                        }





                    }

                });



            }
        });



        listUser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(service.test("/allReports"));

            }
        });

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
