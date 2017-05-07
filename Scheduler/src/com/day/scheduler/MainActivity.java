package com.day.scheduler;

import com.day.schadualer.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private SharedPreferences sp ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);
		final TextView username = (TextView)findViewById(R.id.login_edtId);
		final TextView pwd = (TextView)findViewById(R.id.login_edtPwd);
		
		sp = this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
		username.setText(sp.getString("username", ""));
		pwd.setText(sp.getString("pwd", ""));
		Button btn_login = (Button)findViewById(R.id.login_btnLogin);
		btn_login.setOnClickListener(new OnClickListener() {
			int hostidx = 1;
			
			@Override
			public void onClick(View v) {
				final String pwdStr = pwd.getText().toString();
				final String usernameStr = username.getText().toString();
				
				int id = rg.getCheckedRadioButtonId();
				if(id == R.id.radio_test)
					hostidx = 0;
				else
					hostidx = 1;
				goToContentView(usernameStr, pwdStr,hostidx);
			}
		});
	}
	
	public void goToContentView(String username,String pwd,int hostidx){
		Intent i = new Intent();
		i.setClass(this, ContentActivity.class);
		i.putExtra("username", username);
		i.putExtra("pwd", pwd);
		Editor edit = sp.edit();
		edit.putString("username", username);
		edit.putString("pwd", pwd);
		edit.commit();
		i.putExtra("hostidx", hostidx);
		startActivity(i);
		this.finish();
	}
}
