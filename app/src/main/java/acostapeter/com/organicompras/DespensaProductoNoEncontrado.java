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

import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.PRIMERA_COLUMNA;
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
                args.putString("nombre", nombres);
                args.putString("codigo", ides);
                myDialog.setArguments(args);
            }
        });
    }
    public void cargar(){
        lista.clear();
        Productos productos = new Productos(activity);
        ArrayList<HashMap<String, String>> listado_productos;
        listado_productos = productos.lista_producto_no_encontrado_despensa();
        int bucle = listado_productos.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_productos.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                ide = hashmap.get(OCTAVA_COLUMNA);

                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(OCTAVA_COLUMNA, ide);
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
