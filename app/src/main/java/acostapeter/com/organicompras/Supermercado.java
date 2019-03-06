package acostapeter.com.organicompras;


import android.content.Context;

import acostapeter.com.organicompras.data.DbCRUD;
@SuppressWarnings("all")
public class Supermercado {
    private int id;
    private String nombre;
    private DbCRUD admin;
    Supermercado(Context context) {
        this.admin = new DbCRUD(context, null);
    }
    Supermercado(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    void setNombre(String nombre) {
        this.nombre = nombre;
    }

    void eleccion_supermercado(){
        //se consulta en la BD para obtener el id segun el nombre.
        id = admin.eleccion_supermercado(nombre);
    }
}
