package acostapeter.com.organicompras.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@SuppressWarnings("all")
public class DbCRUD extends DbHelper {

    private SQLiteDatabase db;

    public DbCRUD(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, factory);
        DbHelper admin = new DbHelper(context, null);
        db = admin.getReadableDatabase();
    }


    public int eleccion_supermercado(String eleccion) {
        int idsuper = 0;
        final String query = "select * from " + DbTablas.TablaSupermercados.TABLA_SUPER
                + " where " + DbTablas.TablaSupermercados.CAMPO_DESCRIP + " = '" + eleccion + "'";
        Cursor supermercado_elegido = db.rawQuery(query, null);
        if (supermercado_elegido != null) {
            try {
                if (supermercado_elegido.moveToFirst()) {
                    String supermercado = supermercado_elegido.getString(0);
                    idsuper = Integer.parseInt(supermercado);
                }
            } finally {
                supermercado_elegido.close();
            }
        }
        return idsuper;
    }
    public void agregar_compra(String max, int idsuper, String dia){
            ContentValues registros = new ContentValues();
            registros.put(DbTablas.TablaCompras.CAMPO_FK_ID_SUPER, idsuper);
            registros.put(DbTablas.TablaCompras.CAMPO_FECHA, dia);
            if (max==null) { //varia dependiendo si agrego un maximo o no. Revisar si guarda bien los datos
                registros.put(DbTablas.TablaCompras.CAMPO_MAX, 0);
            }else {
                registros.put(DbTablas.TablaCompras.CAMPO_MAX, max);
            }
            registros.put(DbTablas.TablaCompras.CAMPO_CANT_PROD, 0);
            registros.put(DbTablas.TablaCompras.CAMPO_TOTAL, 0);
            registros.put(DbTablas.TablaCompras.CAMPO_TOT_UNITARIO, 0);
            db.insert(DbTablas.TablaCompras.TABLA_COMPRAS, null, registros);
    }

    public Cursor maximo_compra(){

        final String query = "select * from " + DbTablas.TablaCompras.TABLA_COMPRAS
                + " where " + DbTablas.TablaCompras.CAMPO_ID_COMPRA + " =(select max(" + DbTablas.TablaCompras.CAMPO_ID_COMPRA + " )"  + " from " + DbTablas.TablaCompras.TABLA_COMPRAS + ")";
        Cursor fila = db.rawQuery(query, null);
        if (fila != null){
            fila.moveToFirst();
        }
        return fila;
    }
}
