package acostapeter.com.organicompras;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import java.util.Locale;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

import static acostapeter.com.organicompras.ConstantesColumnasCompras.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.OCTAVA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasCompras.TERCERA_COLUMNA;
@SuppressWarnings("all")
public class FragmentCompras extends android.support.v4.app.Fragment implements View.OnClickListener {
    Button scanBtn;
    TextView formatTxt, contentTxt, lblmx, textView;
    static TextView txt_total, cantidad_producto;
    static ListView listado_productos;
    static ArrayList<HashMap<String, String>> lista;
    static Supermercado supermercado;
    static Compras compras;
    static Productos productos;
    static Context contexto;
    static Activity activity;
    static String datos_no_editados [][], matriz_compras[][], matriz_detalles[][];
    static int max, id_compras, id_supermercado;
    static String text_total_compras;
    static boolean guardar;
    String dia = "",seleccion = "";
    DecimalFormat df = new DecimalFormat("0.00");
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);
        scanBtn = view.findViewById(R.id.scan_button);
        lblmx = view.findViewById(R.id.lbl_maximo);
        formatTxt = view.findViewById(R.id.scan_format);
        contentTxt = view.findViewById(R.id.scan_content);
        txt_total = view.findViewById(R.id.total);
        textView = view.findViewById(R.id.max_compra);
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
        mensaje();
        contexto = getActivity();
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    listas(lista);
        int count;
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            String formato = "EAN_13";
            if (formato.equals(scanFormat)) { ///se tiene que comprobar que solo se scanee EAN 13
                compras.maximo_compra(); //obtener el id y el supermercado donde compra
                id_compras = compras.getId();
                id_supermercado = compras.getSupermercado();
                productos.setId(scanContent);
                productos.setId_supermercado(id_supermercado);
                productos.precio();
                double precio = productos.getPrecio();
                if (precio != 0){ //verifico que el producto este en la bd de lo contrario tiene que agregar el usuario
                    compras.total_productos(scanContent);
                    count = compras.getCant_total_productos();
                    if (count == 0) { //verifico que no este en la lista para que no cargue dos veces el mismo producto
                        lista.clear();
                        comprar(scanContent, precio);
                        cargar();
                    }else {
                        Toast.makeText(getActivity(), "Este producto ya se encuentra en la lista", Toast.LENGTH_LONG).show();
                        cargar();
                    }
                }else{
                    productos.setId(scanContent); //me fijo si el usuario no dio de alta en tabla provisoria
                    productos.producto_no_encontrado();
                    precio = productos.getPrecio();
                    if (precio !=0){ //si dio de alta puede comprar el producto
                        comprar(scanContent, precio);
                        cargar();
                    }else{//de lo contrario se da de alta en la tabla provisoria.
                        productonoencontrado(id_supermercado, scanContent);
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
    public void comprar(String scanContent, double precio_producto){
        double suma_total, total_compras_anterior;
        int acumulador_cantidad_productos, total_de_productos;
        String txt_cantidad, total;
        Compras compras = new Compras(contexto);
        compras.setId(id_compras);
        compras.setTotal_unitario(precio_producto);
        compras.agregar_detalle_compra(scanContent);
        ComparacionProductos(scanContent); //enviar el codigo de barra del producto agregado. Despues de agregar a la tabla
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
    public void cargar(){
        ArrayList<HashMap<String, String>> listado_compras;
        String id_producto, cantidades, montos, neto, medida, nombre, marca, precio_unitario;
        lista.clear();
        Compras compras = new Compras(contexto);
        compras.maximo_compra(); //obtener el id y el supermercado donde compra
        id_compras = compras.getId();
        id_supermercado = compras.getSupermercado();
        listado_compras = compras.detalle_compras(id_compras);
        int bucle = listado_compras.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_compras.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                marca = hashmap.get(SEGUNDA_COLUMNA);
                precio_unitario = hashmap.get(TERCERA_COLUMNA);
                cantidades = hashmap.get(CUARTA_COLUMNA);
                montos = hashmap.get(QUINTA_COLUMNA);
                id_producto = hashmap.get(SEXTA_COLUMNA);
                neto = hashmap.get(SEPTIMA_COLUMNA);
                medida = hashmap.get(OCTAVA_COLUMNA);

                HashMap<String, String> temporal = new HashMap<String, String>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, marca);
                temporal.put(TERCERA_COLUMNA, precio_unitario);
                temporal.put(CUARTA_COLUMNA, cantidades);
                temporal.put(QUINTA_COLUMNA, montos);
                temporal.put(SEXTA_COLUMNA, id_producto);
                temporal.put(SEPTIMA_COLUMNA, neto);
                temporal.put(OCTAVA_COLUMNA, medida);
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
    public TextView actualizar_txt_total()
    {
        return txt_total;
    }
    public TextView actualizar_txt_cantidad()
    {
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
                startActivity(new Intent(getActivity(), FragmentComprasBorrarLista.class));
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
    public String retornarcantidad()
    {
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
        compras.maximo_compra();
        id_compras = compras.getId();
        compras.contar_productos_compra();
        int cantidad = compras.getCant_total_productos();
        String cantidad_productos = String.valueOf(cantidad);
        if (cantidad != 0) {
            compras.setId(id_compras);
            compras.calcular_total_compra();
            double total_compra = compras.getTotal();
            String total = String.valueOf(total_compra);
            txt_total.setText(total);
            cantidad_producto.setText(cantidad_productos);
        }else {
            txt_total.setText("0");
            cantidad_producto.setText("0");
        }
    }

    public void ComparacionProductos(String CodBarra) {
        String id;
        ArrayList<HashMap<String, String>> listado_despensa;
        ArrayList<HashMap<String, String>> listado_producto;
        Despensa despensa = new Despensa(contexto);
        Productos productos = new Productos(contexto);
        listado_despensa = despensa.detalle_inventario();//recorro despensa
        int bucle = listado_despensa.size();
        if (bucle != 0){
            HashMap<String, String> hashmap = listado_despensa.get(0);
            id = hashmap.get(ConstantesColumnasDespensa.TERCERA_COLUMNA);
            for(int i=0; i<bucle; i++) {
                if (id.equals(CodBarra)) { //pregunto si hay un id igual al codigo de barra
                    despensa.setId_producto(CodBarra);
                    despensa.borrar_item();//borra por el codigo de barra.
                    break;
                } else {//entonces no esta por codigo de barra hay que buscar por nombre.
                    productos.setId(CodBarra);
                    listado_producto = productos.cargar_producto_especifico();
                    int bucle_producto = listado_despensa.size();
                    if (bucle_producto != 0) {
                        HashMap<String, String> hashmapa = listado_despensa.get(0);
                        String idnombre = hashmapa.get(ConstantesColumnasDespensa.TERCERA_COLUMNA);//tengo el id del nombre del producto
                        despensa.setId_producto(idnombre);
                        despensa.borrar_item();//borra por el id
                        break;
                    }
                }
            }
        }
    }
}
