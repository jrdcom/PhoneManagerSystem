package com.app2.pms;


import java.util.ArrayList;
import java.util.List;

import com.app2.pms.debug.app.DebugService;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements ADBClientFragment.Callback{

    private ViewPager mViewPager;
    private ImageView mImageView;
    private TextView mContactText;
    private TextView mCommentText;
    private TextView mPrivateText;
    private List<Fragment> mListFragment;
    private float offset;
    private int currIndex;
    private int bmpWidth;
  /*  private View mContactView;
    private View mCommentView;
    private View mPrivateView;*/
    
    private final String ADB_CLIENT = "adb_client";
    private final String COMMENT_LAYOUT = "comment_layout";
    private final String PRIVATE_LAYOUT = "private_layout";
    private final String MANAGER_LIST_FRAGMENT = "ManagerListFragment";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
        initTextView();
        initImageView();
    }
    
    private void initTextView() {
        mContactText = (TextView) findViewById(R.id.tab_adbclient);
        mCommentText = (TextView) findViewById(R.id.tab_comment);
        mPrivateText = (TextView) findViewById(R.id.tab_private);
        
        mContactText.setOnClickListener(new MyOnClickListener(0));
        mCommentText.setOnClickListener(new MyOnClickListener(1));
        mPrivateText.setOnClickListener(new MyOnClickListener(2));
        
    }
    
    private void initViewPager() {
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);
        mListFragment = new ArrayList<Fragment>();
        //LayoutInflater inflater = getLayoutInflater();
        FragmentManager mFragmentM = getFragmentManager();
        FragmentTransaction mTrans = mFragmentM.beginTransaction();
        BasicFragment scrollFragmentB = new BasicFragment(R.layout.scroll_layout);
        mListFragment.add(scrollFragmentB);
        mTrans.add(R.id.view_pager, scrollFragmentB);
        
        ADBClientFragment adbClientFragment = new ADBClientFragment();
        mListFragment.add(adbClientFragment);
        mTrans.add(R.id.view_pager, adbClientFragment, ADB_CLIENT);
        //transacte.hide(adbClientFragment);
        
        BasicFragment commentFragment = new BasicFragment(R.layout.comment_layout);
        mListFragment.add(commentFragment);
        mTrans.add(R.id.view_pager, commentFragment, COMMENT_LAYOUT);
        
        BasicFragment privateFragment = new BasicFragment(R.layout.private_layout);
        mListFragment.add(privateFragment);
        mTrans.add(R.id.view_pager, privateFragment, PRIVATE_LAYOUT);
        
        BasicFragment scrollFragmentE = new BasicFragment(R.layout.scroll_layout);
        mListFragment.add(scrollFragmentE);
        mTrans.add(R.id.view_pager, scrollFragmentE);
        
        mTrans.commitAllowingStateLoss();
        mFragmentM.executePendingTransactions();
        
        PagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), mListFragment);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
    }
    
    private void initImageView() {
        mImageView = (ImageView) findViewById(R.id.cursor);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), 
                R.drawable.solid_line);
        bmpWidth = bitmap.getWidth();
        mImageView.setImageBitmap(bitmap);
        
       /* Drawable image = (Drawable) getResources().getDrawable(R.drawable.solid_line_xml);
        mImageView.setImageDrawable(image);
        bmpWidth = 480;*/
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int showWidth = dm.widthPixels / 3 * 4 / 5;
        offset = (dm.widthPixels / 3 - showWidth) /2;
        Matrix matrix= new Matrix();
        matrix.setScale((float)showWidth / bmpWidth, 1);
        matrix.postTranslate(offset, 0);
        mImageView.setImageMatrix(matrix);
        bmpWidth = showWidth;
        currIndex = mViewPager.getCurrentItem() - 1;
    }
    
    public class MyFragmentPagerAdapter extends PagerAdapter{
        private List<Fragment> mListFragments;
        private FragmentTransaction mTransaction = null;
        private FragmentManager fManager;
        
        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list){
            this.mListFragments = list;
            this.fManager = fm;
        }
        
        @Override
        public void destroyItem(View container, int position, Object object) {
            //BasicFragment fragment = mListFragments.get(position);
            if (mTransaction == null) {
                mTransaction = fManager.beginTransaction();
            }
            mTransaction.hide((Fragment)object);
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position){
            if (mTransaction == null) {
                mTransaction = fManager.beginTransaction();
            }
            Fragment fragment = mListFragments.get(position);
            mTransaction.show(fragment);
            return fragment;
        }
        
        @Override
        public void setPrimaryItem(View container, int position, Object object) {
            if (mTransaction == null) {
                mTransaction = fManager.beginTransaction();
            }
            mTransaction.show((Fragment)object);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mListFragments.size();
        }  private View mContactView;
        private View mCommentView;
        private View mPrivateView;
        @Override
        public void finishUpdate(View container) {
            if (mTransaction != null) {
                mTransaction.commitAllowingStateLoss();
                mTransaction = null;
                fManager.executePendingTransactions();
            }
        }
        
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return ((Fragment) arg1).getView() == arg0;
        }

    }
    
    private class MyOnClickListener implements OnClickListener{
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i + 1;
        }
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(index);
        }
    }
    
    public class MyOnPageChangeListener implements OnPageChangeListener{
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 >= 1 && arg0 <=3){
                float fromX = (offset * 2 + bmpWidth) * currIndex;
                float toX = (offset * 2 + bmpWidth) * (arg0 - 1);
                
                Animation animation = null;
                animation = new TranslateAnimation(fromX, toX, 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(300);
                mImageView.startAnimation(animation); 
                currIndex = arg0 - 1;
                
            }else if (arg0 < 1) {
                mViewPager.setCurrentItem(3);
            }else {
                mViewPager.setCurrentItem(1);
            }
        }
    }

    @Override
    public void onImageButton(View v) {
        // TODO Auto-generatedVISIBLE method stub
        BasicFragment frag;
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        stopService(new Intent(this, DebugService.class));
        return super.onOptionsItemSelected(item);
    }
    
}
