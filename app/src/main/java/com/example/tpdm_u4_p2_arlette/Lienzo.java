package com.example.tpdm_u4_p2_arlette;

import android.graphics.Canvas;

public class Lienzo extends  Thread{
    private Vista vista;
    private boolean running = false;

    public Lienzo(Vista view) {
        this.vista = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        while (running) {
            Canvas c = null;
            try {
                c = vista.getHolder().lockCanvas();
                synchronized (vista.getHolder()) {
                    vista.onDraw(c);
                }
            } finally {
                if (c != null) {
                    vista.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}
