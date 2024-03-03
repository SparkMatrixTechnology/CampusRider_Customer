package com.example.campusrider_customer.Grocery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campusrider_customer.Food.adapters.CartAdapter;
import com.example.campusrider_customer.Food.models.ProductModel;
import com.example.campusrider_customer.Grocery.Model.GroceryItemModel;
import com.example.campusrider_customer.R;
import com.example.campusrider_customer.databinding.GroceryItemCartBinding;
import com.example.campusrider_customer.databinding.ItemCartBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class GroceryCartAdapter extends RecyclerView.Adapter<GroceryCartAdapter.GroceryCartViewHolder> {

    int quantity;
    Context context;
    ArrayList<GroceryItemModel> products;
    Cart cart;
   CartListener cartListener;

    public interface CartListener {
        public void onQuantityChanged();
    }

    public GroceryCartAdapter(Context context, ArrayList<GroceryItemModel> products, CartListener cartListener) {
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public GroceryCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroceryCartViewHolder(LayoutInflater.from(context).inflate(R.layout.grocery_item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryCartViewHolder holder, int position) {

        GroceryItemModel grocery=products.get(position);
        Glide.with(context).load(grocery.getImage()).into(holder.binding.detailedImg);
        quantity = grocery.getQuantity();
        holder.binding.foodName.setText(grocery.getName());
        holder.binding.price.setText("TK " +(grocery.getPrice()*quantity));
        holder.binding.quantity.setText(""+grocery.getQuantity());
        holder.binding.foodUnit.setText(grocery.getUnit());
        holder.binding.imageAddOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = grocery.getQuantity();
                quantity++;
                grocery.setQuantity(quantity);
                holder.binding.quantity.setText(String.valueOf(quantity));
                holder.binding.price.setText("TK "+(grocery.getPrice()*quantity));
                notifyDataSetChanged();
                cart.updateItem(grocery,grocery.getQuantity());

                cartListener.onQuantityChanged();
            }
        });
        holder.binding.imageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = grocery.getQuantity();
                if(quantity<=0){
                    quantity=0;
                    grocery.setQuantity(quantity);
                    holder.binding.quantity.setText(String.valueOf(quantity));
                    holder.binding.price.setText("TK "+(grocery.getPrice()));
                    notifyDataSetChanged();
                    cart.updateItem(grocery, grocery.getQuantity());
                    cartListener.onQuantityChanged();
                }
                else {
                    quantity--;
                    grocery.setQuantity(quantity);
                    holder.binding.quantity.setText(String.valueOf(quantity));
                    holder.binding.price.setText("TK "+(grocery.getPrice()*quantity));
                    notifyDataSetChanged();
                    cart.updateItem(grocery, grocery.getQuantity());
                    cartListener.onQuantityChanged();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    public  class GroceryCartViewHolder extends RecyclerView.ViewHolder{
        GroceryItemCartBinding binding;
        public GroceryCartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=GroceryItemCartBinding.bind(itemView);
        }

    }
}
