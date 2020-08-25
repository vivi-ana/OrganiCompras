package acostapeter.com.organicompras;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.NOVENA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.TERCERA_COLUMNA;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
public class FragmentComprasListViewAdapter extends BaseAdapter {
    private DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<HashMap<String, String>> lista;
    private Activity activity;
    private int contador = 0;
    FragmentComprasListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> lista){
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
        TextView nombre, sub_total, descripcion, marca, cantidad_producto, precio_unidad, id_producto, neto, medida;
        Button bmas, bmenos;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null){ //cargar la interfaz de cada fila
            convertView = inflater.inflate(R.layout.fragment_compras_columnas, parent, false);
            holder = new ViewHolder();
            holder.bmas = convertView.findViewById(R.id.mas);
            holder.bmenos = convertView.findViewById(R.id.menos);
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.descripcion = convertView.findViewById(R.id.descrip);
            holder.marca =  convertView.findViewById(R.id.marca);
            holder.neto = convertView.findViewById(R.id.neto);
            holder.medida = convertView.findViewById(R.id.medida);
            holder.cantidad_producto = convertView.findViewById(R.id.cantidad_prod);
            holder.precio_unidad = convertView.findViewById(R.id.precio);
            holder.sub_total = convertView.findViewById(R.id.subtotal);
            holder.id_producto = convertView.findViewById(R.id.id);
            holder.bmenos.setEnabled(false);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = lista.get(position); //cargar cada fila de datos
        holder.nombre.setText(map.get(PRIMERA_COLUMNA));
        holder.descripcion.setText(map.get(SEGUNDA_COLUMNA));
        holder.marca.setText(map.get(TERCERA_COLUMNA));
        holder.precio_unidad.setText(map.get(CUARTA_COLUMNA));
        holder.cantidad_producto.setText(map.get(QUINTA_COLUMNA));
        holder.sub_total.setText(map.get(SEXTA_COLUMNA));
        holder.id_producto.setText(map.get(SEPTIMA_COLUMNA));
        holder.neto.setText(map.get(OCTAVA_COLUMNA));
        holder.medida.setText(map.get(NOVENA_COLUMNA));
        String txt_cantidad;//verificar el textview del menos en la lista para deshabilitar el menos.
        int contador_cantidad;
        txt_cantidad = holder.cantidad_producto.getText().toString();
        contador_cantidad = Integer.parseInt(txt_cantidad);
        if (contador_cantidad == 1){
            holder.bmenos.setEnabled(false);
        }else {
            holder.bmenos.setEnabled(true);
        }
        holder.bmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) { //evento click del mas y el menos.
                String total, cantidad_producto;
                int id_compras;
                String txt_cantidad = holder.cantidad_producto.getText().toString();
                String txt_precio_por_unidad = holder.precio_unidad.getText().toString();
                double precio_unidad = Double.parseDouble(txt_precio_por_unidad);
                contador = Integer.parseInt(txt_cantidad);
                contador++;
                double calculo_subtotal =  precio_unidad * contador ; //subtotal de cada producto
                String precio_subtotal = (df.format(calculo_subtotal)).replace(",","."); //enviar al txtsubtotal
                holder.sub_total.setText(precio_subtotal);
                if (contador != 1) {
                    holder.bmenos.setEnabled(true);
                }
                cantidad_producto = Integer.toString(contador);
                holder.cantidad_producto.setText(cantidad_producto);
                String id_producto = holder.id_producto.getText().toString(); //Codigo de barra
                Compras compras = new Compras(vista.getContext());
                id_compras = compras.getId();
                //maximo_detalle_compra(); //id maximo del detalle de la compra.
                compras.setCantidad(contador);  //hacer el update
                compras.setTotal_unitario(calculo_subtotal);
                compras.setId(id_compras);
                compras.actializar_cantidad_comprada(id_producto);
                total = compras.verificar_monto(); //traer los datos actualizados del total y cantidad para los TXT
                cantidad_producto =  compras.total_productos_comprados();
                compras.verificar_maximo_compra();
                    int maximo = compras.getMax();
                    if (maximo > 0) {
                        double total_comprado = Double.parseDouble(total);
                        if (total_comprado >= maximo) {
                            Toast.makeText(vista.getContext(), "Estas comprando de mas", Toast.LENGTH_SHORT).show();
                        }
                    }
                actualizar_fragment(total, cantidad_producto);
            }
        });
        holder.bmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                String total, cantidad_producto;
                int id_compras;
                String txt_cantidad = holder.cantidad_producto.getText().toString();
                String txt_precio_por_unidad = holder.precio_unidad.getText().toString();
                double precio_unidad = Double.parseDouble(txt_precio_por_unidad);
                contador = Integer.parseInt(txt_cantidad);
                contador--;
                double calculo_subtotal =  precio_unidad * contador ; //subtotal de cada producto
                String precio_subtotal = (df.format(calculo_subtotal)).replace(",","."); //enviar al txtsubtotal
                holder.sub_total.setText(precio_subtotal);
                if (contador == 1) {
                    holder.bmenos.setEnabled(false);
                }
                cantidad_producto = Integer.toString(contador);
                holder.cantidad_producto.setText(cantidad_producto);
                String id_producto = holder.id_producto.getText().toString(); //Codigo de barra
                Compras compras = new Compras(vista.getContext());
                id_compras = compras.getId();
                //maximo_detalle_compra(); //id maximo del detalle de la compra.
                compras.setCantidad(contador);//hacer el update
                compras.setTotal_unitario(calculo_subtotal);
                compras.setId(id_compras);
                compras.actializar_cantidad_comprada(id_producto);
                total = compras.verificar_monto();//traer los datos actualizados del total y cantidad para los TXT
                cantidad_producto =  compras.total_productos_comprados();
                actualizar_fragment(total,cantidad_producto);
            }
        });
        return convertView;
    }
    private void actualizar_fragment(String total, String cantidad) { //esto estaba en una clase externa
        double monto = Double.parseDouble(total);
        String nuevo_monto = (df.format(monto)).replace(",","."); //dar formato
        FragmentCompras fragmentCompras =new FragmentCompras();
        TextView txt_total = fragmentCompras.actualizar_txt_total();
        txt_total.setText(nuevo_monto);
        TextView txt_cantidad = fragmentCompras.actualizar_txt_cantidad();
        txt_cantidad.setText(cantidad);
    }
}
