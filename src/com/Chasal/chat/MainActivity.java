package com.Chasal.chat;

import java.util.ArrayList;
import java.util.List;

import com.jauker.widget.BadgeView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mDatas;

	private TextView mChatTextView;
	private TextView mFriendTextView;
	private TextView mContactTextView;
	private LinearLayout mChatLinearLayout;
	
	//右上角提示
	private BadgeView mBadgeView;
	
	//滚动条
	private ImageView mTableLine;
	private int mScreen1_3;
	private int mCurrentPageIndex;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//初始化TabLine
		initTabLine();
		//初始化显示布局
		initView();
		
	}
	
	private void initTabLine(){
		mTableLine = (ImageView) findViewById(R.id.id_iv_tabline);
		
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		
		mScreen1_3 = outMetrics.widthPixels / 3;
		LayoutParams lp = mTableLine.getLayoutParams();
		lp.width = mScreen1_3;
		mTableLine.setLayoutParams(lp);
		
		
	}
	
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mChatTextView = (TextView) findViewById(R.id.id_tv_chat);
		mFriendTextView = (TextView) findViewById(R.id.id_tv_friend);
		mContactTextView = (TextView) findViewById(R.id.id_tv_contact);
		mChatLinearLayout = (LinearLayout) findViewById(R.id.id_ll_chat);

		// 添加数据Fragment
		mDatas = new ArrayList<Fragment>();
		ChatMainTabFragment tab01 = new ChatMainTabFragment();
		FriendMainTabFragment tab02 = new FriendMainTabFragment();
		ContactMainTabFragment tab03 = new ContactMainTabFragment();
		mDatas.add(tab01);
		mDatas.add(tab02);
		mDatas.add(tab03);

		// 适配器
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mDatas.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mDatas.get(position);
			}
		};

		mViewPager.setAdapter(mAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					if(mBadgeView != null){
						mChatLinearLayout.removeView(mBadgeView);
					}
					mBadgeView = new BadgeView(MainActivity.this);
					mBadgeView.setBadgeCount(7);
					mChatLinearLayout.addView(mBadgeView);
					mChatTextView.setTextColor(Color.parseColor("#008000"));
					break;
					
				case 1:
					mFriendTextView.setTextColor(Color.parseColor("#008000"));
					break;
				case 2:
					mContactTextView.setTextColor(Color.parseColor("#008000"));
					break;

				}
				
				mCurrentPageIndex = position;
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {
				Log.i("TAG", position + "," + positionOffset + "," + positionOffsetPx);
				
				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTableLine.getLayoutParams();
				
				if(mCurrentPageIndex == 0 && position == 0){         // 0 --> 1
					lp.leftMargin = (int) (positionOffset * mScreen1_3 + mCurrentPageIndex * mScreen1_3);
				}else if(mCurrentPageIndex == 1 && position == 0){   // 1 --> 0
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1) * mScreen1_3);
				}else if(mCurrentPageIndex == 1 && position == 1){   // 1 --> 2
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset * mScreen1_3);
				}else if(mCurrentPageIndex == 2 && position == 1){	 // 2 --> 1
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1) * mScreen1_3);
				}
				mTableLine.setLayoutParams(lp);
				
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
				
			}
		});
	}
	
	
	private void resetTextView(){
		mChatTextView.setTextColor(Color.BLACK);
		mFriendTextView.setTextColor(Color.BLACK);
		mContactTextView.setTextColor(Color.BLACK);
	}

}
