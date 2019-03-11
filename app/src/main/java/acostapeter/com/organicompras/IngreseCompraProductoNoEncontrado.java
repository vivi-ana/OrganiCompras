package acostapeter.com.organicompras;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngreseCompraProductoNoEncontrado extends DialogFragment {
    Button Botonaceptar, Botoncancelar;
    EditText nombre, precio;
    DecimalFormat df = new DecimalFormat("0.00");
    double precio_producto;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_compras_ingrese_producto, container);
        Botonaceptar = rootview.findViewById(R.id.btnAceptar);
        Botoncancelar = rootview.findViewById(R.id.btnCancelar);
        nombre = rootview.findViewById(R.id.editNombre);
        precio = rootview.findViewById(R.id.editPrecio);
        Botonaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre_producto = nombre.getText().toString();
                Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
                Matcher prodn = pn.matcher(nombre_producto);
                boolean bs = prodn.matches();
                String pre = precio.getText().toString();
                if (nombre_producto.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                }else if(!bs){
                    nombre.setError("El producto no debe contener numeros");
                } else {
                    if (pre.matches("")) {
                        Toast.makeText(getActivity(), "Debe ingresar un precio", Toast.LENGTH_SHORT).show();
                    } else {
                        precio_producto = Double.parseDouble(precio.getText().toString());
                        String preciopro = (df.format(precio_producto)).replace(",", ".");
                        if (precio.getText().length() < 3) {
                            precio.setError("Mínimo 3 dígitos");
                        } else if (precio.getText().length() >= 6) {
                            precio.setError("Máximo de 5 dígitos");
                        } else if (Double.parseDouble(preciopro) < 0) {
                            precio.setError("El valor ingresado debe ser mayor a 0");
                        } else {//validado
                            if(getArguments()!=null) {
                                String id = getArguments().getString("idsuper");
                                String codigo = getArguments().getString("codigo");
                                if (id != null) {
                                    int id_super = Integer.parseInt(id);
                                    Productos producto_no_encontrado = new Productos(getActivity());
                                    producto_no_encontrado.setId(codigo);
                                    producto_no_encontrado.setId_supermercado(id_super);
                                    producto_no_encontrado.setNombre(nombre_producto);
                                    producto_no_encontrado.setPrecio(precio_producto);
                                    producto_no_encontrado.agregar_compras_producto_no_encontrado();
                                    dismiss();
                                    Toast.makeText(getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
                                    String p = precio.getText().toString();
                                    double preun = Double.parseDouble(p);
                                    String precioun = (df.format(preun)).replace(",", ".");
                                    FragmentCompras compras = new FragmentCompras();
                                    compras.comprar(codigo, Double.parseDouble(precioun));
                                    compras.cargar();
                                }
                            }
                        }
                    }
                }
            }
        });
        Botoncancelar.setOnClickListener(new View.OnClickListener() {
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
