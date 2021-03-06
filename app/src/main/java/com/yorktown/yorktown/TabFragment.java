package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yorktown.yorktown.view.SlidingTabLayout;

/**
 * Created by Daniel on 1/29/2015.
 */
public class TabFragment extends Fragment {
    // {@link ViewPager} will host the section contents
    ViewPager mViewPager;

    // {@link FragmentPagerAdapter} derivative keeps every loaded fragment in memory
    SectionsPagerAdapter mSectionsPagerAdapter;

// *** LIFECYCLE ***
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // set up the sliding tabs and link its scrolling motion to the ViewPager, which manages the tabs
        /*
      A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
      above, but is designed to give continuous feedback to the user when scrolling.
     */
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

}
