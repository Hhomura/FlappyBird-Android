package com.example.birdflappyandroid.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.birdflappyandroid.objects.Cano;
import com.example.birdflappyandroid.view.MainActivity;
import com.example.birdflappyandroid.objects.Passaro;
import com.example.birdflappyandroid.R;

import java.util.ArrayList;

public class GameView extends View {

    private int pontos;
    private Passaro passaro;

    private Handler handler;
    private Runnable runnable;
    private ArrayList<Cano> cano = new ArrayList<>();
    private int deslizo;
    private int distancia;

    private int score, melhorPonto = 0;
    private boolean rodando;
    private Context context;
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("configuracoes", Context.MODE_PRIVATE);
        if(sp!= null){
            melhorPonto = sp.getInt("configuracoes", 0);
        }
        score = 0;
        pontos = 0;
        rodando = false;
        passaro = new Passaro();
        //Medidas do pássaro
        passaro.setLargura(100* MainActivity.LARGURA_TELA/1080);
        passaro.setAltura(100*MainActivity.ALTURA_TELA/1920);
        passaro.setX(100*MainActivity.LARGURA_TELA/1080);
        passaro.setY(MainActivity.ALTURA_TELA/2-passaro.getAltura()/2);
        //Carregando Imagens do pássaro
        ArrayList<Bitmap> arr = new ArrayList<Bitmap>();
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame1));
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame2));
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame3));
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame4));
        passaro.setImgs(arr);

        deslizo = 6;
        distancia = 500 * MainActivity.ALTURA_TELA/1920;
        cano = new ArrayList<>();
        for(int i = 0; i < deslizo; i++){
            if(i < deslizo/2){
                this.cano.add(new Cano(MainActivity.LARGURA_TELA + i * ((MainActivity.LARGURA_TELA + 200 * MainActivity.LARGURA_TELA/1080) / (deslizo/2)),
                        0, 200 * MainActivity.LARGURA_TELA/1080, MainActivity.ALTURA_TELA/2));
                this.cano.get(this.cano.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.toptube));
                this.cano.get(this.cano.size()-1).randomY();
            }else{
                this.cano.add(new Cano(this.cano.get(i - deslizo/2).getX(), this.cano.get(i-deslizo/2).getY() + this.cano.get(i-deslizo/2).getAltura() + this.distancia, 200*MainActivity.LARGURA_TELA/1080, MainActivity.ALTURA_TELA/2));
                this.cano.get(this.cano.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottomtube));
            }
        }

        //Motor de Renderização
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                //Vai atualizar o Draw
                invalidate();
            }
        };
    }

    public void draw(Canvas canvas){

        //Desenho do pássaro
        if(rodando){
            super.draw(canvas);
            passaro.draw(canvas);
            for(int i = 0; i < deslizo; i++){

                //CHECAGEM DE COLISÃO E FIM DE JOGO
                if(passaro.getRect().intersect(cano.get(i).getRect())||passaro.getY()-passaro.getAltura() < 0 || passaro.getY() > MainActivity.ALTURA_TELA){
                    Cano.velocidade = 0;
                    MainActivity.melhorPonto.setText("Melhor Pontuação: " + melhorPonto);
                    MainActivity.pontos.setVisibility(INVISIBLE);
                    MainActivity.gameOver.setVisibility(VISIBLE);
                }

                //CHECAGEM DE PONTOS QUANDO PASSA DE UM CANO
                if(this.passaro.getX() + this.passaro.getLargura() > cano.get(i).getX() + cano.get(i).getLargura()/2 && this.passaro.getX() + this.passaro.getLargura() <= cano.get(i).getX() + cano.get(i).getLargura()/2 + Cano.velocidade
                        && i < deslizo/2){
                    pontos++;
                    if(pontos > melhorPonto){
                        melhorPonto = pontos;
                        //ESSA CLASSE GUADA VALORES QUE PERSISTIRÃO DURANTE A EXECUÇÃO OU APÓS O FECHAMENTO DA APLICAÇÃO
                        //MANEIRA DE SALVAR MAIOR PONTUAÇÃO
                        SharedPreferences sp = context.getSharedPreferences("configuracoes", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("melhorPonto", melhorPonto);
                        editor.apply();
                    }
                    MainActivity.pontos.setText("PONTUAÇÃO: "+pontos);
                }
                if(this.cano.get(i).getX() < -cano.get(i).getLargura()){
                    this.cano.get(i).setX(MainActivity.LARGURA_TELA);
                    if(i < deslizo/2){
                        cano.get(i).randomY();
                    }else{
                        cano.get(i).setY(this.cano.get(i-deslizo/2).getY() + this.cano.get(i-deslizo/2).getAltura() + this.distancia);
                    }
                }
                this.cano.get(i).draw(canvas);
            }
        }else{
            if(passaro.getY() > MainActivity.ALTURA_TELA/2){
                passaro.setGravidade(-15*MainActivity.ALTURA_TELA/1920);
            }
            //passaro.draw(canvas);
        }
        //0.01 secs
        handler.postDelayed(runnable, 5);
    }

    //MÉTODO CAPTURA O MOMENTO DE TOQUE NA TELA
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            passaro.setGravidade(-20);
        }
        return true;
    }

    public boolean isRodando() {
        return rodando;
    }

    public void setRodando(boolean rodando) {
        this.rodando = rodando;
    }

    //REINICIA TODAS AS CONFIGURAÇÕES DO JOGO
    public void reset() {
        MainActivity.pontos.setText("0");
        pontos = 0;
        passaro = new Passaro();
        //Medidas do pássaro
        passaro.setLargura(100* MainActivity.LARGURA_TELA/1080);
        passaro.setAltura(100*MainActivity.ALTURA_TELA/1920);
        passaro.setX(100*MainActivity.LARGURA_TELA/1080);
        passaro.setY(MainActivity.ALTURA_TELA/2-passaro.getAltura()/2);
        //Carregando Imagens do pássaro
        ArrayList<Bitmap> arr = new ArrayList<Bitmap>();
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame1));
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame2));
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame3));
        arr.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_frame4));
        passaro.setImgs(arr);

        deslizo = 6;
        distancia = 500 * MainActivity.ALTURA_TELA/1920;
        cano = new ArrayList<>();
        for(int i = 0; i < deslizo; i++){
            if(i < deslizo/2){
                this.cano.add(new Cano(MainActivity.LARGURA_TELA + i * ((MainActivity.LARGURA_TELA + 200 * MainActivity.LARGURA_TELA/1080) / (deslizo/2)),
                        0, 200 * MainActivity.LARGURA_TELA/1080, MainActivity.ALTURA_TELA/2));
                this.cano.get(this.cano.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.toptube));
                this.cano.get(this.cano.size()-1).randomY();
            }else{
                this.cano.add(new Cano(this.cano.get(i - deslizo/2).getX(), this.cano.get(i-deslizo/2).getY() + this.cano.get(i-deslizo/2).getAltura() + this.distancia, 200*MainActivity.LARGURA_TELA/1080, MainActivity.ALTURA_TELA/2));
                this.cano.get(this.cano.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottomtube));
            }
        }
    }
}
