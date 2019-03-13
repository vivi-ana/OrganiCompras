package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static acostapeter.com.organicompras.ConstantesFilaCompras.SEXTA_COLUMNA;

public class Tabs extends AppCompatActivity {
    int id_compra = 0;
    static boolean edicion = false;
    FragmentCompras Lista = new FragmentCompras();
    Compras compras = new Compras(getBaseContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Compras"));
        tabLayout.addTab(tabLayout.newTab().setText("Despensa"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        //envia que fragment se quiere mostrar
        final TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            //para saber que fragment se selecciono
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Compras compras = new Compras(getBaseContext());
        ListView listView = Lista.getLista();
        boolean guardar = Lista.getGuardar();
        compras.maximo_compra(); //obtener el id
        id_compra = compras.getId();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (listView != null) {
                int count = listView.getAdapter().getCount();
                if (!guardar) {
                    if (count >=1){
                        mensaje_guardar();
                    }else { //si no se guardo y encima no hay elementos en la lista. _El usuario solo quiere salir
                        compras.setId(id_compra); //se envia el id de la compra para borrar
                        compras.borrar_compra();
                        finish();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }//borrar lo creado
                }
                else { //guardar es verdadero
                    if (count >=1) {
                        actualizar_compra();
                        finish(); // Ya se guardo esta ok solo se sale
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                    else{//se guardo pero borro un articulo se dejo la lista vacia. NO se puede guardar lista vacia.
                        mesaje_vacio();
                    }
                }
            }else{
                compras.setId(id_compra); //se envia el id de la compra para borrar
                compras.borrar_compra();
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i); //borrar la compra y salir sin mensaje.
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private void mensaje_guardar() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Tabs.this);
        alertBuilder.setTitle(R.string.salirGuardar);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.salirGuardarMsj);
        alertBuilder.setNeutralButton("Seguir comprando", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            //Si acepta el mensaje
            public void onClick(DialogInterface dialog, int which) {//decir que se guardo y salir del activity.
                actualizar_compra();
                Toast.makeText(Tabs.this, "La lista ha sido guardada", Toast.LENGTH_SHORT).show();
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //Si cancela el mensaje tiene que borrar y salir del activity.
                borrar();
                finish();
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void actualizar_compra(){
        String txttotal = Lista.retornartotal();
        String cantidad_compra = Lista.retornarcantidad();
        int cantidad = Integer.parseInt(cantidad_compra);
        double total = Double.parseDouble(txttotal);
        double total_unitario = total / Double.parseDouble(cantidad_compra);
        compras.setId(id_compra);
        compras.setCantidad(cantidad);
        compras.setTotal(total);
        compras.setTotal_unitario(total_unitario);
        compras.actualizar_compra();//con productos en la lista
    }
    private void mesaje_vacio() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Tabs.this);
        alertBuilder.setTitle(R.string.salirVacio);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.msjSalirVacio);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {//Si acepta el mensaje
            @Override
            public void onClick(DialogInterface dialog, int which) {  //Si se queda a agregar productos. se cierra el mensaje.
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {  //Si cancela el mensaje tiene que borrar y salir.
            borrar();
            finish();
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void borrar(){
        FragmentComprasDetalles editar = new FragmentComprasDetalles();
        edicion = editar.getEditar();
        compras.setId(id_compra);
        if (!edicion){ //si no viene de la edicion.
            compras.borrar_compra();
            compras.borrar_detalle_compra();
        }
        else {
            int j =0;
            ArrayList<HashMap<String, String>> listado_compras;
            String datos_no_editados[][]= Lista.getDatosnoeditados();
            listado_compras = compras.detalle_compras(id_compra);
            int cantidad_filas = datos_no_editados.length;
            int bucle = listado_compras.size(), nueva_cantidad;
            if (bucle != 0){
                for(int i=0; i<bucle; i++) {
                    if (j<=cantidad_filas-1){
                        HashMap<String, String> hashmap = listado_compras.get(i);
                        String codigo_nuevo = datos_no_editados[j][0], codigo_viejo =  hashmap.get(SEXTA_COLUMNA); //traigo los id de la matriz y de la BD
                        if (codigo_nuevo.equals(codigo_viejo)) { // comparo los codigos
                            nueva_cantidad = Integer.parseInt(datos_no_editados[j][1]);
                            compras.setCantidad(nueva_cantidad);
                            compras.cantidad_producto_editada(codigo_nuevo); //datos necesarios cantidad, id_compra, codigo
                            j++;
                        }
                    }
                }
            }
            compras.borrar_compra();//borrar todos los detalles y las compras para restaurar
            compras.borrar_detalle_compra();
            String mdetalles[][] = Lista.getMatriz_detalles();
            String mcompras[][] = Lista.getMatriz_compras();
            for (int i=0; i<=cantidad_filas-1;i++){
                String detalle = mdetalles[i][0];
                int id_detalle = Integer.parseInt(detalle);
                String id_producto = mdetalles[i][2];
                String cant = mdetalles[i][3];
                int cantidad = Integer.parseInt(cant);
                String nuevo_monto = mdetalles[i][4];
                double monto = Double.parseDouble(nuevo_monto);
                compras.setCantidad(cantidad);
                compras.resetear_detalle(id_detalle, id_producto, monto);
            }
            String id_super= mcompras[0][1];
            int id_supermercado = Integer.parseInt(id_super);
            String fecha = mcompras[0][2];
            String max= mcompras[0][3];
            int maximo = Integer.parseInt(max);
            String cant_prod = mcompras[0][4];
            int cantidad = Integer.parseInt(cant_prod);
            String total = mcompras[0][5];
            double total_compra = Double.parseDouble(total);
            String total_unitario= mcompras[0][6];
            double total_unitario_compra = Double.parseDouble(total_unitario);
            compras.setSupermercado(id_supermercado);
            compras.setMax(maximo);
            compras.setCantidad(cantidad);
            compras.setTotal(total_compra);
            compras.setFecha(fecha);
            compras.setTotal_unitario(total_unitario_compra);
            compras.resetear_compras();
        }
        finish();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
