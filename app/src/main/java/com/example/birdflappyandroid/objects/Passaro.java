package com.example.birdflappyandroid.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Passaro extends PlayerObjeto {

    private ArrayList<Bitmap> imgs = new ArrayList<Bitmap>();

    private int count, vFlap, idCurrentBitmap;

    private float gravidade;
    public Passaro(){
        this.count = 0;
        this.vFlap = 5;
        this.idCurrentBitmap = 0;
        this.gravidade = 0;
    }


    public float getGravidade() {
        return gravidade;
    }

    public void setGravidade(float gravidade) {
        this.gravidade = gravidade;
    }

    @Override
    public Bitmap getBm() {
        count ++;

        //  VERIFICAÇÃO E ATUALIZAÇÃO DAS IMAGENS DO PASSARINHO
        if(this.count == this.vFlap){
            for(int i = 0; i < imgs.size(); i++){
                if(i == imgs.size()-1){
                    this.idCurrentBitmap = 0;
                    break;
                }else if(this.idCurrentBitmap == i){
                    idCurrentBitmap = i+1;
                    break;
                }
            }
            count = 0;
        }
        if(this.gravidade < 0){
            Matrix matrix = new Matrix();
            matrix.postRotate(-45);
            return Bitmap.createBitmap(imgs.get(idCurrentBitmap), 0, 0, imgs.get(idCurrentBitmap).getWidth(), imgs.get(idCurrentBitmap).getHeight(), matrix, true);
        }else if(gravidade >= 0){
            Matrix matrix = new Matrix();
            if(gravidade < 70){
                matrix.postRotate(-45+(gravidade * 2));
            }else{
                matrix.postRotate(65);
            }
            return Bitmap.createBitmap(imgs.get(idCurrentBitmap), 0, 0, imgs.get(idCurrentBitmap).getWidth(), imgs.get(idCurrentBitmap).getHeight(), matrix, true);
        }
        return this.imgs.get(idCurrentBitmap);
    }

    //DESENHO DO PASSARINHO
    public void draw(Canvas canvas){
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
        gravidade();
    }
    private void gravidade(){
        //Força da gravidade aumenta constantemente e é subtraída com a posição da altura
        this.gravidade += 1.4;
        this.y += this.gravidade;
    }

    public ArrayList<Bitmap> getImgs() {
        return imgs;
    }

    //recebendo imagens
    public void setImgs(ArrayList<Bitmap> imgs) {
        this.imgs = imgs;
        for(int i =0; i < imgs.size(); i++){
            this.imgs.set(i, Bitmap.createScaledBitmap(this.imgs.get(i), this.largura, this.altura, true));
        }
    }
}
