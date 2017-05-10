package cn.sina.is;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestGetNews {

	static CloseableHttpClient client = HttpClients.createDefault();
	
	public static void main(String[] args) {
//		String req = "https://ent.sina.cn/star/tv/2017-05-10/detail-ifyeycfp9440805.d.html";
		String req = "https://sports.sina.cn/china/2017-05-10/detail-ifyfeius7787037.d.html";
		
		HttpGet get = new HttpGet(req);
//Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//Accept-Encoding:gzip,deflate,sdch
//Accept-Language:zh-CN,zh;q=0.8
//Connection:keep-alive
//		User-Agent:Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36
		get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		get.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		get.addHeader("Connection", "keep-alive");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36");
		String html = "";
		try {
			CloseableHttpResponse res = client.execute(get);
			html = EntityUtils.toString(res.getEntity());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(html);
		Document doc = Jsoup.parse(html);
		//获取标题
		String title = doc.getElementsByClass("art_title_h1").text();
		System.out.println(title);
		//摘要
		String description = doc.getElementsByAttributeValue("name", "description").attr("content");
		System.out.println(description);
		
		//正文
		String body = "";
		Elements bodyeles = doc.getElementsByClass("art_t");
		int size = bodyeles.size();
		for (Element e : bodyeles) {
			body += e.text();
		}
		System.out.println(body);
	}
}
