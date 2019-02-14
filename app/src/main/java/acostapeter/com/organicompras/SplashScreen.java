package acostapeter.com.organicompras;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity { //extiende de una actividad para que puedo ser un splash screen
ImageView icono;
Animation animationIconIzquierda;
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

        animationIconIzquierda= AnimationUtils.loadAnimation(this, R.anim.animation_icon_izquierda);
        desplazamiento.addAnimation(animationIconIzquierda);
        icono.startAnimation(desplazamiento);


    }
}
