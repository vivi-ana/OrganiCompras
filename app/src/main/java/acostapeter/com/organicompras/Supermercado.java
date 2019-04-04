package acostapeter.com.organicompras;


import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

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
    void supermercado_compra_editada(int id_compras){
        Cursor datos_compras = admin.compra_datos(id_compras);
        if (datos_compras.moveToFirst()) {
            id = datos_compras.getInt(1);
        }
    }
    ArrayList<HashMap<String, String>> lista_supermercado(){
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        Cursor supermercado_lista = admin.supermercado();
        if (supermercado_lista.moveToFirst()) {
            do {
                HashMap<String, String> temp = new HashMap<String, String>();
                String id_supermercado= supermercado_lista.getString(0);
                nombre = supermercado_lista.getString(1);
                temp.put("nombre", nombre);
                temp.put("id", id_supermercado );
                lista.add(temp);
            }while (supermercado_lista.moveToNext());
        }
        return lista;
    }

}
