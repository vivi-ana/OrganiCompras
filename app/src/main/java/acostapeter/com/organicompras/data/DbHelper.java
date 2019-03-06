package acostapeter.com.organicompras.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BDapp"; //nombre de la bd
    private static int DB__VERSION = 1; //numero de version de la bd

    public DbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB__VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        DbTablas tablas = new DbTablas(); //crear las tablas
        DbDatos datos = new DbDatos(); //ingresar datos
        db.execSQL(tablas.COMPRAS_CREATE);
        db.execSQL(tablas.DETALLES_CREATE);
        db.execSQL(tablas.PROD_CREATE);
        db.execSQL(tablas.INVENTARIO_CREATE);
        db.execSQL(tablas.SUPER_CREATE);
        db.execSQL(tablas.PRODXSUPER_CREATE);
        db.execSQL(tablas.DETALLEPROD_CREATE);
        db.execSQL(tablas.DESPENSANOPROD_CREATE);
        db.execSQL(tablas.SUPERNOPROD_CREATE);
        datos.Ingresar(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

   }
