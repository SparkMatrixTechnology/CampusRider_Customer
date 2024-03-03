package com.example.campusrider_customer.Food.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.campusrider_customer.Food.MainActivity;
import com.example.campusrider_customer.Food.adapters.CartAdapter;
import com.example.campusrider_customer.Food.models.ProductModel;
import com.example.campusrider_customer.databinding.ActivityCheckoutBinding;
import com.example.campusrider_customer.session.SharedPrefManager;
import com.example.campusrider_customer.utils.Constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    public interface feeCallBack{
        void onSuccess(int response);
        void onError(int error);
    }
    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    SharedPrefManager sharedPrefManager;
    int vendor_id;
    int total_price=0,subtotal,delivery;
    ArrayList<ProductModel> productModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager=new SharedPrefManager(getApplicationContext());

        binding.nameText.setText(sharedPrefManager.getUser().getCustomer_name());
        binding.phoneText.setText(sharedPrefManager.getUser().getCustomer_phone());
        binding.addressBox.setText(sharedPrefManager.getUser().getAddress());
      getFee(new feeCallBack() {
      @Override
      public void onSuccess(int response) {
        delivery=response;
        binding.deliveryFee.setText("TK "+ delivery);
          productModels=new ArrayList<>();
          Cart cart = TinyCartHelper.getCart();
          for(Map.Entry<Item,Integer> item:cart.getAllItemsWithQty().entrySet()) {
              ProductModel product = (ProductModel) item.getKey();
              int quantity = item.getValue();
              product.setQuantity(quantity);
              vendor_id=product.getVendor_id();
              productModels.add(product);
          }
          adapter=new CartAdapter(CheckoutActivity.this, productModels, new CartAdapter.CartListener() {
              @Override
              public void onQuantityChanged() {
                  binding.subtotal.setText("TK "+ cart.getTotalPrice());

              }
          });
          LinearLayoutManager layoutManager=new LinearLayoutManager(CheckoutActivity.this);
          DividerItemDecoration itemDecoration=new DividerItemDecoration(CheckoutActivity.this,layoutManager.getOrientation());
          binding.cartList.setLayoutManager(layoutManager);
          binding.cartList.addItemDecoration(itemDecoration);
          binding.cartList.setAdapter(adapter);
          binding.subtotal.setText("TK "+ cart.getTotalPrice());
          subtotal=cart.getTotalPrice().intValue();
          total_price=cart.getTotalPrice().intValue()+delivery;
          binding.total.setText("TK "+total_price);
          binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  processOrder();
              }
          });


      }

    @Override
    public void onError(int error) {

    }
});
        productModels=new ArrayList<>();
        Cart cart = TinyCartHelper.getCart();
        for(Map.Entry<Item,Integer> item:cart.getAllItemsWithQty().entrySet()) {
            ProductModel product = (ProductModel) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);
            vendor_id=product.getVendor_id();
            productModels.add(product);
        }
        adapter=new CartAdapter(this, productModels, new CartAdapter.CartListener() {
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
        subtotal=cart.getTotalPrice().intValue();
        total_price=cart.getTotalPrice().intValue()+delivery;

        binding.total.setText("TK "+total_price);

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processOrder();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    public void processOrder() {

        int customer_id = sharedPrefManager.getUser().getCustomer_id();

        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {

            productOrder.put("customer_id", customer_id);
            productOrder.put("vendor_id", vendor_id);
            productOrder.put("address", binding.addressBox.getText().toString());
            productOrder.put("cost",subtotal);
            productOrder.put("delivery_fee",delivery);
            productOrder.put("total_price",total_price);
            productOrder.put("comment", binding.commentBox.getText().toString());
            productOrder.put("payment_type","Cash on Delivery");
            productOrder.put("payment_status","Unpaid");
            productOrder.put("order_status","Placed");
            productOrder.put("order_date",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()));

            dataObject.put("product_order", productOrder);
            Log.e("err", dataObject.toString());

        } catch (JSONException e) {
        }

        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
        Log.e("err1", dataObject.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getJSONObject("data").getString("status").equals("success")) {
                        String orderId = response.getJSONObject("data").getString("order_id");
                        process_order_details(orderId);
                        //Toast.makeText(CheckoutActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(CheckoutActivity.this,MainActivity.class));
                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Successful")
                                .setCancelable(false)
                                .setMessage("Your order number is: " + orderId)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                    else {
                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Failed")
                                .setMessage("Something went wrong, please try again.")
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                        Toast.makeText(CheckoutActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.e("res", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        queue.add(request);
    }
    public void process_order_details(String orderId){

        JSONObject dataObject = new JSONObject();
        try {

            JSONArray product_order_detail = new JSONArray();
            Cart cart = TinyCartHelper.getCart();
            for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                ProductModel product = (ProductModel) item.getKey();
                int quantity = item.getValue();
                int price=product.getPrice()*quantity;
                product.setQuantity(quantity);
                JSONObject productObj = new JSONObject();
                productObj.put("order_id",orderId);
                productObj.put("product_id", product.getId());
                productObj.put("quantity", quantity);
                productObj.put("vendor_id",vendor_id);
                productObj.put("price", price);
                productObj.put("order_date",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()));

                product_order_detail.put(productObj);
            }

            dataObject.put("product_order_detail", product_order_detail);

        } catch (JSONException e) {
        }

        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_DETAILS_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getJSONObject("data").getString("status").equals("success")) {
                        Toast.makeText(CheckoutActivity.this, "from product order details", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        queue.add(request);
    }

    public void getFee(feeCallBack feeCallBack){
        RequestQueue queue= Volley.newRequestQueue(CheckoutActivity.this);
        StringRequest request =new StringRequest(Request.Method.POST, Constants.GET_delivery_fee_URL+sharedPrefManager.getUser().getArea(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                            int fee=mainObj.getInt("delivery_fee");
                            feeCallBack.onSuccess(fee);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(request);
    }


}