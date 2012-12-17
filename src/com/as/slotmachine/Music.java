package com.as.slotmachine;

import android.content.Context;
import android.media.MediaPlayer;

public class Music 
{
	private static MediaPlayer mp = null;
	
	public static void play(Context context, int resource) 
	{
		stop(context);
	    mp = MediaPlayer.create(context, resource);
	    mp.setLooping(true);
	    mp.start();
	}
	
	public static void sound (Context context, int resource) 
	{
		MediaPlayer sp;
	    sp = MediaPlayer.create(context, resource);
	    sp.start();
	}
	   
	   
	public static void stop(Context context) 
	{ 
		if (mp != null) 
		{
	         mp.stop();
	         mp.release();
	         mp = null;
	    }
	}
}
