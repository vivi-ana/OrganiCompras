package acostapeter.com.organicompras.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


class DbDatos {

    private void Supermercado(String nombre, SQLiteDatabase db) {
        ContentValues ingresarSuper = new ContentValues();
        ingresarSuper.put(DbTablas.TablaSupermercados.CAMPO_NOMBRE, nombre);  //clave valor
        db.insert(DbTablas.TablaSupermercados.TABLA_SUPERMERCADOS, null, ingresarSuper); //Insertamos el registro en la base de datos
    }
    private void Existentes(String id, int id_producto, SQLiteDatabase db){
        ContentValues existentesProductos = new ContentValues();
        existentesProductos.put(DbTablas.TablaExistentes.CAMPO_ID_EXISTENTE, id); //clave valor
        existentesProductos.put(DbTablas.TablaExistentes.CAMPO_FK_ID_PROD, id_producto);
        db.insert(DbTablas.TablaExistentes.TABLA_EXISTENTES, null, existentesProductos);
    }
    private void Productos(String nombre, String descripcion, String marca, String neto, String medida, SQLiteDatabase db){
        ContentValues ingresarProductos = new ContentValues();
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_NOMBRE, nombre);
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_DESCP, descripcion);
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_MARCA, marca);
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_CONT_NETO, neto);
        ingresarProductos.put(DbTablas.TablaProductos.CAMPO_MEDIDA, medida);
        db.insert(DbTablas.TablaProductos.TABLA_PRODUCTOS, null, ingresarProductos);
    }
    private void ProductosXSuper(String idProd, String idSuper, String precio, SQLiteDatabase db) {
        ContentValues ingresarProdXSuper = new ContentValues();
        ingresarProdXSuper.put(DbTablas.TablaProdXSuper.CAMPO_FK_ID_EXISTENTE, idProd); //clave valor
        ingresarProdXSuper.put(DbTablas.TablaProdXSuper.CAMPO_FK_ID_SUPER, idSuper);
        ingresarProdXSuper.put(DbTablas.TablaProdXSuper.CAMPO_PRECIO, precio);
        db.insert(DbTablas.TablaProdXSuper.TABLA_PRODXSUPER, null, ingresarProdXSuper);
    }
        void Ingresar(SQLiteDatabase db) {
        Supermercado("Cáceres", db);
        Supermercado("Carrefour", db);
        Supermercado("Chango Más", db);
        Existentes("7791234567898", 1, db);
        Existentes("9310779300005", 2, db);
        Existentes("5901234123457", 3, db);
        Existentes("8414533043847", 4, db);
        Existentes("9788461671113", 5, db);
        Existentes("7790070412355", 6, db);
        Existentes("7793360108426", 7, db);
        Existentes("7790040932708", 8, db);
        Existentes("7790398100118", 9, db);
        Existentes("7790070318619", 10, db);
        Existentes("7622300868512", 11, db);
        Existentes("7790407031013", 12, db);
        Existentes("7798116010015", 13, db);
        Existentes("8412345678905", 14, db);
        Existentes("7794000597525", 15, db);
        Existentes("7790920008707", 16, db);
        Existentes("7790787153664", 17, db);
        Existentes("7794000598317", 18, db);
        Productos("Dulce de leche", "Repostero" ,"Arcor", "300", "gr", db);
        Productos("Arveja", "Al natural","Alco", "500", "gr", db);
        Productos("Arroz", "Largo fino","Gallo", "1000", "gr", db);
        Productos("Arveja", "Al natural","Marolio", "1000", "ml", db);
        Productos("Yerba", "Suave","Amanda", "1000", "gr", db);
        Productos("Flan", "Sabor dulce de leche","Exquisita", "60", "gr", db);
        Productos("Choclo", "Blanco cremoso","La Campagnola", "300", "gr", db);
        Productos("Galletitas", "Surtidas","Bagley", "93", "gr", db);
        Productos("Queso rallado", "Reggianito","La Paulina", "40", "gr", db);
        Productos("Fideo", "Codito","Matarazzo", "500", "gr", db);
        Productos("Jugo", "Sabor naranja","Tang", "18", "gr", db);
        Productos("Chocolate", "Amargo","Aguila", "100", "gr", db);
        Productos("Arroz","Largo Fino", "Itajai", "1000", "gr", db);
        Productos("Arroz", "Largo Grueso", "Marolio", "500", "gr", db);
        Productos("Caldo", "Sabor gallina", "Knorr", "114", "gr", db);
        Productos("Picadillo", "De carne", "Safra", "90", "gr", db);
        Productos("Dulce de leche", "Clasico", "iLolay", "400", "gr", db);
        Productos("Sopa crema", "Arvejas con jamon", "Knorr", "64", "gr", db);
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
        ProductosXSuper("7790070412355", "1", "40.20", db);
        ProductosXSuper("7790070412355", "2", "41.20", db);
        ProductosXSuper("7790070412355", "3", "40.50", db);
        ProductosXSuper("7793360108426", "1", "80.20", db);
        ProductosXSuper("7793360108426", "2", "81.50", db);
        ProductosXSuper("7793360108426", "3", "88.50", db);
        ProductosXSuper("7790040932708", "1", "33.20", db);
        ProductosXSuper("7790040932708", "2", "34.50", db);
        ProductosXSuper("7790040932708", "3", "38.50", db);
        ProductosXSuper("7790398100118", "1", "123.20", db);
        ProductosXSuper("7790398100118", "2", "124.50", db);
        ProductosXSuper("7790398100118", "3", "138.50", db);
        ProductosXSuper("7790070318619", "1", "23.20", db);
        ProductosXSuper("7790070318619", "2", "24.50", db);
        ProductosXSuper("7790070318619", "3", "38.50", db);
        ProductosXSuper("7622300868512", "1", "153.20", db);
        ProductosXSuper("7622300868512", "2", "159.50", db);
        ProductosXSuper("7622300868512", "3", "165.50", db);
        ProductosXSuper("7790407031013", "1", "153.20", db);
        ProductosXSuper("7790407031013", "2", "159.50", db);
        ProductosXSuper("7790407031013", "3", "165.50", db);
        ProductosXSuper("7798116010015", "1", "153.20", db);
        ProductosXSuper("7798116010015", "2", "159.50", db);
        ProductosXSuper("7798116010015", "3", "165.50", db);
        ProductosXSuper("8412345678905", "1", "50.25", db);
        ProductosXSuper("8412345678905", "2", "50.70", db);
        ProductosXSuper("8412345678905", "3", "52.25", db);
        ProductosXSuper("7794000597525", "1", "60.25", db);
        ProductosXSuper("7794000597525", "2", "60.70", db);
        ProductosXSuper("7794000597525", "3", "62.25", db);
        ProductosXSuper("7790920008707", "1", "70.25", db);
        ProductosXSuper("7790920008707", "2", "70.70", db);
        ProductosXSuper("7790920008707", "3", "72.25", db);
        ProductosXSuper("7790787153664", "1", "80.25", db);
        ProductosXSuper("7790787153664", "2", "80.70", db);
        ProductosXSuper("7790787153664", "3", "82.25", db);
        ProductosXSuper("7794000598317", "1", "90.25", db);
        ProductosXSuper("7794000598317", "2", "90.70", db);
        ProductosXSuper("7794000598317", "3", "92.25", db);
    }
}
