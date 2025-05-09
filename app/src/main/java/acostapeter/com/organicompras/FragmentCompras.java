package acostapeter.com.organicompras;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

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
public class FragmentCompras extends Fragment implements View.OnClickListener {
    Button scanBtn;
    TextView formatTxt, contentTxt, lblmx, textView;
    static TextView txt_total, cantidad_producto, descrip;
    static ListView listado_productos;
    static ArrayList<HashMap<String, String>> lista;
    static Supermercado supermercado;
    static Compras compras;
    static Productos productos;
    static Context contexto;
    static Activity activity;
    static String datos_no_editados [][], matriz_compras[][], matriz_detalles[][];
    static int max, id_compras = 0, id_supermercado = 0;
    static String text_total_compras, scanContent ="";
    static boolean guardar;
    String dia = "",seleccion = "", datoRecibido = null;
    DecimalFormat df = new DecimalFormat("0.00");
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);
        scanBtn = view.findViewById(R.id.scan_button);
        lblmx = view.findViewById(R.id.lbl_maximo);
        formatTxt = view.findViewById(R.id.scan_format);
        contentTxt = view.findViewById(R.id.scan_content);
        txt_total = view.findViewById(R.id.total);
        textView = view.findViewById(R.id.max_compra);
        descrip = view.findViewById(R.id.descrip);
        cantidad_producto = view.findViewById(R.id.cantidad_producto);
        scanBtn.setOnClickListener(this);
        lista = new ArrayList<>();
        setHasOptionsMenu(true);
        supermercado = new Supermercado(getActivity()); //objeto supermercado
        compras = new Compras(getActivity()); //objeto compras
        productos = new Productos(getActivity()); // objeto productos
        listado_productos = view.findViewById(R.id.lista_despensa);
        listas(lista);
        obtener_dia();
        contexto = getActivity();
        activity = getActivity();
        Intent recibir = getActivity().getIntent();
        datoRecibido = recibir.getStringExtra("id");
        if (datoRecibido == null) {
            mensaje();
        }
        else{
            listas(lista);
            editar();
            reestar();
        }
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    listas(lista);
        int count, id_producto = 0;
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            String formato = "EAN_13";
            if (formato.equals(scanFormat)) { ///se tiene que comprobar que solo se scanee EAN 13
                //id_supermercado = compras.getSupermercado();
                productos.setCodigo(scanContent);
                productos.setId_supermercado(id_supermercado);
                productos.precio_existente();
                double precio = productos.getPrecio();
                productos.obtener_id_producto();
                id_producto = productos.getId_producto();
                if (id_producto != 0){ //verifico que el producto este en la bd de lo contrario tiene que agregar el usuario
                    compras.total_productos(id_producto);
                    count = compras.getCant_total_productos();
                    if (count == 0) { //verifico que no este en la lista para que no cargue dos veces el mismo producto
                        lista.clear();
                        comprar(id_producto, precio);
                        cargar();
                    }else {
                        Toast.makeText(getActivity(), "Este producto ya se encuentra en la lista", Toast.LENGTH_LONG).show();
                        cargar();
                    }
                }else{
                    productos.datos_producto_no_encontrado(); //me fijo si el usuario no dio de alta en tabla provisoria
                    id_producto = productos.getId_producto();//buscar en la otra tabla
                    compras.total_productos(id_producto);
                    count = compras.getCant_total_productos();
                    if (count == 0) { //verifico que no este en la lista para que no cargue dos veces el mismo producto
                        precio = productos.getPrecio();
                            if (precio !=0){ //si dio de alta puede comprar el producto
                                lista.clear();
                                comprar(id_producto, precio);
                                cargar();
                            }else{//de lo contrario se da de alta en la tabla provisoria.
                                productonoencontrado(id_supermercado, scanContent);
                            }
                    }else {
                        Toast.makeText(getActivity(), "Este producto ya se encuentra en la lista", Toast.LENGTH_LONG).show();
                        cargar();
                    }
                }
            } else {
                cargar();
                Toast.makeText(getActivity(), "Solo se permite codigos de productos a la venta", Toast.LENGTH_LONG).show();}
            } else {
            cargar();
            Toast.makeText(getActivity(), "No se recibe datos", Toast.LENGTH_SHORT).show();
        }
    }
    public void comprar(int id_producto, double precio_producto){
        double suma_total, total_compras_anterior;
        int acumulador_cantidad_productos, total_de_productos;
        String txt_cantidad, total;
        Compras compras = new Compras(contexto);
        compras.setId(id_compras);
        compras.setTotal_unitario(precio_producto);
        compras.agregar_detalle_compra(id_producto);
        ComparacionProductos(id_producto); //enviar el id del producto agregado. Despues de agregar a la tabla
        text_total_compras = txt_total.getText().toString(); //capturo lo que tiene txttotal
        total_compras_anterior = Double.parseDouble(text_total_compras);//traigo el precio unitario
        suma_total = total_compras_anterior + precio_producto;
        if (max != 0) {
            if (suma_total > max) {
                Toast.makeText(contexto, "Estas comprando de más", Toast.LENGTH_SHORT).show();
            }
        }
        total = (df.format(suma_total)).replace(",", ".");
        txt_total.setText(total);
        txt_cantidad = cantidad_producto.getText().toString(); //capturo lo que tiene cantidad de productos en el txt
        total_de_productos = Integer.parseInt(txt_cantidad);
        acumulador_cantidad_productos = total_de_productos + 1; //acumulo a la cantidad total que habia
        String cantidad_sumada = Integer.toString(acumulador_cantidad_productos);
        cantidad_producto.setText(cantidad_sumada); //envio la nueva cantidad total de productos
    }
    public void ComparacionProductos(int id_producto) {
        String id, nombreProducto;
        ArrayList<HashMap<String, String>> listado_despensa;
        Despensa despensa = new Despensa(contexto);
        Productos productos = new Productos(contexto);
        listado_despensa = despensa.detalle_inventario();//recorro despensa
        int bucle = listado_despensa.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap = listado_despensa.get(i);
                nombreProducto = hashmap.get(ConstantesColumnasDespensa.PRIMERA_COLUMNA); //producto de despensa
                id = hashmap.get(ConstantesColumnasDespensa.CUARTA_COLUMNA);
                int producto_id = Integer.parseInt(id); //id del producto en despensa
                productos.setId_producto(id_producto);
                boolean vacio = productos.nombre_producto();
                if (vacio != true){
                    String nombre = productos.getNombre();//producto de la lista de compra
                    if (nombreProducto.equals(nombre)){
                        despensa.setId_producto(producto_id);
                        despensa.borrar_item();//borra por el id.
                        break;
                    }else{
                        Long codigo = productos.validar_producto(producto_id); //traigo el codigo de producto de ese id si tiene (existente o casa)
                        if (codigo != 0) {
                            String codigo_despensa = String.valueOf(codigo);
                            if (scanContent.equals(codigo_despensa)) { //pregunto si hay un cod igual en la lista de despensa
                                despensa.setId_producto(producto_id);
                                despensa.borrar_item();//borra por el id.
                                break;
                            }
                        }
                        else{

                        }
                    }
                }
            }
        }
    }
    public void cargar(){
        ArrayList<HashMap<String, String>> listado_compras;
        String id_producto, cantidades, montos, descripcion, neto, medida, nombre, marca, precio_unitario;
        lista.clear();
        //Compras compras = new Compras(contexto);
        //compras.maximo_compra(); //obtener el id y el supermercado donde compra
        //id_compras = compras.getId();
        //id_supermercado = compras.getSupermercado();
        compras.setSupermercado(id_supermercado);
        listado_compras = compras.detalle_compras(id_compras);
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
            }
        }else {
            Toast.makeText(contexto, "No hay productos en la lista", Toast.LENGTH_SHORT).show();
        }
    }
    public void productonoencontrado(final int id_super, final String codigo){
        final String id_supermercado = Integer.toString(id_super);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(R.string.tituloProdNoEncontrado);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.prodNoEncontrado);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {  //Si acepta el mensaje debe salir otro mensaje para introducir el producto
                if(getActivity() != null) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    IngreseCompraProductoNoEncontrado producto_no_encontrado = new IngreseCompraProductoNoEncontrado();
                    producto_no_encontrado.show(manager, "ProductoNoEncontrado");
                    Bundle datos = new Bundle();
                    datos.putString("idsuper", id_supermercado);
                    datos.putString("codigo", codigo);
                    producto_no_encontrado.setArguments(datos);
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//No quiere dar de alta el producto
            }
        });
        Dialog dialogo = alertBuilder.create();
        dialogo.show();
    }
    public void mensaje(){
        final String[] items = {"Cáceres", "Carrefour", "Chango Más"};//crear un dialogo donde el usuario seleccione de manera obligatoria donde comprara
        final AlertDialog.Builder supermercado = new AlertDialog.Builder(getActivity());
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
                supermercado_elegido();
                dialog.dismiss();
            }
        });
        supermercado.setNegativeButton(R.string.atras, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), MainActivity.class)); //Si cancela el mensaje tiene que entrar igual a la app.
                if (getActivity()!= null){
                    getActivity().finish();
                }
            }
        });
        Dialog dialog = supermercado.create();
        dialog.show();
    }

    private void supermercado_elegido() {
        if (getActivity() != null) {  //se obtiene el maximo que se trae del mensaje
            max = getActivity().getIntent().getIntExtra("max", 0);
        }
        supermercado.setNombre(seleccion);//se obtiene el id del supermercado a partir del nombre que eligio.
        supermercado.eleccion_supermercado();
        id_supermercado = supermercado.getId();
        compras.setMax(max); //se envia los datos
        compras.setSupermercado(id_supermercado);
        compras.setFecha(dia);
        compras.agregar_compra(); //se agrega la compra
        if (max !=0){ //si no ingreso monto el maximo por default es 0
            textView.setVisibility(View.VISIBLE);
            lblmx.setVisibility(View.VISIBLE);
            String maximo_compra = Integer.toString(max);
            textView.setText(maximo_compra);
        }
        compras.maximo_compra(); //obtener el id
        id_compras = compras.getId();
    }
    public void listas(ArrayList<HashMap<String, String>> lista) {
        if (getActivity()!= null) {
            FragmentComprasListViewAdapter adapter = new FragmentComprasListViewAdapter(getActivity(), lista);
            listado_productos.setAdapter(adapter);
        }
    }
    public void obtener_dia(){
        Calendar miCalendario = Calendar.getInstance();
        Date el_dia = miCalendario.getTime();  //obtener el dia del sistema
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dia = simpleDate.format(el_dia);
    }
    public TextView actualizar_txt_total() {
        return txt_total;
    }
    public int getId_compras(){
        return id_compras;
    }
    public TextView actualizar_txt_cantidad() {
        return cantidad_producto;
    }
    public void onClick(View v){ //evento del boton scanear
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
            scanIntegrator.initiateScan();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { //cada fragment tiene un menu distinto
        inflater.inflate(R.menu.menu_fragment_compras, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_borrar:
                Intent j = new Intent(getActivity(),FragmentComprasBorrarLista.class);
                j.putExtra("idsuper",id_supermercado);
                j.putExtra("id", id_compras);
                startActivity(j);
                return true;
            case R.id.accion_guardar:
                if (listado_productos != null){
                    int count = listado_productos.getAdapter().getCount();
                    if (count >=1){
                        Toast.makeText(getActivity(), "La lista ha sido guardada", Toast.LENGTH_SHORT).show();
                        guardar = true;
                    } else{
                        Toast.makeText(getActivity(), "Debe colocar al menos un producto", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Debe colocar al menos un producto", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.accion_lista:
                Intent i = new Intent (getActivity(), ComprasProductoNoEncontrado.class);
                i.putExtra("idsuper", id_supermercado);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean getGuardar(){
        return guardar;
    }
    public ListView getLista(){
        return listado_productos;
    }
    public String retornartotal() {
        return txt_total.getText().toString();
    }
    public String retornarcantidad() {
        return cantidad_producto.getText().toString();
    }
    public String[][] getDatosnoeditados() {
        return datos_no_editados;
    }
    public String[][] getMatriz_compras() {
        return matriz_compras;
    }
    public String[][] getMatriz_detalles() {
        return matriz_detalles;
    }
    public void limpiar(){//se usa en la lista de productos no encontrados
        listado_productos = getLista();
        lista.clear();
    }
    public void llenar() {//se usa en la lista de productos no encontrados
        FragmentComprasListViewAdapter adapter = new FragmentComprasListViewAdapter(activity, lista);
        listado_productos.setAdapter(adapter);
    }
    public void cambiarTxtTotal(){ //aca se altera el total general de la tabla compras tambien.
        //compras.maximo_compra();
        //id_compras = compras.getId();
        compras.setId(id_compras);
        compras.total_productos_comprados();
        int cantidad = compras.getCant_total_productos();
        String cantidad_productos = String.valueOf(cantidad);
        if (cantidad != 0) {
           //compras.setId(id_compras);
            compras.calcular_total_compra();
            double total_compra = compras.getTotal();
            txt_total.setText(df.format(total_compra));
            cantidad_producto.setText(cantidad_productos);
        }else {
            txt_total.setText("0");
            cantidad_producto.setText("0");
        }
    }
    public void editar() {
        lista.clear();
        String id_producto, montos, neto, medida, cantidades, descripcion, nombre, marca, precio_unitario;
        //int supermercado_editar = 0;
        Compras compras = new Compras(contexto);
        Supermercado supermercado = new Supermercado(contexto);
        id_compras = Integer.valueOf(datoRecibido);
        compras.setId(id_compras);
        compras.cargar_algunos_detalles_compras();
        if (!compras.isVacio()) {//si no esta vacio el detalle
            supermercado.supermercado_compra_editada(id_compras);
            id_supermercado = supermercado.getId();
            compras.setSupermercado(id_supermercado);
            double total_compra = compras.getTotal();
            txt_total.setText(df.format(total_compra));
            int cant = compras.getCantidad();
            cantidad_producto.setText(String.valueOf(cant));
            int maximo = compras.getMax();
            if (maximo != 0){
                textView.setVisibility(View.VISIBLE);
                lblmx.setVisibility(View.VISIBLE);
                textView.setText(String.valueOf(max));
            }
        }
        compras.contar_productos_compra();
        int i = compras.getCant_total_productos();
        datos_no_editados= new String[i][2];
        int j = 0;
        ArrayList<HashMap<String, String>> listado_edicion;
        listado_edicion = compras.detalle_compras(id_compras);
        int bucle = listado_edicion.size();
        if (bucle != 0){
            for(int inicio=0; inicio<bucle; inicio++) {
                HashMap<String, String> hashmap= listado_edicion.get(inicio);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                descripcion = hashmap.get(SEGUNDA_COLUMNA);
                marca = hashmap.get(TERCERA_COLUMNA);
                precio_unitario = hashmap.get(CUARTA_COLUMNA);
                cantidades = hashmap.get(QUINTA_COLUMNA);
                montos = hashmap.get(SEXTA_COLUMNA);
                id_producto = hashmap.get(SEPTIMA_COLUMNA);
                neto = hashmap.get(OCTAVA_COLUMNA);
                medida = hashmap.get(NOVENA_COLUMNA);

                datos_no_editados[j][0] = id_producto;
                datos_no_editados [j][1] = cantidades;
                j++;

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
        }else {
            Toast.makeText(contexto, "No hay productos en la lista", Toast.LENGTH_SHORT).show();
        }
    }
    public void reestar(){
        List<String> indice = new ArrayList<String>();
        indice.add("PRIMERA_COLUMNA");
        indice.add("SEGUNDA_COLUMNA");
        indice.add("TERCERA_COLUMNA");
        indice.add("CUARTA_COLUMNA");
        indice.add("QUINTA_COLUMNA");
        indice.add("SEXTA_COLUMNA");
        indice.add("SEPTIMA_COLUMNA");
        matriz_compras = new String[1][7];
        Compras compras = new Compras(contexto);
        id_compras = Integer.valueOf(datoRecibido);
        compras.setId(id_compras);
        compras.contar_productos_compra();
        int fila = compras.getCant_total_productos();
        matriz_detalles= new  String[fila][5];
        ArrayList<HashMap<String, String>> listado_compra;
        listado_compra =  compras.cargar_compra(id_compras);
        HashMap<String, String> hashmap= listado_compra.get(0);
            for(int i=0;i<=6;i++){
                matriz_compras[0][i] = hashmap.get(indice.get(i)); //carga un vector con el registro de la compra
            }
        ArrayList<HashMap<String, String>> listado_detalle;
        listado_detalle =  compras.cargar_detalle(id_compras);
            for (int filas = 0; filas <= fila-1; filas++) {
                HashMap<String, String> map= listado_detalle.get(filas);
                for (int columnas = 0; columnas <= 4; columnas++) {
                    matriz_detalles[filas][columnas] = map.get(indice.get(columnas));//matriz de todos los detalles para determinada compra
                }
            }
        }
    public void onResume(){
        super.onResume();
        boolean bandera = FragmentComprasBorrarLista.ReturnCargar();
        if(bandera) {
            limpiar();
            cargar();
            llenar();
            cambiarTxtTotal();
            FragmentComprasBorrarLista.SetCargar();
        }
    }
    }
