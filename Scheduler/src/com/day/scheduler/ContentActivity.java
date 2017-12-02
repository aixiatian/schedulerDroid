package com.day.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.day.entity.ErrorProject;
import com.day.schadualer.R;
import com.day.service.ServiceSchadualer;
import com.day.service.ServiceSchadualerNewHpm;

public class ContentActivity extends Activity /*implements SwipeRefreshLayout.OnRefreshListener*/{
	
	private List<ErrorProject> errProjList = new ArrayList<ErrorProject>();
    private ProjectAdapter projAdapter;

	ServiceSchadualer ss = null;
	String token = "";
	TextView token_text = null;
	SwipeRefreshLayout srl = null;
	
	String username;
	String pwd;
	static int hostidx;
	
	
	
	/* private Handler mHandler = new Handler()  
	    {  
	        public void handleMessage(android.os.Message msg)  
	        {  
	            switch (msg.what)  
	            {  
	            case REFRESH_COMPLETE:  
	            	String resStr = ss.getProjectsForStatus("FAILED");
					errProjList.clear();
					errProjList = getResponseMap(resStr);
					initArrayListView();
					if(errProjList.size() < 1){
						token_text.setText("�Բ���δ�ҵ����������ļ�¼!");
					}  
					projAdapter.notifyDataSetChanged();
					srl.setRefreshing(false);  
	                break;  
	  
	            }  
	        };  
	    }; 
	
	int ref = 0;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projects);
		
		Intent i = getIntent();
		username = i.getStringExtra("username");
		pwd = i.getStringExtra("pwd");
		hostidx = i.getIntExtra("hostidx", 1);
		token_text = (TextView)findViewById(R.id.token_text);
		
		
		GetListView gv = new GetListView();
		gv.execute(username,pwd,hostidx+"");
		/*srl = (SwipeRefreshLayout)findViewById(R.id.proj_down_refresh);
		srl.setOnRefreshListener(this);  
		srl.setColorSchemeColors(R.color.down_refresh_color_1, R.color.down_refresh_color_2,  
				R.color.down_refresh_color_3, R.color.down_refresh_color_4);
		ref++;*/
	}
	
	private void initArrayListView(){
		ListView errProjsListView = (ListView)findViewById(R.id.listView);
		projAdapter = new ProjectAdapter();
		errProjsListView.setAdapter(projAdapter);
		errProjsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ErrorProject ep = errProjList.get(position);
				String grainsid = ep.getGraphInsId();
				String idstr = ep.getId();
				String projid = ep.getProjectId();
				if(idstr.contains("@") && !idstr.contains("@24"))
					projid = idstr.replace(grainsid, "");
				
				Intent i = new Intent();
				i.setClass(ContentActivity.this, NodeActivity.class);
				i.putExtra("projid", projid);
				i.putExtra("insgraid", grainsid);
				i.putExtra("username", username);
				i.putExtra("pwd", pwd);
				i.putExtra("hostidx", hostidx);
				startActivity(i);
			}
			
		});
	}
	
	private class ProjectAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return errProjList.size();
		}

		@Override
		public Object getItem(int position) {
			return errProjList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			ErrorProject rp = errProjList.get(position);
			// �۲�convertView��ListView�������
			LayoutInflater inflater = (LayoutInflater) ContentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_list_projects, null);
				holder = new ViewHolder();
				/* �õ������ؼ��Ķ��� */
				holder.ImageView = (ImageView) convertView.findViewById(R.id.proj_icon);
				holder.textView = (TextView) convertView.findViewById(R.id.proj_name);
				convertView.setTag(holder);// ��ViewHolder����
			} else {
				holder = (ViewHolder) convertView.getTag();// ȡ��ViewHolder����
			}
			holder.textView.setText(rp.getName()+"\n"+rp.getDesc());
			return convertView;
		}
          
		
		private class ViewHolder{
			public ImageView ImageView;
			public TextView textView;
		}
		
	}
	
	
	
	private class GetListView extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			String username = params[0];
			String pwd = params[1];
			int hostidx = Integer.parseInt(params[2]);
			if(hostidx == 2){
				ss = new ServiceSchadualerNewHpm(username,pwd,hostidx);
			}else{
				ss = new ServiceSchadualer(username, pwd, hostidx);
			}
			token = ss.login(username, pwd);
			String resStr = ss.getProjectsForStatus("FAILED");
			return resStr;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			token_text.setText("");
			errProjList.clear();
			errProjList = getResponseMap(result);
			initArrayListView();
			if(errProjList.size() < 1){
				token_text.setText("Yeah!ȫ����������!^.^");
			}
			
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			token_text.setText("loading ...");
		}
	}
	
	static List<ErrorProject> getResponseMap(String json){
		List<ErrorProject> items = new ArrayList<ErrorProject>();
		try {
			JSONObject jo = new JSONObject(json);
			String success = hostidx == 2 ? jo.getString("code"):jo.getString("status");
			if(!"success".equals(success) && !"000".equals(success))
				throw new RuntimeException("����ֵ״̬->"+success);
			JSONArray jarr = jo.getJSONArray("data");
			if(jarr.length()>0){
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject o = (JSONObject)jarr.get(i);
					items.add(ErrorProject.jsonObj2Node(o));
				}
				return items;
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return items;
	}

	/*@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
	}*/
	
}
