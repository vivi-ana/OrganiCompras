package acostapeter.com.organicompras;

import android.content.Context;
import android.database.Cursor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.TERCERA_COLUMNA;
import acostapeter.com.organicompras.data.DbCRUD;
@SuppressWarnings("all")
public class Productos {
    private String id_producto;
    private String nombre;
    private String marca;
    private double neto;
    private String medida;
    private double precio;
    private int id_supermercado;
    private DbCRUD admin;

    Productos(Context context) {
        this.admin = new DbCRUD(context, null);
    }

    public int getId_supermercado() {
        return id_supermercado;
    }

    void setId_supermercado(int id_supermercado) {
        this.id_supermercado = id_supermercado;
    }

    public String getId() {
        return id_producto;
    }

    public void setId(String id) {
        this.id_producto = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getNeto() {
        return neto;
    }

    public void setNeto(double neto) {
        this.neto = neto;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    void precio() {
        precio = admin.por_supermercado(id_producto, id_supermercado);
    }

    void producto_no_encontrado() {
        Cursor producto_no_encontrado = admin.compra_producto_no_encontrado(id_producto);
        if (producto_no_encontrado.moveToFirst()) {
            nombre = producto_no_encontrado.getString(0);
            String precio_producto = producto_no_encontrado.getString(1);
            precio = Double.parseDouble(precio_producto);
        }

    }
    void agregar_compras_producto_no_encontrado() {
        admin.compras_agregar_prod_no_encontrado(id_producto, id_supermercado, nombre, precio);
    }
    ArrayList<HashMap<String, String>> cargar_producto_no_encontrado() {
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        Cursor producto_no_encontrado = admin.cargar_no_producto(id_supermercado);
        if (producto_no_encontrado.moveToFirst()) {
            do {
                long producto_id = producto_no_encontrado.getLong(0);//id del producto
                id_producto = Long.toString(producto_id); //id del producto
                nombre = producto_no_encontrado.getString(2);
                precio = producto_no_encontrado.getDouble(3);
                String precio_producto = (df.format(precio)).replace(",", ".");
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEGUNDA_COLUMNA, precio_producto);
                temp.put(TERCERA_COLUMNA, id_producto);
                lista.add(temp);
            } while (producto_no_encontrado.moveToNext());
        }
        return lista;
    }
    void actualizar_producto_no_encontrado(){
        admin.editar_producto_no_encontrado(nombre, precio,id_supermercado,id_producto);
    }
    ArrayList<HashMap<String, String>> cargar_producto_especifico(){
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        Cursor producto_especifico = admin.buscar_producto_especifico(id_producto);
        if (producto_especifico.moveToFirst()) {
            do {
                long producto_id = producto_especifico.getLong(0);//id del producto
                id_producto = Long.toString(producto_id); //id del producto
                nombre = producto_especifico.getString(1);
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(TERCERA_COLUMNA, id_producto);
                lista.add(temp);
            } while (producto_especifico.moveToNext());
        }
        return lista;
    }
    ArrayList<HashMap<String, String>> producto_por_nombre(){
        ArrayList<HashMap<String, String>> lista_producto = new ArrayList<>();
        Cursor producto_especifico = admin.buscar_producto_por_nombre(nombre);
        if (producto_especifico.moveToFirst()) {
            do {
                long producto_id = producto_especifico.getLong(0);//id del producto
                id_producto = Long.toString(producto_id); //id del producto
                nombre = producto_especifico.getString(1);
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(TERCERA_COLUMNA, id_producto);
                lista_producto.add(temp);
            } while (producto_especifico.moveToNext());
        }
        return lista_producto;
    }
    List<MiDespensaActivityObjeto> editText_buscar(String item){
        List<MiDespensaActivityObjeto> lista_productos = new ArrayList<MiDespensaActivityObjeto>();
        lista_productos = admin.editText_busqueda(item);
        return lista_productos;
    }
    void insertar_producto_no_encontrado_despensa(){
        admin.producto_no_encontrado_despensa(nombre);
    }
    int maximo_producto_no_encontrado_despensa(){
        int maximo = 0;
        maximo = admin.maximo_producto_no_encontrado();
        return maximo;
    }
    void guardar_producto_no_encontrado_despensa(){
        admin.guardar_producto_no_encontrado_despensa();
    }
    boolean buscar_producto_no_encontrado(){
        boolean lista = false;
        Cursor producto_especifico = admin.buscar_producto_no_encontrado_despensa(nombre);
        if (producto_especifico.moveToFirst()) {
            lista = true;
        }
        return lista;
    }
    boolean buscar_producto_no_encontrado_despensa(){
        boolean lista_productos = false;
        Cursor producto_especifico = admin.buscar_producto_especifico_despensa(id_producto, nombre);
        if (producto_especifico.moveToFirst()) {
            lista_productos = true;
        }
        return lista_productos;
    }
    boolean producto_no_encontrado_despensa(){
        boolean lista_producto_no_encontrado = false;
        Cursor producto_especifico = admin.recargar_producto_no_encontrado(id_producto);
        if (producto_especifico.moveToFirst()) {
            lista_producto_no_encontrado = true;
        }
        return lista_producto_no_encontrado;
    }
    boolean producto_especifico_despensa(){
        boolean lista_producto_especifico = false;
        Cursor producto_especifico = admin.producto_especifico_despensa(nombre);
        if (producto_especifico.moveToFirst()) {
            lista_producto_especifico = true;
        }
        return lista_producto_especifico;
    }
    void borrar_producto_no_encontrado_inventario(){
        admin.borrar_producto_no_encontrado_inventario();
    }
    void agregar_despensa_producto_no_encontrado(){
        admin.agregar_producto_no_encontrado_despensa(id_producto, nombre);
    }
    boolean nombre_producto(){
        boolean vacio = true;
        Cursor nombre_producto = admin.producto(id_producto);
        if (nombre_producto.moveToFirst()){
            nombre = nombre_producto.getString(1);
            vacio = false;
        }
        return vacio;
    }
    ArrayList<HashMap<String, String>> lista_producto_no_encontrado_despensa(){
        ArrayList<HashMap<String, String>> lista_producto = new ArrayList<>();
        Cursor producto_no_encontrado = admin.listado_productos_no_encontrados_despensa();
        if (producto_no_encontrado.moveToFirst()) {
            do {
                id_producto = producto_no_encontrado.getString(0); //id del producto
                nombre = producto_no_encontrado.getString(1);
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(TERCERA_COLUMNA, id_producto);
                lista_producto.add(temp);
            } while (producto_no_encontrado.moveToNext());
        }
        return lista_producto;
    }
}
