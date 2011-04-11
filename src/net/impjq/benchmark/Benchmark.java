package net.impjq.benchmark;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.w3c.dom.Text;

import android.R.integer;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Benchmark extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private static final String TAG = Benchmark.class.getSimpleName();
	TextView mTextView;
	Button mStartButton;
	EditText mUrEditText;
	EditText mNThreadEditText;
	EditText mTimesEditText;

	long mStartTime;
	Date mStartDate;
	long mFinishTime;
	Date mFinishDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mTextView = (TextView) findViewById(R.id.textview);
		mUrEditText = (EditText) findViewById(R.id.url_edittext);
		mNThreadEditText = (EditText) findViewById(R.id.nthread_edittext);
		mTimesEditText = (EditText) findViewById(R.id.times_edittext);
		mStartButton = (Button) findViewById(R.id.start_button);

		mStartButton.setOnClickListener(this);
	}

	int count = 0;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Object obj = msg.obj;
			int key = msg.what;

			count++;

			String tmpString = mResultText;
			tmpString += getString(R.string.count_times) + " " + count + '\n';
			mTextView.setText(tmpString);
			// mTextView.setText("Run for " + count + " times");

			int total = getTimes();
			if (count >= total) {
				enable();
				count = 0;
				mTextView.append('\n' + getString(R.string.finish));
				printResult();
			}

			switch (key) {
			case 0:

				break;

			default:
				break;
			}

		}

	};

	public void enable() {
		mStartButton.setEnabled(true);
		mUrEditText.setEnabled(true);
		mNThreadEditText.setEnabled(true);
		mTimesEditText.setEnabled(true);
	}

	public void disable() {
		mStartButton.setEnabled(false);
		mUrEditText.setEnabled(false);
		mNThreadEditText.setEnabled(false);
		mTimesEditText.setEnabled(false);
	}

	String mResultText;

	public static final String REPLACE_COUNT = "REPLACE_COUNT";

	public void start() {
		mStartTime = System.currentTimeMillis();
		mStartDate = new Date();

		int nThread = getNThread();
		ExecutorService service = Executors.newFixedThreadPool(nThread);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				doWork();
			}
		};

		int times = getTimes();

		for (int i = 0; i < times; i++) {
			service.execute(runnable);
		}

		String url = mUrEditText.getText().toString();

		mResultText = getString(R.string.start_benchmark);
		mResultText += '\n';
		mResultText += url + '\n';
		mResultText += nThread + " "
				+ getString(R.string.thread_running_at_the_same_time) + '\n';
		mResultText += getString(R.string.total_times) + " " + times + '\n';
		// mResultText += getString(R.string.replace_count);
		mTextView.setText(mResultText);

		service.shutdown();
		// mStartButton.setEnabled(false);
		disable();
	}
	// git remote add origin git@github.com:pjq/Benchmark.git
//	  git push -u origin master

	private void printResult() {
		// TODO Auto-generated method stub
		mFinishTime = System.currentTimeMillis();
		mFinishDate = new Date();
		long useTime = mFinishTime - mStartTime;
		String result = "***************************" + '\n';
		result += getString(R.string.start_at) + " "
				+ mStartDate.toLocaleString() + '\n';
		result += getString(R.string.finish_at) + " "
				+ mFinishDate.toLocaleString() + '\n';
		result += getString(R.string.used_time) + " " + (useTime / 1000) + " "
				+ getString(R.string.second) + '\n';
		mTextView.append('\n' + result);
	}

	public int getTimes() {
		String timeString = mTimesEditText.getText().toString();
		int times = Integer.valueOf(timeString);
		return times;
	}

	public int getNThread() {
		String nThreadString = mNThreadEditText.getText().toString();
		int nThread = Integer.valueOf(nThreadString);
		return nThread;

	}

	public void doWork() {
		URL url;
		try {
			url = new URL("http://www.taobao.com/");
			URLConnection connection = url.openConnection();

			InputStream inputStream = connection.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				Log.i(TAG, line);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateTimes();
	}

	/**
	 * Use {@link #mHandler} to update the TextView.
	 */
	private void updateTimes() {
		Message msg = mHandler.obtainMessage(0, "");
		mHandler.sendMessage(msg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();

		switch (key) {
		case R.id.start_button:
			start();
			break;

		default:
			break;
		}
	}
}