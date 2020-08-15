package com.rapidbox.emergencyresponseapplication.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rapidbox.emergencyresponseapplication.tab1basicinfo;
import com.rapidbox.emergencyresponseapplication.tab2medicalinfo;
import com.rapidbox.emergencyresponseapplication.tab3emergencycontacts;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

   // @StringRes
   // private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    //private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
//        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                tab1basicinfo tab1 = new tab1basicinfo();
                return tab1;
            case 1:
                tab2medicalinfo tab2 = new tab2medicalinfo();
                return tab2;
            case 2:
                tab3emergencycontacts tab3 = new tab3emergencycontacts();
                return tab3;
            default:
                return null;

        }


        // getItem is called to instantiate the fragment for the given page.

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Basic Information";
            case 1:
                return "Medical";
            case 2:
                return "Emergency Contacts";

        }
        return  null;
    }

    //}

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}