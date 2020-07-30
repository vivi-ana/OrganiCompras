package acostapeter.com.organicompras;

import android.content.Context;
import android.database.Cursor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEXTA_COLUMNA;

import acostapeter.com.organicompras.data.DbCRUD;
@SuppressWarnings("all")
public class Despensa {
    private int id_producto;
    private int cantidad;
    private String detalle;
    private DbCRUD admin;
    Despensa(Context context){
        this.admin = new DbCRUD(context, null);
    }
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    ArrayList<HashMap<String, String>> detalle_inventario(){
        int cantidad_id;
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        String nombre="", cantidad_producto;
        Cursor lista_inventario = admin.recargar_despensa();
        if (lista_inventario.moveToFirst()){
            do {
                String descripcion = "", marca = "", neto = "", medida = "", id = "", detalle = "";
                boolean dato =false;
                id_producto = lista_inventario.getInt(0); //id del producto
                id = Integer.toString(id_producto);
                detalle = lista_inventario.getString(3);
                cantidad = lista_inventario.getInt(1);
                cantidad_producto = Integer.toString(cantidad);
                Cursor producto_detalle = admin.producto_lista(id_producto);
                if (producto_detalle.moveToFirst() || detalle.equals("V")) dato = true;
                Cursor datos_producto = admin.producto(id_producto);
                if(datos_producto.moveToFirst()) {
                    nombre = datos_producto.getString(1);
                    if (dato == true) {
                        descripcion = datos_producto.getString(2);
                        marca = datos_producto.getString(3);
                        neto = datos_producto.getString(4);
                        medida = datos_producto.getString(5);
                    }
                }
                if (neto.equals(0)) neto = "";
                HashMap<String, String> temporal = new HashMap<String, String>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(TERCERA_COLUMNA, cantidad_producto);
                temporal.put(CUARTA_COLUMNA, id);
                temporal.put(SEGUNDA_COLUMNA, descripcion);
                temporal.put(QUINTA_COLUMNA, marca);
                temporal.put(SEXTA_COLUMNA, neto);
                temporal.put(SEPTIMA_COLUMNA, medida);
                lista.add(temporal);
            } while (lista_inventario.moveToNext());
        }
        return lista;
    }
    void borrar_item(){
        admin.borrar_item_despensa(id_producto);
    }
    void insertar_inventario(){
        admin.insertar_inventario(id_producto, detalle);
    }
    int cantidad_productos_inventario(){
        int cantidad =0;
        cantidad = admin.cantidad_inventario();
        return cantidad;
    }
    void guardar_inventario(){
        admin.guardar_inventario();
    }
    void actualizar_inverntario(){
        admin.actualizar_inventario(cantidad, id_producto);
    }
    void borrar_inventario(){
        admin.borrar_inventario();
    }
    double calcular_total_aproximado(int id_supermercado){
        DecimalFormat df = new DecimalFormat("0.00");
        String codigo = "";
        double total = -1, subtotal = 0, subtotal2 = 0, multiplicacion = 0;
        Cursor lista_inventario = admin.recargar_despensa();//traer el inventario
        if (lista_inventario.moveToFirst()){
            total = 0;
            do {//si no tiene datos el valor quedara por defecto "0"
                id_producto = lista_inventario.getInt(0); //id del producto
                cantidad = lista_inventario.getInt(1);
                Long cod = admin.getCodigo(id_producto);
                codigo = Long.toString(cod);
                Cursor datos1 = admin.calcular_despensa(codigo, id_supermercado);
                if (datos1.moveToFirst()){ //esta en la tabla productosxsuper
                    double mont = Double.parseDouble(datos1.getString(2));
                    String precio = (df.format(mont)).replace(",",".");
                    multiplicacion = Double.parseDouble(precio) * cantidad; // sacar el total = precio x cantidad
                }else{
                    String nombre = admin.por_nombre(id_producto);
                    int cantidad_productos = admin.contar_productos_despensa(nombre);
                    if (cantidad_productos > 0) { //hay productos.
                        Cursor datos2 = admin.calcular_productos_despensa(nombre); //traer todos los productos con ese id
                        if (datos2.moveToFirst()) {
                            do {
                                id_producto = datos2.getInt(0); //entrar en la lista y traer el id
                                Long code = admin.getCodigo(id_producto);
                                codigo = Long.toString(code);
                                Cursor datos4 = admin.calcular_despensa(codigo, id_supermercado);
                                if (datos4.moveToFirst()) {
                                    double mont = Double.parseDouble(datos4.getString(2));
                                    String precio = (df.format(mont)).replace(",", ".");
                                    subtotal = subtotal + Double.parseDouble(precio);
                                }
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
