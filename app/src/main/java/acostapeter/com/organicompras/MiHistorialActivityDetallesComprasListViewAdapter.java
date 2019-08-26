package acostapeter.com.organicompras;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class MiHistorialActivityDetallesComprasListViewAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> lista;
    private Activity activity;
    int id;
    private boolean cambiar;
    private Compras compras;
    private Supermercado supermercado;
    private static String [] itemcantidad;
    MiHistorialActivityDetallesComprasListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista, int id) {
        super();
        this.id = id;
        this.activity = activity;
        this.lista = lista;
        compras = new Compras(activity);
        supermercado = new Supermercado(activity);
        itemcantidad = new String[lista.size()];
        cambiar = false;
    }
    private void cambiar_cantidad(){
        ArrayList<HashMap<String, String>> listado_compras;
        compras.setId(id);
        supermercado.supermercado_compra_editada(id);
        int id_supermercado = supermercado.getId();
        compras.setSupermercado(id_supermercado);//enviar id del supermercado tambien para que traiga las cantidades del producto bien.
        listado_compras = compras.detalle_compras(id);
        int bucle = listado_compras.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_compras.get(i);
                itemcantidad[i] = hashmap.get(QUINTA_COLUMNA);
            }
        }
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
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView nombre, descripcion, id, neto, marca, medida, precio_unidad;
        TextView producto_cantidad, subtotal;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.activity_mi_historial_compras_detalles_columnas,parent, false);
            holder = new ViewHolder();
            holder.marca = convertView.findViewById(R.id.marca);
            holder.descripcion = convertView.findViewById(R.id.descrip);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida = convertView.findViewById(R.id.medida);
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.subtotal = convertView.findViewById(R.id.subtotal);
            holder.producto_cantidad = convertView.findViewById(R.id.cantidad_prod);
            holder.precio_unidad =  convertView.findViewById(R.id.precio);
            holder.id =  convertView.findViewById(R.id.id);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = lista.get(position);
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        if (cambiar){
            cambiar_cantidad();
            holder.producto_cantidad.setText(itemcantidad[position]);
        }else {
            holder.producto_cantidad.setText(map.get(QUINTA_COLUMNA));
        }
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        holder.descripcion.setText(map.get(SEGUNDA_COLUMNA));
        holder.marca.setText(map.get(TERCERA_COLUMNA));
        holder.precio_unidad.setText(map.get(CUARTA_COLUMNA));
        holder.subtotal.setText(map.get(SEXTA_COLUMNA));
        holder.id.setText(map.get(SEPTIMA_COLUMNA));
        holder.neto.setText(map.get(OCTAVA_COLUMNA));
        holder.medida.setText(map.get(NOVENA_COLUMNA));
        return convertView;
    }
}
