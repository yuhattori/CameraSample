package co.jp.camerasample.Fragment;

import co.jp.camerasample.R;
import co.jp.camerasample.Extra.CameraSurfaceView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TopFragment extends Fragment {
	private static final String TAG = TopFragment.class.getSimpleName();
	private Activity mAct;

	private CameraSurfaceView mCameraSurfaceView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		init();

		mAct.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		FrameLayout frameLayout = new FrameLayout(mAct);
		mCameraSurfaceView = new CameraSurfaceView(mAct);
		frameLayout.addView(mCameraSurfaceView);
		View rectView = new View(mAct) {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			@Override
			protected void onDraw(Canvas canvas) {
				Paint paint = new Paint();
				paint.setColor(Color.BLACK);
				paint.setAlpha(50);

				WindowManager wm = mAct.getWindowManager();
				// Displayのインスタンス取得
				Display disp = wm.getDefaultDisplay();
				float centerX;
				float centerY;

				// バージョンによってはgetWidth、getHeighが非推奨の場合があるため
				if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {
					centerX = disp.getWidth() / 2f;
					centerY = disp.getHeight() / 2f;
				} else {
					Point size = new Point();
					disp.getSize(size);
					centerX = size.x / 2f;
					centerY = size.y / 2f;
				}
				canvas.drawCircle(centerX, centerY, 180, paint);
			}
		};

		frameLayout.addView(rectView);
		mAct.setContentView(frameLayout);
		// return inflater.inflate(R.layout.fragment_main, container, false);
		return this.getView();
	}

	private void init() {
		mAct = this.getActivity();

	}
}
