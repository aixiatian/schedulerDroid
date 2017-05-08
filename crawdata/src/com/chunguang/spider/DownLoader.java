package com.chunguang.spider;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DownLoader {

	HttpClient client = HttpClients.createDefault();

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}
	
	
	public HtmlPage executeGet(HttpGet get){
		try {
			HttpResponse res = client.execute(get);
			return HtmlPage.create(EntityUtils.toString(res.getEntity()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public HtmlPage executePost(HttpPost post){
		try {
			HttpResponse res = client.execute(post);
			return HtmlPage.create(EntityUtils.toString(res.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
