package cn.fsyz.shishuo.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by JALOR on 2018/2/27.
 */

//碎片适配器
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    //tag的标题
    private String[] mTitles = new String[]{"生活分享", "畅所欲言", "解忧小店"};

    //framgent的适配器返回界面
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //返回界面
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new Fragment2();
        }
        else if (position == 2) {
            return new Fragment3();
        }
        return new Fragment1();
    }


    //返回数量
    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
