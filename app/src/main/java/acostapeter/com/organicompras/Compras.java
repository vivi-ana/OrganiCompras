package acostapeter.com.organicompras;
import acostapeter.com.organicompras.data.DbCRUD;
import android.content.Context;
import android.database.Cursor;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static acostapeter.com.organicompras.ConstantesColumnasCompras.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.NOVENA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.TERCERA_COLUMNA;
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
    void agregar_detalle_compra(int id_producto){
        admin.agregar_detalle_compra(id, id_producto, total_unitario);
    }
    ArrayList<HashMap<String, String>> detalle_compras(int id_compra){
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        String producto_id = "", cantidades, montos, descripcion ="", neto = "", medida = "", nombre = "", marca = "", precio_unitario = "";
        DecimalFormat df = new DecimalFormat("0.00");
        int id_producto;
        double monto;
        Cursor lista_compras = admin.detalle_compra(id_compra);
        if (lista_compras.moveToFirst()){
            do {
                id_producto  = lista_compras.getInt(2);
                producto_id = Integer.toString(id_producto); //id del producto
                cantidades = lista_compras.getString(3);
                montos = lista_compras.getString(4);
                monto = Double.parseDouble(montos);
                montos = (df.format(monto)).replace(",",".");
                Cursor datos_producto = admin.producto(id_producto);
                if (datos_producto.moveToFirst()) {
                    marca = "";
                    neto = "";
                    medida = "";
                    descripcion = "";
                    nombre = datos_producto.getString(1);
                    descripcion = datos_producto.getString(2);
                    marca = datos_producto.getString(3);
                    neto = datos_producto.getString(4);
                    medida = datos_producto.getString(5);
                    String codigo = admin.getCodigo(id_producto);
                    double preun = 0;
                    if(!codigo.equals("")){
                    preun = admin.por_supermercado(codigo, supermercado); //se busca primero en la tabla existentes
                    precio_unitario = (df.format(preun)).replace(",", ".");
                    }else{
                        Cursor producto_no_encontrado = admin.producto_comprado_no_encontrado(id_producto, supermercado);
                        if(producto_no_encontrado.moveToFirst()){
                            preun = Double.parseDouble(producto_no_encontrado.getString(0));
                            precio_unitario = (df.format(preun)).replace(",", ".");
                        }
                    }
                }
                HashMap<String, String> temporal = new HashMap<>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, descripcion);
                temporal.put(TERCERA_COLUMNA, marca);
                temporal.put(CUARTA_COLUMNA, precio_unitario);
                temporal.put(QUINTA_COLUMNA, cantidades);
                temporal.put(SEXTA_COLUMNA, montos);
                temporal.put(SEPTIMA_COLUMNA, producto_id);
                temporal.put(OCTAVA_COLUMNA, neto);
                temporal.put(NOVENA_COLUMNA, medida);
                lista.add(temporal);
            } while (lista_compras.moveToNext());
        }
        return lista;
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
    void total_productos(int id_producto){
    cant_total_productos = admin.contar_productos(id, id_producto);
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
        if(cantidad_productos!=null)
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
    void contar_productos_compra(){
        cant_total_productos = admin.contar_productos_detalle(id);
    }
    void calcular_total_compra(){
        total = admin.calcular_total(id);
    }

    ArrayList<HashMap<String, String>> detalle_compras_editada(int id_producto){
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        String prod_id, cantidades, montos, descripcion = "", neto = "", medida = "", nombre = "", marca = "", precio_unitario = "";
        DecimalFormat df = new DecimalFormat("0.00");
        int producto_id;
        double monto;
        Cursor lista_compras = admin.detalle_compra_editada(id, id_producto);
        if (lista_compras.moveToFirst()){
            do {
                producto_id = lista_compras.getInt(2);//id del producto
                prod_id = Integer.toString(producto_id);
                cantidades = lista_compras.getString(3);
                montos = lista_compras.getString(4);
                monto = Double.parseDouble(montos);
                montos = (df.format(monto)).replace(",",".");
                Cursor datos_producto = admin.producto(producto_id);
                if (datos_producto.moveToFirst()) {
                    marca = ""; neto = ""; medida = ""; descripcion = "";
                    nombre = datos_producto.getString(1);
                    descripcion = datos_producto.getString(2);
                    marca = datos_producto.getString(3);
                    neto = datos_producto.getString(4);
                    medida = datos_producto.getString(5);
                    String codigo = admin.getCodigo(id_producto);
                    double preun = 0;
                    if(!codigo.equals("")) {
                        preun = admin.por_supermercado(codigo, supermercado); //se busca primero en la tabla existentes
                        precio_unitario = (df.format(preun)).replace(",", ".");
                    }else{
                        Cursor producto_no_encontrado = admin.producto_comprado_no_encontrado(id_producto, supermercado);
                        if(producto_no_encontrado.moveToFirst()){
                           preun = Double.parseDouble(producto_no_encontrado.getString(0));
                            precio_unitario = (df.format(preun)).replace(",", ".");
                        }
                    }
                }
                HashMap<String, String> temporal = new HashMap<>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, descripcion);
                temporal.put(TERCERA_COLUMNA, marca);
                temporal.put(CUARTA_COLUMNA, precio_unitario);
                temporal.put(QUINTA_COLUMNA, cantidades);
                temporal.put(SEXTA_COLUMNA, montos);
                temporal.put(SEPTIMA_COLUMNA, prod_id);
                temporal.put(OCTAVA_COLUMNA, neto);
                temporal.put(NOVENA_COLUMNA, medida);
                lista.add(temporal);
            } while (lista_compras.moveToNext());
        }
        return lista;
    }
    void actualizar_monto_detalle(int id_producto){
        admin.actualizar_monto(total ,id, id_producto);
    }
    ArrayList<HashMap<String, String>> cargar_historial(int year, String mes){
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String lugar = "", cantidad = "", total = "", id = "", fecha_sin_formato = "", fecha = "";
        DecimalFormat df = new DecimalFormat("0.00");
        Cursor lista_historial = admin.cargar_historial(year, mes);
        if (lista_historial.moveToFirst()){
            do {
                fecha_sin_formato = lista_historial.getString(2);
                try {
                    fecha = myFormat.format(fromUser.parse(fecha_sin_formato));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                double totaluni = lista_historial.getDouble(6);
                String total_unitario = df.format(totaluni);
                int supermercado = lista_historial.getInt(1);
                Cursor nombre_supermerado = admin.nombre_supermercado(supermercado);
                if (nombre_supermerado.moveToFirst()) {
                    lugar = nombre_supermerado.getString(1);
                }
                cantidad = lista_historial.getString(4);
                double t = Double.parseDouble(lista_historial.getString(5));
                total = df.format(t);
                id = lista_historial.getString(0);
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(ConstantesColumnasHistorial.PRIMERA_COLUMNA, fecha);
                temp.put(ConstantesColumnasHistorial.SEGUNDA_COLUMNA, lugar);
                temp.put(ConstantesColumnasHistorial.TERCERA_COLUMNA, cantidad);
                temp.put(ConstantesColumnasHistorial.CUARTA_COLUMNA, total);
                temp.put(ConstantesColumnasHistorial.QUINTA_COLUMNA, total_unitario);
                temp.put(ConstantesColumnasHistorial.SEXTA_COLUMNA, id);
                lista.add(temp);
            } while (lista_historial.moveToNext());
        }
        return lista;
    }
    ArrayList<HashMap<String, String>> cargar_detalle(int id_compra){
        ArrayList<HashMap<String, String>> listado_compras = new ArrayList<>();
        String id_producto, cantidades, montos, id_detalle;
        DecimalFormat df = new DecimalFormat("0.00");
        long producto_id;
        double monto;
        Cursor lista_compras = admin.detalle_compra(id_compra);
        if (lista_compras.moveToFirst()){
            do {
                id_detalle = lista_compras.getString(0);
                producto_id = lista_compras.getLong(2);
                id_producto = Long.toString(producto_id); //id del producto
                cantidades = lista_compras.getString(3);
                montos = lista_compras.getString(4);
                monto = Double.parseDouble(montos);
                montos = (df.format(monto)).replace(",",".");
                HashMap<String, String> temporal = new HashMap<>();
                temporal.put(PRIMERA_COLUMNA, id_detalle);
                temporal.put(SEGUNDA_COLUMNA, String.valueOf(id_compra));
                temporal.put(TERCERA_COLUMNA, id_producto);
                temporal.put(CUARTA_COLUMNA, cantidades);
                temporal.put(QUINTA_COLUMNA, montos);
                listado_compras.add(temporal);
            } while (lista_compras.moveToNext());
        }
        return listado_compras;
    }
    ArrayList<HashMap<String, String>> cargar_compra(int id_compra){
        ArrayList<HashMap<String, String>> listado_compras = new ArrayList<>();
        String montos,precio_unitario = "";
        DecimalFormat df = new DecimalFormat("0.00");
        Cursor lista_compras = admin.compra_datos(id_compra);
        if (lista_compras.moveToFirst()){
            do {
                supermercado = lista_compras.getInt(1);
                fecha = lista_compras.getString(2);
                max = lista_compras.getInt(3);
                cantidad = lista_compras.getInt(4);
                total = lista_compras.getDouble(5);
                montos = (df.format(total)).replace(",",".");
                total_unitario = lista_compras.getDouble(6);
                precio_unitario = (df.format(total_unitario)).replace(",",".");

                HashMap<String, String> temporal = new HashMap<>();
                temporal.put(PRIMERA_COLUMNA, String.valueOf(id_compra));
                temporal.put(SEGUNDA_COLUMNA, String.valueOf(supermercado));
                temporal.put(TERCERA_COLUMNA, fecha);
                temporal.put(CUARTA_COLUMNA, String.valueOf(max));
                temporal.put(QUINTA_COLUMNA, String.valueOf(cantidad));
                temporal.put(SEXTA_COLUMNA, montos);
                temporal.put(SEPTIMA_COLUMNA, precio_unitario);
                listado_compras.add(temporal);
            } while (lista_compras.moveToNext());
        }
        return listado_compras;
    }
}
