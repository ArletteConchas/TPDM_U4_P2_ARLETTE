package com.example.tpdm_u4_p2_arlette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Vista extends SurfaceView {
    private SurfaceHolder holder;
    private Lienzo lienzo;
    private List<Sprite> sprites;
    private List<Sprite> sprites_;
    private Sprite jefe=null;
    Handler handler;
    Timer timer;
    private int moscas_restantes=30, tiempo_restante_moscas=60, veces_restantes_jefe=5, Tiempo_restante_jefe=10;
    private boolean perdio=false;

    @SuppressLint("HandlerLeak")
    public Vista(Context context) {
        super(context);
        lienzo = new Lienzo(this);

        sprites = new ArrayList<>();
        sprites_ = new ArrayList<>();
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                lienzo.setRunning(false);
                while (retry) {
                    try {
                        lienzo.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                lienzo.setRunning(true);
                lienzo.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });


        handler = new Handler(){
            @Override
            public void handleMessage(Message message){

            }
        };

        sprites_.add(createSprite(R.drawable.mosca,128));
        sprites_.add(createSprite(R.drawable.mosca,128));
        sprites_.add(createSprite(R.drawable.mosca,128));
        sprites_.add(createSprite(R.drawable.mosca,128));
        sprites_.add(createSprite(R.drawable.mosca,128));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(perdio)
                    return;
                if(moscas_restantes<=0 && jefe==null){
                    jefe=createSprite(R.drawable.mosca,1000);
                }
                tiempo_restante_moscas--;
                if(tiempo_restante_moscas<=0 || Tiempo_restante_jefe<=0)
                    perdio=true;
                if(moscas_restantes<=0)
                    Tiempo_restante_jefe--;
                handler.sendEmptyMessage(0);
            }
        },0,1000);

    }

    private Sprite createSprite(int resource, int tam){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), resource);
        bm = Bitmap.createScaledBitmap(bm, tam, tam, true);
        return new Sprite(this, bm);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        if (canvas!=null){
            canvas.drawColor(Color.WHITE);
            sprites=sprites_;
            if(jefe!=null){
                jefe.onDraw(canvas);
            }
            for(Sprite pl : sprites){
                pl.onDraw(canvas);
            }
            if(veces_restantes_jefe<=0){
                timer.cancel();
                sprites_=new ArrayList<>();
                jefe=null;
                p.setTextSize(200);
                p.setColor(Color.GREEN);
                canvas.drawText("GANASTEEEEE", getWidth()/4,getHeight()/2,p);
                return;
            }
            if(perdio){
                sprites_=new ArrayList<>();
                p.setTextSize(150);
                p.setColor(Color.RED);
                canvas.drawText("YA PERDISTE :(", getWidth()/4,getHeight()/2,p);
                return;
            }
            if(moscas_restantes>0) {
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.GRAY);
                canvas.drawRect(0, 0, getWidth(), 70, p);
                p.setColor(Color.MAGENTA);
                canvas.drawRect(0, 0, getWidth() * (tiempo_restante_moscas / 60f), 70, p);
                p.setColor(Color.BLACK);
                p.setTextSize(60);
                canvas.drawText("Moscas restantes: " + moscas_restantes, 10, 50, p);
            }else{
                sprites_=new ArrayList<>();
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.GRAY);
                canvas.drawRect(0, 0, getWidth(), 70, p);
                p.setColor(Color.RED);
                canvas.drawRect(0, 0, getWidth() * (Tiempo_restante_jefe / 10f), 70, p);
                p.setColor(Color.BLACK);
                p.setTextSize(60);
                canvas.drawText("Vidas Jefe: " + veces_restantes_jefe, 10, 50, p);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        synchronized (getHolder()){
            if(jefe!=null){
                if (jefe.isColliding((int) event.getX(), (int)event.getY())){
                    veces_restantes_jefe--;
                    return super.onTouchEvent(event);
                }
            }
            Sprite sp = null;
            for (int i = 0; i<sprites_.size();i++){
                sp=sprites_.get(i);
                if (sp.isColliding((int) event.getX(), (int)event.getY())){
                    sprites_.remove(sp);
                    Random r=new Random();
                    int num = r.nextInt(4);
                    for(int k=0;k<num;k++){
                        sprites_.add(createSprite(R.drawable.mosca,128));
                    }
                    moscas_restantes--;
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
