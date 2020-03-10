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
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.TERCERA_COLUMNA;
public class ComprasProductoNoEncontradoListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> lista;
    Activity activity;

    ComprasProductoNoEncontradoListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista) {
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
        TextView nombre, id, precio, marca, neto, medida, descripcion, codigo;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.compras_producto_no_encontrado_columnas, parent, false);
            holder = new ViewHolder();
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.descripcion = convertView.findViewById(R.id.descrip);
            holder.marca = convertView.findViewById(R.id.marca);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida = convertView.findViewById(R.id.medida); //medida es un text porque muestra los datos nomas
            holder.precio = convertView.findViewById(R.id.precio);
            holder.id = convertView.findViewById(R.id.id);
            holder.codigo = convertView.findViewById(R.id.codigo);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = lista.get(position);
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        holder.descripcion.setText(map.get(SEGUNDA_COLUMNA));
        holder.marca.setText(map.get(TERCERA_COLUMNA));
        holder.neto.setText(map.get(CUARTA_COLUMNA));
        holder.medida.setText(map.get(QUINTA_COLUMNA));
        holder.precio.setText(map.get(SEXTA_COLUMNA));
        holder.id.setText(map.get(SEPTIMA_COLUMNA));
        holder.codigo.setText(map.get(OCTAVA_COLUMNA));
        return convertView;
    }
}
