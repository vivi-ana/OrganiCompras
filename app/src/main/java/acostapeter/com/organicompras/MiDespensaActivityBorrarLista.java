package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import acostapeter.com.organicompras.data.DbHelper;

import static acostapeter.com.organicompras.ConstantesColumnasDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.MiDespensaActivity.lista_despensa;
@SuppressWarnings("all")
public class MiDespensaActivityBorrarLista extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> lista;
    String marca = "";
    DbHelper databaseH;
    ListView listaDespensa;
    ArrayList<String> item_borrar = new ArrayList<String>();
    public static boolean cargar = false;
    CheckBox mCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_despensa_borrar_lista);
        lista = new ArrayList<HashMap<String, String>>();
        listaDespensa = findViewById(R.id.listaDespensa);
        final MiDespensaActivityBorrarListaListViewAdapter adapter = new MiDespensaActivityBorrarListaListViewAdapter(this, lista);
        listaDespensa.setAdapter(adapter);
        databaseH = new DbHelper(this, null);
        Button btnBorrar = findViewById(R.id.btnBorrar);
        cargar();
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (lista_despensa.getAdapter().getCount() >= 1){
                    item_borrar.clear();
                    String [] item = adapter.getMid();
                    for (String anItem : item) {
                        if (anItem != null) {
                            item_borrar.add(anItem); //agregar a item_borrar todos los id de los items a borrar
                        }
                    }
                    mensaje();
                }else{
                    Toast.makeText(MiDespensaActivityBorrarLista.this, "No hay productos para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            getSupportActionBar().setCustomView(R.layout.checkboxbar);
            mCheckbox = actionBar.getCustomView().findViewById(R.id.checkBox);
            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        adapter.selectAll();
                        listaDespensa.setAdapter(adapter);
                    } else {
                        adapter.unselectAll();
                        listaDespensa.setAdapter(adapter);
                    }
                }
            });
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        }
    }
    public static boolean ReturnCargar () {return cargar;}
    public static void SettingCargar() {cargar = false;}
    public void mensaje(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MiDespensaActivityBorrarLista.this);
        alertBuilder.setTitle(R.string.borrar);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.mensajeBorrarProducto);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int cantidad = item_borrar.size();
                if (cantidad >= 1){
                    cargar = true;
                    for (int i = 0; i < item_borrar.size(); i++) {
                        Despensa despensa = new Despensa(MiDespensaActivityBorrarLista.this);
                        String item = item_borrar.get(i);
                        int id_item = Integer.parseInt(item);
                        //if (item.startsWith("N")) {
                            despensa.setId_producto(id_item);
                            despensa.borrar_item();
                            //String nuevoitem = item.substring(1);
                            //despensa.setId_producto(nuevoitem); //no debo dejar que borre el producto que agrego aca.
                            //despensa.borrar_producto_no_encontrado(); //se borra el nuevo producto de la tabla no producto tambien.
                        //} else {
                        //    despensa.setId_producto(item);
                        //    despensa.borrar_item();
                        //}
                        Toast.makeText(getBaseContext(), "Los productos se borraron de forma correcta", Toast.LENGTH_LONG).show();
                    }
                    listaDespensa.setAdapter(null);
                    MiDespensaActivityBorrarListaListViewAdapter adapter = new MiDespensaActivityBorrarListaListViewAdapter(MiDespensaActivityBorrarLista.this, lista);
                    listaDespensa.setAdapter(adapter);
                    cargar();
                    item_borrar.clear();
                    mCheckbox.setChecked(false);
                } else{
                    Toast.makeText(getBaseContext(), "Debe seleccionar algun producto", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//Si cancela el mensaje se cierra el dialogo
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void cargar(){
        ArrayList<HashMap<String, String>> listado_despensa;
        String nombre, descripcion, cantidad, marca, neto, medida, id_producto;
        lista.clear();
        Despensa despensa = new Despensa(MiDespensaActivityBorrarLista.this);
        listado_despensa = despensa.detalle_inventario();
        int bucle = listado_despensa.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_despensa.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                descripcion = hashmap.get(SEGUNDA_COLUMNA);
                cantidad = hashmap.get(TERCERA_COLUMNA);
                id_producto = hashmap.get(CUARTA_COLUMNA);
                marca = hashmap.get(QUINTA_COLUMNA);
                neto = hashmap.get(SEXTA_COLUMNA);
                medida = hashmap.get(SEPTIMA_COLUMNA);

                HashMap<String, String> temporal = new HashMap<String, String>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, descripcion);
                temporal.put(TERCERA_COLUMNA, cantidad);
                temporal.put(CUARTA_COLUMNA, id_producto);
                temporal.put(QUINTA_COLUMNA, marca);
                if (neto.equals("0"))neto = "";
                temporal.put(SEXTA_COLUMNA, neto);
                temporal.put(SEPTIMA_COLUMNA, medida);
                lista.add(temporal);
                MiDespensaActivityBorrarListaListViewAdapter adapterDespensa = new MiDespensaActivityBorrarListaListViewAdapter(MiDespensaActivityBorrarLista.this, lista);
                listaDespensa.setAdapter(adapterDespensa);
            }
        }else{
            Toast.makeText(this, "No hay datos para borrar", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}