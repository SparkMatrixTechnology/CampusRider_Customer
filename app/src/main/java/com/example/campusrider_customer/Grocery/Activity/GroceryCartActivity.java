package com.example.campusrider_customer.Grocery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.campusrider_customer.Food.activity.CartActivity;
import com.example.campusrider_customer.Food.activity.CheckoutActivity;
import com.example.campusrider_customer.Food.adapters.CartAdapter;
import com.example.campusrider_customer.Grocery.Adapter.GroceryCartAdapter;
import com.example.campusrider_customer.Grocery.Model.GroceryItemModel;
import com.example.campusrider_customer.R;
import com.example.campusrider_customer.databinding.ActivityCartBinding;
import com.example.campusrider_customer.databinding.ActivityGroceryCartBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class GroceryCartActivity extends AppCompatActivity {
    ActivityGroceryCartBinding binding;
    GroceryCartAdapter adapter;
    ArrayList<GroceryItemModel> productModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroceryCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productModels=new ArrayList<>();
        Cart cart = TinyCartHelper.getCart();
        for(Map.Entry<Item,Integer> item:cart.getAllItemsWithQty().entrySet()) {
            GroceryItemModel product = (GroceryItemModel) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);
            productModels.add(product);
        }

        adapter=new GroceryCartAdapter(this, productModels, new GroceryCartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText("TK "+ cart.getTotalPrice());
            }
        });

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);
        binding.subtotal.setText("TK "+ cart.getTotalPrice());


        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroceryCartActivity.this, GroceryCheckoutActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}