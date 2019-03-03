package acostapeter.com.organicompras;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("all")
public class FragmentCompras extends android.support.v4.app.Fragment implements View.OnClickListener {
    Button scanBtn;
    TextView formatTxt, contentTxt, lblmx, textView;
    static TextView txttotal, cantidadproducto;
    static ListView listado_productos;
    static ArrayList<HashMap<String, String>> lista;
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
        return view;
    }
    @Override
    public void onClick(View view) {

    }
}
