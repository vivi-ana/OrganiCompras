package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static acostapeter.com.organicompras.ConstantesColumnasCompras.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.NOVENA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.TERCERA_COLUMNA;
@SuppressWarnings("all")
public class MiHistorialActivityDetallesCompras extends AppCompatActivity {
    static boolean editar = false;
    String id, maximo, cantidad, total;
    ListView lista_compras;
    int id_compra;
    private ArrayList<HashMap<String, String>> lista;
    DecimalFormat df = new DecimalFormat("0.00");
    TextView txt_maximo, label_maximo, txt_total, cantidad_producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historial_compras_detalles);
        lista = new ArrayList<HashMap<String, String>>();
        lista_compras = findViewById(R.id.lista_despensa);
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        id_compra = Integer.parseInt(id);
        MiHistorialActivityDetallesComprasListViewAdapter adapter = new MiHistorialActivityDetallesComprasListViewAdapter(this, lista,id_compra);
        lista_compras.setAdapter(adapter);
        txt_maximo = findViewById(R.id.max_compra);
        label_maximo = findViewById(R.id.lbl_maximo);
        txt_total = findViewById(R.id.total);
        cantidad_producto = findViewById(R.id.cantidad_producto);
        Compras compras = new Compras(getBaseContext());
        compras.setId(id_compra);
        compras.cargar_algunos_detalles_compras();
        if (!compras.isVacio()) { //si no esta vacio el detalle
            int max = compras.getMax();
            maximo = String.valueOf(max);
            int cant = compras.getCantidad();
            cantidad = String.valueOf(cant);
            double total_compra = compras.getTotal();
            total = df.format(total_compra);
            txt_total.setText(total);
            cantidad_producto.setText(cantidad);
        }else{
            Toast.makeText(this, "No existe el detalle", Toast.LENGTH_SHORT).show();
        }
        if (Integer.parseInt(maximo) != 0){
            txt_maximo.setVisibility(View.VISIBLE);
            label_maximo.setVisibility(View.VISIBLE);
            txt_maximo.setText(maximo);
        }
        cargar();
    }
    public void cargar(){
        ArrayList<HashMap<String, String>> listado_compras;
        String id_producto, descripcion, cantidades, montos, neto, medida, nombre, marca, precio_unitario;
        lista.clear();
        Compras compras = new Compras(getBaseContext());
        Supermercado supermercado = new Supermercado(getBaseContext());
        supermercado.supermercado_compra_editada(id_compra);
        int id_supermercado = supermercado.getId();
        compras.setSupermercado(id_supermercado);//enviar id del supermercado tambien para que traiga los precios del producto bien.
        compras.setId(id_compra);
        listado_compras = compras.detalle_compras(id_compra);
        int bucle = listado_compras.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_compras.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                descripcion = hashmap.get(SEGUNDA_COLUMNA);
                marca = hashmap.get(TERCERA_COLUMNA);
                precio_unitario = hashmap.get(CUARTA_COLUMNA);
                cantidades = hashmap.get(QUINTA_COLUMNA);
                montos = hashmap.get(SEXTA_COLUMNA);
                id_producto = hashmap.get(SEPTIMA_COLUMNA);
                neto = hashmap.get(OCTAVA_COLUMNA);
                medida = hashmap.get(NOVENA_COLUMNA);

                HashMap<String, String> temporal = new HashMap<String, String>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, descripcion);
                temporal.put(TERCERA_COLUMNA, marca);
                temporal.put(CUARTA_COLUMNA, precio_unitario);
                temporal.put(QUINTA_COLUMNA, cantidades);
                temporal.put(SEXTA_COLUMNA, montos);
                temporal.put(SEPTIMA_COLUMNA, id_producto);
                temporal.put(OCTAVA_COLUMNA, neto);
                temporal.put(NOVENA_COLUMNA, medida);
                lista.add(temporal);
                MiHistorialActivityDetallesComprasListViewAdapter adapter = new MiHistorialActivityDetallesComprasListViewAdapter(this, lista,id_compra);
                lista_compras.setAdapter(adapter);
            }
        }else {
            Toast.makeText(getBaseContext(), "No hay datos en el historial", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mi_historial_detalles_compras, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_borrar:
                mensaje();
                return true;
            case R.id.accion_editar:
                Intent i = new Intent(MiHistorialActivityDetallesCompras.this, Tabs.class);
                editar = true;
                i.putExtra("id", id);
                startActivity(i);
                finish();
                return true;
            case R.id.consumo_mensual:
               startActivity(new Intent(MiHistorialActivityDetallesCompras.this, MiHistorialActivityGraficoMensual.class));
               return true;
            case R.id.consumo_anual:
                startActivity(new Intent(MiHistorialActivityDetallesCompras.this, MiHistorialActivityGraficoAnual.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void mensaje(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MiHistorialActivityDetallesCompras.this);
        alertBuilder.setTitle(R.string.borrarCompra);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.mensajeBorrarCompra);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            //Si acepta el mensaje
            public void onClick(DialogInterface dialog, int which) {//decir que se guardo y salir del activity.
                Compras compras = new Compras(getBaseContext());
                compras.setId(id_compra);
                compras.borrar_compra();
                Toast.makeText(MiHistorialActivityDetallesCompras.this, "La compra ha sido eliminada", Toast.LENGTH_SHORT).show();
                finish();
                Intent i = new Intent(getApplicationContext(), MiHistorialActivity.class);
                startActivity(i);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//Si cancela el mensaje tiene que borrar y salir del activity
                dialog.dismiss();
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public boolean getEditar() {
        return editar;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent i = new Intent(this, MiHistorialActivity.class);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}
