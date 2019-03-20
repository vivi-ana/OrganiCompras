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

import static acostapeter.com.organicompras.ConstantesDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.TERCERA_COLUMNA;

public class MiDespensaActivityBorrarListaListViewAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> lista;
    private Activity activity;
    private static boolean[] checked;
    private static String [] itemborrar;
    private Despensa despensa;
    MiDespensaActivityBorrarListaListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista) {
        super();
        this.activity = activity;
        this.lista = lista;
        checked =  new boolean[lista.size()];
        itemborrar = new String[lista.size()];
        despensa = new Despensa(activity);
    }
    @Override
    public int getCount() {
        return 0;
    }
    String[] getMid(){
        return itemborrar;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
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
                itemborrar[i] = hashmap.get(TERCERA_COLUMNA); //traer el id de los productos seleccionados
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
        TextView nombre, neto, marca, medida;
        CheckBox id_producto;
        TextView producto_cantidad;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MiDespensaActivityBorrarListaListViewAdapter.ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_mi_despensa_borrar_lista_columnas, parent);
            holder = new ViewHolder();
            holder.marca = convertView.findViewById(R.id.marca);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida =  convertView.findViewById(R.id.medida);
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.producto_cantidad = convertView.findViewById(R.id.cantidad_prod);
            holder.id_producto = convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = ( MiDespensaActivityBorrarListaListViewAdapter.ViewHolder) convertView.getTag();
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
        holder.producto_cantidad.setText(map.get(SEGUNDA_COLUMNA));
        holder.id_producto.setText(map.get(TERCERA_COLUMNA));
        holder.marca.setText(map.get(CUARTA_COLUMNA));
        holder.neto.setText(map.get(QUINTA_COLUMNA));
        holder.medida.setText(map.get(SEXTA_COLUMNA));
        if (!"".equals(map.get(CUARTA_COLUMNA))) { //si no esta vacia la cuarta columna se habilita el resto de los text
            holder.marca.setVisibility(View.VISIBLE);
            holder.neto.setVisibility(View.VISIBLE);
            holder.medida.setVisibility(View.VISIBLE);
        }
        holder.id_producto.setTag(position);
        holder.id_producto.setChecked(checked[position]);
        return convertView;
    }

}
