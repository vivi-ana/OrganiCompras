package acostapeter.com.organicompras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static acostapeter.com.organicompras.ConstantesColumnasHistorial.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEXTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.TERCERA_COLUMNA;
@SuppressWarnings("all")
public class MiHistorialActivityBorrarHistorial extends AppCompatActivity {
    ListView listaHistorial;
    Compras compras;
    private ArrayList<HashMap<String, String>> lista;
    ArrayList<String> item_borrar = new ArrayList<String>();
    public static boolean cargar = false;
    Calendar miCalendario = Calendar.getInstance();
    int mes = miCalendario.get(Calendar.MONTH)+1;
    int year = miCalendario.get(Calendar.YEAR);
    private boolean mSpinnerInitialized, aSpinnerInitialized;
    NumberFormat formatter = new DecimalFormat("00");
    String month = formatter.format(mes); // ----> 01
    int spinnerPosition = 0;
    private Spinner spinnerM, spinnerA;
    boolean historial = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historial_borrar_historial);
        listaHistorial =  findViewById(R.id.listaHistorial);
        lista = new ArrayList<HashMap<String, String>>();
        final MiHistorialActivityBorrarHistorialListViewAdapter adapter = new MiHistorialActivityBorrarHistorialListViewAdapter(this, lista);
        listaHistorial.setAdapter(adapter);
        compras = new Compras(this);
        Button btnborrar = findViewById(R.id.btnBorrar);
        mes = getIntent().getIntExtra("mes", miCalendario.get(Calendar.MONTH)+1);
        year = Integer.valueOf(getIntent().getStringExtra("year"));
        month = formatter.format(mes); // ----> 01
        cargar(year, month);
        btnborrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                item_borrar.clear();
                listaHistorial.setAdapter(adapter);

                String [] item = adapter.getMid();
                for (String anItem : item) {
                    if (anItem != null) {
                        item_borrar.add(anItem);
                    }
                }
                mensaje();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
        getSupportActionBar().setCustomView(R.layout.checkboxbar);
        final CheckBox mCheckbox = actionBar.getCustomView().findViewById(R.id.checkBox);
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.selectAll();
                    listaHistorial.setAdapter(adapter);
                } else {
                    adapter.unselectAll();
                    listaHistorial.setAdapter(adapter);
                }
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        }
        addItemsOnSpinner();
        spinnerM.setSelection(mes - 1);
        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!aSpinnerInitialized) {
                    aSpinnerInitialized = true; //para que no tome la primera vez que entra al oncreate
                    spinnerA.setSelection(spinnerPosition);
                    return;
                }
                String text = spinnerA.getSelectedItem().toString();
                year = Integer.parseInt(text);
                cargar(year, month);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "nada", Toast.LENGTH_LONG).show();
            }
        });
        spinnerM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mSpinnerInitialized) {
                    mSpinnerInitialized = true; //para que no tome la primera vez que entra al oncreate.
                    return;
                }
                mes = position +1;
                String month = formatter.format(mes); // ----> 01
                cargar(year, month);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "nada", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addItemsOnSpinner() {
        spinnerM = findViewById(R.id.spinnerM);
        ArrayAdapter<CharSequence> spinneradapterM =
                ArrayAdapter.createFromResource(this, R.array.valores_array, android.R.layout.simple_spinner_item);
        spinneradapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerM.setAdapter(spinneradapterM);
        spinnerA = findViewById(R.id.spinnerA);
        ArrayAdapter<CharSequence> spinneradapterA =
                ArrayAdapter.createFromResource(this, R.array.valores_año, android.R.layout.simple_spinner_item);
        spinneradapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerA.setAdapter(spinneradapterA);
        spinnerPosition= spinneradapterA.getPosition(Integer.toString(year));
    }
    public void mensaje(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MiHistorialActivityBorrarHistorial.this);
        alertBuilder.setTitle(R.string.borrar);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(R.string.mensajeBorrarCompra);
        alertBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int cantidad = item_borrar.size();
                if (cantidad >= 1) {
                    cargar = true;
                    for (int i = 0; i < item_borrar.size(); i++) {
                        String item = item_borrar.get(i);
                        Compras compras = new Compras(getBaseContext());
                        int borrar_item = Integer.parseInt(item);
                        compras.setId(borrar_item);
                        compras.borrar_compra();
                        compras.borrar_detalle_compra();
                    }
                    listaHistorial.setAdapter(null);
                    MiHistorialActivityBorrarHistorialListViewAdapter adapter = new MiHistorialActivityBorrarHistorialListViewAdapter(MiHistorialActivityBorrarHistorial.this, lista);
                    listaHistorial.setAdapter(adapter);
                    cargar(year,month);
                    item_borrar.clear();
                } else {
                    Toast.makeText(getBaseContext(), "Debe seleccionar alguna compra", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
    public void cargar(Integer year, String month){
        ArrayList<HashMap<String, String>> listado_historial;
        String lugar, cantidad, total, id, fecha, total_unitario;
        lista.clear();
        listado_historial = compras.cargar_historial(year, month);
        int bucle = listado_historial.size();
        if (bucle != 0){
            for(int i=0; i<bucle; i++) {
                HashMap<String, String> hashmap= listado_historial.get(i);
                fecha = hashmap.get(PRIMERA_COLUMNA);
                lugar = hashmap.get(SEGUNDA_COLUMNA);
                cantidad = hashmap.get(TERCERA_COLUMNA);
                total = hashmap.get(CUARTA_COLUMNA);
                total_unitario = hashmap.get(QUINTA_COLUMNA);
                id = hashmap.get(SEXTA_COLUMNA);

                HashMap<String, String> temporal = new HashMap<String, String>();
                temporal.put(PRIMERA_COLUMNA, fecha);
                temporal.put(SEGUNDA_COLUMNA, lugar);
                temporal.put(TERCERA_COLUMNA, cantidad);
                temporal.put(CUARTA_COLUMNA, total);
                temporal.put(QUINTA_COLUMNA, total_unitario);
                temporal.put(SEXTA_COLUMNA, id);
                lista.add(temporal);
            }
        }else{
            if (historial = true) { //quiere decir que el historial esta abierto y no hay solo para esa fecha, no se puede cerrar la ventana de manera brusca;
                Toast.makeText(this, "No hay compras en el historial para esa fecha", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No hay compras en el historial", Toast.LENGTH_SHORT).show();
                finish();
            }
            MiHistorialActivityBorrarHistorialListViewAdapter adapterDespensa = new MiHistorialActivityBorrarHistorialListViewAdapter(MiHistorialActivityBorrarHistorial.this, lista);
            listaHistorial.setAdapter(adapterDespensa);
        }
        MiHistorialActivityBorrarHistorialListViewAdapter adapterDespensa = new MiHistorialActivityBorrarHistorialListViewAdapter(MiHistorialActivityBorrarHistorial.this, lista);
        listaHistorial.setAdapter(adapterDespensa);
    }
    public static boolean ReturnCargar () {return cargar;}
    public static void SettingCargar() {cargar = false;}
}
