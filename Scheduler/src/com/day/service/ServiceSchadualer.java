package com.day.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ServiceSchadualer implements Serializable{

	private static final long serialVersionUID = 1L;
	String token = "";
	String username="";
	String ssn="";
	CloseableHttpClient client = null;
	
	public String getToken() {
		return token;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public CloseableHttpClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}

	String pwd = "";
	//0 :测试
	//1 :正式
	String[] hosts = new String[]{
		"yz2154.hadoop.data.sina.com.cn:80","scheduler.data.sina.com.cn:8080"
		};
	
	int hostIdx = 0;
	
	/**
	 * 
	 * @param username 用户名
	 * @param pwd		密码
	 * @param hostidx	正式or测试
	 */
	public ServiceSchadualer(String username,String pwd,int hostidx){
		this.username = username;
		this.pwd = pwd;
		this.hostIdx = hostidx;
	}
	
	public String getLogUrlFromMsg(String msgOut,String runIp){
		String errUrl = "";
		errUrl = msgOut.substring(msgOut.indexOf("[INFO] 日志文件:")+12);
		errUrl = errUrl.substring(0,errUrl.indexOf(".log")+4);
		System.out.println("url-->"+errUrl);
		return "http://"+runIp+":8081/viewLog?log_path="+errUrl;
	}
	
	/*public static void main(String[] args) {
		ServiceSchadualer ss = new ServiceSchadualer("dongkai3","@#dk1987",0);
//		login(username, pwd);
		*//***
		 * RUNNING ：运行中 
		 * INITED ：等待中
		 * FAILED ：错误 
		 * COMPELETED ：已完成
		 *//*
//		getProjectsForStatus("FAILED");
		//"ae003556-334d-11e4-bfec-14feb5d379aa"],
		 //"insGraphID":"29509C4B-CF17-D1F6-3EAE-D388D2F8354C"
//		ss.getNodesForProject("ae003556-334d-11e4-bfec-14feb5d379aa", "29509C4B-CF17-D1F6-3EAE-D388D2F8354C");
	}*/
	
	public String login(String username,String pwd) {
//		client = HttpClients.createDefault();
		client = HttpClients.createDefault();
		
		
		HttpGet get = new HttpGet("https://cas.erp.sina.com.cn/cas/login?ext=http://"+hosts[hostIdx]+"/schedulerManager/&service=http://"+hosts[hostIdx]+"/schedulerManager/index.php/login");
		get.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("Connection", "Keep-Alive");
		get.addHeader("Host", "cas.erp.sina.com.cn");
		get.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
		String res = "";
		try {
			HttpResponse resp = client.execute(get);
			int status = resp.getStatusLine().getStatusCode();
//			if(302 != status)
//				return "登录失败！";
			if(302 == status){
				String uri = resp.getFirstHeader("Location").getValue();
				HttpGet g = new HttpGet(uri);
				resp = client.execute(g);
			}
			String body = EntityUtils.toString(resp.getEntity(), "utf-8");
			String lt = body.split("name=\"lt\" value=\"")[1];
			lt = lt.substring(0,lt.indexOf("\""));
			HttpPost post = new HttpPost("https://cas.erp.sina.com.cn/cas/login?ext=http://"+hosts[hostIdx]+"/schedulerManager/&service=http://"+hosts[hostIdx]+"/schedulerManager/index.php/login");
			post.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			post.addHeader("Accept-Encoding", "gzip, deflate");
			post.addHeader("Accept-Language", "zh-CN");
			post.addHeader("Cache-Control", "no-cache");
			post.addHeader("Connection", "Keep-Alive");
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
			post.addHeader("Host", "cas.erp.sina.com.cn");
			post.addHeader("Referer", "https://cas.erp.sina.com.cn/cas/login?ext=http://"+hosts[hostIdx]+"/schedulerManager/&service=http://"+hosts[hostIdx]+"/schedulerManager/index.php/login");
			post.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
			
			Map<String,String> parameterMap = new HashMap<String,String>();
		    parameterMap.put("auth_type", "ldap");
		    parameterMap.put("ext", "http://"+hosts[hostIdx]+"/schedulerManager/");
		    parameterMap.put("lt", lt);
		    parameterMap.put("password", pwd);
		    parameterMap.put("qrc_email", "");
		    parameterMap.put("qrc_status", "");
		    parameterMap.put("username", username);
		    
		    UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
		    post.setEntity(postEntity);
		    
		    HttpResponse presp = client.execute(post);
		    body = EntityUtils.toString(presp.getEntity(), "utf-8");

			String loginUrl = body.substring(body.indexOf("\"")+1, body.lastIndexOf("\""));
			HttpGet login = new HttpGet(loginUrl);
			presp = client.execute(login);
			body = EntityUtils.toString(presp.getEntity(), "utf-8");
			getSsn(body);
			res = getTokenStr(loginUrl);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static List<BasicNameValuePair> getParam(Map<String,String> parameterMap) {
	    List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
	    Iterator<Map.Entry<String,String>> it = parameterMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String,String> parmEntry = it.next();
	      param.add(new BasicNameValuePair((String) parmEntry.getKey(),
	          (String) parmEntry.getValue()));
	    }
	    return param;
	}
	
	public String getSsn(String loginBody){
		
		if("".equals(ssn) || loginBody != null){
			String ssnStr = loginBody.substring(loginBody.indexOf("\"sinaSchedulerSsn\",\"")+20);
			ssn = ssnStr.substring(0,ssnStr.indexOf("\""));
		}
		
		return ssn;
	}
	
	String getTokenStr(String loginurl){
		if("".equals(token)){
			/***
			 * http://yz2154.hadoop.data.sina.com.cn:80/schedulerManager/index.php/login?
			 * ticket=ST-288980-TeOhlvOBjD8zH5bNB0WH
			 * &ext=http%3A%2F%2Fyz2154.hadoop.data.sina.com.cn%3A80%2FschedulerManager%2F
			 */
			token = loginurl.substring(loginurl.indexOf("ticket=")+7,loginurl.indexOf("&"));
		}
		return token;
	}
	
	//RUNNING ：运行中 INITED ：等待中 FAILED ：错误 COMPELETED ：已完成
	public String getProjectsForStatus(String status){
		String url = "http://"+hosts[hostIdx]+"/schedulerManager/index.php/interface/project_instances?";
		url += "ssn="+ssn;
		url += "&token="+token;
		url += "&userName="+username;
		url += "&nodestatus="+status;
		url += "&&nodeExample%5Bid%5D=p_001&nodeExample%5Bname%5D=test1&nodeExample%5BnameCN%5D=%E6%B5%8B%E8%AF%951&nodeExample%5Bdesc%5D=111&nodeExample%5BrunUser%5D=xiaofei5&nodeExample%5BsvnUrl%5D=t&nodeExample%5Bcontent%5D=123&nodeExample%5Bparams%5D=&nodeExample%5BrunIP%5D=10.39.0.100&nodeExample%5Bpriority%5D=3&nodeExample%5Bcreator%5D=xiaofei5&nodeExample%5BcreateTime%5D=20130128&nodeExample%5BupdateTime%5D=20130128&nodeExample%5Bowner%5D=xiaofei5&nodeExample%5Bgroup%5D=&nodeExample%5BdeptID%5D=3&nodeExample%5BprojectID%5D=1&nodeExample%5BmaxRetry%5D=2&nodeExample%5BstartTime%5D=03++00&nodeExample%5BwarningCond%5D=test&nodeExample%5BinputPath%5D%5B%5D=%2Ffile%2Ftblog%2Fbehavior%2Fscribe&nodeExample%5BoutputPath%5D%5B%5D=%2Ffile%2Ftblog%2Fbehavior%2F14000003&nodeExample%5BoutputPath%5D%5B%5D=%2Ffile%2Ftblog%2Fbehavior%2F14000004&nodeExample%5BhadoopVersion%5D=v1&nodeExample%5BparamMap%5D%5Bkey1%5D=val1&nodeExample%5BparamMap%5D%5Bkey2%5D=val2&nodeExample%5BdefID%5D=n_002&nodeExample%5BinsGraphID%5D=ins001&nodeExample%5BactualStartTime%5D=330088888&nodeExample%5BactualEndTime%5D=330088889&nodeExample%5Bprogress%5D=50&nodeExample%5Bstatus%5D=FAILED&nodeExample%5BexitCode%5D=3&nodeExample%5BerrMsg%5D=some+error&nodeExample%5BoutMsg%5D=&nodeExample%5BerrPath%5D=%3A8080%2FlogView%3Flogpath%3D%2Ftemp%2Ftest2.err&nodeExample%5BoutPath%5D=%3A8080%2FlogView%3Flogpath%3D%2Ftemp%2Ftest1.out&nodeExample%5Bdepth%5D=3";
		System.out.println(url);
		HttpGet get = new HttpGet(url);
		String body = "";
		try {
			HttpResponse resp = client.execute(get);
			body = EntityUtils.toString(resp.getEntity(),"utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("projects in status :"+status+"\n\t"+body);
		return body;
	}
	
	public String getNodesForProject(String projid,String insgraid){
		String interface_url = "http://"+hosts[hostIdx]+"/schedulerManager/index.php/interface/rest";
		String res = "";
		HttpPost post = new HttpPost(interface_url);
		
		/***
		 * Accept:application/json, text/javascript, *\*; q=0.01
		 * Accept-Encoding:gzip,deflate,sdch
		 * Accept-Language:zh-CN,zh;q=0.8,en;q=0.6 
		 * Connection:keep-alive
		 * Content-Length:801 
		 * Content-Type:application/x-www-form-urlencoded;charset=UTF-8 
		 * Host:yz2154.hadoop.data.sina.com.cn
		 * Origin:http://yz2154.hadoop.data.sina.com.cn
		 * Referer:http://yz2154.hadoop.data.sina.com.cn/schedulerManager/
		 * User-Agent:Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML,
		 * like Gecko) Chrome/31.0.1650.63 Safari/537.36
		 * X-Requested-With:XMLHttpRequest
		 */
		
		/***
		 * { "projIDs":["ae003556-334d-11e4-bfec-14feb5d379aa"],
		 * "insGraphID":"29509C4B-CF17-D1F6-3EAE-D388D2F8354C", 
		 * "ssn":"200720",
		 * "userName":"dongkai3", 
		 * "token":"ST-319213-i0oQPUAQCPhWZQpircz8" 
		 * }
		 * sendtype:POST 
		 * rdurl:/interface/ins_graph/by_proj_ids
		 * rdm:0.28114761179313064
		 */
		post.addHeader("Accept", "application/json, text/javascript, *\\*; q=0.01");
		post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		post.addHeader("Connection", "Keep-Alive");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		post.addHeader("Host", hosts[hostIdx]);
		post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		post.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		String data = "{";
		data +="\"projIDs\":[\""+projid+"\"],";
		data +="\"insGraphID\":\""+insgraid+"\",";
		data +="\"ssn\":\""+ssn+"\",";
		data +="\"userName\":\""+username+"\",";
		data +="\"token\":\""+token+"\",";
		data += getNodeExecample();
		data += "}";
		
		Map<String,String> parameterMap = new HashMap<String,String>();
	    parameterMap.put("data", data);
	    parameterMap.put("sendtype", "POST");
	    parameterMap.put("rdurl", "/interface/ins_graph/by_proj_ids");
	    parameterMap.put("rdm", Math.random()+"");
	    
	    
	    UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			post.setEntity(postEntity);
			HttpResponse resp = client.execute(post);
			res = EntityUtils.toString(resp.getEntity(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}
	
	static String getNodeExecample(){
		/***
		 * "nodeExample":{
		 * "id":"p_001",
		 * "name":"test1",
		 * "nameCN":"测试1",
		 * "desc":"111",
		 * "runUser":"xiaofei5",
		 * "svnUrl":"t",
		 * "content":"123",
		 * "params":"",
		 * "runIP":"10.39.0.100",
		 * "priority":3,
		 * "creator":"xiaofei5",
		 * "createTime":"20130128",
		 * "updateTime":"20130128",
		 * "owner":"xiaofei5",
		 * "group":"",
		 * "deptID":"3",
		 * "projectID":"1",
		 * "maxRetry":2,
		 * "startTime":"03  00",
		 * "warningCond":"test",
		 * "inputPath":["/file/tblog/behavior/scribe"],
		 * "outputPath":["/file/tblog/behavior/14000003","/file/tblog/behavior/14000004"],
		 * "hadoopVersion":"v1",
		 * "paramMap":{"key1":"val1","key2":"val2"},
		 * "defID":"n_002",
		 * "insGraphID":"ins001",
		 * "actualStartTime":330088888,
		 * "actualEndTime":330088889,
		 * "progress":50,
		 * "status":"FAILED",
		 * "exitCode":3,
		 * "errMsg":"some error",
		 * "outMsg":"",
		 * "errPath":":8080/logView?logpath=/temp/test2.err",
		 * "outPath":":8080/logView?logpath=/temp/test1.out",
		 * "depth":3
		 * }
		 */
		String res = "\"nodeExample\":{\"id\":\"p_001\",\"name\":\"test1\",\"nameCN\":\"测试1\",\"desc\":\"111\",\"runUser\":\"xiaofei5\",\"svnUrl\":\"t\",\"content\":\"123\",\"params\":\"\",\"runIP\":\"10.39.0.100\",\"priority\":3,\"creator\":\"xiaofei5\",\"createTime\":\"20130128\",\"updateTime\":\"20130128\",\"owner\":\"xiaofei5\",\"group\":\"\",\"deptID\":\"3\",\"projectID\":\"1\",\"maxRetry\":2,\"startTime\":\"03  00\",\"warningCond\":\"test\",\"inputPath\":[\"/file/tblog/behavior/scribe\"],\"outputPath\":[\"/file/tblog/behavior/14000003\",\"/file/tblog/behavior/14000004\"],\"hadoopVersion\":\"v1\",\"paramMap\":{\"key1\":\"val1\",\"key2\":\"val2\"},\"defID\":\"n_002\",\"insGraphID\":\"ins001\",\"actualStartTime\":330088888,\"actualEndTime\":330088889,\"progress\":50,\"status\":\"FAILED\",\"exitCode\":3,\"errMsg\":\"some error\",\"outMsg\":\"\",\"errPath\":\":8080/logView?logpath=/temp/test2.err\",\"outPath\":\":8080/logView?logpath=/temp/test1.out\",\"depth\":3}";
		return res;
	}
	
	/***
	 * Request URL:http://yz2154.hadoop.data.sina.com.cn/schedulerManager/index.php/interface/rest
		Request Method:POST
		Status Code:200 OK
		Request Headersview source

		Accept:application/json, text/javascript, *\*; q=0.01
		Accept-Encoding:gzip,deflate,sdch
		Accept-Language:zh-CN,zh;q=0.8,en;q=0.6
		Connection:keep-alive
		Content-Length:358
		Content-Type:application/x-www-form-urlencoded; charset=UTF-8
		Cookie:SINAGLOBAL=61.135.152.210_1436239681.16793; vjuids=-845261764.14e676b0f50.0.94d558f8; SUB=_2AkMix_CPf8NhqwJRmPoWzW_hbo13ywHEiebDAH_sJxIyHlJN7FXRIcR63LxCJzbd27re-wWL7Oi8; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhsghqZHKa04hgfiHpFbNZ8; U_TRS1=000000d2.93c748bd.55c16e85.09a7c611; SGUID=1438740177727_39563340; vjlast=1448018375; lxlrtst=1448019720_o; lxlrttp=1448019720; UOR=video.dp.erp.sina.com.cn,news.video.sina.com.cn,; ULV=1449737480948:15:2:2:10.220.16.182_1449737454.706967:1449737454630; PHPSESSID=8bb5245032f68cceecc02e9dade2e540
		Host:yz2154.hadoop.data.sina.com.cn
		Origin:http://yz2154.hadoop.data.sina.com.cn
		Referer:http://yz2154.hadoop.data.sina.com.cn/schedulerManager/
		User-Agent:Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36
		X-Requested-With:XMLHttpRequest

		Form Dataview sourceview URL encoded

		data:{
		"insGraphID":"29509C4B-CF17-D1F6-3EAE-D388D2F8354C",
		"nodeInsID":"7E200FB4-8226-D210-A3E5-3013A9154127",
		"status":"READY",
		"ssn":"200720",
		"userName":"dongkai3",
		"token":"ST-328192-Pizkxivd0anRAm4V6VjQ"
		}
		sendtype:PUT
		rdurl:/interface/ins_node/set_status
		rdm:0.15614004316739738

		返回：
		{"status":"success","why":"SUCCESSFUL"}
	 * @param nodeid
	 * @category 重试节点
	 * @return
	 */
	public String retryNode(String insGraphID,String nodeInsID){
		String interface_url = "http://"+hosts[hostIdx]+"/schedulerManager/index.php/interface/rest";
		String res = "";
		HttpPost post = new HttpPost(interface_url);
		post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		post.addHeader("Host", hosts[hostIdx]);
		post.addHeader("Origin", "http://"+hosts[hostIdx]);
		post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		
		String data = "{";
		data +="\"insGraphID\":\""+insGraphID+"\",";
		data +="\"nodeInsID\":\""+nodeInsID+"\",";
		data +="\"status\":\"READY\",";
		data +="\"ssn\":\""+ssn+"\",";
		data +="\"userName\":\""+username+"\",";
		data +="\"token\":\""+token+"\"";
		data += "}";
		
		System.out.println("data-->"+data);
		
		Map<String,String> parameterMap = new HashMap<String,String>();
	    parameterMap.put("data", data);
	    parameterMap.put("sendtype", "PUT");
	    parameterMap.put("rdurl", "/interface/ins_node/set_status");
	    parameterMap.put("rdm", Math.random()+"");
	    
	    UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			post.setEntity(postEntity);
			HttpResponse resp = client.execute(post);
			res = EntityUtils.toString(resp.getEntity(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}
	
	public String passNode(String insGraphID,String nodeInsID){
		String interface_url = "http://"+hosts[hostIdx]+"/schedulerManager/index.php/interface/rest";
		String res = "";
		HttpPost post = new HttpPost(interface_url);
		post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		post.addHeader("Host", hosts[hostIdx]);
		post.addHeader("Origin", "http://"+hosts[hostIdx]);
		post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		
		String data = "{";
		data +="\"insGraphID\":\""+insGraphID+"\",";
		data +="\"nodeInsID\":\""+nodeInsID+"\",";
		data +="\"status\":\"COMPELETED\",";
		data +="\"ssn\":\""+ssn+"\",";
		data +="\"userName\":\""+username+"\",";
		data +="\"token\":\""+token+"\"";
		data += "}";
		
		System.out.println("data-->"+data);
		
		Map<String,String> parameterMap = new HashMap<String,String>();
	    parameterMap.put("data", data);
	    parameterMap.put("sendtype", "PUT");
	    parameterMap.put("rdurl", "/interface/ins_node/set_status");
	    parameterMap.put("rdm", Math.random()+"");
	    
	    UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			post.setEntity(postEntity);
			HttpResponse resp = client.execute(post);
			res = EntityUtils.toString(resp.getEntity(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}
}
