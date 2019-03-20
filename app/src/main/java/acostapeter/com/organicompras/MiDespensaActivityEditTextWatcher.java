package acostapeter.com.organicompras;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
@SuppressWarnings("all")
public class MiDespensaActivityEditTextWatcher implements TextWatcher {
    private Context context;

    MiDespensaActivityEditTextWatcher(Context context){
        this.context = context;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence productoIngresado, int start, int before, int count) {
        MiDespensaActivity midespensa = ((MiDespensaActivity) context);
        midespensa.item = midespensa.getItemsDb(productoIngresado.toString());// buscar en la Bd segun lo que ingrese
        midespensa.adapter.notifyDataSetChanged();    // actualizar el adaptador;
        midespensa.adapter = new ArrayAdapter<String>(midespensa, android.R.layout.simple_dropdown_item_1line, midespensa.item);
        midespensa.Eproducto.setAdapter(midespensa.adapter);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
