package co.jp.camerasample.Extra;

import java.io.IOException;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

public class CuttingPic {
	public static String TAG = "CuttingPic";
	private byte[] mPic;
	private Rect mRect;
	BitmapRegionDecoder mRegionDecoder;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	CuttingPic(byte[] pic, Rect rect) throws Exception {
		this.mPic = pic;
		this.mRect = rect;

		mRegionDecoder = BitmapRegionDecoder.newInstance(pic, 0, pic.length,
				false);

	}

	public Bitmap run() throws Exception {
		if (mRegionDecoder == null) {
			throw new Exception("初期化で失敗しています。");
		}
		Bitmap bitmap = mRegionDecoder.decodeRegion(mRect, null);
		return bitmap;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public byte[] getPic() {
		return mPic;
	}

	public void setPic(byte[] mPic) {
		this.mPic = mPic;
	}

	public Rect getRect() {
		return mRect;
	}

	public void setRect(Rect mRect) {
		this.mRect = mRect;
	}

}
