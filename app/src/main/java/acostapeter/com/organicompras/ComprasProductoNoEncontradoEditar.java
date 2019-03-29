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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static acostapeter.com.organicompras.ConstantesColumnasCompras.CUARTA_COLUMNA;

public class ComprasProductoNoEncontradoEditar extends DialogFragment {
    Button BtnAaceptar, BtnCancelar;
    EditText nombre, precio;
    DecimalFormat df = new DecimalFormat("0.00");
    double precioproducto,  preciounitario;
    String id, codigo, nombre_producto, precio_unitario = "", precio_producto = "";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_compras_producto_no_encontrado_editar, container);
        BtnAaceptar = rootview.findViewById(R.id.btnAceptar);
        BtnCancelar = rootview.findViewById(R.id.btnCancelar);
        nombre = rootview.findViewById(R.id.editNombre);
        precio = rootview.findViewById(R.id.editPrecio);
        if (getArguments() != null) {
            id = getArguments().getString("idsuper");
            codigo = getArguments().getString("codigo");
            nombre_producto = getArguments().getString("nombre");
            precio_producto = getArguments().getString("precio");
            if (precio_producto!=null) {
                preciounitario = Double.parseDouble(precio_producto);
                precio_unitario = (df.format(preciounitario)).replace(",", ".");
                nombre.setText(nombre_producto);
                precio.setText(precio_unitario);
            }
        }
        BtnAaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nprod = nombre.getText().toString();
                Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
                Matcher prodn = pn.matcher(nprod);
                boolean bs = prodn.matches();
                String pre = precio.getText().toString();
                if (nprod.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                }else if(!bs){
                    nombre.setError("El producto no debe contener numeros");
                } else {
                    if (pre.matches("")) {
                        Toast.makeText(getActivity(), "Debe ingresar un precio", Toast.LENGTH_SHORT).show();
                    } else {
                        precioproducto = Double.parseDouble(precio.getText().toString());
                        String preciopro = (df.format(precioproducto)).replace(",", ".");
                        if (preciopro.length() < 3) {
                            precio.setError("Mínimo 3 dígitos");
                        } else if (preciopro.length() > 8) {
                            precio.setError("Máximo de 5 dígitos"); //5 digitos + 3 (el punto y 2 decimales mas)
                        } else if (Double.parseDouble(preciopro) <= 1) {
                            precio.setError("El valor ingresado debe ser mayor a 1");
                        } else {//validado si esta validado
                            Productos productos = new Productos(getActivity());
                            productos.setNombre(nprod);
                            double precio = Double.parseDouble(preciopro);
                            productos.setPrecio(precio);
                            int id_supermercado = Integer.parseInt(id);
                            productos.setId_supermercado(id_supermercado);
                            productos.setId(codigo);
                            productos.actualizar_producto_no_encontrado();
                            Compras compras = new Compras(getActivity());
                            compras.maximo_compra(); //obtener el id de compra
                            int id_compras = compras.getId();
                            compras.setSupermercado(id_supermercado);
                            ArrayList<HashMap<String, String>> listado_compras;
                            listado_compras = compras.detalle_compras_editada(codigo);
                            int bucle = listado_compras.size();
                            if (bucle != 0){ //si hay detalles en esa compra posiblemente se necesite cambiar el precio.
                                for(int i=0; i<bucle; i++) {
                                    HashMap<String, String> hashmap= listado_compras.get(i);
                                    String cantidad = hashmap.get(CUARTA_COLUMNA);
                                    if (cantidad != null) {
                                        int cantidad_nueva = Integer.parseInt(cantidad);
                                        double multiplicacion = cantidad_nueva * precioproducto; //subtotal producto
                                        String precio_formateado = (df.format(multiplicacion)).replace(",", ".");
                                        double precio_nuevo = Double.parseDouble(precio_formateado);
                                        compras.setTotal(precio_nuevo);
                                        compras.setId(id_compras);
                                        compras.actualizar_monto_detalle(codigo); //editar el nuevo precio del producto en la compra;
                                    }
                                }
                            }
                            dismiss();
                            Toast.makeText(getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
                            ComprasProductoNoEncontrado lista = new ComprasProductoNoEncontrado();
                            lista.cargar(id_supermercado);
                        }
                    }
                }
            }
        });
        BtnCancelar.setOnClickListener(new View.OnClickListener() {
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
