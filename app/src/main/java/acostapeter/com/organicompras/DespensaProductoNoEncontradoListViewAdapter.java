package acostapeter.com.organicompras;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.TERCERA_COLUMNA;

public class DespensaProductoNoEncontradoListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> lista;
    Activity activity;

    DespensaProductoNoEncontradoListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista) {
        super();
        this.activity = activity;
        this.lista = lista;
    }
    @Override
    public int getCount() {
        return lista.size();
    }
    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder {
        TextView nombre, id, descripcion, medida, neto, marca;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DespensaProductoNoEncontradoListViewAdapter.ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.despensa_producto_no_encontrado_columnas, parent, false);
            holder = new DespensaProductoNoEncontradoListViewAdapter.ViewHolder();
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.descripcion = convertView.findViewById(R.id.descrip);
            holder.marca = convertView.findViewById(R.id.marca);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida = convertView.findViewById(R.id.medida);
            holder.id = convertView.findViewById(R.id.id);
            convertView.setTag(holder);
        }
        else {
            holder = (DespensaProductoNoEncontradoListViewAdapter.ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = lista.get(position);
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        holder.descripcion.setText(map.get(SEGUNDA_COLUMNA));
        holder.marca.setText(map.get(TERCERA_COLUMNA));
        holder.neto.setText(map.get(CUARTA_COLUMNA));
        holder.medida.setText(map.get(QUINTA_COLUMNA));
        holder.id.setText(map.get(SEPTIMA_COLUMNA));
        if ("".equals(map.get(SEGUNDA_COLUMNA))) holder.descripcion.setVisibility(View.GONE);
        if ("".equals(map.get(TERCERA_COLUMNA))) holder.marca.setVisibility(View.GONE);
        if ("".equals(map.get(CUARTA_COLUMNA)))holder.neto.setVisibility(View.GONE);
        if ("".equals(map.get(QUINTA_COLUMNA)))holder.medida.setVisibility(View.GONE);
        return convertView;
    }
}
