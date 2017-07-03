package com.ygrs.eqcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Context context;
    EditText Usuario;
    EditText Contrasena;
    FirebaseAuth.AuthStateListener mAuth;
    boolean isLoginPress=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        context=this;

        getview();
        setListenerClicks();
        ListenerAuth();


    }

    private void validateAuth() {
        // custom dialog
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_generic);

        // Tittle
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(context.getString(R.string.Warning));

        // Text
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(context.getString(R.string.Content_dialog));


        Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonNOK = (Button) dialog.findViewById(R.id.dialogButtonNOK);

        dialogButtonNOK.setVisibility(View.GONE);

        // if button is clicked, close the custom dialog
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void ListenerAuth() {

        mAuth= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if(user!=null){
                    Intent gotomain = new Intent(context, MainActivity.class);
                    startActivity(gotomain);
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuth!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuth);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuth);
    }

    private void getview() {
        Usuario=(EditText) findViewById(R.id.name);
        Contrasena=(EditText) findViewById(R.id.password);
    }

    private void setListenerClicks() {

        Button Acept =(Button) findViewById(R.id.buttonAcept_login);
        TextView newAcound= (TextView) findViewById(R.id.txtnewacound);

        Acept.setOnClickListener(onClickListener);
        newAcound.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.buttonAcept_login:

                    if(Checkinfo()) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                Usuario.getText().toString(),Contrasena.getText().toString()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof FirebaseAuthException) {
                                    //((FirebaseAuthException) e).getErrorCode();
                                    validateAuth();
                                }
                            }
                        });
                        isLoginPress=true;
                    }
                    break;

                case R.id.txtnewacound:
                    Intent gotoCreateAcoun= new Intent(context, register.class);
                    startActivity(gotoCreateAcoun);
                    break;

            }
        }
    };

    private boolean Checkinfo() {
        boolean isvalidate=true;

        if (Usuario.getText().toString().trim().equals("")) {
            Usuario.setError(context.getString(R.string.required_user));
            isvalidate=false;
        }
        if (Contrasena.getText().toString().trim().equals("")) {
            Contrasena.setError(context.getString(R.string.required_password));
            isvalidate=false;
        }

        return isvalidate;
    }


}
