package test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.mytest.R;

public class MytestActivity extends Activity{
	
	TextView test = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//01-14 15:25:23.506: E/AndroidRuntime(387): Caused by: java.lang.ClassNotFoundException: com.example.mytest.MytestActivity in loader dalvik.system.PathClassLoader[/data/app/com.example.mytest-2.apk]
		setContentView(R.layout.loginpage_1);
		
//		test = (TextView)findViewById(R.id.test_view);
//		
//		test.setText("hello world!");
	}
	
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		
		return super.onCreateView(name, context, attrs);
	}
}
