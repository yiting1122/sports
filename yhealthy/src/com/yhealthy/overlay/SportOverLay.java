package com.yhealthy.overlay;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.OverlayItem;

public class SportOverLay extends ItemizedOverlay<OverlayItem>{
    private Context context;
    private Activity activity;
    private List<OverlayItem>UserList= new ArrayList<OverlayItem>();
	@Override
	protected OverlayItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return UserList.get(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return UserList.size();
	}
  public SportOverLay(Drawable drawable,Context context,List<OverlayItem>videoList,Activity activity){
	  super(drawable);
	  this.context=context;
	  this.activity=activity;
	  this.UserList.addAll(videoList);
	  populate();
  }
	protected boolean onTap(int i) {
		Toast.makeText(context, UserList.get(i).getTitle(),
		 Toast.LENGTH_SHORT).show();
		return true;
	}
	
	
}
