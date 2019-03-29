package acostapeter.com.organicompras;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DespensaProductoNoEncontradoEditar extends DialogFragment {
    Button BotonAceptar, BotonCancelar;
    EditText Enombre;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_despensa_producto_no_encontrado_editar, container);
        BotonAceptar = rootview.findViewById(R.id.btnAceptar);
        BotonCancelar = rootview.findViewById(R.id.btnCancelar);
        Enombre = rootview.findViewById(R.id.editNombre);

        if (getArguments() != null) {
            String nombre_producto = getArguments().getString("nombre");
            Enombre.setText(nombre_producto);

        }
        BotonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom;
                String nprod = Enombre.getText().toString();
                Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
                Matcher prodn = pn.matcher(nprod);
                boolean bs = prodn.matches();
                if (nprod.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                }else if(!bs){
                    Enombre.setError("El producto no debe contener numeros");
                } else {
                    nom = Enombre.getText().toString();
                    if(getArguments()!=null) {
                        String codigo = getArguments().getString("codigo");
                        Productos productos = new Productos(getActivity());
                        if (codigo != null) {
                            Despensa despensa = new Despensa(getActivity());
                            ArrayList<HashMap<String, String>> listado_despensa, lista_producto_no_encontrado, lista_producto;
                            String id_producto, nombre;
                            int bucle_lista_productos;
                            int codigo_producto = codigo.length();
                            if (codigo_producto !=13) {
                                listado_despensa = despensa.detalle_inventario();
                                int bucle = listado_despensa.size();
                                lista_producto_no_encontrado = productos.lista_producto_no_encontrado_despensa();
                                int bucle_producto = lista_producto_no_encontrado.size();
                                productos.setNombre(formato(nom));
                                lista_producto = productos.producto_por_nombre();
                                bucle_lista_productos = lista_producto.size();
                                if (bucle != 0) {
                                    for (int i = 0; i < bucle; i++) {//cuando el id no sea EAN 13 el id tiene que ser distinto si el nombre es igual.
                                        HashMap<String, String> hashmap = listado_despensa.get(i);
                                        nombre = hashmap.get(ConstantesColumnasDespensa.PRIMERA_COLUMNA);
                                        id_producto = hashmap.get(ConstantesColumnasDespensa.TERCERA_COLUMNA);//hay que verificar que no se haya dado de alta antes.
                                        if (nombre != null) {
                                            String nuevo_codigo = "N" + codigo;
                                            if (nombre.equals(nom) && !nuevo_codigo.equals(id_producto)) { //se verifica que no este escribiendo un producto ya ingresado con distinto id
                                                Toast.makeText(getActivity(), "Este producto ya fue ingresado", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    }
                                }
                                if (bucle_lista_productos != 0) { //verificar que no este en la lista de productos
                                   Toast.makeText(getActivity(), "Este producto ya existe", Toast.LENGTH_SHORT).show();
                                   return;
                                }
                                if (bucle_producto != 0) {
                                    for (int i = 0; i < bucle_producto; i++) { //se verifica que no este editando un producto que ya existe en la base de datos de los productos no encontrados
                                        HashMap<String, String> hashmap = lista_producto_no_encontrado.get(i);
                                        id_producto = hashmap.get(ConstantesColumnasDespensa.TERCERA_COLUMNA);
                                        nombre = hashmap.get(ConstantesColumnasDespensa.PRIMERA_COLUMNA);
                                        if (nombre != null) {
                                            if (nombre.equals(nom) && !codigo.equals(id_producto)) {
                                                Toast.makeText(getActivity(), "Este producto ya existe", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                            productos.setId(codigo);
                            productos.setNombre(nom);
                            productos.editar_producto_no_encontrado();
                            dismiss();
                            Toast.makeText(getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
                            DespensaProductoNoEncontrado noEncontrado = new DespensaProductoNoEncontrado();
                            noEncontrado.cargar();
                        }
                    }
                }
            }
        });
        BotonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCancelable(false);
        getDialog().setTitle("Producto no encontrado");
        return rootview;
    }
    public static String formato(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();//La primera letra en mayuscula y las demas en minuscula.
        }
    }
}
