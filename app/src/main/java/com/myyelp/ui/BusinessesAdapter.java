package com.myyelp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myyelp.R;
import com.myyelp.data.models.Business;
import com.myyelp.data.models.Category;

import java.util.ArrayList;

public class BusinessesAdapter extends RecyclerView.Adapter<BusinessesAdapter.ViewHolder> {
    private ArrayList<Business> businesses = new ArrayList<Business>();
    private MainViewModel mainViewModel;
    private Boolean isFavourites = false;

    public BusinessesAdapter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    public BusinessesAdapter(MainViewModel mainViewModel, Boolean isFavourites) {
        this.mainViewModel = mainViewModel;
        this.isFavourites = isFavourites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Business business = businesses.get(position);
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvName.setText(business.name);
        holder.tvPhone.setText(business.phone);
        holder.tvPrice.setText(business.price);
        holder.tvAddress.setText(business.location.address1 + "," + business.location.address2 + "," + business.location.address3);

        StringBuilder str = new StringBuilder("");
        for (Category category : business.categories) {
            str.append(category.title).append(",");
        }
        holder.tvCategory.setText(str.toString());
        Glide.with(holder.itemView.getContext())
                .load(business.image_url)
                .into(holder.imgBusiness);
        holder.ratingBar.setRating(business.rating);

        if (!isFavourites) {
            holder.itemView.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Add to favorite?");
                builder.setMessage("Do you want to add this item to favorite");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    mainViewModel.addFavourite(business);
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                });
                builder.create().show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    //update adapter with new data
    public void updateData(ArrayList<Business> newData) {
        businesses.clear();
        businesses.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIndex;
        public TextView tvName;
        public RatingBar ratingBar;
        public TextView tvPrice;
        public TextView tvCategory;
        public TextView tvPhone;
        public TextView tvAddress;
        public ImageView imgBusiness;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            imgBusiness = (ImageView) itemView.findViewById(R.id.img_business);
        }
    }
}
