package acostapeter.com.organicompras;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.NOVENA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.TERCERA_COLUMNA;

public class FragmentComprasBorrarListaListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> lista;
    private Activity activity;
    FragmentComprasBorrarListaListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista){
        super();
        this.activity = activity;
        this.lista = lista;
    }
    private class ViewHolder {
        TextView nombre, subtotal, descripcion, marca, cantidad_producto, precio_unidad, neto, medida;
        CheckBox id;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.fragment_compras_borrar_lista_columnas, parent, false);
            holder = new ViewHolder();
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.descripcion = convertView.findViewById(R.id.descrip);
            holder.marca = convertView.findViewById(R.id.marca);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida = convertView.findViewById(R.id.medida);
            holder.cantidad_producto = convertView.findViewById(R.id.cantidad_prod);
            holder.precio_unidad = convertView.findViewById(R.id.precio);
            holder.subtotal = convertView.findViewById(R.id.subtotal);
            holder.id =  convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else{
            holder=(ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = lista.get(position);
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        holder.descripcion.setText(SEGUNDA_COLUMNA);
        holder.marca.setText(map.get(TERCERA_COLUMNA));
        holder.precio_unidad.setText(map.get(CUARTA_COLUMNA));
        holder.cantidad_producto.setText(map.get(QUINTA_COLUMNA));
        holder.subtotal.setText(map.get(SEXTA_COLUMNA));
        holder.id.setText(map.get(SEPTIMA_COLUMNA));
        holder.neto.setText(map.get(OCTAVA_COLUMNA));
        holder.medida.setText(map.get(NOVENA_COLUMNA));
        return convertView;
    }
}
