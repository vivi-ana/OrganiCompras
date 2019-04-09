package acostapeter.com.organicompras;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
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

import acostapeter.com.organicompras.data.DbCRUD;

public class MiHistorialActivityGraficoMensual extends AppCompatActivity {
    private BarChart barChart;
    private Spinner spinner;
    DbCRUD dbHelper;
    Calendar miCalendario = Calendar.getInstance();
    int year = miCalendario.get(Calendar.YEAR);
    int mes = miCalendario.get(Calendar.MONTH)+1;
    NumberFormat formatter = new DecimalFormat("00");
    String month = formatter.format(mes); // ----> 01
    private boolean mSpinnerInitialized;
    int a =5, count = 0, indice;
    double totalunitario = 0, totalu = 0;
    boolean condatos = false,  dibujarlinea = false;
    int days = 0;
    LimitLine ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historial_grafico_mensual);
        barChart = findViewById(R.id.pieChart);
        dbHelper = new DbCRUD(this, null);
        addItemsOnSpinner();
        spinner.setSelection(mes - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mSpinnerInitialized) {
                    mSpinnerInitialized = true; //para que no tome la primera vez que entra al oncreate.
                    return;
                }
                mes = position +1;
                String month = formatter.format(mes); // ----> 01
                graficar(month, mes, count, totalu, condatos, a);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "nada", Toast.LENGTH_LONG).show();
            }
        });
        graficar(month, mes, count, totalu, condatos, a);
    }
    public void addItemsOnSpinner() {
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinneradapter =
                ArrayAdapter.createFromResource(this, R.array.valores_array, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);
    }
    public void graficar(String month, int mes, int count, double totalu, boolean condatos, int a ){
        Calendar cal = new GregorianCalendar(year, mes , 0);
        days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Cursor datos1 = dbHelper.supermercado();
        BarDataSet barDataSety;
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        ArrayList<String> valsX = new ArrayList<>();   /*creamos una lista para los valores X que seran los dias de determinado mes*/
        for (int i=1; i<=days; i++){
            String dia = Integer.toString(i); //cargar los dias de un determinado mes
            valsX.add("Día " + dia);
        }
        if (datos1.moveToFirst()) { /*creamos una lista para los valores Y que son los consumos*/
            do {
                String nombre = datos1.getString(1);
                String id = datos1.getString(0);
                int ide = Integer.parseInt(id);
                ArrayList<BarEntry> valsY = new ArrayList<>();  //cargar nombre del supermercado y su id
                for (int i = 1; i <= days; i++) {
                    indice = i - 1;
                    String dia = formatter.format(i);
                    Cursor datos2 = dbHelper.estadistica(ide, year, month, dia);
                    int cantidad = datos2.getCount();
                    if (cantidad > 0) {
                        do {
                            count = count + 1;
                            String total = datos2.getString(6);
                            totalu = totalu + Double.parseDouble(total);
                        } while (datos2.moveToNext());
                        totalunitario = totalu / count; //por si hay mas de una compra en ese dia, se saca promedio de los totales unitarios.
                        valsY.add(new BarEntry((float) totalunitario, indice));
                        count = 0;
                        totalu = 0;
                        condatos = true;
                    } else {
                        valsY.add(new BarEntry((0), indice)); //no tiene datos para ese dia}
                    }
                }
                barDataSety = new BarDataSet(valsY, nombre);
                barDataSety.setColor(Color.rgb(0, a, 0)); //cargar color
                a = a + 180;
                dataSets.add(barDataSety);
            } while (datos1.moveToNext()); //ir al siguiente supermercado
            BarData datos = new BarData(valsX, dataSets);
            datos.setValueTextSize(10f);
            barChart.setData(datos);
            barChart.setTouchEnabled(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
            barChart.animateXY(2000, 2000);
            barChart.invalidate();
            barChart.setDescription("");
            barChart.setScaleMinima(7f, 1f); //escala
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setTextSize(12f); // set the text size
            leftAxis.setDrawGridLines(true);
            leftAxis.setAxisMinValue(0); //que comience de cero
            leftAxis.setValueFormatter(new MiHistorialActivityGraficoEjeYFormato());
            float posicion = linea(mes);  //linea limite
            if (dibujarlinea) {  //posicion y frase
                ll = new LimitLine(posicion, "Promedio de compras anteriores"+"$"+posicion);
                ll.setLineColor(Color.RED);
                ll.setLineWidth(1f);
                ll.setTextColor(Color.BLACK);
                ll.setTextSize(12f);
                leftAxis.addLimitLine(ll);
                dibujarlinea = false;
            }else{
                leftAxis.removeLimitLine(ll);
            }
            YAxis yAxisRigh = barChart.getAxisRight();
            yAxisRigh.setAxisMinValue(0); //que comience de cero
            yAxisRigh.setValueFormatter(new MiHistorialActivityGraficoEjeYFormato());
            XAxis xAxis = barChart.getXAxis();
            xAxis.setGridLineWidth(1); //tamaño de lineas X
            if (!condatos) {
                Toast.makeText(this, "No se tiene datos para este mes", Toast.LENGTH_LONG).show();
            }
            String dias = Integer.toString(days);
            Toast.makeText(this,dias , Toast.LENGTH_SHORT).show();
        }
    }
    public float linea (int mes){
        dibujarlinea = false;
        float linea = 0;
        int contador = 0, i, cantidad = 0;
        double totalu = 0, totalun, totaldelmes = 0;
        for (int j = 1; j <=3;j++){
            if (mes == 1) {
                mes = 12; //se tiene que restar y no hay mes 0
            }
            mes = mes - 1;
            Calendar cal = new GregorianCalendar(year, mes, 0);
            days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (i = 1; i <= days; i++) {
                indice = i - 1;
                String dia = formatter.format(i); //----01
                String month = formatter.format(mes); // ----> 01
                Cursor datos = dbHelper.estadistica_mensual(year, month, dia);
                cantidad = datos.getCount();
                if (cantidad > 0) {
                    do {
                        contador = contador + 1;
                        String total = datos.getString(6);
                        totalu = totalu + Double.parseDouble(total);
                    } while (datos.moveToNext());
                    totalun = totalu / contador;
                    totaldelmes = totaldelmes + totalun;//por si hay mas de una compra en ese dia, se saca promedio de los totales unitarios.
                    contador = 0;
                    totalu = 0;
                    dibujarlinea = true;
                }
            }
            if (cantidad >0) {
                totaldelmes = totaldelmes / cantidad; //sacar el promedio de ese mes totaldelmes dividido por la cantidad de compras en ese mes
                float tm = (float) totaldelmes;
                if (j == 1) {
                    linea = tm;
                } else {
                    if (linea > tm) {
                        if (totaldelmes != 0) {
                            linea = tm;
                        }
                    }
                }
                totaldelmes = 0;
            }
        }
        return linea;
    }
}
