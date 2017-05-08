package com.chunguang.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.chunguang.utils.CUtil;

public class HtmlPage {

	Document doc ;
	String html;
	
	public HtmlPage(){}
	
	public HtmlPage(String html){
		doc = Jsoup.parse(html);
	}
	
	public Element getElementById(String id){
		if(doc == null)
			initDoc(); 
		return doc.getElementById(id);
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	void initDoc(){
		if(CUtil.isStrNull(this.html))
			throw new  RuntimeException("run setHtml");
		doc = Jsoup.parse(this.html);
	}
	
	public static HtmlPage create(String html){
		HtmlPage page = new HtmlPage(html);
		return page;
	}
}
