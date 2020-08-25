package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class FragmentComprasBorrarLista extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> lista;
    static int id_compras = 0, id_supermercado = 0;
    String cantidad, total;
    ListView lista_compras;
    DecimalFormat df = new DecimalFormat("0.00");
    final ArrayList<String> item_borrar = new ArrayList<String>();
    public static boolean cargar = false;
    TextView txt_total, cantidad_producto;
    CheckBox checkbox;
    CheckBox mCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compras_borrar_lista);
        lista = new ArrayList<HashMap<String, String>>();
        lista_compras = findViewById(R.id.lista_despensa);
        FragmentComprasBorrarListaListViewAdapter adapter = new FragmentComprasBorrarListaListViewAdapter(this, lista);
        lista_compras.setAdapter(adapter);
        Button btnborrar = findViewById(R.id.btnBorrar);
        id_supermercado = getIntent().getIntExtra("idsuper", 0);
        id_compras = getIntent().getIntExtra("id", 0);
        cargar();
        btnborrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int count = lista_compras.getAdapter().getCount();
                for (int i = 0; i < count; i++) {
                    ConstraintLayout itemLayout = (ConstraintLayout) lista_compras.getChildAt(i); // Encontrar el objeto
                    checkbox = itemLayout.findViewById(R.id.check);
                    if (checkbox.isChecked()) {
                        item_borrar.add(checkbox.getText().toString());
                    }
                }
                mensaje();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            getSupportActionBar().setCustomView(R.layout.checkboxbar);
            mCheckbox = actionBar.getCustomView().findViewById(R.id.checkBox);
            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int count = lista_compras.getAdapter().getCount();
                        for (int i = 0; i < count; i++) {
                            ConstraintLayout itemLayout = (ConstraintLayout) lista_compras.getChildAt(i); // Encontrar el objeto dentro del constraint
                            CheckBox checkbox = itemLayout.findViewById(R.id.check);
                            checkbox.setChecked(true);
                        }
                    } else {
                        int count = lista_compras.getAdapter().getCount();
                        for (int i = 0; i < count; i++) {
                            ConstraintLayout itemLayout = (ConstraintLayout) lista_compras.getChildAt(i); // Encontrar el objeto dentro del constraint
                            CheckBox checkbox = itemLayout.findViewById(R.id.check);
                            checkbox.setChecked(false);
                        }
                    }
                }
            });
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        }
        txt_total = findViewById(R.id.total);
        cantidad_producto = findViewById(R.id.cantidad_producto);
        cambiartxt();
    }
    public void cambiartxt(){
        Compras compras = new Compras(getBaseContext());
        //compras.maximo_compra(); //obtener el id de la compra
        //id_compras = compras.getId();
        compras.setId(id_compras);
        compras.setSupermercado(id_supermercado);
        compras.cargar_algunos_detalles_compras();
        if (!compras.isVacio()) {
            compras.total_productos_comprados();
            compras.calcular_total_compra();//si no esta vacio el detalle
            int cant = compras.getCant_total_productos();
            cantidad = String.valueOf(cant);
            double total_compra = compras.getTotal();
            total = df.format(total_compra);
            txt_total.setText(total);
            cantidad_producto.setText(cantidad);
        }
    }
    public void mensaje() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FragmentComprasBorrarLista.this);
        alertBuilder.setTitle(R.string.eliminar);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.mensajeBorrarProducto);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int cantidad = item_borrar.size();
                if (cantidad >= 1) {
                    cargar = true;
                    for (int i = 0; i < item_borrar.size(); i++) {
                        Compras compras = new Compras(getBaseContext());
                        String item = item_borrar.get(i);
                        compras.setId(id_compras);
                        compras.borrar_item(item);
                    }
                    lista_compras.setAdapter(null);
                    FragmentComprasBorrarListaListViewAdapter adapter = new FragmentComprasBorrarListaListViewAdapter(FragmentComprasBorrarLista.this, lista);
                    lista_compras.setAdapter(adapter);
                    cargar();
                    item_borrar.clear();//borrar
                    cambiartxt();
                    mCheckbox.setChecked(false);
                } else {
                    Toast.makeText(getBaseContext(), "Debe seleccionar algun producto", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//Si cancela el mensaje tiene que entrar igual a la app.
                dialog.dismiss();
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void cargar() {
        ArrayList<HashMap<String, String>> listado_compras;
        String id_producto, cantidades, descripcion, montos, neto, medida, nombre, marca, precio_unitario;
        lista.clear();
        Compras compras = new Compras(getBaseContext());
        //compras.maximo_compra(); //obtener el id
        //id_compras = compras.getId();
        compras.setSupermercado(id_supermercado);
        listado_compras = compras.detalle_compras(id_compras);
        int bucle = listado_compras.size();
        if (bucle != 0) {
            for (int i = 0; i < bucle; i++) {
                HashMap<String, String> hashmap = listado_compras.get(i);
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
            }
        } else {
            Toast.makeText(getBaseContext(), "No hay datos para borrar", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean ReturnCargar() {return cargar;}
    public static void SetCargar() {cargar = false;}
}
