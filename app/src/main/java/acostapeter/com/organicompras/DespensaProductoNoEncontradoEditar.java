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
                String nprod = Enombre.getText().toString();
                Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
                Matcher prodn = pn.matcher(nprod);
                boolean bs = prodn.matches();
                if (nprod.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                }else if(!bs){
                    Enombre.setError("El producto no debe contener numeros");
                } else {
                    String nom = Enombre.getText().toString();
                    if(getArguments()!=null) {
                        String codigo = getArguments().getString("codigo");
                        Productos productos = new Productos(getActivity());
                        productos.setId(codigo);
                        productos.setNombre(nom);
                        if (codigo != null) {
                            Despensa despensa = new Despensa(getActivity());
                            ArrayList<HashMap<String, String>> listado_despensa;
                            String id_producto, nombre;
                            int codigo_producto = codigo.length();
                            if (codigo_producto !=13) {
                                listado_despensa = despensa.detalle_inventario();
                                int bucle = listado_despensa.size();
                                if (bucle != 0) {
                                    for (int i = 0; i < bucle; i++) {//cuando el id no sea EAN 13 el id tiene que ser distinto si el nombre es igual.
                                        HashMap<String, String> hashmap = listado_despensa.get(i);
                                        nombre = hashmap.get(ConstantesDespensa.PRIMERA_COLUMNA);
                                        id_producto = hashmap.get(ConstantesDespensa.TERCERA_COLUMNA);//hay que verificar que no se haya dado de alta antes.
                                        if (nombre != null) {
                                            String nuevo_codigo = "N"+codigo;
                                            if (nombre.equals(nom) && !nuevo_codigo.equals(id_producto)) { //se verifica que no este escribiendo un producto ya ingresado con distinto id
                                                Toast.makeText(getActivity(), "Este producto ya fue ingresado", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            //si lista es falsa es porque es un producto nuevo que hay que ingresarlo.
                                        }
                                    }
                                }
                            }
                            productos.editar_producto_no_encontrado();
                            dismiss();
                            Toast.makeText(getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
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

}
