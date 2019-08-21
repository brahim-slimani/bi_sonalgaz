package com.slimani.bi_sonalgaz.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.AdhocActivity;
import com.slimani.bi_sonalgaz.auth.LoginActivity;
import com.slimani.bi_sonalgaz.dashboards.DashboardActivity;
import com.slimani.bi_sonalgaz.dashboards.DashboardListActivity;
import com.slimani.bi_sonalgaz.emailHelper.GMailSender;
import com.slimani.bi_sonalgaz.reports.ReportListActivity;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoUser;
import com.slimani.bi_sonalgaz.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.slimani.bi_sonalgaz.auth.LoginActivity.loggedUser;

public class HomeActivity extends AppCompatActivity {

    String email;
    boolean b = false;
    int randValue;
    PojoUser user = new PojoUser();
    private static final String TAG = "HomeActivity";
    JSONArray emailsJSONArray = new JSONArray();
    List<String> emailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final CardView dash_card = findViewById(R.id.dash_btn);
        final CardView report_card = findViewById(R.id.report_btn);
        final CardView adhoc_card = findViewById(R.id.adhoc_btn);
        final CardView setting_card = findViewById(R.id.setting_btn);
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.popup_update_password);
        EditText password_text = dialog.findViewById(R.id.password);
        EditText confirm_password_text = dialog.findViewById(R.id.confirm_password);
        EditText email_text = dialog.findViewById(R.id.email_user);
        Button save_btn = dialog.findViewById(R.id.save_btn);
        dialog.setCanceledOnTouchOutside(false);

        final Dialog dialog2 = new Dialog(HomeActivity.this);
        dialog2.setContentView(R.layout.popup_confirmation_code);
        EditText code_text = dialog2.findViewById(R.id.code_text);
        Button btn_confirm = dialog2.findViewById(R.id.confirm_code_btn);
        dialog2.setCanceledOnTouchOutside(false);

        final ProgressDialog dialogWaitUpdating = new ProgressDialog(HomeActivity.this);
        dialogWaitUpdating.setTitle("Password updating");
        dialogWaitUpdating.setMessage("Please wait");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager dataManager = new DataManager();
                Service service = new Service(getApplicationContext());
                try {
                    email = service.getEmail("/checkEmail?username=" + loggedUser);
                    Log.v("TAG", "email: " + email);

                    HomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (email.equals("email doesn't exist")) {
                                dialog.show();
                            }

                        }
                    });
                } catch (Exception e) {
                    Log.v("TAG", "exception message: " + e.getMessage() + " |||");
                    e.printStackTrace();

                }
            }
        });

        thread.start();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Service service = new Service(getApplicationContext());
                        try {
                            emailsJSONArray = service.consumesRest("/usersEmails");
                            if (emailsJSONArray != null) {
                                for (int i = 0; i < emailsJSONArray.length(); i++) {

                                    emailsList.add(emailsJSONArray.getString(i));
                                }

                            }
                            int i = 0;
                            for (String x : emailsList) {
                                i++;
                                Log.v("TAG", "email json array: " + i + " " + x);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread1.start();
                Log.v("TAG", "exist: " + existsEmail(email_text.getText().toString(), emailsList));
                if (existsEmail(email_text.getText().toString(), emailsList)) {
                    alert("The email address that you've is already used", R.drawable.problem, "Error", false);
                } else {
                    dialogWaitUpdating.show();
                    if (password_text.getText().toString().equals(confirm_password_text.getText().toString()) && password_text.length() >= 5 && email_text.getText() != null) {
                        user.setUsername(loggedUser);
                        user.setEmail(email_text.getText().toString());
                        user.setPassword(password_text.getText().toString());
                        Random random = new Random();
                        randValue = random.nextInt(999999);
                        Log.v("TAG", "randValue: " + randValue);
                        List<String> emails = new ArrayList<>();
                        emails.add(email_text.getText().toString());
                        b = true;
                        dialog.dismiss();
                        dialog2.show();

                        sendMessage("SONELGAZ BI tool Confirmation Code",
                                "Your email confirmation code is: " + randValue,
                                "sonelgazelit@gmail.com",
                                email_text.getText().toString(),
                                "sonelgazelit@gmail.com",
                                "Sonelgaz/elit2019");

                    } else if (password_text.getText().toString() != confirm_password_text.getText().toString()) {
                        alert("Passowrd doesn't match", R.drawable.problem, "Error", false);

                    } else if (password_text.length() < 5) {
                        alert("Passowrd length should be greater then 5", R.drawable.problem, "Error", false);

                    }

                }
            }

        });

        Log.v("TAG", "exist: " + existsEmail(email_text.getText().toString(), emailsList));

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (randValue == Integer.valueOf(code_text.getText().toString())) {

                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Service service = new Service(getApplicationContext());
                            String response = service.updateUser("/updateUser", user);
                            if (response.equals("Operation failed, REST issue !")) {

                                HomeActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else if (response.equals("Password updated successfully !")) {
                                HomeActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        dialogWaitUpdating.dismiss();
                                        alert("Password updated successfully", R.drawable.success, "Success", true);

                                    }
                                });
                            }

                        }
                    });

                    thread2.start();
                } else {
                    alert("code incorrect", R.drawable.problem, "Error", false);

                }
            }
        });


        adhoc_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AdhocActivity.class);
                startActivity(intent);
            }
        });

        setting_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        report_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReportListActivity.class);
                startActivity(intent);
            }
        });

        dash_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DashboardListActivity.class);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_item:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.popup_about);
                Button btnOK = dialog.findViewById(R.id.aboutOk_btn);

                dialog.show();
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


        }
        switch (item.getItemId()) {

            case R.id.logout_item:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Confirmation..?");
                alertDialogBuilder.setIcon(R.drawable.ask);
                alertDialogBuilder.setMessage("Are you sure that you want to logout ?");


                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                        final ProgressDialog progressdialog = new ProgressDialog(HomeActivity.this);
                        progressdialog.setMessage("Logout....");
                        progressdialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressdialog.dismiss();
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }, 1000);

                    }
                });

                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    public void alert(String msg, int idDrawable, String title, boolean b) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(idDrawable);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                if (b) {
                    final ProgressDialog progressdialog = new ProgressDialog(HomeActivity.this);
                    progressdialog.setMessage("Logout....");
                    progressdialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressdialog.dismiss();
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }, 1000);
                }
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sendMessage(String subject, String message, String from, String to, String user, String password) {
        final ProgressDialog dialog = new ProgressDialog(HomeActivity.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(user, password);
                    sender.sendMail(subject,
                            message,
                            from,
                            to);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    public boolean existsEmail(String email, List<String> list) {
        boolean b = false;
        for (String x : list) {
            if (email.equals(x)) {
                b = true;
                break;
            }
        }
        return b;
    }




}
