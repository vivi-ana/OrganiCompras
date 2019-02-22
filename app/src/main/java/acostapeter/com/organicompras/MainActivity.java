package acostapeter.com.organicompras;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    CardView cardCompra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardCompra = findViewById(R.id.cardCompras);
        cardCompra.setOnClickListener(this);
    }
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.cardCompras:
                dialogoMaximo();
                break;
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
                FragmentManager manager = getSupportFragmentManager();
                IngreseMax ingreseMax = new IngreseMax();
                ingreseMax.show(manager, "IngresarMax");
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Si cancela el mensaje tiene que entrar igual a la app.
                startActivity(new Intent(MainActivity.this, Tabs.class));
                finish();
            }
        });
        alertBuilder.setNeutralButton(R.string.atras,
                //Si quiere volver al menu
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
}
