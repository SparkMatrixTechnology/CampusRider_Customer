package com.example.campusrider_customer.Food.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.campusrider_customer.Food.models.CategoryModel;
import com.example.campusrider_customer.Food.models.ProductModel;
import com.example.campusrider_customer.R;
import com.example.campusrider_customer.databinding.CatWiseProductBinding;
import com.example.campusrider_customer.databinding.ShopListItemBinding;
import com.example.campusrider_customer.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        Context context;
        private  RecyclerView.RecycledViewPool viewPool=new RecyclerView.RecycledViewPool();
        ArrayList<CategoryModel> list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_wise_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel categoryModel=list.get(position);
        holder.binding.catName.setText(categoryModel.getName());

        LinearLayoutManager layoutManager=new LinearLayoutManager(holder.binding.productRec.getContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setInitialPrefetchItemCount(categoryModel.getProductModelArrayList().size());
        ProductAdapter productAdapter=new ProductAdapter(context,categoryModel.getProductModelArrayList());
        holder.binding.productRec.setLayoutManager(layoutManager);
        holder.binding.productRec.setAdapter(productAdapter);
        holder.binding.productRec.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

        public class ViewHolder extends RecyclerView.ViewHolder{

            CatWiseProductBinding binding;

            public ViewHolder(@NonNull View itemView){
                super(itemView);
                binding=CatWiseProductBinding.bind(itemView);

            }
        }


}
