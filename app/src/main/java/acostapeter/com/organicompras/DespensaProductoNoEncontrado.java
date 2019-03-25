package acostapeter.com.organicompras;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.TERCERA_COLUMNA;
@SuppressWarnings("all")
public class DespensaProductoNoEncontrado extends AppCompatActivity {
    static private ArrayList<HashMap<String, String>> lista;
    static Activity activity;
    ListView listado;
    String nombre = "", ide = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.despensa_producto_no_encontrado);
        listado = findViewById(R.id.listaProductoNoEncontrado);
        lista = new ArrayList<>();
        activity = this;
        cargar();
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
                ide = hashmap.get(TERCERA_COLUMNA);

                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(TERCERA_COLUMNA, ide);
                lista.add(temp);
                DespensaProductoNoEncontradoListViewAdapter adapter = new DespensaProductoNoEncontradoListViewAdapter(activity, lista);
                listado.setAdapter(adapter);
            }
        }else{
            Toast.makeText(this, "No hay productos en la lista", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
