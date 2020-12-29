package acostapeter.com.organicompras;

import android.app.Activity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
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
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.TERCERA_COLUMNA;
@SuppressWarnings("all")
public class ComprasProductoNoEncontrado extends AppCompatActivity {
    static private ArrayList<HashMap<String, String>> lista;
    String nombre = "", precio, ide = "", descripcion ="", marca = "", neto = "", medida = "", codigo = "";
    static ListView lista_producto_no_encontrado;
    String id_super;
    static Activity activity;
    ListView listado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compras_producto_no_encontrado);
        lista_producto_no_encontrado = findViewById(R.id.listaProductoNoEncontrado);
        activity = this;
        lista = new ArrayList<>();
        int id_supermercado = getIntent().getIntExtra("idsuper", 0);
        id_super = String.valueOf(id_supermercado);
        cargar(id_supermercado);
        lista_producto_no_encontrado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FragmentManager manager = getSupportFragmentManager();
                ComprasProductoNoEncontradoEditar myDialog = new ComprasProductoNoEncontradoEditar();
                myDialog.show(manager, "MyDialog");
                Bundle args = new Bundle();
                String precios = ((TextView) view.findViewById(R.id.precio)).getText().toString(); //traer el precio del producto que selecciona
                String nombres = ((TextView) view.findViewById(R.id.nombre)).getText().toString();
                String descripcion = ((TextView) view.findViewById(R.id.descrip)).getText().toString();
                String marca = ((TextView) view.findViewById(R.id.marca)).getText().toString();
                String neto = ((TextView) view.findViewById(R.id.neto)).getText().toString();
                String medida = ((TextView) view.findViewById(R.id.medida)).getText().toString();
                String ides = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String codigo = ((TextView) view.findViewById(R.id.codigo)).getText().toString();
                args.putString("idsuper", id_super);
                args.putString("nombre", nombres);
                args.putString("descrip", descripcion);
                args.putString("marca", marca);
                args.putString("neto", neto);
                args.putString("medida", medida);
                args.putString("precio", precios);
                args.putString("id", ides);
                args.putString("codigo", codigo);
                myDialog.setArguments(args);
            }
        });
    }
    public void cargar(int id_super){
        lista.clear();
        Productos productos = new Productos(activity);
        ArrayList<HashMap<String, String>> listado_productos;
        productos.setId_supermercado(id_super);
        listado_productos = productos.cargar_producto_no_encontrado();
        int bucle = listado_productos.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_productos.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                descripcion = hashmap.get(SEGUNDA_COLUMNA);
                marca = hashmap.get(TERCERA_COLUMNA);
                neto = hashmap.get(CUARTA_COLUMNA);
                medida = hashmap.get(QUINTA_COLUMNA);
                precio = hashmap.get(SEXTA_COLUMNA);
                ide = hashmap.get(SEPTIMA_COLUMNA);
                codigo = hashmap.get(OCTAVA_COLUMNA);

                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEGUNDA_COLUMNA, descripcion);
                temp.put(TERCERA_COLUMNA, marca);
                temp.put(CUARTA_COLUMNA, neto);
                temp.put(QUINTA_COLUMNA, medida);
                temp.put(SEXTA_COLUMNA, precio);
                temp.put(SEPTIMA_COLUMNA, ide);
                temp.put(OCTAVA_COLUMNA, codigo);
                lista.add(temp);
                ComprasProductoNoEncontradoListViewAdapter adapter = new ComprasProductoNoEncontradoListViewAdapter(activity, lista);
                lista_producto_no_encontrado.setAdapter(adapter);
            }
        }else{
            Toast.makeText(this, "No hay datos en la lista", Toast.LENGTH_SHORT).show();
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
