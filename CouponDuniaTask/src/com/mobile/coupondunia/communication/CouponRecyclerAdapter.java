package com.mobile.coupondunia.communication;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.couponduniatask.R;
import com.mobile.couponduniatask.constants.CoreConstants;
import com.mobile.couponduniatask.entities.FeetItem;
import com.squareup.picasso.Picasso;

public class CouponRecyclerAdapter extends RecyclerView.Adapter<CouponRecyclerAdapter.ViewHolder> {
	private Context context;
	private ArrayList<FeetItem> listCouponDuniaFranchise;
	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView tvBrandName, tvOffers, tvCategoryType, tvDistanceWithNeightbourhood;
		public ImageView ivLogo;
		public ViewHolder(View v) {
			super(v);
			tvBrandName = (TextView) v.findViewById(R.id.tvBrandName);
			tvOffers = (TextView) v.findViewById(R.id.tvOffers);
			tvCategoryType = (TextView) v.findViewById(R.id.tvCategoryType);
			tvDistanceWithNeightbourhood = (TextView) v.findViewById(R.id.tvDistanceWithNeightbourhood);
			ivLogo = (ImageView) v.findViewById(R.id.ivLogo);
		}
	}

	/*  public void add(int position, String item) {
    	listCouponDuniaFranchise.add(position, item);
    	notifyItemInserted(position);
  	}*/
  
	public void remove(String item) {
		int position = listCouponDuniaFranchise.indexOf(item);
		listCouponDuniaFranchise.remove(position);
		notifyItemRemoved(position);
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public CouponRecyclerAdapter(Context context, ArrayList<FeetItem> listCouponDuniaFranchise) {
		this.context = context;
		this.listCouponDuniaFranchise = listCouponDuniaFranchise;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public CouponRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
		// set the view's size, margins, paddings and layout parameters
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		holder.tvBrandName.setText(listCouponDuniaFranchise.get(position).getBrandName());
		holder.tvOffers.setText(listCouponDuniaFranchise.get(position).getNumCoupons() + " Offers");
		Picasso.with(context).load(listCouponDuniaFranchise.get(position).getLogoURL()).into(holder.ivLogo);
		
		String categoryType = "";
		for(int i = 0; i < listCouponDuniaFranchise.get(position).getCategoriesList().size(); i++){
			categoryType = categoryType.concat("\u2022" + listCouponDuniaFranchise.get(position).getCategoriesList().get(i).getCategoryName() + " ");
		}
		holder.tvCategoryType.setText(categoryType);
		
		String categoryDistanceWithArea = listCouponDuniaFranchise.get(position).getDistance() + " m  " + listCouponDuniaFranchise.get(position).getNeighbourhoodName();
		holder.tvDistanceWithNeightbourhood.setText(categoryDistanceWithArea);
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return listCouponDuniaFranchise.size();
	}
	
	
} 
