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
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.TERCERA_COLUMNA;

public class MiDespensaActivityListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> lista;
    Activity activity;
    private int contador = 0;
    private boolean cambiar = false;
    private Despensa despensa;
    private static String [] item_cantidad;
    MiDespensaActivityListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> lista) {
        super();
        this.activity = activity;
        this.lista = lista;
        despensa = new Despensa(activity);
        item_cantidad = new String[lista.size()];
    }
    private void cambiar_cantidad(){
        ArrayList<HashMap<String, String>> listado_despensa;
        listado_despensa = despensa.detalle_inventario();
        int bucle = listado_despensa.size();
        if (bucle != 0) {
            for (int i = 0; i < bucle; i++) {
                HashMap<String, String> hashmap = listado_despensa.get(i);
                item_cantidad[i] = hashmap.get(TERCERA_COLUMNA);
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
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder {
        TextView nombre, id, neto, marca, medida, descripcion;
        TextView producto_cantidad;
        Button btnMas;
        Button btnMenos;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_mi_despensa_columnas, parent, false);
            holder = new ViewHolder();
            holder.descripcion = convertView.findViewById(R.id.descrip);
            holder.marca = convertView.findViewById(R.id.marca);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida = convertView.findViewById(R.id.medida);
            holder.nombre =  convertView.findViewById(R.id.nombre);
            holder.btnMas = convertView.findViewById(R.id.mas);
            holder.btnMenos = convertView.findViewById(R.id.menos);
            holder.producto_cantidad = convertView.findViewById(R.id.cantidad_prod);
            holder.id = convertView.findViewById(R.id.id);
            holder.btnMenos.setEnabled(false);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = lista.get(position);
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        if (cambiar){
            cambiar_cantidad();
            holder.producto_cantidad.setText(item_cantidad[position]);
        }else {
            holder.producto_cantidad.setText(map.get(TERCERA_COLUMNA));
        }
        holder.descripcion.setText(map.get(SEGUNDA_COLUMNA));
        holder.id.setText(map.get(CUARTA_COLUMNA));
        holder.marca.setText(map.get(QUINTA_COLUMNA));
        holder.neto.setText(map.get(SEXTA_COLUMNA));
        holder.medida.setText(map.get(SEPTIMA_COLUMNA));
        if (!"".equals(map.get(QUINTA_COLUMNA))){
            holder.descripcion.setVisibility(View.VISIBLE);
            holder.marca.setVisibility(View.VISIBLE);
            holder.neto.setVisibility(View.VISIBLE);
            holder.medida.setVisibility(View.VISIBLE);
        }
        String txtCantidad;  //verificar el textview del menos en la lista para deshabilitar el menos.
        int contador_cantidad;
        txtCantidad = holder.producto_cantidad.getText().toString();
        contador_cantidad = Integer.parseInt(txtCantidad);
        if (contador_cantidad == 1){
            holder.btnMenos.setEnabled(false);
        }else {
            holder.btnMenos.setEnabled(true);}
            holder.btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiar = true;
                String cantidad_producto;
                String txtContador = holder.producto_cantidad.getText().toString();
                contador = Integer.parseInt(txtContador);
                contador++;
                if (contador != 1) {
                    holder.btnMenos.setEnabled(true);
                }
                String txtProducto = Integer.toString(contador);
                holder.producto_cantidad.setText(txtProducto);
                String id_producto = holder.id.getText().toString(); //Codigo
                Despensa despensa = new Despensa(v.getContext()); //TRAER CONTEXTO EN LISTVIEW ADAPTER!
                despensa.setCantidad(contador);
                despensa.setId_producto(id_producto);
                despensa.actualizar_inverntario();
                int total= despensa.cantidad_productos_inventario();
                cantidad_producto = Integer.toString(total);
                ((MiDespensaActivity) activity).producto(cantidad_producto);
            }
        });
        holder.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiar = true;
                String cantidad_producto;
                String txtCantidad = holder.producto_cantidad.getText().toString();
                contador = Integer.parseInt(txtCantidad);
                contador--;
                if (contador == 1) {
                    holder.btnMenos.setEnabled(false);
                }
                String txtProducto = Integer.toString(contador);
                holder.producto_cantidad.setText(txtProducto);
                String id_producto = holder.id.getText().toString(); //Codigo de barra
                Despensa despensa = new Despensa(v.getContext()); //TRAER CONTEXTO EN LISTVIEW ADAPTER!
                despensa.setCantidad(contador);
                despensa.setId_producto(id_producto); //hacer el update
                despensa.actualizar_inverntario();
                int total= despensa.cantidad_productos_inventario();
                cantidad_producto = Integer.toString(total);
                ((MiDespensaActivity) activity).producto(cantidad_producto);
            }
        });
        return convertView;
    }
}

