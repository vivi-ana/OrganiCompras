package acostapeter.com.organicompras;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

class DbDatos {

    private void Supermercado(String nombre, SQLiteDatabase db) {
        ContentValues ingresarSuper = new ContentValues();
        ingresarSuper.put(DbTablas.TablaSupermercados.CAMPO_DESCRIP, nombre);  //clave valor
        db.insert(DbTablas.TablaSupermercados.TABLA_SUPER, null, ingresarSuper); //Insertamos el registro en la base de datos
    }
    private void Detalle(String id, String nombre, SQLiteDatabase db){
        ContentValues detalleProducto = new ContentValues();
        detalleProducto.put(DbTablas.TablaDetallesProd.CAMPO_ID_PROD, id); //clave valor
        detalleProducto.put(DbTablas.TablaDetallesProd.CAMPO_NOMBRE, nombre);
        db.insert(DbTablas.TablaDetallesProd.TABLA_DETALLE_PROD, null, detalleProducto);
    }
    private void Productos(String id, String nombre, String marca, String neto, String medida, SQLiteDatabase db){
        ContentValues ingresarProductos = new ContentValues();
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_ID_PROD, id); //clave valor
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_NOMBRE, nombre);
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_MARCA, marca);
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_CONT_NETO, neto);
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_MEDIDA, medida);
        db.insert(DbTablas.TablaProductos.TABLA_PRODUCTOS, null, ingresarProductos);
    }
    private void ProductosXSuper(String idProd, String idSuper, String precio, SQLiteDatabase db) {
        ContentValues ingresarProdXSuper = new ContentValues();
        ingresarProdXSuper.put(DbTablas.TablaProdXSuper.CAMPO_FK_ID_PROD, idProd); //clave valor
        ingresarProdXSuper.put(DbTablas.TablaProdXSuper.CAMPO_FK_ID_SUPER, idSuper);
        ingresarProdXSuper.put(DbTablas.TablaProdXSuper.CAMPO_PRECIO, precio);
        db.insert(DbTablas.TablaProdXSuper.TABLA_PRODXSUPER, null, ingresarProdXSuper);
    }
        void Ingresar(SQLiteDatabase db) {
        Supermercado("Cáceres", db);
        Supermercado("Carrefour", db);
        Supermercado("Chango Más", db);
        Detalle("1", "Dulce de leche", db);
        Detalle("2", "Arveja", db);
        Detalle("3", "Arroz", db);
        Productos("7791234567898", "1", "Arcor", "300", "gr", db);
        Productos("9310779300005", "2", "Alco", "500", "gr", db);
        Productos("5901234123457", "3", "Gallo", "1000", "gr", db);
        Productos("8414533043847", "1", "Marolio", "1000", "ml", db);
        Productos("9788461671113", "1", "Amanda", "1000", "gr", db);
        ProductosXSuper("7791234567898", "1", "30.20", db);
        ProductosXSuper("7791234567898", "2", "35", db);
        ProductosXSuper("7791234567898", "3", "40", db);
        ProductosXSuper("9310779300005", "1", "40.65", db);
        ProductosXSuper("9310779300005", "2", "45", db);
        ProductosXSuper("9310779300005", "3", "50", db);
        ProductosXSuper("5901234123457", "1", "50.59", db);
        ProductosXSuper("5901234123457", "2", "55", db);
        ProductosXSuper("5901234123457", "3", "60", db);
        ProductosXSuper("8414533043847", "1", "20.65", db);
        ProductosXSuper("8414533043847", "2", "25", db);
        ProductosXSuper("8414533043847", "3", "30.20", db);
        ProductosXSuper("9788461671113", "1", "22.65", db);
        ProductosXSuper("9788461671113", "2", "15", db);
        ProductosXSuper("9788461671113", "3", "19.20", db);
    }

}
