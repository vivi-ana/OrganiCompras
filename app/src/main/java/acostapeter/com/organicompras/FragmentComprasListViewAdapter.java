package acostapeter.com.organicompras;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
@SuppressWarnings("all")
public class FragmentComprasListViewAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> lista;
    Activity activity;

    FragmentComprasListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista) {
        super();
        this.activity = activity;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
