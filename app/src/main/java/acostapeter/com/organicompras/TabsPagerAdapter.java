package acostapeter.com.organicompras;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
@SuppressWarnings("all")
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
        switch (position) { //para mostrar alguno de los dos fragments
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
