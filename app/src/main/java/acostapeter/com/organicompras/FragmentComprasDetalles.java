package acostapeter.com.organicompras;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FragmentComprasDetalles extends AppCompatActivity {
    static boolean editar = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compras_detalles);
    }
    public boolean getEditar() {
        return editar;
    }
}
