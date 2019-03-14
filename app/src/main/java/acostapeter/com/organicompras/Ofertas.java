package acostapeter.com.organicompras;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled") //elimina el warning de JS
public class Ofertas extends AppCompatActivity {
    private String seleccion;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);
        progressBar = findViewById(R.id.loading);
        mensaje();
    }
    public void mensaje(){
        //crear un dialogo donde el usuario seleccione de manera obligatoria de donde quiere conocer las ofertas
        final String[] items = {"Apa", "Carrefour", "Chango Más", "Diarco" };
        final AlertDialog.Builder supermercado = new AlertDialog.Builder(Ofertas.this);
        supermercado.setTitle(R.string.ofertas);
        supermercado.setCancelable(false);
        supermercado.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                seleccion = items[item];
            }
        });
        supermercado.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (seleccion == null) {
                    seleccion = "Apa";
                }
                paginaofertas(seleccion);
                dialog.dismiss();
            }


        });
        supermercado.setNegativeButton(R.string.atras, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Si cancela el mensaje tiene que volver al menú
                startActivity(new Intent(Ofertas.this, MainActivity.class));
                finish();
            }
        });
        Dialog dialog = supermercado.create();
        dialog.show();
    }
    //cargar el webView.
    public void paginaofertas(String seleccionoferta){
        WebView myWebView = findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);
        myWebView.setWebViewClient(new WebViewClient() { //mostrar el loading
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                setTitle("Cargando...");
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                setTitle(view.getTitle());
            }
        });
        if (isNetworkAvailable(getApplicationContext()))
        {
            switch (seleccionoferta) {
                case "Apa":
                    myWebView.loadUrl("https://es-la.facebook.com/APASupermercados/");
                break;
                case "Chango Más":
                    myWebView.loadUrl("https://www.changomas.com.ar/contenidos/promociones");
                break;
                case "Carrefour":
                    myWebView.loadUrl("http://www.carrefour.com.ar/promociones");
                break;
                case "Diarco":
                    myWebView.loadUrl("https://www.diarco.com.ar/ofertas");
                break;
            }
        }
        else{
            new AlertDialog.Builder(Ofertas.this)
            .setTitle("Ups")
            .setMessage("Tu conexión a Internet no esta disponible por el momento. Por favor intenta más tarde.")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            })
            .show();
        }
    }
    //comprobar si tiene acceso a internet
    public boolean isNetworkAvailable(Context context) {
        boolean value = false;
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connec.getActiveNetworkInfo();
        if (networkInfo!=null) {
            if (networkInfo.isConnected()) {
                value = true;
            }
        }
        return value;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //boton hacia atrás, debe volver al menu principal.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent i = new Intent(Ofertas.this, MainActivity.class);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }

}
