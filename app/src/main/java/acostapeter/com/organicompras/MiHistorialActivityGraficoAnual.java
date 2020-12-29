package acostapeter.com.organicompras;

import android.database.Cursor;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import acostapeter.com.organicompras.data.DbCRUD;

public class MiHistorialActivityGraficoAnual extends AppCompatActivity {
    private BarChart barChart;
    private Spinner spinner;
    int spinnerPosition = 0;
    Supermercado supermercado;
    Compras compras;
    Calendar miCalendario = Calendar.getInstance();
    int year = miCalendario.get(Calendar.YEAR);
    NumberFormat formatter = new DecimalFormat("00");
    private boolean mSpinnerInitialized;
    int a =5, count = 0, numeromes;
    Cursor datos;
    DbCRUD dbHelper;
    double totalunitario = 0, totalu = 0, totaldelmes;
    boolean condatos = false;
    int days = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historial_grafico_anual);
        barChart = findViewById(R.id.pieChart);
        compras = new Compras(this);
        dbHelper = new DbCRUD(this, null);
        supermercado = new Supermercado(this);
        addItemsOnSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mSpinnerInitialized) {
                    mSpinnerInitialized = true; //para que no tome la primera vez que entra al oncreate
                    spinner.setSelection(spinnerPosition);
                    return;
                }
                String text = spinner.getSelectedItem().toString();
                year = Integer.parseInt(text);
                graficar(year, count, totalu, a);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "nada", Toast.LENGTH_LONG).show();

            }
        });
        graficar(year, count, totalu, a);

    }
    public void addItemsOnSpinner() {

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinneradapter =
                ArrayAdapter.createFromResource(this, R.array.valores_año, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);
        spinnerPosition= spinneradapter.getPosition(Integer.toString(year));
    }
    public void graficar(int year, int count, double totalu, int a ){
        condatos = false;
        setTitle("Consumo anual");
        BarDataSet barDataSety;
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        /*creamos una lista para los valores X que seran los dias de determinado mes*/
        ArrayList<String> valsX = new ArrayList<>();
        valsX.add("Enero");
        valsX.add("Febrero");
        valsX.add("Marzo");
        valsX.add("Abril");
        valsX.add("Mayo");
        valsX.add("Junio");
        valsX.add("Julio");
        valsX.add("Agosto");
        valsX.add("Septiembre");
        valsX.add("Octubre");
        valsX.add("Noviembre");
        valsX.add("Diciembre");
        ArrayList<HashMap<String, String>> lista_supermercado;
        lista_supermercado = supermercado.lista_supermercado();
        int bucle = lista_supermercado.size();
        if (bucle != 0){//entro al primer supermercado;
            for(int dato=0; dato<bucle; dato++) {
                HashMap<String, String> hashmap= lista_supermercado.get(dato);
                String nombre = hashmap.get("nombre");
                String id = hashmap.get("id");
                if(id != null){
                int ide = Integer.parseInt(id);
                //cargar nombre del supermercado y su id
                ArrayList<BarEntry> valsY = new ArrayList<>();
                for (numeromes=1; numeromes<=12; numeromes++) { //entrar meses
                    /*creamos una lista para los valores Y que son los consumos*/
                    Calendar cal = new GregorianCalendar(year, numeromes , 0); //sacar cuantos dias para ese mes;
                    days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 1; i <= days; i++) {
                        String dia = formatter.format(i); //----01
                        String month = formatter.format(numeromes); // ----> 01
                        //ArrayList<HashMap<String, String>> estadistica;
                        datos = dbHelper.estadistica(ide, year, month, dia);
                       /* try {
                            int cantidad = datos.getCount();
                            if (cantidad != 0) {
                                for (int j = 0; j < cantidad; j++) {
                                    count = count + 1;
                                    //HashMap<String, String> temp = estadistica.get(j);
                                    //String total = temp.get("cantidad");
                                    //if (total != null) {
                                    //    totalu = totalu + Double.parseDouble(total);
                                    //}
                                    do {
                                        String total = datos.getString(6);
                                        totalu = totalu + Double.parseDouble(total);
                                    }while (datos.moveToNext());
                                }
                                totalunitario = totalu / count;

                                totaldelmes = totaldelmes + totalunitario;//por si hay mas de una compra en ese dia, se saca promedio de los totales unitarios.
                                count = 0;
                                totalu = 0;
                                condatos = true;
                            }
                        }finally {
                            datos.close();
                        }
                    }*/
                        try {
                            int cantidad = datos.getCount();
                            if (cantidad > 0) {
                                do {
                                    count = count + 1;
                                    String total = datos.getString(6);
                                    totalu = totalu + Double.parseDouble(total);
                                } while (datos.moveToNext());
                                totalunitario = totalu / count;
                                totaldelmes = totaldelmes + totalunitario;//por si hay mas de una compra en ese dia, se saca promedio de los totales unitarios.
                                count = 0;
                                totalu = 0;
                                condatos = true;
                            }
                        } finally {
                            datos.close();
                        }
                    }
                    totaldelmes = totaldelmes/days; //sacar el promedio de ese mes totaldelmes dividido por los dias de ese mes
                    valsY.add(new BarEntry((float) totaldelmes, numeromes-1));
                    totaldelmes = 0;
                }
                barDataSety = new BarDataSet(valsY, nombre);
                barDataSety.setColor(Color.rgb(0, a, 0)); //cargar color
                a = a + 180;
                dataSets.add(barDataSety);
                }
            } //ir al siguiente supermercado

            BarData datos = new BarData(valsX,dataSets);
            datos.setValueTextSize(10f);
            barChart.setData(datos);
            barChart.setDescription("");
            barChart.setTouchEnabled(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
            barChart.animateXY(2000, 2000);
            barChart.invalidate();
            barChart.setScaleMinima(7f, 1f); //escala

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setTextSize(12f); // tamaño del texto
            leftAxis.setDrawGridLines(true);
            leftAxis.setAxisMinValue(0); //que comience de cero
            leftAxis.setValueFormatter(new MiHistorialActivityGraficoEjeYFormato());

            YAxis yAxisRigh = barChart.getAxisRight();
            yAxisRigh.setAxisMinValue(0); //que comience de cero
            yAxisRigh.setValueFormatter(new MiHistorialActivityGraficoEjeYFormato());

            XAxis xAxis = barChart.getXAxis();
            xAxis.setGridLineWidth(1); //tamaño de lineas X
            if (!condatos) {
                Toast.makeText(this, "No se tiene datos para este año", Toast.LENGTH_LONG).show();
            }
        }
    }
}
