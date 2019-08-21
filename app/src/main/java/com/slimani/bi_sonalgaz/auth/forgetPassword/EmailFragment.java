package com.slimani.bi_sonalgaz.auth.forgetPassword;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.slimani.bi_sonalgaz.R;

import java.util.Random;


public class EmailFragment extends Fragment {

    View view;
    static EditText email_text;
    public static String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_email, container, false);

        email_text = view.findViewById(R.id.email_text);

        return view;
    }

    public static String getEmail() {
        email = email_text.getText().toString();
        return email;
    }
}
