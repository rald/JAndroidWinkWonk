package edu.hogwarts.siesta.texttwist;

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

	Bitmap font;

	Bitmap big_box;
	Bitmap big_ball;
	Bitmap small_box;
	Bitmap small_ball;

	Bitmap twist_button_up;
	Bitmap enter_button_up;
	Bitmap clear_button_up;
	Bitmap last_word_button_up;
	Bitmap list_button_up;
	Bitmap up_button_up;
	Bitmap down_button_up;
	Bitmap close_button_up;

	Bitmap twist_button_down;
	Bitmap enter_button_down;
	Bitmap clear_button_down;
	Bitmap last_word_button_down;
	Bitmap list_button_down;
	Bitmap up_button_down;
	Bitmap down_button_down;
	Bitmap close_button_down;

	Button twistButton;
	Button enterButton;
	Button clearButton;
	Button lastWordButton;
	Button listButton;
	Button upButton;
	Button downButton;
	Button closeButton;

	boolean isTwistButtonDown;
	boolean isEnterButtonDown;
	boolean isClearButtonDown;
	boolean isLastWordButtonDown;
	boolean isListButtonDown;
	boolean isUpButtonDown;
	boolean isDownButtonDown;
	boolean isCloseButtonDown;

	Ball[] balls=null;
	Box[] boxes=null;
	Peg[] pegs=null;

	long startTime=0;
	long elapsedTime=0;
	long remainingTime=0;
	long allottedTime=0;
	long remainingSecs=0;
	long prevRemainingSecs=0;

	int score=0;
	int highscore=0;

	boolean isMoving=false;

	boolean isHold=false;

	int boxIndex;

	String word;
	String[] words=null;
	String shuffledWord;

	boolean[] guessed;

	double cx,cy;
	double x,y;

	GameState gameState;

	int numGuessed;

	int[] lastWordIndex=null;

	int wordListOffset;
	int delay;
	boolean gameover;

	boolean qualified=false;

	AudioAttributes attrs = new AudioAttributes.Builder()
	        .setUsage(AudioAttributes.USAGE_GAME)
	        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
	        .build();

	SoundPool sp = new SoundPool.Builder()
	        .setMaxStreams(6)
	        .setAudioAttributes(attrs)
	        .build();

