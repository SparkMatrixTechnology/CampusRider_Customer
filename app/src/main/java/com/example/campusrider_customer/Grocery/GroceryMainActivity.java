package com.example.campusrider_customer.Grocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusrider_customer.AfterLoginActivity;
import com.example.campusrider_customer.Food.MainActivity;
import com.example.campusrider_customer.Food.activity.CartActivity;
import com.example.campusrider_customer.Grocery.Activity.GroceryCartActivity;
import com.example.campusrider_customer.R;
import com.example.campusrider_customer.activity.LoginActivity;
import com.example.campusrider_customer.session.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusrider_customer.databinding.ActivityGroceryMainBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

public class GroceryMainActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityGroceryMainBinding binding;
    Button logout;
    ImageView profile_image;
    int cartQuantity=0;
    TextView profile_name,profile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        binding = ActivityGroceryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarGroceryMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headView=navigationView.getHeaderView(0);
        profile_image= headView.findViewById(R.id.profile_image);
        profile_name=headView.findViewById(R.id.profile_name);
        profile_number=headView.findViewById(R.id.profile_phone);
        logout=drawer.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.logout();

                Toast.makeText(GroceryMainActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(GroceryMainActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });

        String name=sharedPrefManager.getUser().getCustomer_name();
        String number=sharedPrefManager.getUser().getCustomer_phone();
        //String image= sharedPrefManager.getUser().getCustomer_image();
        profile_name.setText(name);
        profile_number.setText(number);
        if(name.charAt(0)=='A'){
            profile_image.setImageResource(R.drawable.a);
        }
        else if(name.charAt(0)=='B'){
            profile_image.setImageResource(R.drawable.b);
        }
        else if(name.charAt(0)=='C'){
            profile_image.setImageResource(R.drawable.c);
        }
        else if(name.charAt(0)=='D'){
            profile_image.setImageResource(R.drawable.d);
        }
        else if(name.charAt(0)=='E'){
            profile_image.setImageResource(R.drawable.d);
        }
        else if(name.charAt(0)=='F'){
            profile_image.setImageResource(R.drawable.f);
        }
        else if(name.charAt(0)=='G'){
            profile_image.setImageResource(R.drawable.g);
        }
        else if(name.charAt(0)=='H'){
            profile_image.setImageResource(R.drawable.h);
        }
        else if(name.charAt(0)=='I'){
            profile_image.setImageResource(R.drawable.i);
        }
        else if(name.charAt(0)=='J'){
            profile_image.setImageResource(R.drawable.j);
        }
        else if(name.charAt(0)=='K'){
            profile_image.setImageResource(R.drawable.k);
        }
        else if(name.charAt(0)=='L'){
            profile_image.setImageResource(R.drawable.l);
        }
        else if(name.charAt(0)=='M'){
            profile_image.setImageResource(R.drawable.m);
        }
        else if(name.charAt(0)=='N'){
            profile_image.setImageResource(R.drawable.n);
        }
        else if(name.charAt(0)=='O'){
            profile_image.setImageResource(R.drawable.o);
        }
        else if(name.charAt(0)=='P'){
            profile_image.setImageResource(R.drawable.p);
        }
        else if(name.charAt(0)=='Q'){
            profile_image.setImageResource(R.drawable.q);
        }
        else if(name.charAt(0)=='R'){
            profile_image.setImageResource(R.drawable.r);
        }
        else if(name.charAt(0)=='S'){
            profile_image.setImageResource(R.drawable.s);
        }
        else if(name.charAt(0)=='T'){
            profile_image.setImageResource(R.drawable.t);
        }
        else if(name.charAt(0)=='U'){
            profile_image.setImageResource(R.drawable.u);
        }
        else if(name.charAt(0)=='V'){
            profile_image.setImageResource(R.drawable.v);
        }
        else if(name.charAt(0)=='W'){
            profile_image.setImageResource(R.drawable.w);
        }
        else if(name.charAt(0)=='X'){
            profile_image.setImageResource(R.drawable.x);
        }
        else if(name.charAt(0)=='Y'){
            profile_image.setImageResource(R.drawable.y);
        }
        else if(name.charAt(0)=='Z'){
            profile_image.setImageResource(R.drawable.z);
        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pantry,R.id.nav_order,R.id.nav_help,R.id.nav_complain)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_grocery_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.grocery_main, menu);
        MenuItem menuItem=menu.findItem(R.id.cart);
        View actionView=menuItem.getActionView();
        TextView cart_badgeTextView=actionView.findViewById(R.id.count_badge);
        cart_badgeTextView.setText(String.valueOf(cartQuantity));
        Cart cart = TinyCartHelper.getCart();
        cart_badgeTextView.setVisibility(cart.isCartEmpty() ? View.GONE : View.VISIBLE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.cart){
            startActivity(new Intent(this, GroceryCartActivity.class));
        }
        if(item.getItemId()==R.id.back){
            startActivity(new Intent(this, AfterLoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_grocery_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}