package acostapeter.com.organicompras;
import acostapeter.com.organicompras.data.DbCRUD;
import android.content.Context;
import android.database.Cursor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import static acostapeter.com.organicompras.ConstantesFilaCompras.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesFilaCompras.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesFilaCompras.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesFilaCompras.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesFilaCompras.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesFilaCompras.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesFilaCompras.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesFilaCompras.TERCERA_COLUMNA;
@SuppressWarnings("all")
public class Compras {
private int id;
private int supermercado;
private String fecha;
private int max;
private int cantidad;
private double total;
private double total_unitario;
private int cant_total_productos;
private boolean vacio = false;
private DbCRUD admin;

    public Compras(int id, int supermercado, String fecha, int max, int cantidad, double total, double total_unitario, int cant_total_productos) {
        this.id = id;
        this.supermercado = supermercado;
        this.fecha = fecha;
        this.max = max;
        this.cantidad = cantidad;
        this.total = total;
        this.total_unitario = total_unitario;
        this.cant_total_productos = cant_total_productos;
    }
    Compras(Context context){
        this.admin = new DbCRUD(context, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int getSupermercado() {
        return supermercado;
    }

    void setSupermercado(int supermercado) {
        this.supermercado = supermercado;
    }

    boolean isVacio() {
        return vacio;
    }

    void setFecha(String fecha) {
        this.fecha = fecha;
    }

    int getMax() {
        return max;
    }

    void setMax(int max) {
        this.max = max;
    }

    public int getCantidad() {
        return cantidad;
    }

    void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    void setTotal(double total) {
        this.total = total;
    }

    public double getTotal_unitario() {
        return total_unitario;
    }

    int getCant_total_productos() {
        return cant_total_productos;
    }

    public void setCant_total_productos(int cant_total_productos) {
        this.cant_total_productos = cant_total_productos;
    }
    void setTotal_unitario(double total_unitario) {
        this.total_unitario = total_unitario;
    }
    void agregar_compra(){
        admin.agregar_compra(max, supermercado, fecha);
    }
    void maximo_compra() {
        Cursor lista_compras = admin.maximo_compra();
        if (lista_compras.moveToFirst()) {
            id = lista_compras.getInt(0);
            supermercado = lista_compras.getInt(1);;
        }
    }
    int maximo_detalle_compra() {
        int id_detalle;
        id_detalle = admin.maximo_detalle_compra();
        return id_detalle;
    }
    void total_productos(String id_producto){
    cant_total_productos = admin.contar_productos(id, id_producto);
    }
    void agregar_detalle_compra(String id_producto){
        admin.agregar_detalle_compra(id, id_producto, total_unitario);
    }
    ArrayList<HashMap<String, String>> detalle_compras(int id_compra){
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        String id_producto, cantidades, montos, neto = "", medida = "", nombre = "", marca = "", precio_unitario = "";
        DecimalFormat df = new DecimalFormat("0.00");
        long producto_id;
        double monto;
        Cursor lista_compras = admin.detalle_compra(id_compra);
        if (lista_compras.moveToFirst()){
            do {
                producto_id = lista_compras.getLong(2);
                id_producto = Long.toString(producto_id); //id del producto
                cantidades = lista_compras.getString(3);
                montos = lista_compras.getString(4);
                monto = Double.parseDouble(montos);
                montos = (df.format(monto)).replace(",",".");
                Cursor datos_producto = admin.producto(id_producto);
                if (datos_producto.moveToFirst()) {
                    nombre = datos_producto.getString(1);
                    marca = datos_producto.getString(2);
                    neto = datos_producto.getString(3);
                    medida = datos_producto.getString(4);
                    double preun = admin.por_supermercado(id_producto, supermercado);
                    precio_unitario = (df.format(preun)).replace(",",".");
                }else{
                    Cursor producto_no_encontrado = admin.producto_comprado_no_encontrado(id_producto);
                    if(producto_no_encontrado.moveToFirst()){
                        nombre = producto_no_encontrado.getString(0);
                        double preun = Double.parseDouble(producto_no_encontrado.getString(1));
                        precio_unitario = (df.format(preun)).replace(",", ".");
                    }
                }
                HashMap<String, String> temporal = new HashMap<>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, marca);
                temporal.put(TERCERA_COLUMNA, precio_unitario);
                temporal.put(CUARTA_COLUMNA, cantidades);
                temporal.put(QUINTA_COLUMNA, montos);
                temporal.put(SEXTA_COLUMNA, id_producto);
                temporal.put(SEPTIMA_COLUMNA, neto);
                temporal.put(OCTAVA_COLUMNA, medida);
                lista.add(temporal);
            } while (lista_compras.moveToNext());
        }else {
            lista=null;
        }
        return lista;
    }
    void actializar_cantidad_comprada(String id_producto){
        admin.actualizar_cantidad(cantidad, total_unitario, id, id_producto);
    }
    String verificar_monto(){
        String monto;
        monto = admin.monto(id);
        total = Double.parseDouble(monto);
        return monto;
    }
    String total_productos_comprados(){
        String cantidad_productos;
        cantidad_productos = admin.cantidad_productos_comprados(id);
        cant_total_productos = Integer.parseInt(cantidad_productos);
        return cantidad_productos;
    }
    void verificar_maximo_compra(){
        max = admin.verificar_maximo(id);
    }
    void borrar_compra(){
        admin.borrar_compras(id);
    }
    void actualizar_compra(){
        admin.actualizar_compra(id, cantidad,total,total_unitario);
    }
    void borrar_detalle_compra(){
        admin.borrar_detalle_compras(id);
    }
    void cantidad_producto_editada(String id_producto){
        admin.cantidad_producto_editada(cantidad, id, id_producto);
    }
    void resetear_detalle(int id_detalle, String id_producto, double monto){
        admin.reset_detalle(id_detalle, id, id_producto,cantidad, monto);
    }
    void resetear_compras(){
        admin.reset_compras(id, supermercado, fecha, max, cantidad, total, total_unitario);
    }
    void cargar_algunos_detalles_compras(){
        Cursor datos_compra = admin.compra_datos(id);
        if (datos_compra.moveToFirst()) {
            vacio = false;
            max = datos_compra.getInt(3);
            cantidad = datos_compra.getInt(4);
            total = datos_compra.getDouble(5);
        }else vacio=true;
    }
    void borrar_item(String id_producto){
    admin.borrar_item(id, id_producto);
    }
}
