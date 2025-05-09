package acostapeter.com.organicompras;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class IngreseMax extends DialogFragment {
    Button botonAceptar, botonCancelar;
    EditText max;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialogo_ingrese_max, container);
        botonAceptar = rootview.findViewById(R.id.btnAceptar);
        botonCancelar = rootview.findViewById(R.id.btnCancelar);
        max = rootview.findViewById(R.id.Pprecio);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//validación de lo que ingresa
            String Smax = max.getText().toString();
            Pattern ps = Pattern.compile("[0-9]*");
            Matcher ms = ps.matcher(Smax);
            boolean bs = ms.matches();
            if (Smax.matches("")) {
                Toast.makeText(getActivity(), R.string.msjAceptar, Toast.LENGTH_SHORT).show();
            }else if(!bs){
                max.setError("Introduzca solo numeros");
            }
            else if (max.getText().length() < 3) {
                max.setError("Mínimo 3 dígitos");
            } else if (max.getText().length() >= 6) {
                max.setError("Máximo de 5 dígitos");
            }else if(Integer.parseInt(Smax)<100){ //para que no ingrese 0000
                max.setError("El valor ingresado debe ser mayor a 100");
            } else { //si esta validado
                Intent i = new Intent (getActivity(), Tabs.class);
                String maximo_compra = max.getText().toString();
                int maximo = Integer.parseInt(maximo_compra);
                i.putExtra("max", maximo); //se envia el maximo que ingreso
                startActivity(i);
                if (getActivity()!= null){
                    getActivity().finish();
                }
                dismiss();
            }
            }
        });
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCancelable(false); //solo se permite cancelar el mensaje con el boton cancelar.
        Objects.requireNonNull(getDialog()).setTitle("Máximo");
        return rootview;
    }
}