//	SoundPool sp = new SoundPool(6,AudioManager.STREAM_MUSIC,0);
	int[] soundId=new int[6];


	final int 	SOUND_CHOOSE_LETTER=0,
				SOUND_SLIDE_LETTER=1,
				SOUND_CLOCK_TICK=2,
				SOUND_TIME_UP=3,
				SOUND_LONG_WORD=4,
				SOUND_COMPLETE=5,
				SOUND_MAX=6;

	public Animation(Context context) {

		super(context);

		this.context=context;

		gameState=GameState.GAME_INIT;

		score=0;

		setBackgroundColor(Color.BLACK);

		font=BitmapFactory.decodeResource(context.getResources(),R.drawable.font);

		big_box=BitmapFactory.decodeResource(context.getResources(),R.drawable.big_box);
		big_ball=BitmapFactory.decodeResource(context.getResources(),R.drawable.big_ball);
		small_box=BitmapFactory.decodeResource(context.getResources(),R.drawable.small_box);
		small_ball=BitmapFactory.decodeResource(context.getResources(),R.drawable.small_ball);

		twist_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.twist_button_up);
		enter_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.enter_button_up);
		clear_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.clear_button_up);
		last_word_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.last_word_button_up);
		list_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.list_button_up);
		up_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.up_button_up);
		down_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.down_button_up);
		close_button_up=BitmapFactory.decodeResource(context.getResources(),R.drawable.close_button_up);

		twist_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.twist_button_down);
		enter_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.enter_button_down);
		clear_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.clear_button_down);
		last_word_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.last_word_button_down);
		list_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.list_button_down);
		up_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.up_button_down);
		down_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.down_button_down);
		close_button_down=BitmapFactory.decodeResource(context.getResources(),R.drawable.close_button_down);

		soundId[SOUND_CHOOSE_LETTER]=sp.load(context,R.raw.choose_letter,1);
		soundId[SOUND_SLIDE_LETTER]=sp.load(context,R.raw.slide_letter,1);
		soundId[SOUND_TIME_UP]=sp.load(context,R.raw.time_up,1);
		soundId[SOUND_CLOCK_TICK]=sp.load(context,R.raw.clock_tick,1);
		soundId[SOUND_LONG_WORD]=sp.load(context,R.raw.long_word,1);
		soundId[SOUND_COMPLETE]=sp.load(context,R.raw.complete,1);

		Ball.font=font;
		Ball.bitmap=big_ball;
		Box.bitmap=big_box;
		Peg.bitmap=small_ball;

		SharedPreferences prefs=context.getSharedPreferences("edu.hogwarts.siesta.texttwist",Context.MODE_PRIVATE);
		highscore=prefs.getInt("highscore",0);

	}

	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		if(gameState==GameState.GAME_INIT) {

			String line=readRandomLine(context,R.raw.rand).toUpperCase();

			words=line.split(",");

			word=words[0];

			shuffledWord=shuffleString(word);

			shuffleArray(words);

			sortArrayByLengthAscending(words);

/*
			line="";
			for(int i=0;i<words.length;i++) {
				if(i!=0) line+=",";
				line+=words[i];
			}
			Toast.makeText(context,line,Toast.LENGTH_LONG).show();
*/

			isMoving=false;

			cx=(canvas.getWidth()-SCREEN_WIDTH)/2;
			cy=(canvas.getHeight()-SCREEN_HEIGHT)/2;

			numGuessed=0;
			guessed=new boolean[words.length];
			for(int i=0;i<words.length;i++) {
				guessed[i]=false;
			}

			boxIndex=0;
			boxes=new Box[shuffledWord.length()];
			for(int i=0;i<boxes.length;i++) {
				int size=8;
				x=i*Box.bitmap.getWidth()*size+cx;
				y=cy+SCREEN_HEIGHT-384;
				boxes[i]=new Box(x,y,size);
				boxes[i].ballIndex=-1;
			}

			pegs=new Peg[shuffledWord.length()];
			for(int i=0;i<pegs.length;i++) {
				int size=8;
				x=i*Peg.bitmap.getWidth()*size+cx;
				y=cy+SCREEN_HEIGHT-256;
				pegs[i]=new Peg(x,y,size);
				pegs[i].ballIndex=i;
			}

			balls=new Ball[shuffledWord.length()];
			for(int i=0;i<shuffledWord.length();i++) {
				int size=8;
				x=pegs[i].x;
				y=pegs[i].y;
				balls[i]=new Ball(shuffledWord.charAt(i),x,y,size);
				balls[i].used=false;
				balls[i].containerIndex=i;
			}

			x=32*2+cx;
			y=SCREEN_HEIGHT-128+cy;
			twistButton=new Button(twist_button_up,twist_button_down,x,y,8);
			x+=128;
			enterButton=new Button(enter_button_up,enter_button_down,x,y,8);
			x+=128;
			clearButton=new Button(clear_button_up,clear_button_down,x,y,8);
			x+=128;
			lastWordButton=new Button(last_word_button_up,last_word_button_down,x,y,8);
			x+=128;
			listButton=new Button(list_button_up,list_button_down,x,y,8);

			x=32*2+cx;
			y=SCREEN_HEIGHT-128+cy;
			downButton=new Button(down_button_up,down_button_down,x,y,8);
			x+=128;
			upButton=new Button(up_button_up,up_button_down,x,y,8);
			x+=128*3;
			closeButton=new Button(close_button_up,close_button_down,x,y,8);

			startTime=System.currentTimeMillis();
			elapsedTime=0;
			remainingTime=0;
			allottedTime=3*60*1000;

			lastWordIndex=null;

			wordListOffset=0;
			delay=0;
			gameover=false;

			if(!qualified) score=0;
			qualified=false;

			gameState=GameState.GAME_PLAY;
		}

		if(!gameover) {
			elapsedTime=System.currentTimeMillis()-startTime;
			remainingTime=allottedTime-elapsedTime;
			if(remainingTime<0) remainingTime=0;
		}

		Paint paint = new Paint();

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor("#00AAFF"));
		paint.setFilterBitmap(false);

		canvas.drawRect((int)cx,(int)cy,(int)(cx+SCREEN_WIDTH),(int)(cy+SCREEN_HEIGHT),paint);

