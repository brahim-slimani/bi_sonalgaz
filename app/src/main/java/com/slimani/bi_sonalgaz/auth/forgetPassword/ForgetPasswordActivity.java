package com.slimani.bi_sonalgaz.auth.forgetPassword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.auth.LoginActivity;
import com.slimani.bi_sonalgaz.emailHelper.GMailSender;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoUser;

import java.util.Random;


public class ForgetPasswordActivity extends AppCompatActivity {

    Button continue_btn;
    Button confirm_btn;
    Random random = new Random();
    int randValue;
    PojoUser user = new PojoUser();
    private static final String TAG = "ForgetPasswordActivity";
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        continue_btn = findViewById(R.id.continue_btn);
        confirm_btn = findViewById(R.id.confirm_btn);
        boolean b = false;

        loadFragment(new EmailFragment(), R.id.dynamique_frame);

        final Dialog dialog = new Dialog(ForgetPasswordActivity.this);
        dialog.setContentView(R.layout.popup_confirmation_code);
        EditText code_text = dialog.findViewById(R.id.code_text);
        Button btn_confirm = dialog.findViewById(R.id.confirm_code_btn);
        dialog.setCanceledOnTouchOutside(false);

        final ProgressDialog dialogWaitUpdating = new ProgressDialog(ForgetPasswordActivity.this);
        dialogWaitUpdating.setTitle("Password updating");
        dialogWaitUpdating.setMessage("Please wait");

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            Service service = new Service();
                            DataManager dataManager = new DataManager();

                            response = service.getConnection(dataManager.getCurrentIPaddress(ForgetPasswordActivity.this),
                                    dataManager.getCurrentPortNumber(ForgetPasswordActivity.this), getApplicationContext());

                            System.out.println(response);
                            System.out.println(dataManager.getCurrentIPaddress(ForgetPasswordActivity.this));

                            if(response.equals("connection successful")){

                                if(EmailFragment.getEmail().isEmpty()){

                                    ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alert("The field must not be empty", R.drawable.problem, "Error", false);

                                        }
                                    });

                                }else if(!EmailFragment.getEmail().contains("@") || !EmailFragment.getEmail().contains(".") ) {

                                    ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alert("Please introduce a valid address mail !", R.drawable.problem, "Error", false);

                                        }
                                    });


                                }else{

                                    String mail = service.checkEmail("/checkEmail?param="+ EmailFragment.getEmail(), ForgetPasswordActivity.this);

                                    if(mail.equals(EmailFragment.getEmail())){

                                        ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                randValue = random.nextInt(999999);

                                                try {
                                                    sendMessage("SONELGAZ BI tool Confirmation Code",
                                                            "Your email confirmation code is: " + randValue,
                                                            "sonelgazelit@gmail.com",
                                                            EmailFragment.getEmail(),
                                                            "sonelgazelit@gmail.com",
                                                            "Sonelgaz/elit2019");

                                                    final ProgressDialog progressdialog = new ProgressDialog(ForgetPasswordActivity.this);
                                                    progressdialog.setMessage("Sending verification code...");
                                                    progressdialog.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        public void run() {
                                                            progressdialog.dismiss();
                                                            dialog.show();

                                                        }
                                                    }, 3000);



                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                    ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            alert("Issue connection ! problem internet connection", R.drawable.problem, "Error", false);

                                                        }
                                                    });
                                                }
                                            }
                                        });



                                    }else {
                                        ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                alert("This address mail doesn't match to your account", R.drawable.problem, "Warning !", false);

                                            }
                                        });
                                    }



                                }

                            }else{
                                ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        alert("Issue connection ! connection to server cannot established", R.drawable.problem, "Warning !", false);

                                    }
                                });


                            }

                        }catch (Exception e){
                            e.printStackTrace();

                            ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    alert("Issue connection ! connection to server cannot established", R.drawable.problem, "Warning !", false);

                                }
                            });

                        }



                    }
                });
                thread.start();



            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(code_text.getText().toString().isEmpty()){
                    alert("The field must not be empty", R.drawable.problem, "Error", false);
                }else if (randValue == Integer.valueOf(code_text.getText().toString()) ) {
                    dialog.dismiss();

                    continue_btn.setVisibility(View.INVISIBLE);
                    confirm_btn.setVisibility(View.VISIBLE);
                    alert("Confirmation established successfully" , R.drawable.success, "Succes", false);
                    loadFragment(new PasswordFragment(), R.id.dynamique_frame);

                } else {
                    alert("Code incorrect", R.drawable.problem, "Error", false);
                }
            }
        });


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PasswordFragment.getPassword().equals(PasswordFragment.getConfirm_password()) && !PasswordFragment.getPassword().isEmpty()) {

                    dialogWaitUpdating.show();
                    user.setEmail(EmailFragment.getEmail());
                    user.setPassword(PasswordFragment.getPassword());
                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Service service = new Service(getApplicationContext());


                            String response = service.forgotPassword("/forgotPassword", user);
                            if (response.equals("Operation failed, REST issue !")) {

                                ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else if (response.equals("Password updated successfully !")) {
                                ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
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
                    alert("Enter and confirm your new password", R.drawable.problem, "Error", false);

                }


            }
        });


    }


    public void loadFragment(Fragment fragment, int fragmentId) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }


    private void sendMessage(String subject, String message, String from, String to, String
            user, String password) {
        final ProgressDialog dialog = new ProgressDialog(ForgetPasswordActivity.this);
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
                    final ProgressDialog progressdialog = new ProgressDialog(ForgetPasswordActivity.this);
                    progressdialog.setMessage("Progressing....");
                    progressdialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressdialog.dismiss();
                            Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }, 1000);
                }
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
