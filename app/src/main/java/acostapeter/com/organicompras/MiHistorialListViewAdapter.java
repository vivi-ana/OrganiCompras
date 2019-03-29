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
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEXTA_COLUMNA;

public class MiHistorialListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> lista;
    Activity activity;


    MiHistorialListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista){
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
        TextView fecha, lugar, cantidadp, total, totalunitario;
        CheckBox id;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_mi_historial_columnas, parent, false);
            holder = new ViewHolder();

            holder.fecha = convertView.findViewById(R.id.txtfecha);
            holder.lugar =  convertView.findViewById(R.id.txtlugar);
            holder.cantidadp = convertView.findViewById(R.id.txtcantidadprod);
            holder.total =  convertView.findViewById(R.id.txttotal);
            holder.totalunitario = convertView.findViewById(R.id.txttotalunitario);
            holder.id = convertView.findViewById(R.id.check);
            holder.id.setVisibility(View.GONE);


            convertView.setTag(holder);

        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = lista.get(position);
        holder.fecha.setText(map.get(PRIMERA_COLUMNA));
        holder.lugar.setText(map.get(SEGUNDA_COLUMNA));

        holder.cantidadp.setText(map.get(TERCERA_COLUMNA));
        holder.total.setText(map.get(CUARTA_COLUMNA));
        holder.totalunitario.setText(map.get(QUINTA_COLUMNA));
        holder.id.setText(map.get(SEXTA_COLUMNA));
        return convertView;
    }
}
