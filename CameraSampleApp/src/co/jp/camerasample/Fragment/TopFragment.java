package co.jp.camerasample.Fragment;


import co.jp.camerasample.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopFragment extends Fragment {
	private static final String TAG = TopFragment.class.getSimpleName();
	private SharedPreferences mPref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_main, container, false);
	}
}
