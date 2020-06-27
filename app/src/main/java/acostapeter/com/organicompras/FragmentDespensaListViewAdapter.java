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

import static acostapeter.com.organicompras.ConstantesColumnasDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEXTA_COLUMNA;

public class FragmentDespensaListViewAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> lista;
    private Activity activity;
    private static boolean[] checked;
    private static String [] itemborrar;
    private Despensa despensa;


    FragmentDespensaListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista) {
        super();
        this.activity = activity;
        this.lista = lista;
        checked =  new boolean[lista.size()];
        itemborrar = new String[lista.size()];
        despensa = new Despensa(activity);
    }
    String[] getMid(){
        return itemborrar;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public int getCount() {
        return lista.size();
    }
    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }
    void selectAll() {
        for (int i = 0; i < checked.length; i++) {
            checked[i] = true;
        }
        ArrayList<HashMap<String, String>> listado_despensa;
        listado_despensa = despensa.detalle_inventario();
        int bucle = listado_despensa.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_despensa.get(i);
                itemborrar[i] = hashmap.get(CUARTA_COLUMNA); //traer el id de los productos seleccionados
            }
        }
    }
    void unselectAll() {
        for (int i = 0; i < checked.length; i++) {
            checked[i] = false;

        }
        itemborrar = new String[lista.size()];
    }
    private class ViewHolder {
        TextView nombre, descripcion, neto, marca, medida;
        CheckBox id_producto;
        TextView producto_cantidad;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_despensa_columnas, parent, false);
            holder = new ViewHolder();
            holder.marca = convertView.findViewById(R.id.marca);
            holder.descripcion = convertView.findViewById(R.id.descrip);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida =  convertView.findViewById(R.id.medida);
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.producto_cantidad = convertView.findViewById(R.id.cantidad_prod);
            holder.id_producto = convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.id_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.id_producto.setTag(position);
                if (holder.id_producto.isChecked()) {
                    checked[position] = true;
                    itemborrar[position] = holder.id_producto.getText().toString();
                } else {
                    checked[position] = false;
                    itemborrar[position] = null;
                }
            }
        });
        HashMap<String, String> map = lista.get(position);
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        holder.descripcion.setText(map.get(SEGUNDA_COLUMNA));
        holder.producto_cantidad.setText(map.get(TERCERA_COLUMNA));
        holder.id_producto.setText(map.get(CUARTA_COLUMNA));
        holder.marca.setText(map.get(QUINTA_COLUMNA));
        holder.neto.setText(map.get(SEXTA_COLUMNA));
        holder.medida.setText(map.get(SEPTIMA_COLUMNA));
        holder.id_producto.setTag(position);
        holder.id_producto.setChecked(checked[position]);
        if ("".equals(map.get(QUINTA_COLUMNA))) holder.marca.setVisibility(View.GONE);
        if ("".equals(map.get(SEGUNDA_COLUMNA))) holder.descripcion.setVisibility(View.GONE);
        if ("".equals(map.get(SEXTA_COLUMNA))) holder.neto.setVisibility(View.GONE);
        if ("".equals(map.get(SEPTIMA_COLUMNA))) holder.medida.setVisibility(View.GONE);
        return convertView;
    }

}
