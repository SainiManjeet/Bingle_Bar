package com.bingle.ameba.bingle_bar.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.Users;

import java.util.ArrayList;
import java.util.List;

import chat.ActiveChat;
import chat.UsersList;

import static com.bingle.ameba.bingle_bar.common_functions.Constants.chatWindow;
import static com.bingle.ameba.bingle_bar.common_functions.Constants.tabType;

/**
 * Created by ErAshwini on 12/6/18.
 */

public class SimpleTabsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.circle_transparent,
            R.drawable.circle_drawable,


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tabs);

        chatWindow = "1";

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationIcon(R.drawable.menu_white);
        //myToolbar.setLogo(R.drawable.brandz);
        //myToolbar.setSubtitle("ToolBar");


        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#ffffff"));


        // setupTabIcons();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.e("tabPosition", "tabPosition" + tab.getPosition());
                if (tab.getPosition() == 0) {
                    tabType = "0";
                } else {
                    tabType = "1";
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

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    //To check weather user is inside the Restaurant or Outside
    private String userCurrentStatus() {
        String userStatus = "";
        Users users = new Users(SimpleTabsActivity.this);
        if (users.getGpsLocation().equalsIgnoreCase("true")) {
            //Chat
            userStatus = "true";
        } else {
            //Chat History
            userStatus = "true";
        }
        return userStatus;
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.e("userCurrentStatus", "userCurrentStatus" + userCurrentStatus());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (userCurrentStatus().equalsIgnoreCase("true")) {
            adapter.addFragment(new ActiveChat(), "Chats");
            adapter.addFragment(new UsersList(), "Users");
        } else {
            adapter.addFragment(new ActiveChat(), "Chats");
        }

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List <Fragment> mFragmentList = new ArrayList <>();
        private final List <String> mFragmentTitleList = new ArrayList <>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}

