package com.yhealthy.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommonActivity extends Activity {
	
	public Intent intent = new Intent();
	public ImageView ImageViewOffical = null ;
	public ImageView ImageViewPersonnal = null ;
	public ImageView ImageViewRecommand = null ; 
	public ImageView imageViewMore = null ;
	

	public ImageViewOfficalListener  viewIndexListener = new ImageViewOfficalListener();
	public ImageViewPersonnalListener viewPersonnalListener = new ImageViewPersonnalListener();
	public ImageViewRecommandListener viewRecommandListener = new ImageViewRecommandListener();
	public ImageViewMoreListener viewMoreListener = new ImageViewMoreListener();
	public ListView listViewAll = null ;
	public TextView textViewTitle = null ;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	
	class ImageViewOfficalListener implements OnTouchListener {
         //��ҳ
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				ImageViewOffical.setImageResource(R.drawable.menu_home_pressed);
				intent.setClass(CommonActivity.this,OfficialSportsActivity.class);
				startActivity(intent);
			}
			return false;
		}

	}

	class ImageViewPersonnalListener implements OnTouchListener {
       //����(����)
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				startActivity(new Intent(getApplicationContext(),PersonnalSportsActivity.class));
				ImageViewPersonnal.setImageResource(R.drawable.menu_brand_pressed);
			}
			return false;
		}

	}
    
	

	class ImageViewRecommandListener implements OnTouchListener {
        //�ҵ�����
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				intent.setClass(getApplicationContext(), RecommemdSportsActivity.class);
				startActivity(intent);
				ImageViewRecommand.setImageResource(R.drawable.menu_my_letao_pressed);
			}
			return false;
		}
	}

	class ImageViewMoreListener implements OnTouchListener {
        //�����Ϣ
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == event.ACTION_DOWN) {
				imageViewMore.setImageResource(R.drawable.menu_more_pressed);
				intent.setClass(CommonActivity.this,MoreActivity.class);
				startActivity(intent);
			}
			return false;
		}

	}

}
