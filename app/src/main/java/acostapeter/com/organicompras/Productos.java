package acostapeter.com.organicompras;

import android.content.Context;
import android.database.Cursor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.TERCERA_COLUMNA;
import acostapeter.com.organicompras.data.DbCRUD;
@SuppressWarnings("all")
public class Productos {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String marca;
    private double neto;
    private String medida;
    private double precio;
    private int id_supermercado;
    private int id_producto;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String id) {
        this.codigo = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }
//productos existentes
    void precio_existente() {
        precio = admin.precio_existente(codigo, id_supermercado);
    }
    void agregar_producto(){
        admin.agregar_producto(nombre, descripcion, marca, neto, medida);
    }
    void maximo_producto(){
        Cursor producto = admin.maximo_producto();
        if (producto.moveToFirst()) {
            id_producto = producto.getInt(0);
        }
    }
    void obtener_id_producto(){
        Cursor datos_producto_existente = admin.datos_producto(codigo);
        id_producto = 0;
        if (datos_producto_existente.moveToFirst()) id_producto = datos_producto_existente.getInt(0);
    }

    void datos_producto_no_encontrado() {
        Cursor producto_no_encontrado = admin.compra_producto_no_encontrado(codigo);
        id_producto = 0; precio = 0.0;
        if (producto_no_encontrado.moveToFirst()) {
            id_producto = producto_no_encontrado.getInt(1);
            String precio_producto = producto_no_encontrado.getString(3);
            precio = Double.parseDouble(precio_producto);

        }

    }
    void agregar_compras_producto_no_encontrado() {
        admin.compras_agregar_prod_no_encontrado(codigo, id_supermercado, id_producto, precio);
    }
    void actualizar_producto_no_encontrado(){
        admin.editar_producto_no_encontrado(codigo, id_supermercado, id_producto, precio);
    }
    void actualizar_producto(){
        admin.editar_producto(id_producto, nombre, descripcion, marca,neto,medida);
    }
    ArrayList<HashMap<String, String>> cargar_producto_no_encontrado() {
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        Cursor producto_no_encontrado = admin.cargar_no_producto(id_supermercado);
        if (producto_no_encontrado.moveToFirst()) {
            do {
                descripcion = ""; marca= ""; medida = ""; neto =0.00;
                nombre = producto_no_encontrado.getString(0);
                descripcion = producto_no_encontrado.getString(1);
                marca = producto_no_encontrado.getString(2);
                neto = producto_no_encontrado.getDouble(3);
                medida = producto_no_encontrado.getString(4);
                precio = producto_no_encontrado.getDouble(5);
                String precio_producto = (df.format(precio)).replace(",", ".");
                id_producto = producto_no_encontrado.getInt(6);
                String id = Integer.toString(id_producto), producto_neto = Double.toString(neto);
                long producto_codigo = producto_no_encontrado.getLong(7);//codigo del producto
                codigo = Long.toString(producto_codigo);
                if (producto_neto.equals("0.0")) producto_neto = "";
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEGUNDA_COLUMNA, descripcion);
                temp.put(TERCERA_COLUMNA, marca);
                temp.put(CUARTA_COLUMNA, producto_neto);
                temp.put(QUINTA_COLUMNA, medida);
                temp.put(SEXTA_COLUMNA, precio_producto);
                temp.put(SEPTIMA_COLUMNA, id);
                temp.put(OCTAVA_COLUMNA, codigo);
                lista.add(temp);
            } while (producto_no_encontrado.moveToNext());
        }
        return lista;
    }
    ArrayList<HashMap<String, String>> producto_por_nombre(){
        ArrayList<HashMap<String, String>> lista_producto = new ArrayList<>();
        Cursor producto_especifico = admin.calcular_productos_despensa(nombre);
        if (producto_especifico.moveToFirst()) {
            do {
                id_producto  = producto_especifico.getInt(0);//id del producto
                String producto_id = String.valueOf(id_producto); //id del producto
                nombre = producto_especifico.getString(1);
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEPTIMA_COLUMNA, producto_id);
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
    int maximo_producto_no_encontrado_despensa(){
        int maximo;
        maximo = admin.maximo_producto_no_encontrado();
        return maximo;
    }
    void guardar_producto_no_encontrado_despensa(){
        admin.guardar_producto_no_encontrado_despensa();
    }
    boolean producto_no_encontrado_despensa(String dato){
        boolean lista_producto_no_encontrado = false;
        Cursor producto_especifico = admin.recargar_producto_no_encontrado(dato);
        if (producto_especifico.moveToFirst()) {
            lista_producto_no_encontrado = true;
        }
        return lista_producto_no_encontrado;
    }
    void borrar_producto_no_encontrado_inventario(){
        admin.borrar_producto_no_encontrado_inventario();
    }
    void agregar_despensa_producto_no_encontrado(String dato){
        admin.agregar_producto_no_encontrado_despensa(dato, id_producto);
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
                descripcion = ""; marca= ""; medida = ""; neto =0.00;
                String id = "", producto_id, producto_neto = "";
                //codigo = producto_no_encontrado.getString(0); //id del producto
                id_producto = producto_no_encontrado.getInt(1);
                producto_id = String.valueOf(id_producto);
                Cursor datos_producto = admin.producto(id_producto);
                if(datos_producto.moveToFirst()) {
                    nombre = datos_producto.getString(1);
                    descripcion = datos_producto.getString(2);
                    marca = datos_producto.getString(3);
                    neto = datos_producto.getDouble(4);
                    medida = datos_producto.getString(5);
                    producto_neto = Double.toString(neto);
                }
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEGUNDA_COLUMNA, descripcion);
                temp.put(TERCERA_COLUMNA, marca);
                if (producto_neto.equals("0.0")) producto_neto = "";
                temp.put(CUARTA_COLUMNA, producto_neto);
                temp.put(QUINTA_COLUMNA, medida);
                temp.put(SEPTIMA_COLUMNA, producto_id);
                lista_producto.add(temp);
            } while (producto_no_encontrado.moveToNext());
        }
        return lista_producto;
    }
}
