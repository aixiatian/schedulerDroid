package com.chunguang.neihanduanzi.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.chunguang.neihanduanzi.entity.LoadMoreData;
import com.chunguang.neihanduanzi.entity.UserInfo;
import com.chunguang.neihanduanzi.entity.VideoData;
import com.chunguang.neihanduanzi.entity.VideoGroupData;
import com.chunguang.utils.CUtil;

public class NeiHanSpider {

	static CloseableHttpClient client = HttpClients.createDefault();
	public static String ua = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36";
	public static String video_home = "http://neihanshequ.com/video/";
	public static String home = "http://neihanshequ.com/";
	public static String max_time = "";
	
	public static int loadTimes = 5;
	public static String csrfmiddlewaretoken = null;
	public static String endline = "\n";
	public static String tab = "\t";
	public static String nullstr = "-";
	
	public static Map<String,String> urlMap = new HashMap<String,String>();
	
	public static void main(String[] args) {
		String outputvideo = args[0];
		String outputfile = args[1];
		int loadtimes = Integer.parseInt(args[2]);
		String filetype = args[3];
		
		boolean isloadvideo = false;
		
		StringBuffer sb = new StringBuffer();
		
		if(loadtimes > 0)
			loadTimes = loadtimes;
		
		String home_html = landHomePage();
		
		Document doc = Jsoup.parse(home_html);
		getToken(doc);
		getMaxTime(home_html);
		
		extractUrl(doc);
		addMap2Sb(sb);
		
		
		for (int i = 0; i < loadTimes; i++) {
			
			String str = loadMore();
			System.out.println(str);
			LoadMoreData lmd = new LoadMoreData(str);
			lmd.load();
			String mt = lmd.getVideodata().getMax_time();
			max_time = mt.substring(0,mt.indexOf("."));
			System.out.println("loadtime "+(i+1)+" maxtime:"+max_time);
			loadData2Sb(lmd, sb);
		}
		
		CUtil.writeFile(sb.toString(), outputfile);
		if(args.length >= 5 && args[4].equals("y")){
			isloadvideo = true;
		}
		if(isloadvideo){
			try {
				loadVideo(outputvideo,filetype);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadData2Sb(LoadMoreData lmd,StringBuffer sb){
//		String cid = lmd.getCategory_id();
		VideoData vd = lmd.getVideodata();
		List<VideoGroupData> vgds = vd.getVideogroup();
		for (VideoGroupData vgd : vgds) {
			String cid = vgd.getCategory_id();
			String videoid = vgd.getUri();
			String title = vgd.getText();
			String videourl = vgd.getMp4_url();
			String videotime = vgd.getCreate_time();
			String sharecount = vgd.getShare_count();
			String goodcount = vgd.getFavorite_count();
			String playcount = vgd.getPlay_count();
			String type = vgd.getId();
			UserInfo user = vgd.getUser();
			String usernick = user.getName();
			String userheadimg = user.getAvatar_url();
			String userid = user.getUser_id();
			String cover_image_uri = vgd.getCover_image_uri();
			String catname = vgd.getCategory_name();
			
			sb.append(videoid).append(tab);
			sb.append(title).append(tab);
			sb.append("http://p3.pstatp.com/large/"+cover_image_uri).append(tab);
			sb.append(videourl).append(tab);
			sb.append(videotime).append(tab);
			sb.append(sharecount).append(tab);
			sb.append(goodcount).append(tab);
			sb.append(playcount).append(tab);
			sb.append(type).append(tab);
			sb.append(cid).append(tab);
			sb.append(usernick).append(tab);
			sb.append(userheadimg).append(tab);
			sb.append(userid).append(tab);
			sb.append(catname).append(endline);
			
			urlMap.put(videourl, title);
		}
	}
	
	/**
	 * 
	 * videoId
		videoTitle
		videoImageUrl
		videoUrl
		videoTime
		shareCount
		goodCount
		playCount
		TYPE
		catagoryId
		userNick
		userHeadImgUrl
		userId
	*/
	public static void addMap2Sb(StringBuffer sb){
		for (String url : urlMap.keySet()) {
			String videoid = getVideoId(url);
			sb.append(videoid).append(tab);
			sb.append(urlMap.get(url)).append(tab);
			sb.append(url).append(tab);
			sb.append(nullstr).append(tab);
			sb.append(nullstr).append(tab);
			sb.append(nullstr).append(tab);
			sb.append(nullstr).append(tab);
			sb.append(nullstr).append(tab);
			sb.append("0").append(tab);
			sb.append(nullstr).append(tab);
			sb.append(nullstr).append(tab);
			sb.append(nullstr).append(tab);
			sb.append(nullstr).append(endline);
			
		}
	}
	
	
	
	public static String getVideoId(String url){
		return url.substring(url.indexOf("video_id=") + 9,url.indexOf("&"));
	}
	
	public static void extractUrl(Document doc){
		Element contEle = doc.getElementById("detail-list");
		Elements lists = contEle.getAllElements();
		int size = lists.size();
		for (int i = 0; i < size; i++) {
			Element e = lists.get(i);
			Elements content = e.getElementsByClass("content-wrapper");
			if(content.size() == 0) continue;
			String video_url = getVideoUrl(content.get(0));
//			System.out.println(video_url);
			if("".equals(video_url))continue;
//			if(i > 4)
//				break;
			String title = getTitle(content.get(0));
			urlMap.put(video_url, title);
		}
	}
	
	public static String landHomePage(){
		HttpGet get = new HttpGet(home);
		get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		get.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		get.addHeader("Connection", "keep-alive");
		get.addHeader("Host", "eihanshequ.com");
		get.addHeader("User-Agent", ua);
		try {
			CloseableHttpResponse res = client.execute(get);
			get = new HttpGet(video_home);
			res = client.execute(get);
			return EntityUtils.toString(res.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String loadMore(){
		String req = "http://neihanshequ.com/video/?is_json=1&app_name=neihanshequ_web&max_time="+max_time;
		System.out.println("loadmore_req:"+req);
		HttpGet get = new HttpGet(req);
		get.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		get.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		get.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		get.addHeader("Connection", "keep-alive");
		get.addHeader("Host", "neihanshequ.com");
		get.addHeader("User-Agent", ua);
		get.addHeader("Referer", "http://neihanshequ.com/video/");
		get.addHeader("X-Requested-With", "XMLHttpRequest");
		get.addHeader("X-CSRFToken", csrfmiddlewaretoken);
		
		try {
			CloseableHttpResponse res = client.execute(get);
			
			return EntityUtils.toString(res.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void getToken(Document doc){
		Elements lists = doc.getElementsByTag("input");
		for (Element e : lists) {
			String name = e.attr("name");
			if(!CUtil.isStrNull(name) && "csrfmiddlewaretoken".equals(name)){
				csrfmiddlewaretoken = e.attr("value");
				System.out.println(csrfmiddlewaretoken);
				break;
			}
		}
	}
	
	public static String getVideoUrl(Element e){
//		Elements content = e.getElementsByClass("detail-list");/
		Element videoContainer = e.getElementById("videoContainer");
		Element player_container = videoContainer.getElementsByClass("player-container").get(0);
		String video_url = player_container.attr("data-src");
		return video_url;
	}
	public static String getTitle(Element e){
		Elements content = e.getElementsByClass("upload-txt");
//		Elements content = e.getElementsByClass("name-time-wrapper left");
		if(content.size() == 0) return "";
		Elements name = content.get(0).getElementsByClass("title");
		if(name.size() == 0) return "";
		String namestr = name.get(0).text();
		return namestr;
	}
	
	public static void getMaxTime(String html){
		//max_time: '
		String mtstr = "max_time: '";
		int idx = html.indexOf(mtstr);
		String substr = html.substring(idx + mtstr.length());
		String maxtime = substr.substring(0, substr.indexOf("'"));
		System.out.println("home_page_maxtime:"+maxtime);
		max_time = maxtime;
	}
	
	public static void loadVideo(String out,String filetype) throws Exception{
		for (String url : urlMap.keySet()) {
			
			HttpGet get = new HttpGet(url);
			CloseableHttpResponse res = client.execute(get);
			String name = urlMap.get(url);
			if("".equals(name))
				name = System.currentTimeMillis()+"";
			File storeFile = new File(out+File.separator+name+"."+filetype);
			FileOutputStream output = new FileOutputStream(storeFile);
			res.getEntity().writeTo(output);
			res.close();
			output.close();
			
			Thread.sleep(2000);
		}
	
	}
	
}
