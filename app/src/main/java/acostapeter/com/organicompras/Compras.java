package acostapeter.com.organicompras;

import android.content.Context;


import acostapeter.com.organicompras.data.DbCRUD;
@SuppressWarnings("all")
public class Compras {
private int id;
private int supermercado;
private String fecha;
private int max;
private int cantidad;
private double total;
private double total_unitario;
private DbCRUD admin;

    public Compras(int id, int supermercado, String fecha, int max, int cantidad, double total, double total_unitario) {
        this.id = id;
        this.supermercado = supermercado;
        this.fecha = fecha;
        this.max = max;
        this.cantidad = cantidad;
        this.total = total;
        this.total_unitario = total_unitario;
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

    public int getSupermercado() {
        return supermercado;
    }

    void setSupermercado(int supermercado) {
        this.supermercado = supermercado;
    }

    public String getFecha() {
        return fecha;
    }

    void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getMax() {
        return max;
    }

    void setMax(int max) {
        this.max = max;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal_unitario() {
        return total_unitario;
    }

    public void setTotal_unitario(double total_unitario) {
        this.total_unitario = total_unitario;
    }
    void agregar_compra(){
        admin.agregar_compra(max, supermercado, fecha);
    }
}
