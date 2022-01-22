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

import android.view.MotionEvent;
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
	float r=32;
	float a=0;

	float DEG2RAD=Math.PI/180.0;
	float RAD2DEG=180.0/Math.PI;

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

		if(canvas==null) {

			canvas=holder.lockCanvas();

			x=canvas.getWidth()/2;
			y=canvas.getHeigbt()/2;
			a=Math.random()*360*DEG2RAD;
		}

		Paint paint = new Paint(); 
		paint.setColor(Color.BLACK); 
		paint.setStyle(Style.FILL); 
		canvas.drawPaint(paint); 

		paint.setColor(Color.WHITE); 
   		canvas.drawCircle(x,y,r,paint);

		holder.unlockCanvasAndPost(canvas);
		handler.sendEmptyMessageDelayed(msg.what + 1, 1000/60);
        return true;
    }


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX=event.getX();
		touchY=event.getY();
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN: touchState=TouchState.TOUCH_DOWN; break;
			case MotionEvent.ACTION_MOVE: touchState=TouchState.TOUCH_MOVE; break;
			case MotionEvent.ACTION_UP:   touchState=TouchState.TOUCH_UP;   break;
		}
		return true;
	}



}
