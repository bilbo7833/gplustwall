package de.devfest.gplustwall.ui;


import de.devfest.gplustwall.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public final class SpeechBubble extends View {

	private Bitmap mBackground;
	
	public SpeechBubble(Context context) {
		super(context);
		mBackground = BitmapFactory.decodeResource(getResources(),R.drawable.sprechblase);
	}

	
	public void onDraw(final Canvas canvas){
		super.onDraw(canvas);
		//canvas.drawBitmap(mBackground, matrix, paint);
	}
}
