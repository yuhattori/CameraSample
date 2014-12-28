package co.jp.camerasample.Extra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class CamerSurfaceView extends SurfaceView implements Callback,
		PictureCallback {
	private static final String TAG = CamerSurfaceView.class.getSimpleName();;
	private Camera mCamera;
	private CamerSurfaceView mThisSurfaceView;
	Activity mAct;
	File mSaveDirectory;// 写真保存場所
	String mPictureName;// 写真の名前
	private boolean mIsTake = false;// 撮影を連続で2回以上呼ばれないようにする

	public CamerSurfaceView(Context context) {
		super(context);
		init(context);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		// holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// タッチイベントを設定
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (!mIsTake) {
						// 撮影中の2度押し禁止用フラグ
						mIsTake = true;

						//オートフォーカスを行う
						mCamera.autoFocus(new Camera.AutoFocusCallback() {
							@Override
							public void onAutoFocus(boolean success,
									Camera camera) {
								// 画像取得
								mCamera.takePicture(
										null,
										null,
										(Camera.PictureCallback) mThisSurfaceView);
							}
						});
					}
				}
				return true;
			}
		});
	}

	/**
	 * 初期化処理
	 * 
	 * @param context
	 */
	private void init(Context context) {
		mAct = (Activity) context;
		mThisSurfaceView = this;
		mSaveDirectory = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/test");

		if (!mSaveDirectory.exists()) {
			// 保存先フォルダが存在しない時
			mSaveDirectory.mkdir();
			Toast.makeText(mAct,
					mSaveDirectory.getPath() + "写真を保存するディレクトリを作成しました。",
					Toast.LENGTH_SHORT).show();
		}
	}

	// SurfaceViewが作られた時
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();
			mCamera.setDisplayOrientation(90);// デフォルトで横画面になっているため
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			Log.d(TAG, "カメラの取得に失敗しました");
			Toast.makeText(mAct, "端末のカメラを認識できませんでした。", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// surfaceViewが変更された時
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters p = mCamera.getParameters();
		// p.setPreviewSize(width, height);//機種によって画面いっぱいのサイズではプレビューできない場合がある。

		try {
			mCamera.setParameters(p);
			mCamera.startPreview();
		} catch (Exception e) {
			// プレビュー失敗時
			Toast.makeText(mAct, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	// SurfaceViewが終了した時
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.release();
		mCamera = null;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera c) {
		if (data == null) {
			return;
		}
		if (mIsTake) {
			// 現在ファイルの保存処理を行っていなければ保存する
			savePicture(data);// 写真を保存
		}
		// takePicture するとプレビューが停止するので、再度プレビュースタート
		mCamera.startPreview();
	}

	/**
	 * アンドロイドのデータベースへ画像のパスを登録
	 * 
	 * @param path
	 *            登録するパス
	 */
	private void registAndroidDB(String path) {
		// アンドロイドのデータベースへ登録
		ContentValues values = new ContentValues();
		ContentResolver contentResolver = mAct.getContentResolver();
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put("_data", path);
		contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				values);
	}

	/**
	 * 写真を保存する
	 * 
	 * @param data
	 *            　写真データ
	 */
	private void savePicture(byte[] data) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		mPictureName = format.format(cal.getTime()) + ".jpg";

		// 保存パス
		String imgPath = mSaveDirectory.getPath() + "/" + mPictureName;
		// ファイル保存
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imgPath, true);
			fos.write(data);
			fos.close();

			// アンドロイドのデータベースへ登録
			registAndroidDB(imgPath);

			Toast.makeText(mAct, "ファイルを保存しました。", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(mAct, "ファイルの保存に失敗しました。", Toast.LENGTH_SHORT).show();
		}
		fos = null;

		mIsTake = false;// 保存終了の状態にする
	}
}
