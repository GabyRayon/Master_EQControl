package com.ygrs.eqcontrol;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ygrs.eqcontrol.Objects.FireBaseReference;
import com.ygrs.eqcontrol.Objects.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context context;
    TextView textUser;
    DrawerLayout drawer;
    String userMain=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        userMain=getUserMain();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setListenerClicks();
        setViews();
        ListenerFireBaseUser();


    }

    private void ListenerFireBaseUser() {
        if(userMain!=null){
            FirebaseDatabase Database = FirebaseDatabase.getInstance();
            DatabaseReference EQControlTable = Database.getReference(FireBaseReference.EQCONTROL);
            EQControlTable.child(FireBaseReference.USUARIOS).child(userMain).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("Valores de usuario:", dataSnapshot.toString());
                    User usu = dataSnapshot.getValue(User.class);

                    if(usu.getTipo_de_usuario().toString().equals("Alumno")) {
                        generateQR(generateJsonFormat(usu));
                    }else if(usu.getTipo_de_usuario().toString().equals("Oficial")){
                        Intent gotosecurity = new Intent(context, SecurityActivity.class);
                        startActivity(gotosecurity);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("MI ERROR::::", databaseError.toString());
                }
            });
        }// colocar un respaldo al guardar el ultimos datasnapshot preferences android
    }

    private String generateJsonFormat(User usu) {
        String Joson_format="{"+
                "\"apellido_Materno\"="+"\""+usu.getApellido_Materno()+"\","+
                "\"apellido_Paterno\"="+"\""+usu.getApellido_Paterno()+"\","+
                "\"carrera\"="+"\""+usu.getCarrera()+"\","+
                "\"contrasena\"="+"\""+usu.getContrasena()+"\","+
                "\"foto\"="+"\""+usu.getFoto()+"\","+
                "\"matricula\"="+"\""+usu.getMatricula()+"\","+
                "\"nombre\"="+"\""+usu.getNombre()+"\","+
                "\"tipo_de_usuario\"="+"\""+usu.getTipo_de_usuario()+"\","+
                "\"vigencia_de_registro\"="+"\""+usu.getVigencia_de_registro()+"\""+
                "}";
        return Joson_format;
    }

    private String getUserMain(){
        String[] user_default=null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            user_default=email.split("\\.");
        }

        return user_default[0];
    }

    private void generateQR(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.QR_content)).setImageBitmap(bmp);

        } catch (WriterException e) {

        }
    }

    private void setViews() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.textUser);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            text.setText(email);

        }


    }

    private void setListenerClicks() {

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {


            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.register_equipment) {
            Intent gotoRegisterDevice = new Intent(context, RegisteDevice.class);
            startActivity(gotoRegisterDevice);

        } else if (id == R.id.consulting_equipment) {
            Intent gotoconsulta = new Intent(context, Consult_Equipment.class);
            startActivity(gotoconsulta);

        } else if (id == R.id.edit_register) {
            Intent gotoeditar = new Intent(context, Editar_Registro.class);
            startActivity(gotoeditar);

        } else if (id == R.id.delete_register) {

        } else if (id == R.id.help) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "50626460"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(context, "Necesitas autorización de permisos", Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);

        } else if (id == R.id.exit) {
            FirebaseAuth.getInstance().signOut();
            Intent gotologin= new Intent(context, Login.class);
            startActivity(gotologin);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
