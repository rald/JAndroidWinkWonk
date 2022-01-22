package edu.hogwarts.siesta.winkwonk;

import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
// import android.graphics.Rect;

import android.media.AudioAttributes;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
import android.media.SoundPool;

import android.view.View;
import android.view.MotionEvent;

// import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Random;

public class Animation extends View {

	static int SCREEN_WIDTH=768;
	static int SCREEN_HEIGHT=1024;

	static Random random=new Random();

	Context context;

	TouchState touchState=null;
	double touchX=0,touchY=0;

	double cx,cy;

	double destX,destY;
	
	double x,y,r,a;
	double speed=50;

	boolean hold=false;
	int nclicks=0;
	int clickDelay=0;
	int holdDelay=0;

	double DEG2RAD = Math.PI/180.0;
	double RAD2DEG = 180.0/Math.PI; 

	GameState gameState;

	AudioAttributes attrs = new AudioAttributes.Builder()
	        .setUsage(AudioAttributes.USAGE_GAME)
	        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
	        .build();

	SoundPool sp = new SoundPool.Builder()
	        .setMaxStreams(6)
	        .setAudioAttributes(attrs)
	        .build();

	int[] soundId=new int[6];

	public Animation(Context context) {

		super(context);

		this.context=context;

		gameState=GameState.GAME_INIT;

		setBackgroundColor(Color.BLACK);

	}

	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		if(gameState==GameState.GAME_INIT) {

			cx=(canvas.getWidth()-SCREEN_WIDTH)/2;
			cy=(canvas.getHeight()-SCREEN_HEIGHT)/2;

			x=SCREEN_WIDTH/2;
			y=SCREEN_HEIGHT/2;

			r=32;

			a=Math.random()*360*DEG2RAD;

			destX=x;
			destY=y;

			gameState=GameState.GAME_PLAY;
		}

		Paint paint = new Paint();

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor("#00AAFF"));

		canvas.drawRect(
				(int)cx,(int)cy,
				(int)(cx+SCREEN_WIDTH),
				(int)(cy+SCREEN_HEIGHT),
				paint);

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor("#FFFFFF"));

		canvas.drawCircle((int)(x+cx),(int)(y+cy),(int)r,paint);

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.parseColor("#FF0000"));

		double aa=a+15*DEG2RAD;
		double ab=a-15*DEG2RAD;
		double ar=1024; 
		
		canvas.drawLine((int)(x+cx),(int)(y+cy),
		(int)(ar*Math.cos(aa)+(x+cx)),
		(int)(ar*Math.sin(aa)+(y+cy)),
		paint);

		canvas.drawLine((int)(x+cx),(int)(y+cy),
		(int)(ar*Math.cos(ab)+(x+cx)),
		(int)(ar*Math.sin(ab)+(y+cy)),
		paint);

		if(touchState==TouchState.TOUCH_DOWN) {
			if(!hold) {
				hold=true;
				nclicks++;
			} 
		}

		if(touchState==TouchState.TOUCH_UP) {
			hold=false;
			holdDelay=0;
		}

		if(hold) {
			holdDelay++;
			if(holdDelay>=20) {
				a=Math.atan2(touchY-cy-y,touchX-cx-x);	
			}
		}

		if(nclicks>=1){
			clickDelay++;
			if(clickDelay>=20) {
				nclicks=0;
				clickDelay=0;
			}
		}

		if(nclicks==2) {
			nclicks=0;
			clickDelay=0;
			destX=touchX-cx;
			destY=touchY-cy;			
			a=Math.atan2(destY-y,destX-x);	
		}
	
		x+=(destX-x)/speed;
		y+=(destY-y)/speed;

		if(x<r) x=r;
		if(y<r) y=r;
		if(x>SCREEN_WIDTH-r)  x=SCREEN_WIDTH-r;
		if(y>SCREEN_HEIGHT-r) y=SCREEN_HEIGHT-r;

		invalidate();

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

	public void playSound(int id) {
		sp.play(soundId[id], 1.0f, 1.0f, 1, 0, 1.0f);
	}

}
