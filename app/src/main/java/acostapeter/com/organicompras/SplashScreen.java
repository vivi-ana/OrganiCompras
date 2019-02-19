package acostapeter.com.organicompras;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SplashScreen extends Activity { //extiende de una actividad para que puedo ser un splash screen
ImageView icono, nombreapp;
TranslateAnimation animacionDerecha, animacionIzquierda, animacionNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        icono = findViewById(R.id.iconImage);
        nombreapp = findViewById(R.id.iconoNombre);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
        animacionNombre = new TranslateAnimation(1200,0,0,0);
        animacionNombre.setDuration(1500);
        nombreapp.setAnimation(animacionNombre);
        animacionDerecha = new TranslateAnimation(1200,-1000,0,0);
        animacionDerecha.setDuration(1200);
        icono.setRotationY(180f); //efecto espejo en el icono
        animacionDerecha.setFillEnabled(true);
        animacionDerecha.setFillAfter(true);
        icono.startAnimation(animacionDerecha);
        animacionDerecha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animacionIzquierda = new TranslateAnimation(-1000,0,0,0);
                animacionIzquierda.setDuration(1500);
                icono.setRotationY(360f);
                animacionIzquierda.setFillEnabled(true);
                animacionIzquierda.setFillAfter(true);
                icono.startAnimation(animacionIzquierda);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
