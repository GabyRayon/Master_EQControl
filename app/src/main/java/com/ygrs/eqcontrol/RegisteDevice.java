package com.ygrs.eqcontrol;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisteDevice extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe_device);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        setViews();
    }

    private void setViews() {
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.format_time));
        String currentDateandTime = sdf.format(new Date());

        EditText dateText=(EditText) findViewById(R.id.date_device);
        dateText.setEnabled(false);
        dateText.setText(currentDateandTime);
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
}
