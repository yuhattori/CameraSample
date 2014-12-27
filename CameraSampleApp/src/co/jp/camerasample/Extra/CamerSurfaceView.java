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
import android.widget.Toast;

public class CamerSurfaceView extends SurfaceView implements Callback,
		PictureCallback {
	private static final String TAG = CamerSurfaceView.class.getSimpleName();;
	private Camera mCamera;
	Activity mAct;
	File mSaveDirectory;//写真保存場所
	String mPictureName;

	public CamerSurfaceView(Context context) {
		super(context);
		init(context);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		// holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * 初期化処理
	 * @param context 
	 */
	private void init(Context context) {
		mAct = (Activity) context;
		mSaveDirectory= new File(Environment.getExternalStorageDirectory()
				.getPath() + "/test");
		
		if (!mSaveDirectory.exists()) {
			mSaveDirectory.mkdir();
			Toast.makeText(mAct, mSaveDirectory.getPath()+"写真を保存するディレクトリを作成しました。",
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
		// p.setPreviewSize(width/2, height/2);//
		// 機種によって画面いっぱいのサイズではプレビューできない場合がある。

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

        // 画像保存パス
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String imgPath =mSaveDirectory.getPath() + "/" + sf.format(cal.getTime()) + ".jpg";
System.out.println("");
        // ファイル保存
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imgPath, true);
            fos.write(data);
            fos.close();

            // アンドロイドのデータベースへ登録
            // (登録しないとギャラリーなどにすぐに反映されないため)
            registAndroidDB(imgPath);

        } catch (Exception e) {
            Log.e("Debug", e.getMessage());
        }

        fos = null;

        // takePicture するとプレビューが停止するので、再度プレビュースタート
        mCamera.startPreview();

        //mIsTake = false;
    }
    /**
     * アンドロイドのデータベースへ画像のパスを登録
     * @param path 登録するパス
     */
    private void registAndroidDB(String path) {
        // アンドロイドのデータベースへ登録
        // (登録しないとギャラリーなどにすぐに反映されないため)
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = mAct.getContentResolver();
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put("_data", path);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

	private void savePicture(Bitmap bmp) {
		// TODO 自動生成されたメソッド・スタブ
		MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
				bmp,mPictureName, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			mCamera.takePicture(null, null, this);
		}
		return true;
	}

}
