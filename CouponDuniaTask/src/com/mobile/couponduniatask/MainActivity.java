package com.mobile.couponduniatask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mobile.coupondunia.communication.AsyncResponse;
import com.mobile.coupondunia.communication.CouponRecyclerAdapter;
import com.mobile.couponduniatask.constants.CoreConstants;
import com.mobile.couponduniatask.entities.CategoryItem;
import com.mobile.couponduniatask.entities.FeetItem;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements AsyncResponse {

	private RecyclerView mRecyclerView;
	@SuppressWarnings("rawtypes")
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<FeetItem> listCouponDuniaFranchise;
    
    JSONArray nodeDataJsonArray;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Initialize and execute async task to fetch json response
		FetchCouponList fetchCouponList = new FetchCouponList(this);
		fetchCouponList.execute();
		
        //Initialize components
        initComponents();
        
        //Fetch current location
        fetchCurrentLocation();
		
	}
	
	private void initComponents(){
		//tvName = (TextView) findViewById(R.id.tvName);
		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		// use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        
        listCouponDuniaFranchise = new ArrayList<FeetItem>();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_filter) {
			return true;
		} else if (id == R.id.action_search) {
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}*/
	
	//To fetch json data
	private class FetchCouponList extends AsyncTask<String, String,String> {
		AsyncResponse asyncResponse;
		
		public FetchCouponList(AsyncResponse asyncResponse){
			this.asyncResponse = asyncResponse;
		}
		
		protected String doInBackground(String... urls) {

			try {
				// Create a URL for the desired page
	 		    URL url = new URL(CoreConstants.urlTOFetchJsonData);

	 		    // Read all the text returned by the server
	 		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
	 		    StringBuilder responseResult = new StringBuilder();
	 	        String line;
	 	        while ((line = bufferedReader.readLine()) != null) {
	 	            responseResult.append(line + "\n");
	 	        }
	 	        bufferedReader.close();
	 	        return responseResult.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return null;
		}
		
		@Override
		protected void onPostExecute(String responseResult) {
			// TODO Auto-generated method stub
			super.onPostExecute(responseResult);
			if(responseResult != null)
				asyncResponse.processResponse(responseResult.toString());
			else
				Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void processResponse(String responseResult) {
		// TODO Auto-generated method stub
		if(responseResult != null){
			parseJsonResponse(responseResult);
		}
	}
	
	public void parseJsonResponse(String responseResult){
		try {
			JSONObject responseJsonObject = new JSONObject(responseResult);
			
			// Getting JSON Array node
			nodeDataJsonArray = responseJsonObject.getJSONArray(CoreConstants.TAG_DATA_NODE);

			// looping through All Contacts
			for (int i = 0; i < nodeDataJsonArray.length(); i++) {
				JSONObject jsonObjectData = nodeDataJsonArray.getJSONObject(i);
				FeetItem feedItem = new FeetItem();
				feedItem.setOutletName(jsonObjectData.getString(CoreConstants.TAG_OUTLETNAME));
				feedItem.setLatitude(jsonObjectData.getString(CoreConstants.TAG_LATITUDE));
				feedItem.setLongitude(jsonObjectData.getString(CoreConstants.TAG_lONGITUDE));
				feedItem.setBrandName(jsonObjectData.getString(CoreConstants.TAG_BRANDNAME));
				feedItem.setNumCoupons(jsonObjectData.getString(CoreConstants.TAG_NUMCOUPONS));
				feedItem.setNeighbourhoodName(jsonObjectData.getString(CoreConstants.TAG_NEIGHBOURHOODNAME));
				feedItem.setDistance(calculateDistance(feedItem.getLatitude(), feedItem.getLongitude()));
				feedItem.setLogoURL(jsonObjectData.getString(CoreConstants.TAG_LOGOURL));
				
				// Phone node is JSON Object
				JSONArray categoryJsonArray = jsonObjectData.getJSONArray(CoreConstants.TAG_CATEGORIES_NODE);
				ArrayList<CategoryItem> listSubCategory = new ArrayList<CategoryItem>();
				for(int j = 0; j < categoryJsonArray.length(); j++){
					JSONObject jsonObjectSubCategoryData = categoryJsonArray.getJSONObject(j);
					CategoryItem categoryItem = new CategoryItem();
					categoryItem.setCategoryName(jsonObjectSubCategoryData.getString(CoreConstants.TAG_NAME));
					categoryItem.setCategoryType(jsonObjectSubCategoryData.getString(CoreConstants.TAG_CATEGORYTYPE));
					listSubCategory.add(categoryItem);
				}
				feedItem.setCategoriesList(listSubCategory);
				listCouponDuniaFranchise.add(feedItem);
				Collections.sort(listCouponDuniaFranchise, new CustomComparator());

				//tvName.setText(responseResult);
		        mAdapter = new CouponRecyclerAdapter(this, listCouponDuniaFranchise);
		        mRecyclerView.setAdapter(mAdapter);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fetchCurrentLocation(){
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				CoreConstants.currentLatitude = location.getLatitude();
				CoreConstants.currentLongitude = location.getLongitude();
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		};

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	public int calculateDistance(String latitude, String longitude){
        Location destinationLocation = new Location("");
        destinationLocation.setLatitude(Double.parseDouble(latitude));
        destinationLocation.setLongitude(Double.parseDouble(longitude));

        Location currentLocation = new Location("");
        currentLocation.setLatitude(CoreConstants.currentLatitude);
        currentLocation.setLongitude(CoreConstants.currentLongitude);
        Double distance = (double) currentLocation.distanceTo(destinationLocation);		//in meters
        
        return distance.intValue();
	}
	
	public class CustomComparator implements Comparator<FeetItem> {
		@Override
		public int compare(FeetItem feedItemObject1, FeetItem feedItemObject2) {
			if(feedItemObject1.getDistance() < feedItemObject2.getDistance()) {
				return -1;
			} else if(feedItemObject1.getDistance() > feedItemObject2.getDistance()) {
				return 1;
			}
			return 0;
		}
	}
}
