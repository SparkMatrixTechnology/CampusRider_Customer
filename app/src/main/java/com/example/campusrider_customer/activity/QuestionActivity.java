package com.example.campusrider_customer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.campusrider_customer.Adapter.HelpAdapter;
import com.example.campusrider_customer.R;
import com.example.campusrider_customer.models.HelpModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    TextView qs,ans;
    String ques,answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        qs=findViewById(R.id.qs);
        ans=findViewById(R.id.ans);

        ques=getIntent().getStringExtra("ques");
        answer=getIntent().getStringExtra("ans");

        qs.setText(ques);
        ans.setText(answer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}