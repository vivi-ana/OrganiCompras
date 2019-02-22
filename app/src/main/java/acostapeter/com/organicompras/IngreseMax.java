package acostapeter.com.organicompras;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngreseMax extends DialogFragment {
    Button botonAceptar, botonCancelar;
    EditText max;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_ingrese_max, container, false);
        botonAceptar = rootview.findViewById(R.id.btnAceptar);
        botonCancelar = rootview.findViewById(R.id.btnCancelar);
        max = rootview.findViewById(R.id.Emaximo);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                } else if (max.getText().length() >= 7) {
                    max.setError("Máximo de 5 dígitos");
                }else if(Integer.parseInt(Smax)<100){
                    max.setError("El valor ingresado debe ser mayor a 100");
                } else {
                    //si esta validado
                    Intent i = new Intent (getActivity(), Tabs.class);
                    i.putExtra("max", max.getText().toString());
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
        setCancelable(false);
        getDialog().setTitle("Máximo");
        return rootview;
    }
}
