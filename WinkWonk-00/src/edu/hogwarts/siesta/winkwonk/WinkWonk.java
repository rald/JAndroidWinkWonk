package edu.hogwarts.siesta.winkwonk;

import android.app.Activity;
import android.os.Bundle;

import android.media.AudioManager;

public class WinkWonk extends Activity {

  Animation animation;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);

	setVolumeControlStream(AudioManager.STREAM_MUSIC);

    animation=new Animation(this);
    setContentView(animation);
  }

}
