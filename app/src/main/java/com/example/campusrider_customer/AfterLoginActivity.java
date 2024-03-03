package com.example.campusrider_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.campusrider_customer.Food.MainActivity;
import com.example.campusrider_customer.Food.activity.SelectAddressActivity;
//import com.example.campusrider_customer.Grocery.GroceryMainActivity;
import com.example.campusrider_customer.Grocery.GroceryMainActivity;
import com.example.campusrider_customer.session.SharedPrefManager;

public class AfterLoginActivity extends AppCompatActivity {
    ImageView food,grocery;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());

        food=findViewById(R.id.food_btn);
        grocery=findViewById(R.id.grocery_btn);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AfterLoginActivity.this, SelectAddressActivity.class);
                startActivity(intent);
            }
        });
        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AfterLoginActivity.this, GroceryMainActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onStart() {
        super.onStart();
        if(!sharedPrefManager.isLoggedIn()){
            Intent intent=new Intent(AfterLoginActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}