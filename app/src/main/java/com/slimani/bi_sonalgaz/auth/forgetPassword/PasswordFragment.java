package com.slimani.bi_sonalgaz.auth.forgetPassword;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.slimani.bi_sonalgaz.R;


public class PasswordFragment extends Fragment {

    View view;
    static EditText password_text;
    static EditText condirm_password_text;
    public static String password;
    public static String confirm_password;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password, container, false);
        password_text = view.findViewById(R.id.password_text);
        condirm_password_text = view.findViewById(R.id.condirm_password_text);

        return view;
    }

    public static String getPassword() {
        password = password_text.getText().toString();
        return password;
    }


    public static String getConfirm_password() {
        confirm_password = condirm_password_text.getText().toString();
        return confirm_password;
    }
}
