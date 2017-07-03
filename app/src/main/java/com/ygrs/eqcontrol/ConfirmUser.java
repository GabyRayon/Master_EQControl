package com.ygrs.eqcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmUser extends AppCompatActivity {

    TextView txtname_confirm;
    Button Acept;
    Context context;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_user);

        context=this;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mail = extras.getString("KEY_MAIL");
        }


        getviews();
        setUserName();
    }

    private void setUserName() {

        //String user= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        txtname_confirm.setText(mail);
    }

    private void getviews() {
        txtname_confirm=(TextView) findViewById(R.id.txtname_confirm);
        Acept =(Button) findViewById(R.id.buttonAcept);

        Acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoConfirm= new Intent(context, MainActivity.class);
                startActivity(gotoConfirm);
            }
        });
    }
}
