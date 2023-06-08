package com.example.birdflappyandroid.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.FocusFinder;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.birdflappyandroid.R;
import com.example.birdflappyandroid.control.GameView;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    public static TextView pontos, melhorPonto, titulo;
    public static ConstraintLayout gameOver;
    public static Button play;
    private GameView gameView;
    public static int LARGURA_TELA;
    public static int ALTURA_TELA;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Capturando medidas do celular
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        LARGURA_TELA = dm.widthPixels;
        ALTURA_TELA = dm.heightPixels;
        setContentView(R.layout.activity_main);
        melhorPonto = findViewById(R.id.melhor_ponto);
        gameOver = findViewById(R.id.tela_game_over);
        play = findViewById(R.id.play);
        gameView = findViewById(R.id.gameView);
        titulo = findViewById(R.id.titulo);

        pontos = findViewById(R.id.pontos);

        //MUSICA DE FUNDO
        mediaPlayer = MediaPlayer.create(this, R.raw.magia_back);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.1f, 0.1f);
        mediaPlayer.start();

        //CLIQUE DO BOTÃO QUE INCIA O JOGO
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setRodando(true);
                pontos.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                titulo.setVisibility(View.INVISIBLE);
            }
        });

        //CLIQUE DA TELA QUE REINICIA O JOGO
        gameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.VISIBLE);
                titulo.setVisibility(View.VISIBLE);
                gameOver.setVisibility(View.INVISIBLE);
                gameView.setRodando(false);
                gameView.reset();
            }
        });

    }
}