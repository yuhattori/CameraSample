package co.jp.camerasample.Extra;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.text.GetChars;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

public class CamerSurfaceView extends SurfaceView implements Callback {
	private static final String TAG = CamerSurfaceView.class.getSimpleName();;
	private Camera mCamera;
	Activity mAct;

	public CamerSurfaceView(Context context) {
		super(context);
		mAct = (Activity) context;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		// holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			Log.d(TAG, "カメラの取得に失敗しました");
			Toast.makeText(mAct, "端末のカメラを認識できませんでした。", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters p = mCamera.getParameters();
		//p.setPreviewSize(width/2, height/2);// 機種によって画面いっぱいのサイズではプレビューできない場合がある。
		try {
			mCamera.setParameters(p);
			mCamera.startPreview();
		} catch (Exception e) {
			//プレビュー失敗時
			Toast.makeText(mAct, e.getMessage(), Toast.LENGTH_SHORT).show();
		} 
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.release();
	}

}
