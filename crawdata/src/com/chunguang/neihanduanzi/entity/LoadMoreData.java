package com.chunguang.neihanduanzi.entity;

import com.alibaba.fastjson.JSONObject;

public class LoadMoreData {

	JSONObject data = new JSONObject();
	
	String utm_campaign; // ,
	String hide_top_banner; // null,
	String utm_medium; // ,
	String utm_source; // ,
	String message; // success,
	String category_id; // 18
	
	VideoData videodata;//data
	
	public LoadMoreData(String json){
		this.data = JSONObject.parseObject(json);
	}
	
	public void load(){
		String msg = data.getString("message");
		if(!"success".equals(msg))
			return;
		setUtm_campaign(data.getString("utm_campaign"));
		setHide_top_banner(data.getString("hide_top_banner"));
		setMessage(msg);
		setCategory_id(data.getString("category_id"));
		VideoData vd = new VideoData(data.getString("data"));
		vd.load();
		setVideodata(vd);
	}
	

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public String getUtm_campaign() {
		return utm_campaign;
	}

	public void setUtm_campaign(String utm_campaign) {
		this.utm_campaign = utm_campaign;
	}

	public String getHide_top_banner() {
		return hide_top_banner;
	}

	public void setHide_top_banner(String hide_top_banner) {
		this.hide_top_banner = hide_top_banner;
	}

	public String getUtm_medium() {
		return utm_medium;
	}

	public void setUtm_medium(String utm_medium) {
		this.utm_medium = utm_medium;
	}

	public String getUtm_source() {
		return utm_source;
	}

	public void setUtm_source(String utm_source) {
		this.utm_source = utm_source;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public VideoData getVideodata() {
		return videodata;
	}

	public void setVideodata(VideoData videodata) {
		this.videodata = videodata;
	}
	
	
}
