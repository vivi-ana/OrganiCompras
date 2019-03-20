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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngreseDespensaProductoNoEncontrado extends DialogFragment {
    Button BotonAceptar, BotonCancelar;
    EditText Enombre;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_despensa_ingrese_producto, container);
        BotonAceptar = rootview.findViewById(R.id.btnAceptar);
        BotonCancelar = rootview.findViewById(R.id.btnCancelar);
        Enombre = rootview.findViewById(R.id.editNombre);
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
                        productos.agregar_despensa_producto_no_encontrado();
                        dismiss();
                        Toast.makeText(getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
                        MiDespensaActivity midespensa = new MiDespensaActivity();
                        midespensa.almacenar(codigo);
                        midespensa.cargar();
                        midespensa.cantidad_nueva();
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
