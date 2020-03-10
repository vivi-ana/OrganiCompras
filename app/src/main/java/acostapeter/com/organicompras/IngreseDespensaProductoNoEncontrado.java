package acostapeter.com.organicompras;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngreseDespensaProductoNoEncontrado extends DialogFragment {
    Button BotonAceptar, BotonCancelar;
    EditText Enombre, Edescripcion, Emarca, Eneto;
    Spinner Smedida;
    Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
    String medida_producto = "", codigo = "";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_despensa_ingrese_producto, container);
        BotonAceptar = rootview.findViewById(R.id.btnAceptar);
        BotonCancelar = rootview.findViewById(R.id.btnCancelar);
        Enombre = rootview.findViewById(R.id.editNombre);
        Edescripcion = rootview.findViewById(R.id.editDescrip);
        Emarca = rootview.findViewById(R.id.editMarca);
        Eneto = rootview.findViewById(R.id.editNeto);
        Smedida = rootview.findViewById(R.id.prodMedida);
        if(getArguments()!=null) {
            String dato = getArguments().getString("dato");
            Matcher validacion = pn.matcher(dato);
            boolean val = validacion.matches();
            if(val){
                Enombre.setText(dato);
            }else{
                codigo = dato;
            }
        }
        ArrayAdapter<CharSequence> spinneradapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.medidas, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Smedida.setAdapter(spinneradapter);
        Smedida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                medida_producto = Smedida.getSelectedItem().toString();
            }
        });
        BotonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre_producto, descripcion_producto, marca_producto, neto_producto;
                nombre_producto = Enombre.getText().toString();
                descripcion_producto = Edescripcion.getText().toString();
                marca_producto = Emarca.getText().toString();
                neto_producto = Eneto.getText().toString();
                Matcher prodn = pn.matcher(nombre_producto), prodD = pn.matcher(descripcion_producto);
                boolean bs = prodn.matches(), dp = prodD.matches();
                if (nombre_producto.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                }else if(!bs){
                    Enombre.setError("El producto no debe contener numeros");
                }else if(nombre_producto.length() <3){
                    Enombre.setError("Nombre muy corto");
                }else if(!descripcion_producto.matches("")){//si la descripcion no esta vacia
                    if(!dp){
                        Edescripcion.setError("El producto no debe contener numeros");//se verifica que no tenga numeros
                    }else if(descripcion_producto.length() <3){
                        Edescripcion.setError("Descripcion muy corta");
                    }
                }else if(!marca_producto.matches("")) {
                    if(marca_producto.length() <3){
                        Emarca.setError("Nombre de marca muy corto");
                    }
                }else if(!neto_producto.matches("")) {
                    if (Double.parseDouble(neto_producto) < 0) {
                        Eneto.setError("El neto debe ser mayor a 0");
                    }
                } else {
                    String nom = Enombre.getText().toString();
                    Productos productos = new Productos(getActivity());
                    if(!codigo.equals("")) {
                        productos.setCodigo(codigo);
                        boolean lista = productos.producto_no_encontrado_despensa();//hay que verificar que no se haya dado de alta antes.
                        if (!lista) { //si lista es falsa es porque es un producto nuevo que hay que ingresarlo.
                            productos.agregar_despensa_producto_no_encontrado();
                        }
                    }
                    productos.setNombre(nom);
                    productos.setDescripcion(descripcion_producto);
                    productos.setMarca(marca_producto);
                    productos.setMedida(medida_producto);
                    double neto = Double.parseDouble(neto_producto);
                    productos.setNeto(neto);
                    productos.agregar_producto();
                    productos.maximo_producto();
                    int maximo = productos.getId_producto();
                    dismiss();
                    Toast.makeText(getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
                    MiDespensaActivity midespensa = new MiDespensaActivity();
                    midespensa.almacenar(maximo);
                    midespensa.cargar();
                    midespensa.cantidad_nueva();
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
