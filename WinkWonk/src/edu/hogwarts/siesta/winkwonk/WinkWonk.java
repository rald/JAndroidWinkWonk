package edu.hogwarts.siesta.winkwonk;

import android.app.Activity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.Button;

import android.util.Log;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.Random;

public class WinkWonk extends Activity implements SurfaceHolder.Callback {

	EditText cmd;
	Button btn;
	SurfaceView srf;

	private static final String TAG = "WinkWonk";

	@Override
	public void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		setContentView(R.layout.winkwonk);

		cmd = (EditText) findViewById(R.id.editText1);
		btn = (Button) findViewById(R.id.button1);
		srf = (SurfaceView) findViewById(R.id.surfaceView1);

		srf.getHolder().addCallback(this);
		
	}

	@Override
    public void surfaceCreated(SurfaceHolder holder) {
        tryDrawing(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h) { 
        tryDrawing(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    private void tryDrawing(SurfaceHolder holder) {
		Log.i(TAG, "Trying to draw...");

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
        	Log.e(TAG, "Cannot draw onto the canvas as it's null");
 		} else {
            drawMyStuff(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawMyStuff(final Canvas canvas) {
        Random random = new Random();
        Log.i(TAG, "Drawing...");

		Paint paint = new Paint(); 
		paint.setColor(Color.BLACK); 
		paint.setStyle(Style.FILL); 
		canvas.drawPaint(paint); 

		paint.setColor(Color.WHITE); 
		paint.setTextSize(20); 
        
        canvas.drawText("Hello World",32,32,paint);
    }

}
