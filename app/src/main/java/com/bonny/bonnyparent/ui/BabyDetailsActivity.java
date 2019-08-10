package com.bonny.bonnyparent.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.adapters.PagerAdapter;
import com.bonny.bonnyparent.models.BabyModel;
import com.bonny.bonnyparent.fragments.AppointmentHistoryFragment;
import com.bonny.bonnyparent.fragments.ScheduleFragment;

public class BabyDetailsActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private BabyModel babyModel;
    private TextView tvBName;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_details);
       /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_baby_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/

        tvBName = findViewById(R.id.tvBabyName);


        if (getIntent().hasExtra("babyModel")) {
            babyModel = (BabyModel) getIntent().getExtras().get("babyModel");
            name = babyModel.getFirst_name();
        }

        initUi();

        tabLayout = findViewById(R.id.tlToolbarTabs);
        viewPager = findViewById(R.id.vPSchedule);

        viewPager.setId(R.id.vPSchedule);

        pagerAdapter = new PagerAdapter(BabyDetailsActivity.this.getSupportFragmentManager());
        pagerAdapter.addFragments(ScheduleFragment.newInstance(babyModel.getId()), getString(R.string.schedule));
        pagerAdapter.addFragments(new AppointmentHistoryFragment(), getString(R.string.history));
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initUi() {
        Bundle extras = getIntent().getExtras();
        viewPager = findViewById(R.id.vPSchedule);
        tvBName.setText(name);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
