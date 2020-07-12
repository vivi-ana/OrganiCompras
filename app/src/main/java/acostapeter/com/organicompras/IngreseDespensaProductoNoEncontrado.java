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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngreseDespensaProductoNoEncontrado extends DialogFragment {
    Button BotonAceptar, BotonCancelar;
    EditText Enombre, Edescripcion, Emarca, Eneto;
    Spinner Smedida;
    Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
    String medida_producto = "", codigo = "", dato ="";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_despensa_ingrese_producto, container);
        BotonAceptar = rootview.findViewById(R.id.btnAceptar);
        BotonCancelar = rootview.findViewById(R.id.btnCancelar);
        Enombre = rootview.findViewById(R.id.editNombre);
        Edescripcion = rootview.findViewById(R.id.editDescrip);
        Emarca = rootview.findViewById(R.id.editMarca);
        Eneto = rootview.findViewById(R.id.editNeto);
        Smedida = rootview.findViewById(R.id.prodMedida);
        Enombre.requestFocus();
        if(getArguments()!=null) {
            String dato_despensa = getArguments().getString("dato");
            Matcher validacion = pn.matcher(dato_despensa);
            boolean val = validacion.matches();
            if(val){
                Enombre.setText(dato_despensa);
            }else{
                codigo = dato_despensa;
            }
        }
        ArrayAdapter<CharSequence> spinneradapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.medidas, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Smedida.setAdapter(spinneradapter);
        Smedida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                medida_producto = Smedida.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                Matcher prodn = pn.matcher(nombre_producto);
                double neto = 0;
                if (!neto_producto.equals("")) { neto = Double.parseDouble(neto_producto);}
                boolean bs = prodn.matches();
                if (nombre_producto.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                    Enombre.requestFocus();
                }else if(!bs){
                    Enombre.setError("El producto no debe contener numeros");
                    Enombre.requestFocus();
                }else if(nombre_producto.length() <3){
                    Enombre.setError("Nombre muy corto");
                    Enombre.requestFocus();
                }else if(!descripcion_producto.matches("")& descripcion_producto.length() <3) {
                        Edescripcion.setError("Descripcion muy corta");
                        Edescripcion.requestFocus();
                }else if(!marca_producto.matches("") & marca_producto.length()<3) {
                        Emarca.setError("Nombre de marca muy corto");
                        Emarca.requestFocus();
                }else if(!neto_producto.matches("")& medida_producto.matches("")){
                    ((TextView)Smedida.getSelectedView()).setError("Debe colocar una medida");
                    Smedida.requestFocus();
                }else if(neto_producto.matches("")& !medida_producto.matches("")){
                    Eneto.setError("Debe colocar un neto");
                    Eneto.requestFocus();
                }else if(!neto_producto.matches("") & neto < 0) {
                        Eneto.setError("El neto debe ser mayor a 0");
                        Eneto.requestFocus();
                } else {
                    String nom = Enombre.getText().toString();
                    Productos productos = new Productos(getActivity());
                    nom = nom.substring(0,1).toUpperCase() + nom.substring(1).toLowerCase();
                    productos.setNombre(nom);
                    productos.setDescripcion(descripcion_producto);
                    productos.setMarca(marca_producto);
                    productos.setMedida(medida_producto);
                    productos.setNeto(neto);
                    productos.agregar_producto();
                    productos.maximo_producto();
                    int maximo = productos.getId_producto();
                    productos.setId_producto(maximo);
                    if(!codigo.equals("")) {
                        dato = codigo;
                    }else{
                        dato = String.valueOf(maximo);
                    }
                    boolean lista = productos.producto_no_encontrado_despensa(dato);//hay que verificar que no se haya dado de alta antes.
                    if (!lista) { //si lista es falsa es porque es un producto nuevo que hay que ingresarlo.
                        productos.agregar_despensa_producto_no_encontrado(dato);
                    }
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