//		Graphics.wave(canvas,font,8,8,text,cx,cy,8,startTime);

		if(gameState==GameState.GAME_PLAY) {

			for(int i=0;i<boxes.length;i++) {
				boxes[i].draw(canvas);
			}

			for(int i=0;i<pegs.length;i++) {
				pegs[i].draw(canvas);
			}

			for(int i=0;i<balls.length;i++) {
				balls[i].draw(canvas);
			}

			isMoving=false;
			for(int i=0;i<balls.length;i++) {
				if(balls[i].isMoving) {
					isMoving=true;
					break;
				}
			}

			for(int i=0;i<balls.length;i++) {
				balls[i].update(canvas);
			}

			twistButton.draw(canvas);
			enterButton.draw(canvas);
			clearButton.draw(canvas);
			lastWordButton.draw(canvas);
			listButton.draw(canvas);

			isListButtonDown=listButton.update(canvas,touchX,touchY,touchState);

			if(!gameover) {

				isTwistButtonDown=twistButton.update(canvas,touchX,touchY,touchState);
				isEnterButtonDown=enterButton.update(canvas,touchX,touchY,touchState);
				isClearButtonDown=clearButton.update(canvas,touchX,touchY,touchState);
				isLastWordButtonDown=lastWordButton.update(canvas,touchX,touchY,touchState);

				for(int i=0;i<balls.length;i++) {
					if(!balls[i].used) {
						if(touchState==TouchState.TOUCH_DOWN) {
							if(Graphics.inrect((int)touchX,(int)touchY,(int)balls[i].x,(int)balls[i].y,(int)(Ball.bitmap.getWidth()*balls[i].size),(int)(Ball.bitmap.getHeight()*balls[i].size))) {
								pegs[balls[i].containerIndex].ballIndex=-1;
								balls[i].destx=boxes[boxIndex].x;
								balls[i].desty=boxes[boxIndex].y;
								balls[i].used=true;
								balls[i].isMoving=true;
								balls[i].containerIndex=boxIndex;
								boxes[boxIndex].ballIndex=i;
								boxIndex++;
								playSound(SOUND_CHOOSE_LETTER);
								break;
							}
						}
					}
				}

				if(isTwistButtonDown) {
					int i,j,k;

					boolean isSlide=false;

					for(i=pegs.length-1;i>0;i--) {

						j=random.nextInt(i+1);

						if(pegs[i].ballIndex!=-1) {
							k=pegs[i].ballIndex;
							balls[k].containerIndex=j;
							balls[k].destx=pegs[j].x;
							balls[k].desty=pegs[j].y;
							balls[k].isMoving=true;
							isSlide=true;
						}

						if(pegs[j].ballIndex!=-1) {
							k=pegs[j].ballIndex;
							balls[k].containerIndex=i;
							balls[k].destx=pegs[i].x;
							balls[k].desty=pegs[i].y;
							balls[k].isMoving=true;
							isSlide=true;
						}

						int tmp=pegs[i].ballIndex;
						pegs[i].ballIndex=pegs[j].ballIndex;
						pegs[j].ballIndex=tmp;
					}

					if(isSlide) {
						playSound(SOUND_SLIDE_LETTER);
					}

				}
			}

			if(!isHold) {

				if(isEnterButtonDown) {
					isHold=true;
					checkChosenLetters();
					clearAllLetter();
				}

				if(isClearButtonDown) {
					isHold=true;
					clearOneLetter();
				}

				if(isLastWordButtonDown) {
					isHold=true;
					if(lastWordIndex!=null) {
						clearAllLetter();
						lastWordLetters();
					}
				}

				if(isListButtonDown) {
					isHold=true;
					gameState=GameState.GAME_WORDLIST;
				}

			} else if(
					!(isEnterButtonDown ||
					isClearButtonDown ||
					isLastWordButtonDown ||
					isListButtonDown)) {
				isHold=false;
			}

		} else if(gameState==GameState.GAME_WORDLIST) {

			int numWordsToDisplay=6;

			int sw=wordListOffset;
			int ew=wordListOffset+numWordsToDisplay<words.length?wordListOffset+numWordsToDisplay:words.length;

			for(int j=0;j<ew-sw;j++) {
				String word=words[j+sw];
				for(int i=0;i<word.length();i++) {
					int x=(int)(cx+i*12*8);
					int y=(int)(cy+j*12*8)+(4*8*8);
					if(!guessed[j+sw]) Graphics.draw(canvas,small_box,x,y,8);
					if(gameover || guessed[j+sw]) Graphics.print(canvas,font,8,8,Character.toString(word.charAt(i)),x+32,y+32,8);
				}
			}

			upButton.draw(canvas);
			downButton.draw(canvas);
			closeButton.draw(canvas);

			isUpButtonDown=upButton.update(canvas,touchX,touchY,touchState);
			isDownButtonDown=downButton.update(canvas,touchX,touchY,touchState);
			isCloseButtonDown=closeButton.update(canvas,touchX,touchY,touchState);

			delay++;
			if(delay>=4) {
				delay=0;

				if(isDownButtonDown) {
					if(wordListOffset<words.length-1) wordListOffset++;
				}

				if(isUpButtonDown) {
					if(wordListOffset>0) wordListOffset--;
				}
			}

			if(!isHold) {

				if(isCloseButtonDown) {
					isHold=true;
					if(gameover) {
						gameState=GameState.GAME_INIT;
					} else {
						gameState=GameState.GAME_PLAY;
					}
				}

			} else if(
					!(isUpButtonDown ||
					isDownButtonDown ||
					isCloseButtonDown)) {
				isHold=false;
			}

		}

		Graphics.print(canvas,font,8,8,String.format("Hi    %6d",highscore),(int)cx,(int)cy,8);

		Graphics.print(canvas,font,8,8,String.format("Score %6d",score),(int)cx,(int)(cy+1*8*8),8);

		long min=(remainingTime/1000/60);
		long sec=(remainingTime-min*1000*60)/1000;

		Graphics.print(canvas,font,8,8,String.format("Time  %3d:%02d",min,sec),(int)cx,(int)(cy+2*8*8),8);

		long remainingSecs=min*60+sec;
		if(remainingSecs!=prevRemainingSecs) {
			if(remainingSecs==0) {
				playSound(SOUND_TIME_UP);
				clearAllLetter();
				gameover=true;
				gameState=GameState.GAME_WORDLIST;
			} else if(remainingSecs<10) {
				playSound(SOUND_CLOCK_TICK);
			}
		}
		prevRemainingSecs=remainingSecs;

		Graphics.print(canvas,font,8,8,String.format("Words %6s",numGuessed+"/"+words.length),(int)cx,(int)(cy+3*8*8),8);

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

	public static String shuffleString(String text) {
		int i,j;
		char k;
		char[] characters = text.toCharArray();
		for(i=characters.length-1;i>0;i--) {
			j=random.nextInt(i+1);
			k=characters[i];
			characters[i]=characters[j];
			characters[j]=k;
		}
		return new String(characters);
	}

	static void shuffleArray(String[] arr) {
		int i,j;
		String k;
		for(i=arr.length-1;i>0;i--) {
			j=random.nextInt(i+1);
			k=arr[i];
			arr[i]=arr[j];
			arr[j]=k;
		}
	}

	static int find(String[] h,String n) {
		for(int i=0;i<h.length;i++) {
			if(h[i].equalsIgnoreCase(n)) {
				return i;
			}
		}
		return -1;
	}

	void clearOneLetter() {
		if(boxIndex>0) {
			for(int i=pegs.length-1;i>=0;i--) {
				if(pegs[i].ballIndex==-1) {
					boxIndex--;
					int k=boxes[boxIndex].ballIndex;
					balls[k].containerIndex=i;
					balls[k].destx=pegs[i].x;
					balls[k].desty=pegs[i].y;
					balls[k].used=false;
					balls[k].isMoving=true;
					pegs[i].ballIndex=k;
					boxes[boxIndex].ballIndex=-1;
					break;
				}
			}
		}
	}

	void clearAllLetter() {
		while(boxIndex>0) {
			clearOneLetter();
		}
	}

	public static String readRandomLine(Context ctx, int resId) {

		InputStream is=ctx.getResources().openRawResource(resId);

		InputStreamReader isr=new InputStreamReader(is);
		BufferedReader br=new BufferedReader(isr);

		String current="";
		String line="";

		int n=0;
		try {
			while((line=br.readLine())!=null) {
				n++;
				if(random.nextInt(n)==0) {
					current=line;
				}
			}
		} catch (Exception e) {
			return null;
		}

		return current;
	}

	void sortArrayByLengthAscending(String[] arr) {
		int n=arr.length;
		String tmp;
		for(int i=0;i<n-1;i++) {
			for(int j=0;j<n-i-1;j++) {
				if(arr[j].length()>arr[j+1].length()) {
					tmp=arr[j];
					arr[j]=arr[j+1];
					arr[j+1]=tmp;
				}
			}
		}
	}

	void lastWordLetters() {
		for(int i=0;i<lastWordIndex.length;i++) {
			int j=lastWordIndex[i];
			pegs[balls[j].containerIndex].ballIndex=-1;
			balls[j].destx=boxes[boxIndex].x;
			balls[j].desty=boxes[boxIndex].y;
			balls[j].used=true;
			balls[j].isMoving=true;
			balls[j].containerIndex=boxIndex;
			boxes[boxIndex].ballIndex=j;
			boxIndex++;
		}
	}

	void checkChosenLetters() {

		String guess="";
		for(int i=0;i<boxIndex;i++) {
			guess+=Character.toString(balls[boxes[i].ballIndex].letter);
		}

		int j=find(words,guess);
		if(j!=-1) {
			if(!guessed[j]) {
				guessed[j]=true;
				numGuessed++;

				score+=words[j].length();

				if(score>highscore) {
					highscore=score;
					SharedPreferences prefs=context.getSharedPreferences("edu.hogwarts.siesta.texttwist",Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=prefs.edit();
					editor.putInt("highscore",highscore);
					editor.commit();
				}

				lastWordIndex=new int[boxIndex];
				for(int i=0;i<boxIndex;i++) {
					lastWordIndex[i]=boxes[i].ballIndex;
				}

				if(numGuessed==words.length) {
					playSound(SOUND_LONG_WORD);
					gameover=true;
					gameState=GameState.GAME_WORDLIST;
					qualified=true;
				} else if(words[j].length()==word.length()) {
					playSound(SOUND_LONG_WORD);
					qualified=true;
				} else {
					playSound(SOUND_COMPLETE);
				}

			}
		}
	}

	public void playSound(int id) {
		sp.play(soundId[id], 1.0f, 1.0f, 1, 0, 1.0f);
	}

}

