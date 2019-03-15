package acostapeter.com.organicompras.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DbCRUD extends DbHelper {

    private SQLiteDatabase db;

    public DbCRUD(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, factory);
        DbHelper admin = new DbHelper(context, null);
        db = admin.getReadableDatabase();
    }

    public int eleccion_supermercado(String eleccion) {
        int idsuper = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaSupermercados.TABLA_SUPER
                + " WHERE " + DbTablas.TablaSupermercados.CAMPO_DESCRIP + " = '" + eleccion + "'";
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
        final String query = "SELECT * FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLE
                + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " =(SELECT MAX(" + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " )"  + " FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLE + ")";
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
    public double por_supermercado(String id_producto, int id_super){
        double precio_por_supermercado = 0;
        final String query = "SELECT * FROM " + DbTablas.TablaProdXSuper.TABLA_PRODXSUPER + " WHERE "
                + DbTablas.TablaProdXSuper.CAMPO_FK_ID_PROD + " = " + id_producto + " AND " + DbTablas.TablaProdXSuper.CAMPO_FK_ID_SUPER + " = " + id_super;
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
    public int contar_productos(int id_compras, String scanContent){
        int total = 0;
        final String query = "SELECT count(" + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD +
                ") FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLE + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + scanContent +
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
    public Cursor compra_producto_no_encontrado(String id_producto){
        final String query = "SELECT " + DbTablas.TablaProdNoEncoCompras.CAMPO_NOMBRE + "," + DbTablas.TablaProdNoEncoCompras.CAMPO_ID_NO_EN + " FROM "
                + DbTablas.TablaProdNoEncoCompras.TABLA_PROD_NO_EN_COMP +  " WHERE " +  DbTablas.TablaProdNoEncoCompras.CAMPO_ID_NO_EN + " = " + id_producto;
        Cursor producto_no_encontrado = db.rawQuery(query,null);
        if (producto_no_encontrado != null){
            producto_no_encontrado.moveToFirst();
        }
        return producto_no_encontrado;
    }
    public void agregar_detalle_compra(int id_compras, String id_productos, double monto){
        ContentValues compras = new ContentValues();
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA, id_compras);
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD, id_productos);
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_CANTIDAD, 1); //por defecto compra solo 1 producto al comenzar la compra
        compras.put(DbTablas.TablaDetallesCompras.CAMPO_MONTO, monto);
        db.insert(DbTablas.TablaDetallesCompras.TABLA_DETALLE, null, compras);
    }
    public Cursor detalle_compra(int id_compras){
        final String query = "SELECT * FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLE + " where " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras;
        Cursor compras = db.rawQuery(query,null);
        if (compras != null){
            compras.moveToFirst();
        }
        return compras;
    }
    public Cursor producto(String id_producto){
        final String query = "SELECT " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_ID_PROD + " , "
                + DbTablas.TablaDetallesProd.TABLA_DETALLE_PROD + "." + DbTablas.TablaDetallesProd.CAMPO_NOMBRE + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_MARCA + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_CONT_NETO + " , " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_MEDIDA + " FROM "
                + DbTablas.TablaProductos.TABLA_PRODUCTOS +  " JOIN " +  DbTablas.TablaDetallesProd.TABLA_DETALLE_PROD + " ON " + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_NOMBRE + " = " + DbTablas.TablaDetallesProd.TABLA_DETALLE_PROD + "." + DbTablas.TablaDetallesProd.CAMPO_ID_PROD + " WHERE "
                + DbTablas.TablaProductos.TABLA_PRODUCTOS + "." + DbTablas.TablaProductos.CAMPO_ID_PROD + " = " + id_producto;
        Cursor lista_producto = db.rawQuery(query,null);
        if (lista_producto != null){
            lista_producto.moveToFirst();
        }
        return lista_producto;
    }
    public Cursor producto_comprado_no_encontrado(String id_producto){
        final String query = "SELECT " + DbTablas.TablaProdNoEncoCompras.CAMPO_NOMBRE + " , " + DbTablas.TablaProdNoEncoCompras.CAMPO_PRECIO + " FROM "
                + DbTablas.TablaProdNoEncoCompras.TABLA_PROD_NO_EN_COMP + " WHERE " + DbTablas.TablaProdNoEncoCompras.CAMPO_ID_NO_EN + " = " + id_producto;
        Cursor lista_no_encontrado = db.rawQuery(query,null);
        if (lista_no_encontrado != null){
            lista_no_encontrado.moveToFirst();
        }
        return lista_no_encontrado;
    }
    public void compras_agregar_prod_no_encontrado(String id_producto, int id_supermercado, String nombre, double precio){
        ContentValues compras_prod_no_encontrado = new ContentValues();
        compras_prod_no_encontrado.put(DbTablas.TablaProdNoEncoCompras.CAMPO_ID_NO_EN, id_producto);
        compras_prod_no_encontrado.put(DbTablas.TablaProdNoEncoCompras.CAMPO_FK_ID_SUPER, id_supermercado);
        compras_prod_no_encontrado.put(DbTablas.TablaProdNoEncoCompras.CAMPO_NOMBRE, nombre); //por defecto compra solo 1 producto al comenzar la compra
        compras_prod_no_encontrado.put(DbTablas.TablaProdNoEncoCompras.CAMPO_PRECIO, precio);
        db.insert(DbTablas.TablaProdNoEncoCompras.TABLA_PROD_NO_EN_COMP, null, compras_prod_no_encontrado);
    }
    public void actualizar_cantidad(int cantidad, double monto, int id_compra, String id_producto ){
        ContentValues nueva_cantidad = new ContentValues();
        nueva_cantidad.put(DbTablas.TablaDetallesCompras.CAMPO_CANTIDAD, cantidad);
        nueva_cantidad.put(DbTablas.TablaDetallesCompras.CAMPO_MONTO, monto);
        db.update(DbTablas.TablaDetallesCompras.TABLA_DETALLE , nueva_cantidad, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto, null);
    }
    public String monto(int id_compra){
        String monto_anterior = "";
        final String query = "SELECT SUM(" + DbTablas.TablaDetallesCompras.CAMPO_MONTO + " ) FROM "
                + DbTablas.TablaDetallesCompras.TABLA_DETALLE + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
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
                + DbTablas.TablaDetallesCompras.TABLA_DETALLE + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
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
        db.delete(DbTablas.TablaDetallesCompras.TABLA_DETALLE, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras,null);
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
        db.insert(DbTablas.TablaDetallesCompras.TABLA_DETALLE, null, nuevo_detalle);
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
    public Cursor compra_datos(int id_compra){
        final String query = "SELECT * FROM "
                + DbTablas.TablaCompras.TABLA_COMPRAS + " WHERE " + DbTablas.TablaCompras.CAMPO_ID_COMPRA + " = " + id_compra;
        Cursor datos_compras = db.rawQuery(query,null);
        if (datos_compras != null){
            datos_compras.moveToFirst();
        }
        return datos_compras;
    }
    public void borrar_item(int id_compras, String id_producto){
        db.delete(DbTablas.TablaDetallesCompras.TABLA_DETALLE, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto,null);
    }
    public Cursor cargar_no_producto(int id_supermercado){
        final String query = "SELECT * FROM "
                + DbTablas.TablaProdNoEncoCompras.TABLA_PROD_NO_EN_COMP + " WHERE " + DbTablas.TablaProdNoEncoCompras.CAMPO_FK_ID_SUPER + " = " + id_supermercado;
        Cursor producto_no_encontrado = db.rawQuery(query,null);
        if (producto_no_encontrado != null){
            producto_no_encontrado.moveToFirst();
        }
        return producto_no_encontrado;
    }
    public int contar_productos_detalle(int id_compra){
        int total_producto = 0;
        final String query = "SELECT COUNT(" + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + ") FROM "
                + DbTablas.TablaDetallesCompras.TABLA_DETALLE + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
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

    public double calcular_total(int id_compra){
        double total_compras = 0;
        final String query = "SELECT SUM(" + DbTablas.TablaDetallesCompras.CAMPO_MONTO + ") FROM "
                + DbTablas.TablaDetallesCompras.TABLA_DETALLE + " WHERE " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra;
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
    public void editar_producto_no_encontrado (String nombre, double precio, int id_supermercado, String id_producto){
        ContentValues edicion_producto = new ContentValues();
        edicion_producto.put(DbTablas.TablaProdNoEncoCompras.CAMPO_NOMBRE, nombre);
        edicion_producto.put(DbTablas.TablaProdNoEncoCompras.CAMPO_PRECIO, precio);
        db.update(DbTablas.TablaCompras.TABLA_COMPRAS, edicion_producto, DbTablas.TablaProdNoEncoCompras.CAMPO_FK_ID_SUPER + " = " + id_supermercado + " AND " + DbTablas.TablaProdNoEncoCompras.CAMPO_ID_NO_EN + " = " + id_producto, null);
    }
    public Cursor detalle_compra_editada(int id_compras, String id_producto){
        final String query = "SELECT * FROM " + DbTablas.TablaDetallesCompras.TABLA_DETALLE + " where " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compras + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto;
        Cursor compras = db.rawQuery(query,null);
        if (compras != null){
            compras.moveToFirst();
        }
        return compras;
    }
    public void actualizar_monto(double monto, int id_compra, String id_producto ){
        ContentValues nuevo_monto = new ContentValues();
        nuevo_monto.put(DbTablas.TablaDetallesCompras.CAMPO_MONTO, monto);
        db.update(DbTablas.TablaDetallesCompras.TABLA_DETALLE , nuevo_monto, DbTablas.TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " = " + id_compra + " AND " + DbTablas.TablaDetallesCompras.CAMPO_FK_ID_PROD + " = " + id_producto, null);
    }
        //asi se pone los string?'" + codigo +"'
}
