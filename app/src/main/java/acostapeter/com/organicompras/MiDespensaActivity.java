package acostapeter.com.organicompras;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static acostapeter.com.organicompras.ConstantesColumnasDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasProductoNoEncontrado.TERCERA_COLUMNA;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;
@SuppressWarnings("all")
public class MiDespensaActivity extends AppCompatActivity implements View.OnClickListener {
    AutoCompleteTextView Eproducto;
    ArrayAdapter<String> adapter;
    static Context contexto;
    int id_supermercado = 0;
    String nombre_producto = "", seleccion ="";
    String[] item = new String[] {"Ingrese producto a buscar..."};
    static private ArrayList<HashMap<String, String>> lista;
    static ListView lista_despensa;
    static TextView cantidad_producto;
    Button scanner;
    static boolean guardar =false, verificado = false;
    static Activity activity;
    static Productos productos;
    static Despensa despensa;
    static int bucle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_despensa);
        guardar = true;
        contexto = this;
        activity = this;
        lista_despensa = findViewById(R.id.lista_despensa);
        lista = new ArrayList<HashMap<String, String>>();
        scanner = findViewById(R.id.scan_button);
        Eproducto = findViewById(R.id.Eproducto);
        productos = new Productos(this);
        despensa = new Despensa(this);
        cantidad_producto = findViewById(R.id.cantidad_producto);
        Eproducto.addTextChangedListener(new MiDespensaActivityEditTextWatcher(this));
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,item);
        Eproducto.setAdapter(adapter);
        Eproducto.setThreshold(1); //especifica que con una sola letra ya se puede obtener una lista de productos
        cargar();
        cantidad();
        scanner.setOnClickListener(this); //click en el boton scanner
        Eproducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    nombre_producto = parent.getItemAtPosition(position).toString();
                } else {
                    nombre_producto = parent.getItemAtPosition(position - 1).toString();
                }
                buscar_producto();
            }
        });
    }
    public String[] getItemsDb(String itemBuscar){
        List<MiDespensaActivityObjeto> products = productos.editText_buscar(itemBuscar);// agregar items en el array de forma dinamica
        int rowCount = products.size();
        String[] item = new String[rowCount];
        int x = 0;
        for (MiDespensaActivityObjeto guardar : products) {
            item[x] = guardar.objecto;
            x++;
        }
        return item;
    }
    public void agregar(View v){
        nombre_producto = Eproducto.getText().toString();
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(nombre_producto);
        boolean bs = ms.matches();
        if (nombre_producto.equals("")) {
            Eproducto.setError("Debe introducir un producto");
            Eproducto.requestFocus();
        } else if(!bs){
            Eproducto.setError("El producto no debe contener numeros");
            Eproducto.requestFocus();
        }else{
            buscar_producto(); //buscar si esta en la tabla productos
            if (bucle ==0)mensaje(nombre_producto); //hay que agregar el producto porque no se encontro
        }
    }
    private void mensaje(final String nuevo_producto) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MiDespensaActivity.this);
        alertBuilder.setTitle(R.string.tituloProdNoEncontrado);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.msjProductoNoEncontrado);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            //Si acepta el mensaje
            public void onClick(DialogInterface dialog, int which) {//dar de alta en la tabla producto_no_encontrado y recien guardar.
                verificar(nuevo_producto, null);
                if (!verificado) { //si verificado es falso es decir no esta en la despensa.
                    String id_producto = "";
                    productos.setNombre(nuevo_producto);
                    boolean lista = productos.buscar_producto_no_encontrado();//hay que verificar que no se haya dado de alta antes.
                    if (lista) { //si lista es verdadero es porque hay datos
                        id_producto = productos.getId();
                    }else{//es un producto nuevo hay que ingresarlo
                        productos.insertar_producto_no_encontrado_despensa();
                        int maximo = productos.maximo_producto_no_encontrado_despensa();
                        if (maximo != 0) id_producto = Integer.toString(maximo); //id del producto no encontrado que esta insertado en otra tabla.
                    }
                    despensa.setId_producto("N" + id_producto);
                    despensa.insertar_inventario();
                    guardar = false;
                    cargar();
                    cantidad_nueva();
                }
                Eproducto.setText("");
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Eproducto.setText("");
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    private void buscar_producto(){
        productos.setNombre(nombre_producto); //TRAER EL ID DEL PRODUCTO
        ArrayList<HashMap<String, String>> listado_producto;
        listado_producto = productos.producto_por_nombre();
        bucle = listado_producto.size();
        if (bucle != 0) {
            for (int i = 0; i < bucle; i++) {
                HashMap<String, String> hashmap = listado_producto.get(i);
                String id_producto = hashmap.get(TERCERA_COLUMNA);
                verificar(nombre_producto, null);
                if (!verificado) {
                    guardar = false; //cuando se inserta un producto nuevo se tiene que obligar al usuario que guarde.
                    despensa.setId_producto(id_producto);
                    despensa.insertar_inventario();
                    cargar();
                    cantidad_nueva();
                }
            }
            Eproducto.setText("");
        }
    }
    public void verificar(String nombre_producto, String codigo){//verifica que no se este ingresando el mismo producto en la lista
        ArrayList<HashMap<String, String>> listado_despensa;
        String id_producto, nombre;
        verificado = false;
        listado_despensa = despensa.detalle_inventario();
        int bucle = listado_despensa.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_despensa.get(i);
                nombre = hashmap.get(ConstantesColumnasDespensa.PRIMERA_COLUMNA);
                id_producto = hashmap.get(CUARTA_COLUMNA);
                if (id_producto !=null) {
                    int cantidad_id = id_producto.length();
                    if (cantidad_id == 13) {
                        if (id_producto.equals(codigo)){
                            Toast.makeText(this, "Este producto ya se agregó a la lista", Toast.LENGTH_SHORT).show();
                            verificado = true;
                            return;
                        }
                    }else{
                        if (nombre.equalsIgnoreCase(nombre_producto)) {
                            Toast.makeText(this, "Este producto ya se agregó a la lista", Toast.LENGTH_SHORT).show();
                            verificado = true;
                            return;
                        }
                    }
                }
            }
        }
    }
    public void cargar(){
        ArrayList<HashMap<String, String>> listado_despensa;
        lista.clear();
        Despensa despensa = new Despensa(contexto);
        String nombre, cantidad, decripcion, marca, neto, medida, id_producto;
        listado_despensa = despensa.detalle_inventario();
        int bucle = listado_despensa.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_despensa.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                decripcion = hashmap.get(SEGUNDA_COLUMNA);
                cantidad = hashmap.get(ConstantesColumnasDespensa.TERCERA_COLUMNA);
                id_producto = hashmap.get(CUARTA_COLUMNA);
                marca = hashmap.get(QUINTA_COLUMNA);
                neto = hashmap.get(SEXTA_COLUMNA);
                medida = hashmap.get(SEPTIMA_COLUMNA);

                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(PRIMERA_COLUMNA, nombre);
                temp.put(SEGUNDA_COLUMNA, decripcion);
                temp.put(ConstantesColumnasDespensa.TERCERA_COLUMNA, cantidad);
                temp.put(CUARTA_COLUMNA, id_producto);
                temp.put(QUINTA_COLUMNA, marca);
                temp.put(SEXTA_COLUMNA, neto);
                temp.put(SEPTIMA_COLUMNA, medida);
                lista.add(temp);
                MiDespensaActivityListViewAdapter adapterDespensa = new MiDespensaActivityListViewAdapter(activity, lista);
                lista_despensa.setAdapter(adapterDespensa);
            }
        }else{
            Toast.makeText(contexto, "No tiene productos en la lista de su despensa", Toast.LENGTH_SHORT).show();

        }
    }
    public void cantidad_nueva(){
        String text_cantidad = cantidad_producto.getText().toString(); //capturo lo que tiene cantidad de productos
        int valor_anterior = Integer.parseInt(text_cantidad);
        int cantidad_sumada = valor_anterior + 1;
        String cantidad_total = Integer.toString(cantidad_sumada);
        cantidad_producto.setText(cantidad_total);
    }
    public void cantidad(){
        int cantidad;
        cantidad = despensa.cantidad_productos_inventario();
        String cantidad_inventario = String.valueOf(cantidad);
        cantidad_producto.setText(cantidad_inventario);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mi_despensa_activity, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_borrar:
                startActivity(new Intent(MiDespensaActivity.this, MiDespensaActivityBorrarLista.class));
                return true;
            case R.id.action_guardar:
                int count = lista_despensa.getCount();
                if (count >=1){
                    guardar_lista();
                    Toast.makeText(getApplicationContext(), "La lista ha sido guardada", Toast.LENGTH_SHORT).show();
                    guardar = true;
                } else{
                    Toast.makeText(getApplicationContext(), "Debe colocar al menos un producto", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.gasto:
                mensaje_supermercado();
                return  true;
            case R.id.action_productoNoEncontrado:
                startActivity(new Intent(MiDespensaActivity.this, DespensaProductoNoEncontrado.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void guardar_lista() {
        despensa.guardar_inventario(); //poner en S todos los productos.
        int maximo;
        maximo = productos.maximo_producto_no_encontrado_despensa();
        if(maximo != 0){
            productos.guardar_producto_no_encontrado_despensa(); //Poner S a todos los productos no encontrados
        }
    }
    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    public void almacenar(String scanContent){
        Despensa despensa = new Despensa(contexto);
        despensa.setId_producto(scanContent);
        despensa.insertar_inventario();
    }
    private void mensajeguardar() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MiDespensaActivity.this);
        alertBuilder.setTitle(R.string.salirGuardar);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.despensaGuardar);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            //Si acepta el mensaje
            public void onClick(DialogInterface dialog, int which) {
                guardar_lista();//decir que se guardo y salir del activity.
                Toast.makeText(MiDespensaActivity.this, "La lista ha sido guardada", Toast.LENGTH_SHORT).show();
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrar();
                finish();//Si cancela el mensaje tiene que borrar y salir del activity.
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void borrar(){
        despensa.borrar_inventario();
        int maximo_inventario;
        maximo_inventario = productos.maximo_producto_no_encontrado_despensa();
        if(maximo_inventario!=0){
            productos.borrar_producto_no_encontrado_inventario(); //tengo que borrar la tabla de productos no encontrado por si el usuario agrego algo pero al final borra.
        }
        finish();//"hay que borrar y salir
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents(); //El codigo de barra
            String scanFormat = scanningResult.getFormatName(); //el formato del scanneo.
            String formato = "EAN_13"; //se tiene que comprobar que solo se scanee EAN 13
            if (formato.equals(scanFormat)) {
                productos.setId(scanContent);
                boolean vacio = productos.nombre_producto();//Hay que verificar si el producto esta en la tabla productos.
                if (vacio != true){
                        String nombre = productos.getNombre();
                        verificar(nombre, scanContent);
                        if (!verificado) {
                            guardar = false;//se carga en la lista el dato scanneado.
                            despensa.setId_producto(scanContent);
                            despensa.insertar_inventario();
                            cargar();
                            cantidad();
                        }
                }else {
                    verificar(null,scanContent);
                    if (!verificado)
                    productonoencontrado(scanContent);
                }
            }else{
                cargar();
                cantidad();
                Toast.makeText(getApplicationContext(), "Solo se permite codigos de productos a la venta", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "No se recibe datos", Toast.LENGTH_SHORT).show();
        }
    }
    public void productonoencontrado(final String codigo){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.tituloProdNoEncontrado);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.prodNoEncontrado);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//Si acepta el mensaje debe salir otro mensaje para introducir el maximo.
                FragmentManager manager = getSupportFragmentManager();
                IngreseDespensaProductoNoEncontrado ingreseProducto = new IngreseDespensaProductoNoEncontrado();
                ingreseProducto.show(manager, "Ingrese producto a la despensa");
                Bundle args = new Bundle();
                args.putString("codigo", codigo);
                ingreseProducto.setArguments(args);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //No quiere dar de alta el producto
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void onResume(){
        super.onResume();
        boolean bandera = MiDespensaActivityBorrarLista.ReturnCargar();
        if(bandera) {
            lista_despensa.setAdapter(null);
            MiDespensaActivityListViewAdapter adapter = new MiDespensaActivityListViewAdapter(this, lista);
            lista_despensa.setAdapter(adapter);
            cargar();
            cantidad();
            MiDespensaActivityBorrarLista.SettingCargar();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (lista_despensa != null){//si no creo nada en la lista
                int count = lista_despensa.getCount();
                if (!guardar) {//si no se guardo  y tiene elementos la lista se pregunta con msj
                    if (count >=1){
                        mensajeguardar();
                    }else { //si no se guardo y encima no hay elementos en la lista. _El usuario solo quiere salir
                        finish();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } //borrar lo creado
                }
                else {

                    if (count >=1) { //guardar es verdadero con productos en la lista
                        finish();
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        finish();
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);//se guardo pero se dejo la lista vacia. NO se puede guardar lista vacia.
                        guardar = false;
                    }
                }
            }else {
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);//borrar la compra y salir sin mensaje.
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    public void mensaje_supermercado(){//crear un dialogo donde el usuario seleccione de manera obligatoria donde comprara
        final String[] items = {"Cáceres", "Carrefour", "Chango Más" };
        final AlertDialog.Builder supermercado = new AlertDialog.Builder(MiDespensaActivity.this);
        supermercado.setTitle(R.string.supermercado);
        supermercado.setCancelable(false);
        supermercado.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                seleccion = items[item];
            }
        });
        supermercado.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (seleccion.equals("")) {
                    seleccion = "Cáceres";
                }
                supermercado();
                dialog.dismiss();
            }
        });
        supermercado.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//Si cancela el mensaje
            }
        });
        Dialog dialog = supermercado.create();
        dialog.show();
    }
    public void supermercado(){
        Supermercado supermercado = new Supermercado(this);
        supermercado.setNombre(seleccion);//se obtiene el id del supermercado a partir del nombre que eligio.
        supermercado.eleccion_supermercado();
        id_supermercado = supermercado.getId();
        gasto_aproximado(id_supermercado);
    }
    public void producto(String texto){
        cantidad_producto.setText(texto);
    }
    private void gasto_aproximado(final Integer id_supermercado) {
        double total =  despensa.calcular_total_aproximado(id_supermercado);
        if (total ==-1) {
            Toast.makeText(this, "No se tiene productos en la lista", Toast.LENGTH_LONG).show();
        }else if(total==0){
            Toast.makeText(this, "No se puede calcular el gasto total con los productos no encontrados de la lista", Toast.LENGTH_LONG).show();
        }else{
            DecimalFormat df = new DecimalFormat("0.00");
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MiDespensaActivity.this);
            alertBuilder.setTitle(R.string.tituloGastoAproximado);
            alertBuilder.setCancelable(false);
            String tot = (df.format(total)).replace(",", ".");
            alertBuilder.setMessage("$" + tot);
            alertBuilder.setPositiveButton(R.string.listo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {  //Si acepta el mensaje
                    dialog.dismiss();
                }
            });
            Dialog dialog = alertBuilder.create();
            dialog.show();
        }
    }
}
