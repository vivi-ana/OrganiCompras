package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.cardview.widget.CardView;
import android.view.View;
@SuppressWarnings("all")
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    CardView cardCompra, cardOfertas, cardDespensa, cardHistorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardCompra = findViewById(R.id.cardCompras);
        cardCompra.setOnClickListener(this);
        cardOfertas = findViewById(R.id.cardOfertas);
        cardOfertas.setOnClickListener(this);
        cardDespensa = findViewById(R.id.cardDespensa);
        cardDespensa.setOnClickListener(this);
        cardHistorial = findViewById(R.id.cardHistorial);
        cardHistorial.setOnClickListener(this);
    }
    public void onClick(final View v) {//Evento click por cada boton del menú
        switch (v.getId()) {
            case R.id.cardCompras:
                //String seleccion = "Apa"; //este codigo hay que sacar despues.
                //DbHelper admin;
                //admin = new DbHelper(this, null); //hace correr la bd
                //SQLiteDatabase db = admin.getReadableDatabase();
                //Cursor a = db.rawQuery("Select * from supermercados where descripcion =  '" + seleccion + "'", null);
                //a.close();
                dialogoMaximo();
            break;
            case R.id.cardDespensa:
                startActivity(new Intent(MainActivity.this, MiDespensaActivity.class));
                finish();
                break;
            case R.id.cardOfertas:
                startActivity(new Intent(MainActivity.this, OfertasActivity.class));
                finish();
            break;
            case R.id.cardHistorial:
                startActivity(new Intent(MainActivity.this, MiHistorialActivity.class));
                finish();
        }
    }

    private void dialogoMaximo() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setTitle(R.string.tituloMax);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.msjMax);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            //Si acepta el mensaje debe salir otro mensaje para introducir el maximo.
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager manager = getSupportFragmentManager(); //se muestra el otro dialogo para ingresar el maximo
                IngreseMax ingreseMax = new IngreseMax();
                ingreseMax.show(manager, "IngresarMax");
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//Si cancela el mensaje tiene que entrar igual a la app.
                startActivity(new Intent(MainActivity.this, Tabs.class));
                finish();
            }
        });
        alertBuilder.setNeutralButton(R.string.atras,//Si quiere volver al menu
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog dialogo = alertBuilder.create();
        dialogo.show();
    }
}
