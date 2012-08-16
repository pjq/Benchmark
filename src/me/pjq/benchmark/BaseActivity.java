
package me.pjq.benchmark;

//import com.mobclick.android.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author pjq0274@arcsoft.com
 * @date 2011-4-12
 */

public class BaseActivity extends Activity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

//        MobclickAgent.onPause(this);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        View view = getCurrentFocus();
        // view = getWindow().getDecorView();
        if (null == view) {
            view = getWindow().getDecorView();
        }

        if (null == view) {
            return;
        }

        IBinder token = view.getWindowToken();
        if (null == token) {
            return;
        }

        inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
