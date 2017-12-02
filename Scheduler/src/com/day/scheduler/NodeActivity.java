package com.day.scheduler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.day.entity.ErrorNode;
import com.day.schadualer.R;
import com.day.service.ServiceSchadualer;
import com.day.service.ServiceSchadualerNewHpm;
import com.day.wiget.SwipeMenu;
import com.day.wiget.SwipeMenuCreator;
import com.day.wiget.SwipeMenuItem;
import com.day.wiget.SwipeMenuListView;

public class NodeActivity extends Activity{


	
	private List<ErrorNode> mAppList = new ArrayList<ErrorNode>();
    private AppAdapter mAdapter;

	ServiceSchadualer ss = null;
	String token = "";
	TextView token_text = null;
	
	String insgraid;
	String projid;
	String username;
	String pwd;
	static int hostidx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nodes);
		
		Intent i = getIntent();
		insgraid = i.getStringExtra("insgraid");
		projid = i.getStringExtra("projid");
		pwd = i.getStringExtra("pwd");
		username = i.getStringExtra("username");
		hostidx = i.getIntExtra("hostidx", 1);
		token_text = (TextView)findViewById(R.id.token_text);
	
		GetListView get = new GetListView();
		get.execute(username,pwd,hostidx+"");
		
		
	}
	
	private void initSwipeListView(){
		SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.swipeMenuListView);
        mAdapter = new AppAdapter();
        listView.setAdapter(mAdapter);
        
        SwipeMenuCreator creator = new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
				createMenu1(menu);
			}
			
			private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0x18,
                        0x5E)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_favorite);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.retry);
                menu.addMenuItem(item2);
                SwipeMenuItem item3 = new SwipeMenuItem(
                        getApplicationContext());
                item3.setBackground(new ColorDrawable(Color.rgb(0xE5, 0xE0,
                        0x3F)));
                item3.setWidth(dp2px(90));
                item3.setIcon(R.drawable.pass);
                menu.addMenuItem(item3);
            }

		};
		
		listView.setMenuCreator(creator);
		
		listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            	ErrorNode item = mAppList.get(position);
            	System.out.println("index-->"+index);
                switch (index) {
                    case 0:
                        // �鿴��־
                    	String outMsg = item.getOutMsg();
                    	String runIp = item.getRunIP();
                    	catLogFile(outMsg, runIp);
                        break;
                    case 1:
                        // ���Խڵ�
//					delete(item);
                    	retryNode(position);
                        
                        break;
                    case 2:
                    	// �����ڵ�
                    	
                    	passNode(position);
                    	break;
                }
                return false;
            }
        });
	}
	
	private void catLogFile(String outMsg,String runIp){
		String amsg = outMsg;
		try {
			byte[] sstr = outMsg.getBytes("UTF-8");
			amsg = new String(sstr,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = ss.getLogUrlFromMsg(amsg, runIp);
		Intent i = new Intent();
		i.setClass(getApplicationContext(), LogCatActivity.class);
		i.putExtra("url", url);
		startActivity(i);
	}
	
	private boolean retryNode(final int position){
		ErrorNode node = mAppList.get(position);
		String nodeInsid = node.getId();
		
		SetStatus sstus = new SetStatus("[���Խڵ�]");
		SetStatus sres = (SetStatus)sstus.execute(insgraid,nodeInsid);
		
		return sres.success;
	}
	private boolean passNode(final int position){
		ErrorNode node = mAppList.get(position);
		String nodeInsid = node.getId();
		
		SetStatus sstus = new SetStatus("[�����ڵ�]");
		SetStatus sres = (SetStatus)sstus.execute(insgraid,nodeInsid);
		
		return sres.success;
	}
	
	class SetStatus extends AsyncTask<String, Integer, String>{

		String desc ;
		int position;
		public boolean success = false;
		
		public SetStatus(String desc){
			this.desc = desc;
		}
		
		@Override
		protected String doInBackground(String... params) {
			String insgraid = params[0];
			String nodeInsID = params[1];
			String str = "";
			if(desc.contains("����")){
				str = ss.retryNode(insgraid, nodeInsID);
			}else if(desc.contains("����")){
				str = ss.passNode(insgraid, nodeInsID);
			}
			return str;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(getApplicationContext(), "����"+desc+"���Ժ�...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//{"status":"success","why":"SUCCESSFUL"}
			if(result.contains("SUCCESSFUL")){
				Toast.makeText(getApplicationContext(), desc+"�ɹ���", Toast.LENGTH_SHORT).show();
				mAppList.remove(position);
		        mAdapter.notifyDataSetChanged();
		        success = true;
			}else {
				Toast.makeText(getApplicationContext(), desc+"ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			}
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
			ss.login(username, pwd);
			String resStr = ss.getNodesForProject(projid, insgraid);
			return resStr;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			token_text.setText("");
			mAppList.clear();
			mAppList = getResponseMap(result);
			initSwipeListView();
			if(mAppList.size() < 1){
				token_text.setText("�Բ���δ�ҵ����������ļ�¼!");
			}
			
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			token_text.setText("loading ...");
		}
	}
	
	static List<ErrorNode> getResponseMap(String json){
		List<ErrorNode> items = new ArrayList<ErrorNode>();
		try {
			JSONObject jo = new JSONObject(json);
			String success = hostidx == 2 ? jo.getString("code"):jo.getString("status");
			if(!"success".equals(success) && !"000".equals(success))
				throw new RuntimeException("����ֵ״̬->"+success);
			JSONObject data = jo.getJSONObject("data");
			JSONArray nodes = data.getJSONArray("nodes");
			if(nodes.length()>0){
				for (int i = 0; i < nodes.length(); i++) {
					JSONObject o = (JSONObject)nodes.get(i);
					items.add(ErrorNode.getErrNodeFromJson(o));
				}
				return items;
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public ErrorNode getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            // menu type count
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            // current menu type
            return position % 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_nodes, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ErrorNode item = getItem(position);
            final String status = item.getStatus();
            System.out.println(position+":״̬--->"+status);
            if("COMPELETED".equals(status))
            	holder.iv_icon.setImageResource(R.drawable.success_1);
            else if ("RUNNING".equals(status))
            	holder.iv_icon.setImageResource(R.drawable.running);
            else if("FAILED".equals(item.getStatus()))
            	holder.iv_icon.setImageResource(R.drawable.err);
            else if("READY".equals(item.getStatus()))
            	holder.iv_icon.setImageResource(R.drawable.ready_1);
            else
            	holder.iv_icon.setImageResource(R.drawable.inited);
            	
            	
            holder.tv_name.setText(item.getName());
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }
    }
	
	private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


	
}
