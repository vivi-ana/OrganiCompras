package acostapeter.com.organicompras.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import acostapeter.com.organicompras.MiDespensaActivityObjeto;


public class DbCRUD extends DbHelper {

    private SQLiteDatabase db;

    public DbCRUD(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, factory);
        DbHelper admin = new DbHelper(context, null);
        db = admin.getReadableDatabase();
    }
//supermercado
    public int eleccion_supermercado(String eleccion) {
        int idsuper = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaSupermercados.TABLA_SUPERMERCADOS
                + " WHERE " + DbTablas.TablaSupermercados.CAMPO_NOMBRE + " = '" + eleccion + "'";
        Cursor supermercado_elegido = db.rawQuery(query, null);
        if (supermercado_elegido != null) {
            try {
                if (supermercado_elegido.moveToFirst()) {
                    idsuper = supermercado_elegido.getInt(0);
                }
            } finally {
                supermercado_elegido.close();
            }
        }
        return idsuper;
    }
    //producto
    public Cursor maximo_producto(){
        final String query = "SELECT * FROM " + DbTablas.TablaProductos.TABLA_PRODUCTOS
                + " WHERE " + DbTablas.TablaProductos.CAMPO_ID_PROD + " =(SELECT MAX(" + DbTablas.TablaProductos.CAMPO_ID_PROD + " )"  + " FROM " + DbTablas.TablaProductos.TABLA_PRODUCTOS + ")";
        Cursor fila = db.rawQuery(query, null);
        if (fila != null){
            fila.moveToFirst();
        }
        return fila;
    }
    public Cursor producto(int id_producto){
        final String query = "SELECT * FROM " + DbTablas.TablaProductos.TABLA_PRODUCTOS + " WHERE " + DbTablas.TablaProductos.CAMPO_ID_PROD +" = " + id_producto;
        Cursor lista_producto = db.rawQuery(query,null);
        if (lista_producto != null){
            lista_producto.moveToFirst();
        }
        return lista_producto;
    }
    public String por_nombre(int id_producto){
        String nombre = "";
        final String query = "SELECT " + DbTablas.TablaProductos.CAMPO_NOMBRE + " FROM " + DbTablas.TablaProductos.TABLA_PRODUCTOS + " WHERE " + DbTablas.TablaProductos.CAMPO_ID_PROD + " = " + id_producto;
        Cursor lista_producto = db.rawQuery(query,null);
        if (lista_producto != null){
            try {
                if(lista_producto.moveToFirst()){
                    nombre = lista_producto.getString(0);
                }
            }finally {
                lista_producto.close();
            }
        }
        return nombre;
    }
    public int contar_productos(int id_compras, int id_producto){
        int total = 0;
        final String query = "SELECT count(" + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD +
                ") FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLES + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto +
                " and " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras;
        Cursor contar = db.rawQuery(query, null);
        if (contar != null){
            try {
                if(contar.moveToFirst()){
                    total = contar.getInt(0);
                }
            }finally {
                contar.close();
            }
        }
        return total;
    }
    public void agregar_producto(String nombre, String descrip, String marca, double neto, String medida){
        ContentValues registros = new ContentValues();
        registros.put(DbTablas.TablaProductos.CAMPO_NOMBRE, nombre);
        registros.put(DbTablas.TablaProductos.CAMPO_DESCP, descrip);
        registros.put(DbTablas.TablaProductos.CAMPO_MARCA, marca);
        registros.put(DbTablas.TablaProductos.CAMPO_CONT_NETO, neto);
        registros.put(DbTablas.TablaProductos.CAMPO_MEDIDA, medida);
        db.insert(DbTablas.TablaProductos.TABLA_PRODUCTOS, null, registros);
    }
    //productos existentes
    public double precio_existente(String codigo, int id_super){
        double precio_por_supermercado = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaProdXSuper.TABLA_PRODXSUPER + " WHERE "
                + DbTablas.TablaProdXSuper.CAMPO_FK_ID_EXISTENTE + " = " + codigo + " AND " + DbTablas.TablaProdXSuper.CAMPO_FK_ID_SUPER + " = " + id_super;
        Cursor precio_producto_existente = db.rawQuery(query,null);
        if (precio_producto_existente != null){
            try {
                if(precio_producto_existente.moveToFirst()){
                    precio_por_supermercado = precio_producto_existente.getDouble(2);
                }
            }finally {
                precio_producto_existente.close();
            }
        }
        return precio_por_supermercado;
    }
    public Cursor datos_producto(String codigo){
        final String query = "SELECT * FROM " + DbTablas.TablaProductos.TABLA_PRODUCTOS + " JOIN " + DbTablas.TablaExistentes.TABLA_EXISTENTES + " ON " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_ID_PROD + " = " + DbTablas.TablaExistentes.TABLA_EXISTENTES + "." + DbTablas.TablaExistentes.CAMPO_FK_ID_PROD
            + " WHERE " +  DbTablas.TablaExistentes.TABLA_EXISTENTES + "." + DbTablas.TablaExistentes.CAMPO_ID_EXISTENTE + " = " + codigo;
        Cursor producto_existente_datos = db.rawQuery(query,null);
        if (producto_existente_datos != null){
            producto_existente_datos.moveToFirst();
        }
        return producto_existente_datos;
    }
    public double por_supermercado(String codigo, int id_super){
        double precio_por_supermercado = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaProdXSuper.TABLA_PRODXSUPER + " WHERE "
                + DbTablas.TablaProdXSuper.CAMPO_FK_ID_EXISTENTE + " = " + codigo + " AND " + DbTablas.TablaProdXSuper.CAMPO_FK_ID_SUPER + " = " + id_super;
        Cursor precio_producto = db.rawQuery(query,null);
        if (precio_producto != null){
            try {
                if(precio_producto.moveToFirst()){
                    precio_por_supermercado = precio_producto.getDouble(2);
                }
            }finally {
                precio_producto.close();
            }
        }
        return precio_por_supermercado;
    }
    public long getCodigo(int id_producto){
        long codigo = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaExistentes.TABLA_EXISTENTES + " WHERE "
                + DbTablas.TablaExistentes.CAMPO_FK_ID_PROD + " = " + id_producto;
        Cursor precio_producto = db.rawQuery(query,null);
        if (precio_producto != null){
            try {
                if(precio_producto.moveToFirst()){
                    codigo = precio_producto.getLong(0);
                }
            }finally {
                precio_producto.close();
            }
        }
        return codigo;
    }
    //producto no encontrado
    public void editar_producto(int id_producto, String nombre, String descripcion, String marca, Double neto, String medida){
        ContentValues edicion_producto = new ContentValues();
        edicion_producto.put(DbTablas.TablaProductos.CAMPO_ID_PROD, id_producto);
        edicion_producto.put(DbTablas.TablaProductos.CAMPO_NOMBRE, nombre);
        edicion_producto.put(DbTablas.TablaProductos.CAMPO_DESCP, descripcion);
        edicion_producto.put(DbTablas.TablaProductos.CAMPO_MARCA, marca);
        edicion_producto.put(DbTablas.TablaProductos.CAMPO_CONT_NETO, neto);
        edicion_producto.put(DbTablas.TablaProductos.CAMPO_MEDIDA, medida);
        db.update(DbTablas.TablaProductos.TABLA_PRODUCTOS, edicion_producto, DbTablas.TablaProductos.CAMPO_ID_PROD + " = " + id_producto, null);
    }
    public Cursor producto_comprado_no_encontrado(int id_producto, int id_super){
        final String query = "SELECT * FROM " + DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP
                + " WHERE " + DbTablas.TablaProductosSuper.CAMPO_FK_ID_PROD + " = " + id_producto
                + " AND " + DbTablas.TablaProductosSuper.CAMPO_FK_ID_SUPER + " = " + id_super;
        Cursor lista_no_encontrado = db.rawQuery(query,null);
        if (lista_no_encontrado != null){
            lista_no_encontrado.moveToFirst();
        }
        return lista_no_encontrado;
    }
    public Cursor compra_producto_no_encontrado(String codigo, int id_super){
        final String query = "SELECT * FROM " + DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP
                + " WHERE " +  DbTablas.TablaProductosSuper.CAMPO_ID_PROD_SUPER + " = " + codigo + " AND " + DbTablas.TablaProductosSuper.CAMPO_FK_ID_SUPER + " = " + id_super;
        Cursor producto_no_encontrado = db.rawQuery(query,null);
        if (producto_no_encontrado != null){
            producto_no_encontrado.moveToFirst();
        }
        return producto_no_encontrado;
    }
    public void compras_agregar_prod_no_encontrado(String codigo, int id_supermercado, int id_producto, double precio){
        ContentValues compras_prod_no_encontrado = new ContentValues();
        compras_prod_no_encontrado.put(DbTablas.TablaProductosSuper.CAMPO_ID_PROD_SUPER, codigo);
        compras_prod_no_encontrado.put(DbTablas.TablaProductosSuper.CAMPO_FK_ID_SUPER, id_supermercado);
        compras_prod_no_encontrado.put(DbTablas.TablaProductosSuper.CAMPO_FK_ID_PROD, id_producto);
        compras_prod_no_encontrado.put(DbTablas.TablaProductosSuper.CAMPO_PRECIO, precio);
        db.insert(DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP, null, compras_prod_no_encontrado);
    }
    public void editar_producto_no_encontrado (String codigo, int id_supermercado, int id_producto, double precio){
        ContentValues edicion_producto = new ContentValues();
        edicion_producto.put(DbTablas.TablaProductosSuper.CAMPO_ID_PROD_SUPER, codigo);
        edicion_producto.put(DbTablas.TablaProductosSuper.CAMPO_FK_ID_SUPER, id_supermercado);
        edicion_producto.put(DbTablas.TablaProductosSuper.CAMPO_FK_ID_PROD, id_producto);
        edicion_producto.put(DbTablas.TablaProductosSuper.CAMPO_PRECIO, precio);
        db.update(DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP, edicion_producto, DbTablas.TablaProductosSuper.CAMPO_FK_ID_SUPER + " = " + id_supermercado + " AND " + DbTablas.TablaProductosSuper.CAMPO_ID_PROD_SUPER + " = " + codigo, null);
    }
    public Cursor cargar_no_producto(int id_supermercado){
        final String query = "SELECT " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_NOMBRE + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_DESCP  + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_MARCA  + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_CONT_NETO  + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_MEDIDA
                + " , " + DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP + "." + DbTablas.TablaProductosSuper.CAMPO_PRECIO  + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_ID_PROD  + " , " + DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP + "." +DbTablas.TablaProductosSuper.CAMPO_ID_PROD_SUPER + " FROM "
                + DbTablas.TablaProductos.TABLA_PRODUCTOS + " JOIN " + DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP + " ON " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_ID_PROD + " = " + DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP + "." + DbTablas.TablaProductosSuper.CAMPO_FK_ID_PROD
                + " WHERE " + DbTablas.TablaProductosSuper.TABLA_PROD_NO_EN_COMP + "." + DbTablas.TablaProductosSuper.CAMPO_FK_ID_SUPER + " = " + id_supermercado;
        Cursor producto_no_encontrado = db.rawQuery(query,null);
        if (producto_no_encontrado != null){
            producto_no_encontrado.moveToFirst();
        }
        return producto_no_encontrado;
    }
    //no encontrado despensa
    public Cursor recargar_producto_no_encontrado(String id_producto){
        final String query = "SELECT * FROM " + DbTablas.TablaProductosCasa.TABLA_PROD_CASA + " WHERE " + DbTablas.TablaProductosCasa.CAMPO_ID_CASA + " = " + id_producto;
        Cursor producto_no_encontrado = db.rawQuery(query,null);
        if (producto_no_encontrado != null){
            producto_no_encontrado.moveToFirst();
        }
        return producto_no_encontrado;
    }
    public void agregar_producto_no_encontrado_despensa(String dato, int id_producto){
        ContentValues registros = new ContentValues();
        registros.put(DbTablas.TablaProductosCasa.CAMPO_ID_CASA, dato);
        registros.put(DbTablas.TablaProductosCasa.CAMPO_FK_ID_PROD, id_producto);
        registros.put(DbTablas.TablaProductosCasa.CAMPO_GUARDAR, "S");
        db.insert(DbTablas.TablaProductosCasa.TABLA_PROD_CASA, null, registros);
    }
    //compras
    public void agregar_compra(int max, int id_super, String dia){
        ContentValues registros = new ContentValues();
        registros.put(DbTablas.TablaCompras.CAMPO_FK_ID_SUPER, id_super);
        registros.put(DbTablas.TablaCompras.CAMPO_FECHA, dia);
        registros.put(DbTablas.TablaCompras.CAMPO_MAX, max); //max puede ser 0 por default o el monto que ingreso
        registros.put(DbTablas.TablaCompras.CAMPO_CANT_PROD, 0);
        registros.put(DbTablas.TablaCompras.CAMPO_TOTAL, 0);
        registros.put(DbTablas.TablaCompras.CAMPO_TOT_UNITARIO, 0);
        db.insert(DbTablas.TablaCompras.TABLA_COMPRAS, null, registros);
    }
    public void agregar_detalle_compra(int id_compras, int id_productos, double monto){
        ContentValues compras = new ContentValues();
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA, id_compras);
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD, id_productos);
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_CANTIDAD, 1); //por defecto compra solo 1 producto al comenzar la compra
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_MONTO, monto);
        db.insert(DbTablas.TablaDetallesCompras.TABLA_DETALLES, null, compras);
    }
    public void actualizar_monto(double monto, int id_compra, int id_producto){
        ContentValues nuevo_monto = new ContentValues();
        nuevo_monto.put(DbTablas.TablaDetallesCompras.CAMPO_MONTO, monto);
        db.update(DbTablas.TablaDetallesCompras.TABLA_DETALLES, nuevo_monto, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto, null);
    }
    public Cursor maximo_compra(){
        final String query = "SELECT * FROM " + DbTablas.TablaCompras.TABLA_COMPRAS
                + " WHERE " + DbTablas.TablaCompras.CAMPO_ID_COMPRA + " =(SELECT MAX(" + DbTablas.TablaCompras.CAMPO_ID_COMPRA + " )"  + " FROM " + DbTablas.TablaCompras.TABLA_COMPRAS + ")";
        Cursor fila = db.rawQuery(query, null);
        if (fila != null){
            fila.moveToFirst();
        }
        return fila;
    }
    public int maximo_detalle_compra(){
        int id_detalle = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLES
                + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " =(SELECT MAX(" + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " )"  + " FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLES + ")";
        Cursor lista_detalle = db.rawQuery(query, null);
        if (lista_detalle != null){
            try {
                if(lista_detalle.moveToFirst()){
                    id_detalle = lista_detalle.getInt(1);
                }
            }finally {
                lista_detalle.close();
            }
        }
        return id_detalle;
    }
    public Cursor detalle_compra(int id_compras){
        final String query = "SELECT * FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLES + " where " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras;
        Cursor compras = db.rawQuery(query,null);
        if (compras != null){
            compras.moveToFirst();
        }
        return compras;
    }
    public Cursor detalle_compra_editada(int id_compras, int id_producto){
        final String query = "SELECT * FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLES + " where " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto;
        Cursor compras = db.rawQuery(query,null);
        if (compras != null){
            compras.moveToFirst();
        }
        return compras;
    }
    public Cursor compra_datos(int id_compra){
        final String query = "SELECT * FROM "
                + DbTablas.TablaCompras.TABLA_COMPRAS + " WHERE " + DbTablas.TablaCompras.CAMPO_ID_COMPRA + " = " + id_compra;
        Cursor datos_compras = db.rawQuery(query,null);
        if (datos_compras != null){
            datos_compras.moveToFirst();
        }
        return datos_compras;
    }
    public int contar_productos_detalle(int id_compra){
        int total_producto = 0;
        final String query = "SELECT COUNT(" + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + ") FROM "
                + DbTablas.TablaDetallesCompras.TABLA_DETALLES + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
        Cursor contar_producto = db.rawQuery(query,null);
        if (contar_producto != null){
            try {
                if(contar_producto.moveToFirst()){
                    total_producto = contar_producto.getInt(0);
                }
            }finally {
                contar_producto.close();
            }
        }
        return total_producto;
    }
