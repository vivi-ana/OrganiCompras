package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEPTIMA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasDespensa.SEXTA_COLUMNA;
@SuppressWarnings("all")
public class FragmentDespensa extends android.support.v4.app.Fragment{
    private ArrayList<HashMap<String, String>> lista;
    String marca = "";
    ListView lista_despensa;
    final ArrayList<String> item_borrar = new ArrayList<String>();
    public static boolean cargar = false;
    FragmentDespensaListViewAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_despensa, container, false);
        setHasOptionsMenu(true);
        lista = new ArrayList<HashMap<String, String>>();
        lista_despensa = view.findViewById(R.id.listaDespensa);
        final FragmentDespensaListViewAdapter adapter = new FragmentDespensaListViewAdapter(getActivity(), lista);
        lista_despensa.setAdapter(adapter);
        Button btnborrar = view.findViewById(R.id.btnBorrar);
        cargar();
        btnborrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (lista_despensa.getAdapter().getCount() >= 1){
                item_borrar.clear();
                String [] item = adapter.getMid();
                for (String anItem : item) {
                    if (anItem != null) {
                        item_borrar.add(anItem); //agregar a item_borrar todos los id de los items a borrar
                    }
                }
                mensaje();
            }else{
                Toast.makeText(getActivity(), "No hay productos para eliminar", Toast.LENGTH_SHORT).show();
            }
        }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_despensa_check, menu);
        super.onCreateOptionsMenu(menu, inflater);
        CheckBox checkBox = (CheckBox) menu.findItem(R.id.menucheckbox).getActionView();
        String texto = "Seleccionar todo";
        checkBox.setText(texto);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (lista_despensa.getAdapter().getCount() >= 1){
                    if(b){
                        adapter.selectAll();
                        lista_despensa.setAdapter(adapter);
                    }else{
                        adapter.unselectAll();
                        lista_despensa.setAdapter(adapter);
                    }
                }
            }
        });
    }
    public void mensaje(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(R.string.borrar);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.mensajeBorrarProducto);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int cantidad = item_borrar.size();
                if (cantidad >= 1) {
                    cargar = true;
                    for (int i = 0; i < item_borrar.size(); i++) {
                        Despensa despensa = new Despensa(getActivity());
                        String item = item_borrar.get(i);
                        if (item.startsWith("N")) {
                            despensa.setId_producto(item);
                            despensa.borrar_item();
                            String nuevoitem = item.substring(1);
                            despensa.setId_producto(nuevoitem);
                            despensa.borrar_producto_no_encontrado(); //se borra el nuevo producto de la tabla no producto tambien.
                        } else {
                            despensa.setId_producto(item);
                            despensa.borrar_item();
                        }
                    }
                    lista_despensa.setAdapter(null);
                    FragmentDespensaListViewAdapter adapter = new FragmentDespensaListViewAdapter(getActivity(), lista);
                    lista_despensa.setAdapter(adapter);
                    cargar();
                    item_borrar.clear();
                } else {
                    Toast.makeText(getActivity(), "Debe seleccionar algun producto", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//Si cancela el mensaje tiene que entrar igual a la app.
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void cargar(){
        ArrayList<HashMap<String, String>> listado_despensa;
        String nombre, descripcion, cantidad, marca, neto, medida, id_producto;
        lista.clear();
        Despensa despensa = new Despensa(getActivity());
        listado_despensa = despensa.detalle_inventario();
        int bucle = listado_despensa.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_despensa.get(i);
                nombre = hashmap.get(PRIMERA_COLUMNA);
                descripcion = hashmap.get(SEGUNDA_COLUMNA);
                cantidad = hashmap.get(TERCERA_COLUMNA);
                id_producto = hashmap.get(CUARTA_COLUMNA);
                marca = hashmap.get(QUINTA_COLUMNA);
                neto = hashmap.get(SEXTA_COLUMNA);
                medida = hashmap.get(SEPTIMA_COLUMNA);

                HashMap<String, String> temporal = new HashMap<String, String>();
                temporal.put(PRIMERA_COLUMNA, nombre);
                temporal.put(SEGUNDA_COLUMNA, descripcion);
                temporal.put(TERCERA_COLUMNA, cantidad);
                temporal.put(CUARTA_COLUMNA, id_producto);
                temporal.put(QUINTA_COLUMNA, marca);
                temporal.put(SEXTA_COLUMNA, neto);
                temporal.put(SEPTIMA_COLUMNA, medida);

                lista.add(temporal);
                adapter = new FragmentDespensaListViewAdapter(getActivity(), lista);
                lista_despensa.setAdapter(adapter);
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            cargar(); //vuelve a recargar.
        }
    }
}
