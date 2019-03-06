package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import google.zxing.integration.android.IntentIntegrator;
@SuppressWarnings("all")
public class FragmentCompras extends android.support.v4.app.Fragment implements View.OnClickListener {
    Button scanBtn;
    TextView formatTxt, contentTxt, lblmx, textView;
    static TextView txttotal, cantidadproducto;
    static ListView listado_productos;
    static ArrayList<HashMap<String, String>> lista;
    int max = 0;
    String dia = "", nom = "", marca = "", precioun = "", supermercado = "", idcompra = "",seleccion = "", idsupermercado = "", datorecibido = null;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);
        scanBtn = view.findViewById(R.id.scan_button);
        lblmx = view.findViewById(R.id.lbl_maximo);
        formatTxt = view.findViewById(R.id.scan_format);
        contentTxt = view.findViewById(R.id.scan_content);
        txttotal = view.findViewById(R.id.total);
        textView = view.findViewById(R.id.max_compra);
        cantidadproducto = view.findViewById(R.id.cantidad_producto);
        scanBtn.setOnClickListener(this);
        listado_productos = view.findViewById(R.id.lista_productos);
        lista = new ArrayList<HashMap<String, String>>();
        FragmentComprasListViewAdapter adapter = new FragmentComprasListViewAdapter(getActivity(), lista);
        listado_productos.setAdapter(adapter);
        setHasOptionsMenu(true);
        Calendar miCalendario = Calendar.getInstance();
        //obtener el dia del sistema
        Date el_dia = miCalendario.getTime();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dia = simpleDate.format(el_dia);
        mensaje();
        return view;
    }
    public void mensaje(){
        //crear un dialogo donde el usuario seleccione de manera obligatoria donde comprara
        final String[] items = {"Cáceres", "Carrefour", "Chango Más"};
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
                //Si cancela el mensaje tiene que entrar igual a la app.
                startActivity(new Intent(getActivity(), MainActivity.class));
                if (getActivity()!= null){
                    getActivity().finish();
                }
            }
        });
        Dialog dialog = supermercado.create();
        dialog.show();
    }

    private void supermercado_elegido() {
        //se obtiene el maximo que se trae del mensaje
        if (getActivity() != null) {
            max = getActivity().getIntent().getIntExtra("max", 0);
        }
        //se obtiene el id del supermercado a partir del nombre que eligio.
        Supermercado supermercado = new Supermercado(getActivity());
        supermercado.setNombre(seleccion);
        supermercado.eleccion_supermercado();
        int idsuper = supermercado.getId();
        Compras compras = new Compras(getActivity());
        compras.setMax(max); //se envia los datos
        compras.setSupermercado(idsuper);
        compras.setFecha(dia);
        compras.agregar_compra(); //se agrega la compra
        if (max !=0){ //si no ingreso monto el maximo por default es 0
            textView.setVisibility(View.VISIBLE);
            lblmx.setVisibility(View.VISIBLE);
            String maximo_compra = Integer.toString(max);
            textView.setText(maximo_compra);
        }
    }

    //evento del boton scanear
    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
            scanIntegrator.initiateScan();
        }
    }
    //cada fragment tiene un menu distinto
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_compras, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
