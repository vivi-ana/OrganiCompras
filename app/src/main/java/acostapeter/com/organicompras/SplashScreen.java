package acostapeter.com.organicompras;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity { //extiende de una actividad para que puedo ser un splash screen
ImageView icono;
Animation animacionIconoIzquierda, animacionIconoDerecha;
AnimationSet desplazamiento = new AnimationSet(false);//usar dos animaciones
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        icono = findViewById(R.id.iconImage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
        icono.setVisibility(View.VISIBLE);
        animacionIconoDerecha = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animacion_icono_derecha);
        desplazamiento.addAnimation(animacionIconoDerecha);
        icono.startAnimation(desplazamiento);

        animacionIconoDerecha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {

                animacionIconoIzquierda = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animacion_icono_izquierda);
                desplazamiento.addAnimation(animacionIconoIzquierda);
                icono.startAnimation(desplazamiento);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
