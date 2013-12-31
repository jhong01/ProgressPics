package com.hsi.progresspics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class CardInfo {
	String filename;
	String weight;
	String comments;

	public CardInfo(String filename, String weight, String comments) {
		this.filename = filename;
		this.weight = weight;
		this.comments = comments;
	}

	public CardInfo() {

	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFilename() {
		return filename;
	}

	public String getWeight() {
		return weight;
	}

	public String getComments() {
		return comments;
	}

	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight,
			int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		// Compute the scaling factors to fit the new height and width,
		// respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}
}
