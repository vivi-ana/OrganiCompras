package acostapeter.com.organicompras;

import android.content.Context;
import android.database.Cursor;

import java.text.DecimalFormat;
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
        String nombre="", marca = "", neto = "", medida = "", cantidad_producto;
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
                        if(datos_producto.moveToFirst()){
                        nombre = datos_producto.getString(1);
                        marca = datos_producto.getString(2);
                        neto = datos_producto.getString(3);
                        medida = datos_producto.getString(4);
                        }else{
                            Cursor datos_producto_no_encontrado = admin.recargar_producto_no_encontrado(id_producto);
                            if (datos_producto_no_encontrado.moveToFirst())
                                nombre = datos_producto_no_encontrado.getString(1);
                        }
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
    int cantidad_productos_inventario(){
        int cantidad =0;
        cantidad = admin.cantidad_inventario();
        return cantidad;
    }
    void guardar_inventario(){
        admin.guardar_inventario();
    }
    void insertar_inventario(){
        admin.insertar_inventario(id_producto);
    }
    void actualizar_inverntario(){
        admin.actualizar_inventario(cantidad, id_producto);
    }
    void borrar_inventario(){
        admin.borrar_inventario();
    }

    double calcular_total_aproximado(int id_supermercado){
        DecimalFormat df = new DecimalFormat("0.00");
        double total = -1, subtotal = 0, subtotal2 = 0, multiplicacion = 0;
        Cursor lista_inventario = admin.recargar_despensa();//traer el inventario
        if (lista_inventario.moveToFirst()){
            total = 0;
            do {//si no tiene datos el valor quedara por defecto "0"
                id_producto = lista_inventario.getString(0); //id del producto
                cantidad = lista_inventario.getInt(1);
                Integer cantidadid = id_producto.length();
                if (cantidadid == 13){ //es ean 13
                    Cursor datos1 = admin.calcular_EAN13_despensa(id_producto, id_supermercado);
                    double mont = Double.parseDouble(datos1.getString(2));
                    String precio = (df.format(mont)).replace(",",".");
                    multiplicacion = Double.parseDouble(precio) * cantidad; // sacar el total = precio x cantidad
                }else{
                    Integer cantidad_productos = admin.contar_productos_despensa(id_producto);//traer cuantos productos hay con ese id
                    if (cantidad_productos > 0) { //hay productos.
                        Cursor datos2 = admin.calcular_productos_despensa(id_producto); //traer todos los productos con ese id
                        if (datos2.moveToFirst()) {
                            do {
                                String idproducto = Long.toString(datos2.getLong(0)); //entrar en la lista y traer el id EAN 13
                                Cursor datos4 = admin.calcular_EAN13_despensa(idproducto, id_supermercado);
                                double mont = Double.parseDouble(datos4.getString(2));
                                String precio = (df.format(mont)).replace(",", ".");
                                subtotal = subtotal + Double.parseDouble(precio);
                            } while (datos2.moveToNext());
                            subtotal2 = subtotal / cantidad_productos; //sacar promedio de todos los productos con ese id
                            subtotal = 0;
                        }
                    }
                    multiplicacion = subtotal2 * cantidad; //multiplicar ese promedio general por la cantidad
                }
                total = total + multiplicacion;
            } while (lista_inventario.moveToNext());
        }
        return total;
    }
}
