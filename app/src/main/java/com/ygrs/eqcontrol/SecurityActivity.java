package com.ygrs.eqcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.ygrs.eqcontrol.Objects.User;

import org.json.JSONException;
import org.json.JSONObject;

public class SecurityActivity extends AppCompatActivity {

    DataSnapshot dataSnapshot=null;
    User user;
    Context context;
    static boolean isCancelAction=false;
    TextView txtcontent;
    Button button_accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        context=this;

        setview();

        Toast.makeText(context, context.getString(R.string.Exit_security_toast).toString(),
                Toast.LENGTH_LONG).show();

    }

    private void setview() {
        txtcontent=(TextView) findViewById(R.id.txtcontent);
        button_accept=(Button) findViewById(R.id.buttonAcept_login);

        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // custom dialog
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_generic);

        // Tittle
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(context.getString(R.string.Warning));

        // Text
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(context.getString(R.string.Content_dialog_exit));


        Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonNOK = (Button) dialog.findViewById(R.id.dialogButtonNOK);

        dialogButtonNOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // if button is clicked, close the custom dialog
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent gotologin= new Intent(context, Login.class);
                startActivity(gotologin);
                //super.onBackPressed();
            }
        });

        dialog.show();
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
    if(!isCancelAction) {
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

        Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        startActivity(marketIntent);

        }

        }
        super.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                isCancelAction=true;
                String contents =intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                try {
                    JSONObject mainObject = new JSONObject(contents);
                    user=new User(mainObject.get("nombre").toString(),
                            mainObject.get("apellido_Paterno").toString(),
                            mainObject.get("apellido_Materno").toString(),
                            mainObject.get("carrera").toString(),
                            mainObject.get("matricula").toString(),
                            mainObject.get("contrasena").toString(),
                            mainObject.get("foto").toString(),
                            mainObject.get("tipo_de_usuario").toString(),
                            mainObject.get("vigencia_de_registro").toString()
                    );

                        //Toast.makeText(this, "matricula :"+user.getMatricula().toString(), Toast.LENGTH_LONG).show();

                    String info=user.getNombre()+" "+user.getApellido_Paterno()+" "+user.getApellido_Materno()+
                            " \n"+"Carrera:"+user.getCarrera()+"\nMatricula:"+user.getMatricula()+"\n"+
                            "Vigencia:"+user.getVigencia_de_registro();

                    txtcontent.setText(info);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(this, "Contenido :"+contents, Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                isCancelAction=true;
                // custom dialog
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.dialog_generic);

                // Tittle
                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText(context.getString(R.string.Warning));

                // Text
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText(context.getString(R.string.Content_dialog_exit));


                Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
                Button dialogButtonNOK = (Button) dialog.findViewById(R.id.dialogButtonNOK);

                dialogButtonNOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {

                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                            startActivityForResult(intent, 0);

                        } catch (Exception e) {

                            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            startActivity(marketIntent);

                        }
                    }
                });

                // if button is clicked, close the custom dialog
                dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseAuth.getInstance().signOut();
                        Intent gotologin= new Intent(context, Login.class);
                        startActivity(gotologin);
                        //super.onBackPressed();
                    }
                });

                dialog.show();
            }else{

            }
        }
    }
}
