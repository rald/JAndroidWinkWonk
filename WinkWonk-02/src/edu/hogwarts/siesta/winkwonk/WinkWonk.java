package edu.hogwarts.siesta.winkwonk;

import android.app.Activity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.Button;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import android.os.Handler;
import android.os.Message;

import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;



public class WinkWonk extends Activity implements SurfaceHolder.Callback, Handler.Callback {

	EditText cmd;
	Button btn;
	SurfaceView srf;

	Canvas canvas=null;

	SurfaceHolder holder;

	Handler handler;

	float x=0,y=0;
	float xi=20,yi=20;

	String text="Hello World";



	@Override
	public void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		setContentView(R.layout.winkwonk);

		cmd = (EditText) findViewById(R.id.editText1);
		btn = (Button) findViewById(R.id.button1);
		srf = (SurfaceView) findViewById(R.id.surfaceView1);

		handler=new Handler(this);
		srf.getHolder().addCallback(this);

		btn.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
				text=cmd.getText().toString();
		    }
		});

	}

	@Override
    public void surfaceCreated(SurfaceHolder holder) {
		this.holder=holder;
		handler.sendEmptyMessage(0);	
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h) { 
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
		handler.removeCallbacksAndMessages(null);
    }

	@Override
    public boolean handleMessage(Message msg) {

		canvas=holder.lockCanvas();

		Paint paint = new Paint(); 
		paint.setColor(Color.BLACK); 
		paint.setStyle(Style.FILL); 
		canvas.drawPaint(paint); 

		paint.setColor(Color.WHITE); 
		paint.setTextSize(64); 
        
        canvas.drawText(text,x,y,paint);

		x+=xi;
		y+=yi;

		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int height = bounds.height();
		int width = bounds.width();

		if(x<0) xi=Math.abs(xi);
		if(y<height) yi=Math.abs(yi);
		if(x>canvas.getWidth()-width) xi=-Math.abs(xi);
		if(y>canvas.getHeight()-height) yi=-Math.abs(yi);

		holder.unlockCanvasAndPost(canvas);

		handler.sendEmptyMessageDelayed(msg.what + 1, 1000/60);

        return true;
    }


}
