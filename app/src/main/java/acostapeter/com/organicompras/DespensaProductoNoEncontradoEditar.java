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

public class DespensaProductoNoEncontradoEditar extends DialogFragment {
    Button BotonAceptar, BotonCancelar;
    EditText Enombre, Edescrip, Emarca, Eneto;
    Spinner Smedida;
    String producto_descripcion,producto_marca,producto_neto,producto_medida,nombre_producto;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_despensa_producto_no_encontrado_editar, container);
        BotonAceptar = rootview.findViewById(R.id.btnAceptar);
        BotonCancelar = rootview.findViewById(R.id.btnCancelar);
        Enombre = rootview.findViewById(R.id.editNombre);
        Edescrip = rootview.findViewById(R.id.editDescrip);
        Emarca = rootview.findViewById(R.id.editMarca);
        Eneto = rootview.findViewById(R.id.editNeto);
        Smedida = rootview.findViewById(R.id.prodMedida);
        Enombre.requestFocus();
        ArrayAdapter<CharSequence> spinneradapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.medidas, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Smedida.setAdapter(spinneradapter);

        if (getArguments() != null) {
            nombre_producto = getArguments().getString("nombre");
            producto_descripcion = getArguments().getString("descripcion");
            producto_marca = getArguments().getString("marca");
            producto_neto = getArguments().getString("neto");
            producto_medida = getArguments().getString("medida");
            Enombre.setText(nombre_producto);
            Edescrip.setText(producto_descripcion);
            Emarca.setText(producto_marca);
            Eneto.setText(producto_neto);
            if (producto_medida != null) {
                int spinnerPosition = spinneradapter.getPosition(producto_medida);
                Smedida.setSelection(spinnerPosition);
            }
        }
        Smedida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                producto_medida = Smedida.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        BotonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nprod, descripcion_producto, marca_producto, neto_producto;
                nprod = Enombre.getText().toString();
                descripcion_producto = Edescrip.getText().toString();
                marca_producto = Emarca.getText().toString();
                neto_producto = Eneto.getText().toString();
                Pattern pn = Pattern.compile("^[a-zA-Z ]+$");
                Matcher prodn = pn.matcher(nprod), prodD = pn.matcher(descripcion_producto);
                boolean bs = prodn.matches(), dp = prodD.matches();
                double neto_prod = 0;
                if (!neto_producto.equals("")) { neto_prod = Double.parseDouble(neto_producto);}
                if (nprod.matches("")) {
                    Toast.makeText(getActivity(), R.string.msjProd, Toast.LENGTH_SHORT).show();
                }else if(!bs){
                    Enombre.setError("El producto no debe contener numeros");
                    Enombre.requestFocus();
                }else if(nombre_producto.length() <3){
                    Enombre.setError("Nombre muy corto");
                    Enombre.requestFocus();
                }else if(!descripcion_producto.matches("") & !(dp)){//si la descripcion no esta vacia
                        Edescrip.setError("El producto no debe contener numeros");//se verifica que no tenga numeros
                        Edescrip.requestFocus();
                }else if(!descripcion_producto.matches("") & descripcion_producto.length() <3){
                        Edescrip.setError("Descripcion muy corta");
                        Edescrip.requestFocus();
                }else if(!marca_producto.matches("") & marca_producto.length() <3 ) {
                        Emarca.setError("Nombre de marca muy corto");
                        Emarca.requestFocus();
                }else if(!neto_producto.matches("")& producto_medida.matches("")){
                    ((TextView)Smedida.getSelectedView()).setError("Debe colocar una medida");
                    Smedida.requestFocus();
                }else if(neto_producto.matches("")& !producto_medida.matches("")){
                    Eneto.setError("Debe colocar un neto");
                    Eneto.requestFocus();
                }else if(!neto_producto.matches("") & neto_prod < 0) {
                        Eneto.setError("El neto debe ser mayor a 0");
                        Eneto.requestFocus();
                } else {
                    if(getArguments()!=null) {
                        String producto_id = getArguments().getString("id");
                        Productos productos = new Productos(getActivity());
                        if (producto_id != null) {
                            int id;
                            id = Integer.parseInt(producto_id);
                            nprod = nprod.substring(0,1).toUpperCase() + nprod.substring(1).toLowerCase();
                            productos.setNombre(nprod);
                            productos.setDescripcion(descripcion_producto);
                            productos.setMarca(marca_producto);
                            productos.setNeto(neto_prod);
                            productos.setId_producto(id);
                            productos.setMedida(producto_medida);
                            productos.actualizar_producto();
                                /*
                                 Despensa despensa = new Despensa(getActivity());
                            ArrayList<HashMap<String, String>> listado_despensa, lista_producto_no_encontrado, lista_producto;

                                 String id_producto, nombre;
                                listado_despensa = despensa.detalle_inventario();

                                lista_producto_no_encontrado = productos.lista_producto_no_encontrado_despensa();

                                lista_producto = productos.producto_por_nombre();
                                int bucle = listado_despensa.size();
                                int bucle_producto = lista_producto_no_encontrado.size();
                                bucle_lista_productos = lista_producto.size();
                                if (bucle != 0) {
                                    for (int i = 0; i < bucle; i++) {//cuando el id no sea EAN 13 el id tiene que ser distinto si el nombre es igual.
                                        HashMap<String, String> hashmap = listado_despensa.get(i);
                                        nombre = hashmap.get(ConstantesColumnasDespensa.PRIMERA_COLUMNA);
                                        id_producto = hashmap.get(ConstantesColumnasDespensa.TERCERA_COLUMNA);//hay que verificar que no se haya dado de alta antes.
                                        if (nombre != null) {
                                            if (nombre.equals(nombre_producto) && !nuevo_codigo.equals(id_producto)) { //se verifica que no este escribiendo un producto ya ingresado con distinto id
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
                                            if (nombre.equals(nombre_producto) && !codigo.equals(id_producto)) {
                                                Toast.makeText(getActivity(), "Este producto ya existe", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    }
                                }
                                */
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
    @Override
    public void onResume(){
        super.onResume();
        Enombre.requestFocus();
    }
}
