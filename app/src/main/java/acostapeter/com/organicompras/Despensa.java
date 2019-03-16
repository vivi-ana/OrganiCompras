package acostapeter.com.organicompras;

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.HashMap;
import static acostapeter.com.organicompras.ConstantesDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesDespensa.SEXTA_COLUMNA;

import acostapeter.com.organicompras.data.DbCRUD;
@SuppressWarnings("all")
public class Despensa {
    private String id_producto;
    private int cantidad;
    private DbCRUD admin;
    Despensa(Context context){
        this.admin = new DbCRUD(context, null);
    }
    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    void borrar_item(){
        admin.borrar_item_despensa(id_producto);
    }
    void borrar_producto_no_encontrado(){
        admin.borrar_producto_no_encontrado(id_producto);
    }
    ArrayList<HashMap<String, String>> detalle_inventario(){
        int cantidad_id;
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        String nombre, marca = "", neto = "", medida = "", cantidad_producto;
        Cursor lista_inventario = admin.recargar_despensa();
        if (lista_inventario.moveToFirst()){
            do {
                id_producto = lista_inventario.getString(0); //id del producto
                cantidad = lista_inventario.getInt(1);
                cantidad_producto = Integer.toString(cantidad);
                cantidad_id = id_producto.length();
                if (id_producto.startsWith("N")) { //si el id del  producto tiene N viene de la tabla producto_no_encontrado sino viene de la tabla productos
                    String idsinN = id_producto.substring(1);
                    Cursor recargarnoproducto = admin.recargar_producto_no_encontrado(idsinN);
                    recargarnoproducto.moveToFirst();
                    nombre = recargarnoproducto.getString(1);
                } else {//no tiene N y por ende es un numero preguntar si tiene 13 caracteres o no (EAN 13)
                    if (cantidad_id == 13){//es un producto con codigo de barra sino es un producto sin codigo de barra
                        Cursor datos_producto = admin.producto(id_producto);
                        datos_producto.moveToFirst();
                        nombre = datos_producto.getString(1);
                        marca = datos_producto.getString(2);
                        neto = datos_producto.getString(3);
                        medida = datos_producto.getString(4);
                    }else {
                        Cursor detalle_producto = admin.recargar_detalle_producto(id_producto);
                        detalle_producto.moveToFirst();
                        nombre = detalle_producto.getString(1);
                    }
                }
                HashMap<String, String> temporal = new HashMap<String, String>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, cantidad_producto);
                temporal.put(TERCERA_COLUMNA, id_producto);
                if (cantidad_id == 13){
                    temporal.put(CUARTA_COLUMNA, marca);
                    temporal.put(QUINTA_COLUMNA, neto);
                    temporal.put(SEXTA_COLUMNA, medida);
                }else {
                    temporal.put(CUARTA_COLUMNA, "");
                    temporal.put(QUINTA_COLUMNA, "");
                    temporal.put(SEXTA_COLUMNA, "");
                }
                lista.add(temporal);
            } while (lista_inventario.moveToNext());
        }
        return lista;
    }
}
