package com.example.facebook.imagefromurl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.facebook.R;
import com.example.facebook.SaveData;




public class PhoneContactAdapter extends ArrayAdapter<SaveData> 
{

	private static final String TAG = "DisplayGeneratedPinsAdapter";
	private Context context;
	
	private LayoutInflater inflator;
	ArrayList<SaveData> urlList=new ArrayList<SaveData>();
	
	
	public PhoneContactAdapter(Context context, ArrayList<SaveData> urlList) 
	{
		super(context, 0, urlList);                   //why
		this.context = context;
	    this.urlList=urlList;
	   

		
	}


	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urlList.size();
	}

	/*@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pins.get(position);
	}
*/
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		

		if (convertView == null) {
			inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.item_phone_contact,null);
		}

		ViewHolder holder = new ViewHolder();
		
		try
		{
		holder.image = (ImageView)convertView.findViewById(R.id.phone_Image);
		
		//holder.image.setImageURI(Uri.parse(urlList.get(position).getImagePath()));
	      
		holder.image.setImageBitmap(urlList.get(position).getBitmap());
		}
	
		catch(OutOfMemoryError e)
		{
			
		}
		catch(Exception e)
		{
			
		}
		
		

		return convertView;
	}



	private class ViewHolder
	{
		
		ImageView image;
		
		
	}

	
	
}
