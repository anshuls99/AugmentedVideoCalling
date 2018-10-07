package com.example.anshulsharma.augmentedvideocalling;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    Drawable myDrawable;
    String title;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0)
            return new HomeFragment();
        if(i==1)
            return new ContactsFragment();
        if(i==2)
            return new NotificationFragment();
        else
            return new ProfileFragment();
    }


    @Override
    public int getCount() {
        return 4;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
////        if (position == 0) {
////            return mContext.getString(R.string.home);
////        } else if(position==1){
////            return mContext.getString(R.string.Contacts);
////        } else {
////            return mContext.getString(R.string.Profile);
////        }
//    }

    @Override
    public CharSequence getPageTitle(int position) {

        SpannableStringBuilder sb = null;
        ImageSpan span;
        switch (position) {
            case 0:
                myDrawable = mContext.getResources().getDrawable(R.drawable.home);
                sb = new SpannableStringBuilder("  "); // space added before text for convenience

                myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
                span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);
                sb.setSpan(span, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                return sb;
            case 1:
                myDrawable = mContext.getResources().getDrawable(R.drawable.contacts);
                sb = new SpannableStringBuilder("  "); // space added before text for convenience

                myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
                span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);
                sb.setSpan(span, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                return sb;

            case 2:
                myDrawable = mContext.getResources().getDrawable(R.drawable.notification);
                sb = new SpannableStringBuilder("  "); // space added before text for convenience

                myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
                span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);
                sb.setSpan(span, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                return sb;

            case 3:
                myDrawable = mContext.getResources().getDrawable(R.drawable.profile);
                sb = new SpannableStringBuilder("  "); // space added before text for convenience

                myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
                span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);
                sb.setSpan(span, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                return sb;
        }
        return sb;
    }
}
