package acostapeter.com.organicompras;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEXTA_COLUMNA;
import java.util.ArrayList;
import java.util.HashMap;

public class MiHistorialActivityBorrarHistorialListViewAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> lista;
    Activity activity;
    private String month;
    private int year;
    private static boolean[] checked;
    private static String [] item_borrar;
    Compras compras;
    MiHistorialActivityBorrarHistorialListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista){
        super();
        this.activity = activity;
        this.lista = lista;
        checked =  new boolean[lista.size()];
        item_borrar = new String[lista.size()];
        compras = new Compras(activity);
        MiHistorialActivity mihistorial = new MiHistorialActivity();
        year = mihistorial.getYear();
        month = mihistorial.getMes();
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
    String[] getMid(){
        return item_borrar;
    }
    void selectAll() {
        for (int i = 0; i < checked.length; i++) {
            checked[i] = true;
        }
        ArrayList<HashMap<String, String>> listado_historial;
        listado_historial = compras.cargar_historial(year, month);
        int bucle = listado_historial.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_historial.get(i);
                item_borrar[i] = hashmap.get(SEXTA_COLUMNA); //traer el id de los productos seleccionados
            }
        }
    }
    void unselectAll() {
        for (int i = 0; i < checked.length; i++) {
            checked[i] = false;
        }
        item_borrar = new String[lista.size()];
    }
    private class ViewHolder {
        TextView fecha, lugar, cantidadp, total, totalunitario;
        CheckBox id;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_mi_historial_borrar_historial_columnas, parent, false);
            holder = new ViewHolder();
            holder.fecha = convertView.findViewById(R.id.txtfecha);
            holder.lugar = convertView.findViewById(R.id.txtlugar);
            holder.cantidadp =  convertView.findViewById(R.id.txtcantidadprod);
            holder.total =  convertView.findViewById(R.id.txttotal);
            holder.totalunitario = convertView.findViewById(R.id.txttotalunitario);
            holder.id = convertView.findViewById(R.id.check);
            holder.id.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.id.setTag(position);
                if (holder.id.isChecked()) {
                    checked[position] = true;
                    item_borrar[position] = holder.id.getText().toString();
                } else {
                    checked[position] = false;
                    item_borrar[position] = null;
                }
            }
        });
        HashMap<String, String> map = lista.get(position);
        holder.fecha.setText(map.get(PRIMERA_COLUMNA));
        holder.lugar.setText(map.get(SEGUNDA_COLUMNA));
        holder.cantidadp.setText(map.get(TERCERA_COLUMNA));
        holder.total.setText(map.get(CUARTA_COLUMNA));
        holder.totalunitario.setText(map.get(QUINTA_COLUMNA));
        holder.id.setText(map.get(SEXTA_COLUMNA));
        holder.id.setTag(position);
        holder.id.setChecked(checked[position]);
        return convertView;
    }
}
