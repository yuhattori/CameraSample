package co.jp.camerasample.Fragment;


import co.jp.camerasample.R;
import co.jp.camerasample.Extra.CamerSurfaceView;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class TopFragment extends Fragment {
	private static final String TAG = TopFragment.class.getSimpleName();
	private Activity mAct;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		init();
		
		mAct.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        LinearLayout l = new LinearLayout(mAct);
        l.addView(new CamerSurfaceView(mAct));
        mAct.setContentView(l);
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	private void init() {
		mAct=this.getActivity();
		
	}
}
