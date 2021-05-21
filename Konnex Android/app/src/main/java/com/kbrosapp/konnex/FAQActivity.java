package com.kbrosapp.konnex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FAQActivity extends AppCompatActivity {

    Button askBotButton;
    EditText queryEditText;
    TextView answerTextView;

    EditText ipEditText;
    Button ipButton;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        SharedPreferences sharedPreferences= getSharedPreferences("com.kbrosapp.konnex", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEdit = sharedPreferences.edit();


        askBotButton=findViewById(R.id.askBotButton);
        queryEditText=findViewById(R.id.queryEditText);
        answerTextView=findViewById(R.id.answerTextView);

        ipEditText=findViewById(R.id.ipEditText);
        ipButton=findViewById(R.id.ipButton);

        ipEditText.setText(sharedPreferences.getString("ip","192.168.1.1"));

        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipString=ipEditText.getText().toString();
                sharedPreferencesEdit.putString("ip",ipString).apply();
            }
        });


        OkHttpClient client = new OkHttpClient();

        askBotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String query=queryEditText.getText().toString();
                String local_systems_ip = sharedPreferences.getString("ip","192.168.1.1");
                String url = "http://"+local_systems_ip+":5000/?query="+query;

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();

                        ContextCompat.getMainExecutor(getApplicationContext()).execute(()  -> {
                            // This is where your UI code goes.
                            Toast.makeText(FAQActivity.this, "Please set-up the Chatbot backend!", Toast.LENGTH_LONG).show();
                        });


                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();
                            FAQActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject reader = new JSONObject(myResponse);
                                        answerTextView.setText(reader.getString("chatbot"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(FAQActivity.this, "Please set-up the Chatbot backend!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }
}