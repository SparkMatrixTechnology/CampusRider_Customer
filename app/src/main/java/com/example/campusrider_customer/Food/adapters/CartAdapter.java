package com.example.campusrider_customer.Food.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campusrider_customer.Food.models.ProductModel;
import com.example.campusrider_customer.R;
import com.example.campusrider_customer.databinding.ItemCartBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    int quantity;
    Context context;
    ArrayList<ProductModel> products;
    Cart cart;
    CartListener cartListener;
    public interface CartListener {
        public void onQuantityChanged();
    }

    public CartAdapter(Context context, ArrayList<ProductModel> products, CartListener cartListener) {
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        ProductModel product=products.get(position);
        Glide.with(context).load(product.getImage()).into(holder.binding.detailedImg);
        quantity = product.getQuantity();
        holder.binding.foodName.setText(product.getName());
        holder.binding.price.setText("TK " +(product.getPrice()*quantity));
        holder.binding.quantity.setText(""+product.getQuantity());


        holder.binding.imageAddOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = product.getQuantity();
                quantity++;
                product.setQuantity(quantity);
                holder.binding.quantity.setText(String.valueOf(quantity));
                holder.binding.price.setText("TK "+(product.getPrice()*quantity));

                notifyDataSetChanged();
                cart.updateItem(product, product.getQuantity());
                cartListener.onQuantityChanged();
            }
        });
        holder.binding.imageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 quantity = product.getQuantity();
                if(quantity<=0){
                    quantity=0;
                    product.setQuantity(quantity);
                    holder.binding.quantity.setText(String.valueOf(quantity));
                    holder.binding.price.setText("TK "+(product.getPrice()));
                    notifyDataSetChanged();
                    cart.updateItem(product, product.getQuantity());
                    cartListener.onQuantityChanged();
                }
                else {
                    quantity--;
                    product.setQuantity(quantity);
                    holder.binding.quantity.setText(String.valueOf(quantity));
                    holder.binding.price.setText("TK "+(product.getPrice()*quantity));
                    notifyDataSetChanged();
                    cart.updateItem(product, product.getQuantity());
                    cartListener.onQuantityChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public  class CartViewHolder extends RecyclerView.ViewHolder{
        ItemCartBinding binding;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemCartBinding.bind(itemView);
        }
    }
}
