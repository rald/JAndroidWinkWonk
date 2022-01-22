package edu.hogwarts.siesta.winkwonk;



import android.app.Activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.Handler;
import android.os.Message;

import android.widget.EditText;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import android.view.View;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

import java.net.Socket;



public class WinkWonk extends Activity {



	String srv = "sakura.jp.as.dal.net";
	String nck = "siestu";
	String chn = "#pantasya";
	String pss = null;
	int prt = 6667;



	EditText cmd;
	ScrollView scl;	
	TextView dpy;
	Button btn;

	Thread thread;

	Irc irc = new Irc();



	private final Handler myTextHandler = new Handler(new Handler.Callback() {
	    @Override
	    public boolean handleMessage(Message stringMessage) {
			dpy.append((String)stringMessage.obj);
			scl.smoothScrollTo(0,dpy.getBottom());
	        return true;
	    }
	});



	@Override
	public void onCreate(Bundle bundle) {

		super.onCreate(bundle);    
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.winkwonk);

		cmd = (EditText) findViewById(R.id.editText1);
		scl = (ScrollView) findViewById(R.id.scrollView1);
		dpy = (TextView) findViewById(R.id.textView1);
		btn = (Button) findViewById(R.id.button1);

		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String line=cmd.getText().toString();
				irc.ircWrite(line);
				cmd.setText("");
			}
		});

		thread=new Thread(irc);

		thread.start();
	}



	public void display(String line) {
		Message stringMessage = Message.obtain(myTextHandler);
		stringMessage.obj = line+"\r\n";
		stringMessage.sendToTarget();
	}



	class Irc implements Runnable {
		Socket socket;
		BufferedWriter ircWriter;
		BufferedReader ircReader;



		@Override
		public void run() {

			try {

				display("--> Connecting...");

				socket = new Socket(srv,prt);
				
				ircWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				ircReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String line;

				if(pss!=null) ircWrite("PASS "+pss);
				ircWrite("NICK "+nck);
				ircWrite("USER "+nck+" "+nck+" "+nck+" :"+nck);

				while((line=ircRead())!=null) {
					if(line.toUpperCase().startsWith("PING")) {
						ircWrite("PONG "+line.substring(5));
					} else if(line.indexOf("001")!=-1) {
						ircWrite("JOIN "+chn);
					} else {
					}
				}

				socket.close();

			} catch(Exception e) {
				display("Error: "+e.toString());
			}
		}



		void ircWrite(String line) {
			try {
				display("<-- "+line);
				ircWriter.write(line+"\r\n");
				ircWriter.flush();
			} catch(Exception e) {
				display("Error: "+e.toString());
			}
		}



		String ircRead() {
			String line=null;
			try {
				line=ircReader.readLine();
				display("--> "+line);
			} catch(Exception e) {
				display("Error: "+e.toString());
			}
			return line;
		}



	}



}
