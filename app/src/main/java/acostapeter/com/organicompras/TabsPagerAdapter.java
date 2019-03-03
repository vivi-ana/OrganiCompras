package acostapeter.com.organicompras;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPagerAdapter  extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    TabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentCompras tab1 = new FragmentCompras();
        FragmentDespensa tab2 = new FragmentDespensa();
        switch (position) {
            case 0:
                return tab1;
           case 1:
                return tab2;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
