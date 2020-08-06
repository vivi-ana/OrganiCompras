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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngreseCompraProductoNoEncontrado extends DialogFragment {
    Button Botonaceptar, Botoncancelar;
    EditText nombre, precio, descripcion, neto, marca;
    Spinner medida;
    DecimalFormat df = new DecimalFormat("0.00");
    double precio_producto;
    String medida_producto = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_compras_ingrese_producto, container);
        Botonaceptar = rootview.findViewById(R.id.btnAceptar);
        Botoncancelar = rootview.findViewById(R.id.btnCancelar);
        nombre = rootview.findViewById(R.id.editNombre);
        precio = rootview.findViewById(R.id.editPrecio);
        descripcion = rootview.findViewById(R.id.editDescrip);
        marca = rootview.findViewById(R.id.editMarca);
        neto = rootview.findViewById(R.id.editNeto);
        medida = rootview.findViewById(R.id.ProdMedida);
        nombre.requestFocus();
        final ArrayAdapter<CharSequence> spinneradapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.medidas, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medida.setAdapter(spinneradapter);
        medida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                medida_producto = medida.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        Botonaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre_producto, descripcion_producto, marca_producto, neto_producto, preciopro  ="", producto_precio;
                nombre_producto = nombre.getText().toString();
                descripcion_producto = descripcion.getText().toString();
                marca_producto = marca.getText().toString();
                neto_producto = neto.getText().toString();
                Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
                Matcher prodn = pn.matcher(nombre_producto);
                boolean bs = prodn.matches();
                String pre = precio.getText().toString();
                double neto_prod = 0;
                if (!neto_producto.equals("")) { neto_prod = Double.parseDouble(neto_producto);}
                producto_precio = precio.getText().toString();
                if (!producto_precio.equals("")){
                    precio_producto = Double.parseDouble(precio.getText().toString());
                    preciopro = (df.format(precio_producto)).replace(",", ".");
                }
                if (nombre_producto.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                    nombre.requestFocus();
                }else if(!bs){
                    nombre.setError("El producto no debe contener numeros");
                    nombre.requestFocus();
                }else if(nombre_producto.length() <3){
                    nombre.setError("Nombre muy corto");
                    nombre.requestFocus();
                }else if(!descripcion_producto.matches("") & descripcion_producto.length() <3){
                        descripcion.setError("Descripcion muy corta");
                        descripcion.requestFocus();
                }else if(!marca_producto.matches("") & marca_producto.length() <3 ) {
                        marca.setError("Nombre de marca muy corto");
                        marca.requestFocus();
                }else if(!neto_producto.matches("") & neto_prod <0) {
                    neto.setError("El neto debe ser mayor a 0");
                    neto.requestFocus();
                }else if(!neto_producto.matches("")& medida_producto.matches("")){
                    ((TextView)medida.getSelectedView()).setError("Debe colocar una medida");
                    medida.requestFocus();
                }else if(neto_producto.matches("")& !medida_producto.matches("")){
                    neto.setError("Debe colocar un neto");
                    neto.requestFocus();
                } else if (pre.matches("")){
                        Toast.makeText(getActivity(), "Debe ingresar un precio", Toast.LENGTH_SHORT).show();
                        precio.requestFocus();
                } else if (precio.getText().length() < 3) { //fijarse como poner porque puede haber algo de 9 pesos o de 15 algo exacto y el usuario no puede poner 15.00
                        precio.setError("Mínimo 3 dígitos");
                        precio.requestFocus();
                } else if (precio.getText().length() >= 6) {
                        precio.setError("Máximo de 5 dígitos");
                        precio.requestFocus();
                } else if (Double.parseDouble(preciopro) < 0) {
                        precio.setError("El valor ingresado debe ser mayor a 0");
                        precio.requestFocus();
                } else {//validado
                    if(getArguments()!=null) {
                        String id = getArguments().getString("idsuper");
                        String codigo = getArguments().getString("codigo");
                        if (id != null) {
                            int id_super = Integer.parseInt(id);
                            Productos producto_no_encontrado = new Productos(getActivity());
                            nombre_producto = nombre_producto.substring(0,1).toUpperCase() + nombre_producto.substring(1).toLowerCase();
                            producto_no_encontrado.setNombre(nombre_producto);
                            if(!descripcion_producto.equals("")) descripcion_producto = descripcion_producto.substring(0,1).toUpperCase() + descripcion_producto.substring(1).toLowerCase();
                            producto_no_encontrado.setDescripcion(descripcion_producto);
                            producto_no_encontrado.setMarca(marca_producto);
                            producto_no_encontrado.setNeto(neto_prod);
                            if(!medida_producto.equals("")) medida_producto = medida_producto.substring(0,1).toUpperCase() + medida_producto.substring(1).toLowerCase();
                            producto_no_encontrado.setMedida(medida_producto);
                            producto_no_encontrado.agregar_producto();
                            producto_no_encontrado.maximo_producto();//traer el id del producto recien agregado
                            int id_producto = producto_no_encontrado.getId_producto();
                            producto_no_encontrado.setCodigo(codigo);
                            producto_no_encontrado.setId_supermercado(id_super);
                            producto_no_encontrado.setId_producto(id_producto);
                            producto_no_encontrado.setPrecio(precio_producto);
                            producto_no_encontrado.agregar_compras_producto_no_encontrado();
                            dismiss();
                            Toast.makeText(getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
                            String p = precio.getText().toString();
                            double preun = Double.parseDouble(p);
                            String precioun = (df.format(preun)).replace(",", ".");
                            FragmentCompras compras = new FragmentCompras();
                            compras.comprar(id_producto, Double.parseDouble(precioun));
                            compras.cargar();
                            compras.llenar();
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
