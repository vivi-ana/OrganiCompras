package acostapeter.com.organicompras;

import android.content.Context;
import android.database.Cursor;

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
}
