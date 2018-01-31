package com.developer.android.quickveggis.ui.activity;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.developer.android.quickveggis.R;
import com.developer.android.quickveggis.model.WalletHistory;
import com.developer.android.quickveggis.ui.adapter.HistoryAdapter;
import com.developer.android.quickveggis.ui.adapter.WalletHistoryAdapter;
import com.developer.android.quickveggis.ui.fragments.HistoryListFragment;
import com.developer.android.quickveggis.ui.utils.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity implements android.support.v7.app.ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, HistoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up the action bar.
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("History");

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
        //@Bind(R.id.history_rv)
        RecyclerView history_rv;

        ArrayList<WalletHistory> data = new ArrayList<>();

        WalletHistoryAdapter adapter;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_history, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            history_rv = (RecyclerView)rootView.findViewById(R.id.history_rv);
            data = new ArrayList<>();
            data.add(new WalletHistory("", "", "", "JANUARY 2018", false, true));
            data.add(new WalletHistory("Money Received", "250 ₹", "Facebook Backup", "Tue 06", false, false));
            data.add(new WalletHistory("Money Received", "250 ₹", "HealthKit Backup", "Tue 06", false, false));
            data.add(new WalletHistory("Money Sent", "250 ₹", "Shrinivasan", "Tue 06", true, false));
            data.add(new WalletHistory("Money Received", "250 ₹", "Rama Narayann", "Tue 06", false, false));

            data.add(new WalletHistory("", "", "", "FEBRUARY 2018", false, true));

            data.add(new WalletHistory("Money Received", "250 ₹", "Facebook Backup", "Tue 06", false, false));
            data.add(new WalletHistory("Money Received", "250 ₹", "HealthKit Backup", "Tue 06", false, false));
            data.add(new WalletHistory("Money Sent", "250 ₹", "Shrinivasan", "Tue 06", true, false));
            data.add(new WalletHistory("Money Received", "250 ₹", "Rama Narayann", "Tue 06", false, false));

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle bundle){
            super.onActivityCreated(bundle);
            setRvAdapter();

        }

        void setRvAdapter(){
            history_rv.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new WalletHistoryAdapter(getContext(), data);
            history_rv.setAdapter(this.adapter);

            history_rv.addItemDecoration( new DividerItemDecoration(
                    history_rv.getContext(),
                    (new LinearLayoutManager(getContext())).getOrientation()));
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Purchase Data";
                case 2:
                    return "Sources";
            }
            return null;
        }
    }
}
