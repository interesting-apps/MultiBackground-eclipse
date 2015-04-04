package com.apps.interestingapps.multibackground.animation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.view.SurfaceHolder;

public class SquareWithIncreasingDimensionsAnimation implements BitmapAnimation {

	private static final String TAG = "SquareWithIncreasingDimensionsAnimation";
	private int screenX, screenY;

	public SquareWithIncreasingDimensionsAnimation(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
	}

	public void draw(SurfaceHolder surfaceHolder,
			Bitmap oldBitmap,
			Bitmap nextBitmap) {
		if (oldBitmap == null) {
			Log.d(TAG, "Old bitmap is null.");
			return;
		}

		if (nextBitmap == null) {
			Log.d(TAG, "Next Bitmap is null.");
			return;
		}

		BitmapShader nextBitmapShader = new BitmapShader(nextBitmap,
				Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		BitmapShader oldBitmapShader = new BitmapShader(oldBitmap,
				Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		if (screenX <= 0 || screenY <= 0) {
			Log.d(TAG,
					"Screen X or Screen Y not set. Call the method to set animation params.");
			return;
		}

		Paint p1 = new Paint(Paint.FILTER_BITMAP_FLAG);
		p1.setShader(oldBitmapShader);
		int totalTimeInMillis = 500, timeTakenInMillis = 0;
		long timeDiffPreviousFrame = 0, startTimeForFrame = 0;
		Paint p2 = new Paint(Paint.FILTER_BITMAP_FLAG);
		p2.setShader(nextBitmapShader);
		Canvas canvas = null;
		int screenMidX = screenX / 2, screenMidY = screenY / 2;
		int currentX = 0, currentY = 0;
		int currentLength = 1;
		int smallerScreenEdge = screenX < screenY ? screenX : screenY;
		while (timeTakenInMillis < totalTimeInMillis
				&& currentLength <= smallerScreenEdge) {
			startTimeForFrame = System.currentTimeMillis();
			canvas = surfaceHolder.lockCanvas();
			currentLength = smallerScreenEdge * timeTakenInMillis
					/ totalTimeInMillis;

			canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
			canvas.drawPaint(p1);
			currentX = screenMidX - currentLength / 2;
			currentY = screenMidY - currentLength / 2;
			if (currentX < 0 || currentY < 0) {
				surfaceHolder.unlockCanvasAndPost(canvas);
				break;
			}
			canvas.drawRect(currentX, currentY, currentX + currentLength,
					currentY + currentLength, p2);
			surfaceHolder.unlockCanvasAndPost(canvas);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timeDiffPreviousFrame = System.currentTimeMillis()
					- startTimeForFrame;
			timeTakenInMillis += timeDiffPreviousFrame;
		}
		canvas = surfaceHolder.lockCanvas();
		canvas.drawPaint(p2);
		surfaceHolder.unlockCanvasAndPost(canvas);
	}
}