package com.test.creditinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private int[] layouts = {R.layout.first_slide,R.layout.second_slide,R.layout.third_slide};
    private MpagerAdapter mpagerAdapter;

    private Button BnNext,BnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        if(Build.VERSION.SDK_INT >= 19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_welcome);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mpagerAdapter = new MpagerAdapter(layouts,this);
        mPager.setAdapter(mpagerAdapter);


        BnNext = (Button) findViewById(R.id.bnNext);
        BnSkip = (Button) findViewById(R.id.bnSkip);
        BnNext.setOnClickListener(this);
        BnSkip.setOnClickListener(this);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    if(position == layouts.length-1)
                    {
                        BnNext.setText("Start");
                        BnNext.setBackgroundColor(13919574);
                        BnSkip.setVisibility(View.INVISIBLE);
                    }
                    else {
                        BnNext.setText("Next");
                        BnNext.setBackgroundColor(13919574);
                        BnSkip.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bnNext:
                loadNextSlide();
                break;


            case R.id.bnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }
    }

    private void loadHome()
    {
        startActivity(new Intent(this,LoginViaPhone.class));
        finish();
    }

    private  void loadNextSlide()
    {
        int next_slide = mPager.getCurrentItem()+1;

        if(next_slide < layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
