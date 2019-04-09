package acostapeter.com.organicompras;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.PRIMERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEGUNDA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.TERCERA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.CUARTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.QUINTA_COLUMNA;
import static acostapeter.com.organicompras.ConstantesColumnasHistorial.SEXTA_COLUMNA;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
@SuppressWarnings("all")
public class MiHistorialActivity extends AppCompatActivity {
    ListView listaHistorial;
    Calendar miCalendario = Calendar.getInstance();
    int mes = miCalendario.get(Calendar.MONTH)+1;
    int year = miCalendario.get(Calendar.YEAR);
    private boolean mSpinnerInitialized, aSpinnerInitialized;
    int spinnerPosition = 0;
    NumberFormat formatter = new DecimalFormat("00");
    String month = formatter.format(mes); // ----> 01
    private ArrayList<HashMap<String, String>> lista;
    private Spinner spinnerM, spinnerA;
    Compras compras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historial);
        listaHistorial = findViewById(R.id.listaHistorial);
        lista = new ArrayList<HashMap<String, String>>();
        compras = new Compras(this);
        MiHistorialListViewAdapter adapter = new MiHistorialListViewAdapter(this, lista);
        listaHistorial.setAdapter(adapter);
        cargar(year, month);
        listaHistorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ide = ((CheckBox) view.findViewById(R.id.check)).getText().toString();
                Intent i = new Intent(MiHistorialActivity.this, MiHistorialActivityDetallesCompras.class);
                i.putExtra("ID", ide);
                startActivity(i);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mi_historial, menu);
        return true;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_borrar:
                startActivity(new Intent(MiHistorialActivity.this, MiHistorialActivityBorrarHistorial.class));
                return true;
            case R.id.consumo_mensual:
                startActivity(new Intent(MiHistorialActivity.this, MiHistorialActivityGraficoMensual.class));
                return true;
            case R.id.consumo_anual:
                startActivity(new Intent(MiHistorialActivity.this, MiHistorialActivityGraficoAnual.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                MiHistorialListViewAdapter adapterDespensa = new MiHistorialListViewAdapter(MiHistorialActivity.this, lista);
                listaHistorial.setAdapter(adapterDespensa);
            }
        }else{
            Toast.makeText(this, "No hay compras en el historial", Toast.LENGTH_SHORT).show();
        }
    }
    public String getMes(){
        return month;
    }
    public Integer getYear(){
        return year;
    }
}
