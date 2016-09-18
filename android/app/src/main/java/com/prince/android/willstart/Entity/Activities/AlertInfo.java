package com.prince.android.willstart.Entity.Activities;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.prince.android.willstart.R;

public class AlertInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_info);

        final Dialog dialog = new Dialog(AlertInfo.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.alert_dialog_view);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        TextView text = (TextView) dialog.findViewById(R.id.alertText);
        dialog.show();


    }
}
