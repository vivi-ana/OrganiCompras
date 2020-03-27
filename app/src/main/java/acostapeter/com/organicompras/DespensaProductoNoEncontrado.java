package acostapeter.com.organicompras;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.TERCERA_COLUMNA;

@SuppressWarnings("all")
public class DespensaProductoNoEncontrado extends AppCompatActivity {
    static private ArrayList<HashMap<String, String>> lista;
    static Activity activity;
    static ListView listado;
    String nombre = "", ide = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.despensa_producto_no_encontrado);
        listado = findViewById(R.id.listaProductoNoEncontrado);
        lista = new ArrayList<>();
        activity = this;
        cargar();
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FragmentManager manager = getSupportFragmentManager();
                DespensaProductoNoEncontradoEditar myDialog = new DespensaProductoNoEncontradoEditar();
                myDialog.show(manager, "MyDialog");
                Bundle args = new Bundle();
                String nombres = ((TextView) view.findViewById(R.id.nombre)).getText().toString();
                String ides = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String descripcion = ((TextView) view.findViewById(R.id.descrip)).getText().toString();
                String marca = ((TextView) view.findViewById(R.id.marca)).getText().toString();
                String neto = ((TextView) view.findViewById(R.id.neto)).getText().toString();
                String medida = ((TextView) view.findViewById(R.id.medida)).getText().toString();

                args.putString("nombre", nombres);
                args.putString("id", ides);
                args.putString("descripcion", descripcion);
                args.putString("marca", marca);
                args.putString("neto", neto);
                args.putString("medida", medida);
                myDialog.setArguments(args);
            }
        });
    }
    public void cargar(){
        lista.clear();
        Productos productos = new Productos(activity);
        String descripcion, marca, neto, medida;
        ArrayList<HashMap<String, String>> listado_productos;
        listado_productos = productos.lista_producto_no_encontrado_despensa();
        int bucle = listado_productos.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_productos.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                descripcion = hashmap.get(SEGUNDA_COLUMNA);
                marca = hashmap.get(TERCERA_COLUMNA);
                neto = hashmap.get(CUARTA_COLUMNA);
                medida = hashmap.get(QUINTA_COLUMNA);
                ide = hashmap.get(SEPTIMA_COLUMNA);

                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEGUNDA_COLUMNA, descripcion);
                temp.put(TERCERA_COLUMNA, marca);
                temp.put(CUARTA_COLUMNA, neto);
                temp.put(QUINTA_COLUMNA, medida);
                temp.put(SEPTIMA_COLUMNA, ide);
                lista.add(temp);
                DespensaProductoNoEncontradoListViewAdapter adapter = new DespensaProductoNoEncontradoListViewAdapter(activity, lista);
                listado.setAdapter(adapter);
            }
        }else{
            Toast.makeText(activity, "No hay productos en la lista", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        MiDespensaActivity miDespensaActivity = new MiDespensaActivity();
        miDespensaActivity.cargar(); //hay que actualizar las otras pantallas.
        }
        return super.onKeyDown(keyCode, event);
    }
}
