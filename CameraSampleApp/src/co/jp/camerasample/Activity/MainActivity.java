package co.jp.camerasample.Activity;

import co.jp.camerasample.R;
import co.jp.camerasample.Fragment.TopFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends FragmentActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Fragmentを管理するFragmentManagerを取得
		FragmentManager manager = getSupportFragmentManager();
		// 追加や削除などを1つの処理としてまとめるためのトランザクションクラスを取得
		FragmentTransaction tx = manager.beginTransaction();

		TopFragment fragment = new TopFragment();
		// Fragment をスタックに追加する
		// メインレイアウトに対して追加先のビューのID、Fragment、Fragmentのタグ。
		// add() したときに既にバックスタックに同じタグの Fragment が存在する場合、
		// Fragment は新規作成されず、既にインスタンス化してある Fragment が再表示される。
		tx.add(R.id.top_main, fragment, "top_layout");
		tx.commit();
	}
}
