// This activity controls the login of managers to the app-
// It gives the access only to the user that types the right password,
// and if the password is wrong it notifies the user


package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.dataObjects.Database;
import com.example.payeat.R;

public class ManagerLoginActivity extends AppCompatActivity implements  View.OnClickListener{

        private Button goToManagerOptions;
        private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);
        goToManagerOptions = (Button) findViewById(R.id.activity_manager_login_button);
        goToManagerOptions.setOnClickListener((View.OnClickListener) this);
        password=(EditText) findViewById(R.id.manager_password_text);
        password.requestFocus();
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                goToManagerOptions.callOnClick();
                return true;
            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.activity_manager_login_button :
                String Text=getText();
                if(Text.contentEquals(Database.getPassword()))
                    intent = new Intent(this, ManagerOptionsActivity.class);
                else
                    Toast.makeText(this, "סיסמה שגויה", Toast.LENGTH_SHORT ).show();
                break;
            default:


        }
        if(intent != null)
            startActivity(intent);
    }

    private String getText(){
        if( password== null)
            return "no notes";
        return password.getText().toString();

    }
}