package com.yhealthy.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.yhealthy.model.SportsContents;
import com.yhealthy.overlay.SportOverLay;

public class SportsDetailActivity extends Activity {
	final static String TAG = "MainActivty";
	private MapView mMapView = null;
	
	private MapController mMapController = null;

	FrameLayout mMapViewContainer = null;
	MKMapViewListener mMapListener = null;
	private SportsContents sportsContents;

    private TextView sportsContentTextView;
	private TextView sportsBuildTextView;
	private TextView sportsUsersTextView;
	private TextView sportsClassTextView;
	private TextView sportsScoreTextView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationConfig app = (ApplicationConfig)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(ApplicationConfig.strKey,new ApplicationConfig.MyGeneralListener());
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sports_detail);
        sportsContents=(SportsContents) this.getIntent().getExtras().getSerializable("sportsInfo");
        initWindow();
        initMap();
        
        addOverLay();
    }
    
    private void initWindow()
    {
    	
    	sportsContentTextView=(TextView) this.findViewById(R.id.sports_content);
    	sportsBuildTextView=(TextView) this.findViewById(R.id.sports_user_build);
    	sportsUsersTextView=(TextView) this.findViewById(R.id.sports_users);
    	sportsClassTextView=(TextView) this.findViewById(R.id.sports_class);
    	sportsScoreTextView=(TextView) this.findViewById(R.id.sports_score);
    	sportsContentTextView.setText(sportsContents.getContent());
    	sportsBuildTextView.setText(sportsContents.getUserName());
    	sportsUsersTextView.setText("5");
    	sportsClassTextView.setText(sportsContents.getSportsClassName());
    	sportsScoreTextView.setText(sportsContents.getScore().toString());
    }
    
    
    
    private void initMap()
    {
    	mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        mMapController.enableClick(true);
        mMapController.setZoom(12);
        mMapView.displayZoomControls(true);
        mMapView.setDoubleClickZooming(true);
        mMapView.setOnTouchListener(null);
        mMapView.setLongClickable(true);
		GeoPoint geoPoint = new GeoPoint((int) (sportsContents.getLatitude()* 1E6),
				(int) (sportsContents.getLongitude() * 1E6));
		mMapView.getController().setCenter(geoPoint);
        
        mMapListener = new MKMapViewListener() {
			
			@Override
			public void onMapMoveFinish() {
				// 在此处理地图移动完成消息回调
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				String title = "";
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					Toast.makeText(SportsDetailActivity.this,title,Toast.LENGTH_SHORT).show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// 回调图片数据，保存在"/mnt/sdcard/test"目录下
				Log.d("test", "test"+"onGetCurrentMap");
				 File file = new File("/mnt/sdcard/test.png");
	                FileOutputStream out;
	                try{
	                        out = new FileOutputStream(file);
	                        if(b.compress(Bitmap.CompressFormat.PNG, 70, out)) 
	                        {
	                                out.flush();
	                                out.close();
	                        }
	                } 
	                catch (FileNotFoundException e) 
	                {
	                        e.printStackTrace();
	                } 
	                catch (IOException e) 
	                {
	                        e.printStackTrace(); 
	                }
			}

			@Override
			public void onMapAnimationFinish() {
				// 在此处理地图动画完成回调

			}
		};
		mMapView.regMapViewListener(ApplicationConfig.getInstance().mBMapManager, mMapListener);
		
    }

    

    private void addOverLay()
    {
    	mMapView.getOverlays().clear();
		mMapView.refresh();
		Drawable marker = getApplicationContext().getResources()
				.getDrawable(R.drawable.icon_marka);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		// 为maker定义位置和边界
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());

		OverlayItem myOverlayItem = null;
		List<OverlayItem> overlayItems=new ArrayList<OverlayItem>();
			String address = sportsContents.getAddress();
			Double latitude = sportsContents.getLatitude();
			Double longitude = sportsContents.getLongitude();
			GeoPoint geoPoint = new GeoPoint(
					(int) (latitude * 1E6), (int) (longitude * 1E6));
			myOverlayItem = new OverlayItem(geoPoint,"活动举办地址：" +address , "");
			overlayItems.add(myOverlayItem);
			
		Projection projection = mMapView.getProjection();
		Canvas canvas = new Canvas();
		for (OverlayItem overlayItem : overlayItems) {
			Point point = projection.toPixels(
					overlayItem.getPoint(), null);
			Paint paintText = new Paint();
			paintText.setTextSize(12);
			paintText.setColor(Color.RED);
			canvas.drawText(overlayItem.getTitle(), point.x + 10,
					point.y - 15, paintText);
		}

		// MyGpsLay myGpsLay=new
		// MyGpsLay(marker,getApplicationContext());
		SportOverLay sportOverLay = new SportOverLay(marker,
				getApplicationContext(), overlayItems,
				SportsDetailActivity.this);
			mMapView.getOverlays().add(sportOverLay);
		mMapView.refresh();
    }
    
    //截图，异步方法
    public void captureMapClick() {
    	mMapView.getCurrentMap();
    }
  
    
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        mMapView.destroy();
        ApplicationConfig app = (ApplicationConfig)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
  
}
