package acostapeter.com.organicompras;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;


import java.text.DecimalFormat;
@SuppressWarnings("all")
public class MiHistorialActivityGraficoEjeYFormato implements YAxisValueFormatter {
    private DecimalFormat mFormat;

    MiHistorialActivityGraficoEjeYFormato () {
        mFormat = new DecimalFormat("0.00"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        // avoid memory allocations here (for performance)
        int b=(int)value;  // access the YAxis object to get more information
        return "$ " + b ; // e.g. append a dollar-sign
    }

    @Deprecated
    public interface IAxisValueFormatter
    {
        @Deprecated
        String getFormattedValue(float value, YAxis Yaxis);
    }

}
