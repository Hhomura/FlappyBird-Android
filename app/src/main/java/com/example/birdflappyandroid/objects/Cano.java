package com.example.birdflappyandroid.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.birdflappyandroid.view.MainActivity;

import java.util.Random;

public class Cano extends PlayerObjeto {

    public static int velocidade;

    public Cano(float x, float y, int largura, int altura){
        super(x,y,largura,altura);
        velocidade = 10 * MainActivity.LARGURA_TELA/1080;
    }

    public void draw(Canvas canvas){
        this.x -= velocidade;
        canvas.drawBitmap(this.bm, this.x, this.y, null);
    }

    public void randomY(){
        Random r = new Random();
        this.y = r.nextInt((0+this.altura/4)+1)-this.altura/4;
    }

    @Override
    public void setBm(Bitmap bm) {
        this.bm = Bitmap.createScaledBitmap(bm, largura, altura, true);
    }
}
