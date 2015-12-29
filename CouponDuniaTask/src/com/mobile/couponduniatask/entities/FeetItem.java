package com.mobile.couponduniatask.entities;

import java.util.ArrayList;

public class FeetItem {
	String outletName;
	String latitude;
	String longitude;
	String brandName;
	String numCoupons;
	String neighbourhoodName;
	String logoURL;
	int distance;
	
	ArrayList<CategoryItem> categoriesList;

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getNumCoupons() {
		return numCoupons;
	}

	public void setNumCoupons(String numCoupons) {
		this.numCoupons = numCoupons;
	}

	public String getNeighbourhoodName() {
		return neighbourhoodName;
	}

	public void setNeighbourhoodName(String neighbourhoodName) {
		this.neighbourhoodName = neighbourhoodName;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public ArrayList<CategoryItem> getCategoriesList() {
		return categoriesList;
	}

	public void setCategoriesList(ArrayList<CategoryItem> categoriesList) {
		this.categoriesList = categoriesList;
	}
}