//despensa
    public void borrar_item_despensa(int item){
        db.delete(DbTablas.TablaInventarios.TABLA_INVENTARIOS, DbTablas.TablaInventarios.CAMPO_FK_ID_PROD + " = '" + item +"'",null);
    }
    public void insertar_inventario(int id_producto, String detalle){
        ContentValues registros = new ContentValues();
        registros.put(DbTablas.TablaInventarios.CAMPO_FK_ID_PROD, id_producto);
        registros.put(DbTablas.TablaInventarios.CAMPO_CANT, 1);
        registros.put(DbTablas.TablaInventarios.CAMPO_GUARDAR, "N");
        registros.put(DbTablas.TablaInventarios.CAMPO_DETALLE, detalle);
        db.insert(DbTablas.TablaInventarios.TABLA_INVENTARIOS, null, registros);
    }
    public int maximo_producto_no_encontrado(){
        int id_producto_no_encontrado = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaProductosCasa.TABLA_PROD_CASA
                + " WHERE " + DbTablas.TablaProductosCasa.CAMPO_ID_CASA + " =(SELECT MAX(" + DbTablas.TablaProductosCasa.CAMPO_ID_CASA + ") FROM " + DbTablas.TablaProductosCasa.TABLA_PROD_CASA + ")";
        Cursor lista_productos = db.rawQuery(query, null);
        if (lista_productos != null){
            try {
                if(lista_productos.moveToFirst()){
                    id_producto_no_encontrado = lista_productos.getInt(0);
                }
            }finally {
                lista_productos.close();
            }
        }
        return id_producto_no_encontrado;
    }
    public void guardar_producto_no_encontrado_despensa(){
        ContentValues guardar = new ContentValues();
        guardar.put(DbTablas.TablaProductosCasa.CAMPO_GUARDAR, "S");
        db.update(DbTablas.TablaProductosCasa.TABLA_PROD_CASA, guardar, null, null);
    }
    public Cursor recargar_despensa(){
        final String query = "SELECT * FROM " + DbTablas.TablaInventarios.TABLA_INVENTARIOS;
        Cursor inventario = db.rawQuery(query,null);
        if (inventario != null){
            inventario.moveToFirst();
        }
        return inventario;
    }
    public Cursor calcular_despensa(String codigo, int id_supermercado){
        final String query = "SELECT * FROM " + DbTablas.TablaProdXSuper.TABLA_PRODXSUPER + " WHERE " + DbTablas.TablaProdXSuper.CAMPO_FK_ID_EXISTENTE + " = " + codigo  + " AND " + DbTablas.TablaProdXSuper.CAMPO_FK_ID_SUPER + " = " + id_supermercado;
        Cursor lista_calculo = db.rawQuery(query,null);
        if (lista_calculo != null){
            lista_calculo.moveToFirst();
        }
        return lista_calculo;
    }
    public int contar_productos_despensa(String nombre){
        int total_producto = 0;
        final String query = "SELECT COUNT(" + DbTablas.TablaProductos.CAMPO_ID_PROD + ") FROM "
                + DbTablas.TablaProductos.TABLA_PRODUCTOS + " WHERE " + DbTablas.TablaProductos.CAMPO_NOMBRE + " = '" + nombre +"'";
        Cursor contar_producto = db.rawQuery(query,null);
        if (contar_producto != null){
            try {
                if(contar_producto.moveToFirst()){
                    total_producto = contar_producto.getInt(0);
                }
            }finally {
                contar_producto.close();
            }
        }
        return total_producto;
    }
    public Cursor calcular_productos_despensa(String nombre){
        final String query = "SELECT * FROM " + DbTablas.TablaProductos.TABLA_PRODUCTOS + " WHERE " + DbTablas.TablaProductos.CAMPO_NOMBRE + " = '" + nombre +"'" ;
        Cursor lista_calculo = db.rawQuery(query,null);
        if (lista_calculo != null){
            lista_calculo.moveToFirst();
        }
        return lista_calculo;
    }
    public void actualizar_inventario(int cantidad, int id_producto){
        ContentValues guardar = new ContentValues();
        guardar.put(DbTablas.TablaInventarios.CAMPO_CANT, cantidad);
        db.update(DbTablas.TablaInventarios.TABLA_INVENTARIOS, guardar, DbTablas.TablaInventarios.CAMPO_FK_ID_PROD + " = '" + id_producto +"'", null);
    }
    public void actualizar_cantidad(int cantidad, double monto, int id_compra, String id_producto ){
        ContentValues nueva_cantidad = new ContentValues();
        nueva_cantidad.put(DbTablas.TablaDetallesCompras.CAMPO_CANTIDAD, cantidad);
        nueva_cantidad.put(DbTablas.TablaDetallesCompras.CAMPO_MONTO, monto);
        db.update(DbTablas.TablaDetallesCompras.TABLA_DETALLES, nueva_cantidad, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto, null);
    }
    public String monto(int id_compra){
        String monto_anterior = "";
        final String query = "SELECT SUM(" + DbTablas.TablaDetallesCompras.CAMPO_MONTO + " ) FROM "
                + DbTablas.TablaDetallesCompras.TABLA_DETALLES + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
        Cursor verificar_monto = db.rawQuery(query,null);
        if (verificar_monto != null){
            try {
                if(verificar_monto.moveToFirst()){
                    monto_anterior = verificar_monto.getString(0);
                }
            }finally {
                verificar_monto.close();
            }
        }
        return monto_anterior;
    }
    public String cantidad_productos_comprados(int id_compra){
        String cantidad = "";
        final String query = "SELECT SUM(" + DbTablas.TablaDetallesCompras.CAMPO_CANTIDAD + " ) FROM "
                + DbTablas.TablaDetallesCompras.TABLA_DETALLES + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
        Cursor verificar_cantidad = db.rawQuery(query,null);
        if (verificar_cantidad != null){
            try {
                if(verificar_cantidad.moveToFirst()){
                   cantidad = verificar_cantidad.getString(0);
                }
            }finally {
                verificar_cantidad.close();
            }
        }
        return cantidad;
    }
    public int verificar_maximo(int id_compra){
        int maximo = 0;
        final String query = "SELECT * FROM "
                + DbTablas.TablaCompras.TABLA_COMPRAS + " WHERE " + DbTablas.TablaCompras.CAMPO_ID_COMPRA + " = " + id_compra;
        Cursor maximo_compra_ingresado = db.rawQuery(query,null);
        if (maximo_compra_ingresado != null){
            try {
                if(maximo_compra_ingresado.moveToFirst()){
                    maximo = maximo_compra_ingresado.getInt(3);
                }
            }finally {
                maximo_compra_ingresado.close();
            }
        }
        return maximo;
    }
    public void borrar_compras(int id_compras){
        db.delete(DbTablas.TablaCompras.TABLA_COMPRAS, DbTablas.TablaCompras.CAMPO_ID_COMPRA + " = " + id_compras,null);
    }
    public void actualizar_compra (int id_compra, int cantidad, double total, double total_unitario){
        ContentValues compras = new ContentValues();
        compras.put(DbTablas.TablaCompras.CAMPO_CANT_PROD, cantidad);
        compras.put(DbTablas.TablaCompras.CAMPO_TOTAL, total);
        compras.put(DbTablas.TablaCompras.CAMPO_TOT_UNITARIO, total_unitario);
        db.update(DbTablas.TablaCompras.TABLA_COMPRAS, compras, DbTablas.TablaCompras.CAMPO_ID_COMPRA + " = " + id_compra, null);
    }
    public void borrar_detalle_compras(int id_compras){
        db.delete(DbTablas.TablaDetallesCompras.TABLA_DETALLES, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras,null);
    }
    public void cantidad_producto_editada (int cantidad, int id_compra, String codigo){
        ContentValues edicion_cantidad = new ContentValues();
        edicion_cantidad.put(DbTablas.TablaDetallesCompras.CAMPO_CANTIDAD, cantidad);
        db.update(DbTablas.TablaCompras.TABLA_COMPRAS, edicion_cantidad, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + codigo, null);
    }
    public void reset_detalle(int id_detalle, int id_compra, String id_producto, int cantidad, double monto){
        ContentValues nuevo_detalle = new ContentValues();
        nuevo_detalle.put(DbTablas.TablaDetallesCompras.CAMPO_ID_DETALLE, id_detalle);
        nuevo_detalle.put(DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA, id_compra);
        nuevo_detalle.put(DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD, id_producto);
        nuevo_detalle.put(DbTablas.TablaDetallesCompras.CAMPO_CANTIDAD, cantidad);
        nuevo_detalle.put(DbTablas.TablaDetallesCompras.CAMPO_MONTO, monto);
        db.insert(DbTablas.TablaDetallesCompras.TABLA_DETALLES, null, nuevo_detalle);
    }
    public void reset_compras(int id_compra, int id_super, String dia, int max, int cantidad, double total, double total_unitario){
        ContentValues registros = new ContentValues();
        registros.put(DbTablas.TablaCompras.CAMPO_ID_COMPRA, id_compra);
        registros.put(DbTablas.TablaCompras.CAMPO_FK_ID_SUPER, id_super);
        registros.put(DbTablas.TablaCompras.CAMPO_FECHA, dia);
        registros.put(DbTablas.TablaCompras.CAMPO_MAX, max); //max puede ser 0 por default o el monto que ingreso
        registros.put(DbTablas.TablaCompras.CAMPO_CANT_PROD, cantidad);
        registros.put(DbTablas.TablaCompras.CAMPO_TOTAL, total);
        registros.put(DbTablas.TablaCompras.CAMPO_TOT_UNITARIO, total_unitario);
        db.insert(DbTablas.TablaCompras.TABLA_COMPRAS, null, registros);
    }
    public void borrar_item(int id_compras, String id_producto){
        db.delete(DbTablas.TablaDetallesCompras.TABLA_DETALLES, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto,null);
    }
    public double calcular_total(int id_compra){
        double total_compras = 0;
        final String query = "SELECT SUM(" + DbTablas.TablaDetallesCompras.CAMPO_MONTO + ") FROM "
                + DbTablas.TablaDetallesCompras.TABLA_DETALLES + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
        Cursor contar_producto = db.rawQuery(query,null);
        if (contar_producto != null){
            try {
                if(contar_producto.moveToFirst()){
                    total_compras = contar_producto.getDouble(0);
                }
            }finally {
                contar_producto.close();
            }
        }
        return total_compras;
    }
    public List<MiDespensaActivityObjeto> editText_busqueda(String itemBuscado) {
        List<MiDespensaActivityObjeto> lista_productos = new ArrayList<>();
        final String sql = "SELECT DISTINCT " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_NOMBRE + " FROM " + DbTablas.TablaProductos.TABLA_PRODUCTOS
                + " JOIN " + DbTablas.TablaExistentes.TABLA_EXISTENTES + " ON " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_ID_PROD + "=" + DbTablas.TablaExistentes.TABLA_EXISTENTES + "." + DbTablas.TablaExistentes.CAMPO_FK_ID_PROD + " WHERE " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_NOMBRE + " LIKE '" +itemBuscado+ "%'" + " ORDER BY " + DbTablas.TablaProductos.CAMPO_NOMBRE + " DESC LIMIT 0,5";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null){
            try {
                if(cursor.moveToFirst()){
                    do {
                        String objectName = cursor.getString(cursor.getColumnIndex(DbTablas.TablaProductos.CAMPO_NOMBRE));
                        MiDespensaActivityObjeto objecto = new MiDespensaActivityObjeto(objectName);
                        lista_productos.add(objecto);
                    } while (cursor.moveToNext());
                }
            }finally {
                cursor.close();
            }
        }
        return lista_productos;
    }
    public void guardar_inventario(){
        ContentValues guardar = new ContentValues();
        guardar.put(DbTablas.TablaInventarios.CAMPO_GUARDAR, "S");
        db.update(DbTablas.TablaInventarios.TABLA_INVENTARIOS, guardar, null, null);
    }
    public int cantidad_inventario (){
        int cantidad = 0;
        final String query = "SELECT SUM(" + DbTablas.TablaInventarios.CAMPO_CANT
                + ") FROM " + DbTablas.TablaInventarios.TABLA_INVENTARIOS;
        Cursor cantidad_inventario = db.rawQuery(query, null);
        if (cantidad_inventario != null){
            try {
                if(cantidad_inventario.moveToFirst()){
                    cantidad = cantidad_inventario.getInt(0);
                }
            }finally {
                cantidad_inventario.close();
            }
        }
        return cantidad;
    }
    public void borrar_inventario(){
        db.delete(DbTablas.TablaInventarios.TABLA_INVENTARIOS, DbTablas.TablaInventarios.CAMPO_GUARDAR + " =  'N'",null);
    }
    public void borrar_producto_no_encontrado_inventario(){
        db.delete(DbTablas.TablaProductosCasa.TABLA_PROD_CASA, DbTablas.TablaProductosCasa.CAMPO_GUARDAR + " =  'N'",null);
    }
    public Cursor listado_productos_no_encontrados_despensa(){
        final String query = "SELECT * FROM " + DbTablas.TablaProductosCasa.TABLA_PROD_CASA;
        Cursor lista_producto = db.rawQuery(query,null);
        if (lista_producto != null){
            lista_producto.moveToFirst();
        }
        return lista_producto;
    }
    public Cursor cargar_historial(int year, String mes){
        final String query = "SELECT * from " + DbTablas.TablaCompras.TABLA_COMPRAS + " WHERE strftime('%Y'," + DbTablas.TablaCompras.CAMPO_FECHA + ") =('" + year +"') AND strftime('%m'," + DbTablas.TablaCompras.CAMPO_FECHA  + ") =('" + mes +"')";
        Cursor lista_historial = db.rawQuery(query,null);
        if (lista_historial != null){
            lista_historial.moveToFirst();
        }
        return lista_historial;
    }
    public Cursor nombre_supermercado(int supermercado){
        final String query = "SELECT * from " + DbTablas.TablaSupermercados.TABLA_SUPERMERCADOS + " WHERE " + DbTablas.TablaSupermercados.CAMPO_ID_SUPERMERCADO + " = " + supermercado;
        Cursor supermercado_elegido = db.rawQuery(query,null);
        if (supermercado_elegido != null){
            supermercado_elegido.moveToFirst();
        }
        return supermercado_elegido;
    }
    public Cursor supermercado(){
        final String query = "SELECT * from " + DbTablas.TablaSupermercados.TABLA_SUPERMERCADOS;
        Cursor supermercado = db.rawQuery(query,null);
        if (supermercado != null){
            supermercado.moveToFirst();
        }
        return supermercado;
    }
    public Cursor estadistica(Integer id_supermercado, Integer year, String mes, String dia){
        final String query = "SELECT * from " + DbTablas.TablaCompras.TABLA_COMPRAS + " WHERE " + DbTablas.TablaCompras.CAMPO_FK_ID_SUPER + " = " + id_supermercado + " AND strftime('%Y'," + DbTablas.TablaCompras.CAMPO_FECHA + ") =('" + year +"') AND strftime('%m'," + DbTablas.TablaCompras.CAMPO_FECHA  + ") =('" + mes +"') AND strftime('%d'," + DbTablas.TablaCompras.CAMPO_FECHA  + ") =('" + dia +"')";
        Cursor lista_estadistica = db.rawQuery(query,null);
        if (lista_estadistica != null) {
            lista_estadistica.moveToFirst();
        }
        return lista_estadistica;
    }
    public Cursor estadistica_mensual(Integer year, String mes, String dia){
        final String query = "SELECT * from " + DbTablas.TablaCompras.TABLA_COMPRAS + " WHERE strftime('%Y'," + DbTablas.TablaCompras.CAMPO_FECHA + ") =('" + year +"') AND strftime('%m'," + DbTablas.TablaCompras.CAMPO_FECHA  + ") =('" + mes +"') AND strftime('%d'," + DbTablas.TablaCompras.CAMPO_FECHA  + ") =('" + dia +"')";
        Cursor lista_estadistica = db.rawQuery(query,null);
        if (lista_estadistica != null) {
            lista_estadistica.moveToFirst();
        }
        return lista_estadistica;
    }
    public Cursor producto_lista(int id){
        final String query = "SELECT * FROM " + DbTablas.TablaProductosCasa.TABLA_PROD_CASA + " WHERE " + DbTablas.TablaProductosCasa.CAMPO_FK_ID_PROD + " = " + id;
        Cursor lista_producto = db.rawQuery(query,null);
        if (lista_producto != null){
            lista_producto.moveToFirst();
        }
        return lista_producto;
    }
    //asi se pone los string?'" + codigo +"'
}
