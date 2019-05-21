package com.example.tpdm_u4_p2_arlette;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Sprite {
    public int x = 10; // sprite coordinate x
    public int y = 10; // sprite coordinate y
    public int xSpeed = 25 ; // sprite x speed
    public int ySpeed = 25 ; // sprite y speed

    public Vista gameView;  // reference to GameView
    public Bitmap bmp;         // sprite Bitmap


    public Sprite(Vista gameView, Bitmap bmp) {
        this.gameView=gameView;
        this.bmp=bmp;
        Random rnd = new Random(System.currentTimeMillis());
        x = rnd.nextInt(400)+1;
        y= rnd.nextInt(800)+1;
        Random rnd2 = new Random(System.currentTimeMillis());
        xSpeed = rnd2.nextInt(50)+1;
        ySpeed = rnd2.nextInt(30)+1;
    }

    // boundaries collision for a single bitmap
    private void update() {
        // boundaries collision for east / west
        if (x > gameView.getWidth() - bmp.getWidth() - xSpeed) {
            xSpeed = -xSpeed;
        }
        if (x + xSpeed< 0) {
            xSpeed = 15;
        }
        x = x + xSpeed;

        // boundaries collision for north /south
        if (y > gameView.getHeight() - bmp.getHeight() - ySpeed) {
            ySpeed = -ySpeed;
        }
        if (y + ySpeed< 70) {
            ySpeed = 15;
        }
        y = y + ySpeed;
    }

    public void onDraw(Canvas canvas) {
        update();
        //canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, x , y, null);
    }

    public boolean isColliding(int px, int py){
        return(px >= x && px <= x + bmp.getWidth() && py >= y && py <= y + bmp.getHeight());
    }
}
