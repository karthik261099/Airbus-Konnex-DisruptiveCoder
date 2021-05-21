package com.kbrosapp.konnex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BugActivity extends AppCompatActivity {

    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);

        submitButton=findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BugActivity.this, "Your feedback has been recorded along with your screeshot!\n-Konnex Airbus DisruptiveCoder", Toast.LENGTH_LONG).show();
            }
        });
    }
}