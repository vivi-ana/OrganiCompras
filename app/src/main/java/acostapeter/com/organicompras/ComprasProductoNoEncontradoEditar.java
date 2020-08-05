package acostapeter.com.organicompras;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static acostapeter.com.organicompras.ConstantesColumnasCompras.QUINTA_COLUMNA;

public class ComprasProductoNoEncontradoEditar extends DialogFragment {
    Button BtnAaceptar, BtnCancelar;
    EditText nombre, precio, descripcion, marca, neto;
    Spinner  medida;
    DecimalFormat df = new DecimalFormat("0.00");
    double precioproducto,  preciounitario;
    String id, nombre_producto, producto_codigo = "", producto_neto = "", producto_descripcion = "", producto_marca ="", producto_medida ="", producto_id = "", precio_unitario = "", precio_producto = "";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_compras_producto_no_encontrado_editar, container);
        BtnAaceptar = rootview.findViewById(R.id.btnAceptar);
        BtnCancelar = rootview.findViewById(R.id.btnCancelar);
        nombre = rootview.findViewById(R.id.editNombre);
        descripcion = rootview.findViewById(R.id.editDescrip);
        marca = rootview.findViewById(R.id.editMarca);
        neto = rootview.findViewById(R.id.editNeto);
        medida =rootview.findViewById(R.id.ProdMedida);
        ArrayAdapter<CharSequence> spinneradapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.medidas, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medida.setAdapter(spinneradapter);
        precio = rootview.findViewById(R.id.editPrecio);
        if (getArguments() != null) {
            id = getArguments().getString("idsuper");
            nombre_producto = getArguments().getString("nombre");
            producto_descripcion = getArguments().getString("descrip");
            producto_marca = getArguments().getString("marca");
            producto_neto = getArguments().getString("neto");
            producto_medida = getArguments().getString("medida");
            precio_producto = getArguments().getString("precio");
            producto_id = getArguments().getString("id");
            producto_codigo = getArguments().getString("codigo");
            if (precio_producto!=null) {
                preciounitario = Double.parseDouble(precio_producto);
                precio_unitario = (df.format(preciounitario)).replace(",", ".");
                nombre.setText(nombre_producto);
                descripcion.setText(producto_descripcion);
                marca.setText(producto_marca);
                neto.setText(producto_neto);
                precio.setText(precio_unitario);
                if (producto_medida != null) {
                    int spinnerPosition = spinneradapter.getPosition(producto_medida);
                    medida.setSelection(spinnerPosition);
                }
            }
        }
        medida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                producto_medida = medida.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });
        BtnAaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nprod, descripcion_producto, marca_producto, neto_producto;
                nprod = nombre.getText().toString();
                descripcion_producto = descripcion.getText().toString();
                marca_producto = marca.getText().toString();
                neto_producto = neto.getText().toString();
                Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
                Matcher prodn = pn.matcher(nprod), prodD = pn.matcher(descripcion_producto);
                boolean bs = prodn.matches(), dp = prodD.matches();
                String pre = precio.getText().toString();
                double neto_prod = 0;
                if (!neto_producto.equals("")) { neto_prod = Double.parseDouble(neto_producto);}
                if (nprod.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                    nombre.requestFocus();
                }else if(!bs){
                    nombre.setError("El producto no debe contener numeros");
                    nombre.requestFocus();
                }else if(nombre_producto.length() <3){
                    nombre.setError("Nombre muy corto");
                    nombre.requestFocus();
                }else if(!descripcion_producto.matches("") & !(dp)){//si la descripcion no esta vacia
                        descripcion.setError("El producto no debe contener numeros");//se verifica que no tenga numeros
                        descripcion.requestFocus();
                }else if(!descripcion_producto.matches("") & descripcion_producto.length() <3){
                        descripcion.setError("Descripcion muy corta");
                        descripcion.requestFocus();
                }else if(!marca_producto.matches("") & marca_producto.length() <3 ) {
                        marca.setError("Nombre de marca muy corto");
                        marca.requestFocus();
                }else if(!neto_producto.matches("") & neto_prod < 0) {
                        neto.setError("El neto debe ser mayor a 0");
                        neto.requestFocus();
                }else if(!neto_producto.matches("")& producto_medida.matches("")){
                    ((TextView)medida.getSelectedView()).setError("Debe colocar una medida");
                    medida.requestFocus();
                }else if(neto_producto.matches("")& !producto_medida.matches("")){
                    neto.setError("Debe colocar un neto");
                    neto.requestFocus();
                } else {
                    if (pre.matches("")) {
                        Toast.makeText(getActivity(), "Debe ingresar un precio", Toast.LENGTH_SHORT).show();
                        precio.requestFocus();
                    } else {
                        precioproducto = Double.parseDouble(precio.getText().toString());
                        String preciopro = (df.format(precioproducto)).replace(",", ".");
                        if (preciopro.length() < 3) {
                            precio.setError("Mínimo 3 dígitos");
                            precio.requestFocus();
                        } else if (preciopro.length() > 8) {
                            precio.setError("Máximo de 5 dígitos"); //5 digitos + 3 (el punto y 2 decimales mas)
                            precio.requestFocus();
                        } else if (Double.parseDouble(preciopro) <= 1) {
                            precio.setError("El valor ingresado debe ser mayor a 1");
                            precio.requestFocus();
                        } else {//validado si esta validado
                            Productos productos = new Productos(getActivity());
                            int prod_id = Integer.parseInt(producto_id);
                            productos.setId_producto(prod_id);
                            nprod = nprod.substring(0,1).toUpperCase() + nprod.substring(1).toLowerCase();
                            productos.setNombre(nprod);
                            if (!descripcion_producto.equals("")) descripcion_producto = descripcion_producto.substring(0,1).toUpperCase() + descripcion_producto.substring(1).toLowerCase();
                            productos.setDescripcion(descripcion_producto);
                            if (!marca_producto.equals("")) marca_producto = marca_producto.substring(0,1).toUpperCase() + marca_producto.substring(1).toLowerCase();
                            productos.setMarca(marca_producto);
                            productos.setNeto(neto_prod);
                            productos.setMedida(producto_medida);
                            productos.actualizar_producto();
                            double precio = Double.parseDouble(preciopro);//estos datos son para actualizar la tabla de productos super
                            productos.setPrecio(precio);
                            int id_supermercado = Integer.parseInt(id);
                            productos.setId_supermercado(id_supermercado);
                            productos.setCodigo(producto_codigo);
                            productos.actualizar_producto_no_encontrado();
                            Compras compras = new Compras(getActivity());
                            compras.maximo_compra(); //obtener el id de compra
                            int id_compras = compras.getId();
                            compras.setSupermercado(id_supermercado);
                            ArrayList<HashMap<String, String>> listado_compras;
                            listado_compras = compras.detalle_compras_editada(prod_id);
                            int bucle = listado_compras.size();
                            if (bucle != 0){ //si hay detalles en esa compra posiblemente se necesite cambiar el precio.
                                for(int i=0; i<bucle; i++) {
                                    HashMap<String, String> hashmap= listado_compras.get(i);
                                    String cantidad = hashmap.get(QUINTA_COLUMNA);
                                    if (cantidad != null) {
                                        int cantidad_nueva = Integer.parseInt(cantidad);
                                        double multiplicacion = cantidad_nueva * precioproducto; //subtotal producto
                                        String precio_formateado = (df.format(multiplicacion)).replace(",", ".");
                                        double precio_nuevo = Double.parseDouble(precio_formateado);
                                        compras.setTotal(precio_nuevo);
                                        compras.setId(id_compras);
                                        compras.actualizar_monto_detalle(prod_id); //editar el nuevo precio del producto en la compra;
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
