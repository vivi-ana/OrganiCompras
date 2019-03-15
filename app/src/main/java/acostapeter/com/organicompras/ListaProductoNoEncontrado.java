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
import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesProductoNoEncontrado.TERCERA_COLUMNA;
@SuppressWarnings("all")
public class ListaProductoNoEncontrado extends AppCompatActivity {
    static private ArrayList<HashMap<String, String>> list;
    String nombre = "", precio, ide = "";
    static ListView lista;
    String id_super;
    static Activity activity;
    ListView listado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_producto_no_encontrado);
        lista = findViewById(R.id.listaNoProductos);
        activity = this;
        list = new ArrayList<HashMap<String, String>>();
        id_super = getIntent().getStringExtra("idsuper");
        int id_supermercado = Integer.parseInt(id_super);
        cargar(id_supermercado);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FragmentManager manager = getSupportFragmentManager();
                ListaProductoNoEncontradoEditar myDialog = new ListaProductoNoEncontradoEditar();
                myDialog.show(manager, "MyDialog");
                Bundle args = new Bundle();
                String precios = ((TextView) view.findViewById(R.id.precio)).getText().toString(); //traer el precio del producto que selecciona
                String nombres = ((TextView) view.findViewById(R.id.nombre)).getText().toString();
                String ides = ((TextView) view.findViewById(R.id.id)).getText().toString();
                args.putString("idsuper", id_super);
                args.putString("nombre", nombres);
                args.putString("precio", precios);
                args.putString("codigo", ides);
                myDialog.setArguments(args);
            }
        });
    }
    public void cargar(int id_super){
        list.clear();
        Productos productos = new Productos(this);
        ArrayList<HashMap<String, String>> listado_productos;
        productos.setId_supermercado(id_super);
        listado_productos = productos.cargar_producto_no_encontrado();
        int bucle = listado_productos.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_productos.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                precio = hashmap.get(SEGUNDA_COLUMNA);
                ide = hashmap.get(TERCERA_COLUMNA);

                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEGUNDA_COLUMNA, precio);
                temp.put(TERCERA_COLUMNA, ide);
                list.add(temp);
                ListaProductoNoEncontradoListViewAdapter adapter = new ListaProductoNoEncontradoListViewAdapter(activity, list);
                lista.setAdapter(adapter);
            }
        }else{
            Toast.makeText(this, "No hay datos para borrar", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentCompras fragmentCompras = new FragmentCompras();
            listado = fragmentCompras.getLista();
            if (listado != null){
                fragmentCompras.limpiar();
                fragmentCompras.cargar();
                fragmentCompras.llenar();
                fragmentCompras.cambiarTxtTotal();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
