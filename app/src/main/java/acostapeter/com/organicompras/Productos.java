package acostapeter.com.organicompras;

import android.content.Context;
import android.database.Cursor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
}
