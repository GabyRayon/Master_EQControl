package com.ygrs.eqcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ygrs.eqcontrol.Objects.FireBaseReference;
import com.ygrs.eqcontrol.Objects.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class register extends AppCompatActivity {

    EditText Nombre;
    EditText ApPaterno;
    EditText ApMaterno;
    Spinner Carrera;
    EditText Matricula;
    EditText Contrasena;
    EditText ConfigContrasena;

    Context context;

    String Foto="N/A";
    String Tipo_de_usuario="Alumno";
    String Vigencia_de_registro="";

    Button Acept;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context =this;

        SetView();
        SetCarrerasItem();

    }

    private void SetCarrerasItem() {
         ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this, R.array.carreras,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Carrera.setAdapter(adapter);
    }

    private void SetView() {

        Nombre=(EditText) findViewById(R.id.nameregister);
        ApPaterno=(EditText) findViewById(R.id.ap_paterno);
        ApMaterno=(EditText) findViewById(R.id.ap_materno);
        Carrera=(Spinner) findViewById(R.id.carrera);
        Matricula=(EditText) findViewById(R.id.matricula);
        Contrasena=(EditText) findViewById(R.id.password_register);
        ConfigContrasena=(EditText) findViewById(R.id.confirm_password_register);

        Acept=(Button) findViewById(R.id.buttonAcept);

        Acept.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.buttonAcept:
                    if(GetValidateData()){
                      if(isValidatePassword() && isLenghtpassValidate()){
                          SetValidityDate();
                          createUser();
                      }
                    }
                    break;


            }
        }
    };

    private void createUser() {

        String EmailEQControl="";

        User UserRegis= new User(Nombre.getText().toString(),
        ApPaterno.getText().toString(),
        ApMaterno.getText().toString(),
        Carrera.getSelectedItem().toString(),
        Matricula.getText().toString(),
        Contrasena.getText().toString(),
        Foto,
        Tipo_de_usuario,
        Vigencia_de_registro);

        EmailEQControl=Matricula.getText().toString()+context.getString(R.string.format_email);

        FirebaseDatabase Database= FirebaseDatabase.getInstance();
        DatabaseReference EQControlTable=Database.getReference(FireBaseReference.EQCONTROL);



        EQControlTable.child(FireBaseReference.USUARIOS).child(EmailEQControl).setValue(UserRegis).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        Matricula.getText().toString()+context.getString(R.string.format_email_register),
                        Contrasena.getText().toString());

                FirebaseAuth.getInstance().signOut();

                Intent gotoConfirm= new Intent(context, ConfirmUser.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_MAIL",Matricula.getText().toString()+context.getString(R.string.format_email_register));
                gotoConfirm.putExtras(bundle);
                startActivity(gotoConfirm);
            }
        });

    }

    private boolean isValidatePassword() {
        boolean isvalidate=false;

        if(Contrasena.getText().toString().equals(ConfigContrasena.getText().toString())){
            isvalidate=true;
            return isvalidate;
        }else{
            ConfigContrasena.setError(context.getString(R.string.validate_conficontrasena));
            isvalidate=false;
            return isvalidate;
        }
    }

    private boolean isLenghtpassValidate(){
        boolean isvalidate=false;
        if(Contrasena.getText().toString().trim().length()>5){
            isvalidate=true;
            return isvalidate;
        }else{
            Contrasena.setError(context.getString(R.string.validate_largoconficontrasena));
            isvalidate=false;
            return isvalidate;
        }
    }

    private boolean GetValidateData() {

        boolean isvalidate=true;

        if (Nombre.getText().toString().trim().equals("")) {
            Nombre.setError(context.getString(R.string.required_name));
            isvalidate=false;
        }
        if (ApPaterno.getText().toString().trim().equals("")) {
            ApPaterno.setError(context.getString(R.string.required_appaterno));
            isvalidate=false;
        }
        if (ApMaterno.getText().toString().trim().equals("")) {
            ApMaterno.setError(context.getString(R.string.required_apmaterno));
            isvalidate=false;
        }
        /*if (Carrera.getText().toString().trim().equals("")) {
            Carrera.setError(context.getString(R.string.required_carrera));
            isvalidate=false;
        }*/
        if (Matricula.getText().toString().trim().equals("")) {
            Matricula.setError(context.getString(R.string.required_matricula));
            isvalidate=false;
        }
        if (Contrasena.getText().toString().trim().equals("")) {
            Contrasena.setError(context.getString(R.string.required_contrasena));
            isvalidate=false;
        }
        if (ConfigContrasena.getText().toString().trim().equals("")) {
            ConfigContrasena.setError(context.getString(R.string.required_conficontrasena));
            isvalidate=false;
        }

        return isvalidate;
    }

    private void SetValidityDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.format_time));
        Date vigencia=new Date();
        vigencia.setYear(1);
        Vigencia_de_registro = sdf.format(vigencia);
    }
}