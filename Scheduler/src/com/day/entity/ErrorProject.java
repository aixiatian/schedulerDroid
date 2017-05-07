package com.day.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorProject {

	/***
	 *{"status":"success",
	 *"why":"",
	 *"data":[
	 *{"id":"ae003556-334d-11e4-bfec-14feb5d379aa29509C4B-CF17-D1F6-3EAE-D388D2F8354C@24",
	 *"name":"test_video_playlog",
	 *"desc":"\u89c6\u9891\u65e5\u5fd7\u5904\u7406\u6d4b\u8bd5",
	 *"parentId":"",
	 *"projectId":"ae003556-334d-11e4-bfec-14feb5d379aa",
	 *"deptId":"17",
	 *"graphInsId":"29509C4B-CF17-D1F6-3EAE-D388D2F8354C",
	 *"createTime":"1453284363922","compTime":"0",
	 *"startUser":"dongkai3","nodeCount":"1","status":"FAILED",
	 *"hours":"24","startTime":"1453358307000","endTime":"0"
	 *},
	 *
	 *{"id":"ae003556-334d-11e4-bfec-14feb5d379aaAFCBA452-9153-8E8E-A60B-E203C22330A2@24",
	 *"name":"test_video_playlog","desc":"\u89c6\u9891\u65e5\u5fd7\u5904\u7406\u6d4b\u8bd5",
	 *"parentId":"",
	 *"projectId":"ae003556-334d-11e4-bfec-14feb5d379aa",
	 *"deptId":"17",
	 *"graphInsId":"AFCBA452-9153-8E8E-A60B-E203C22330A2",
	 *"createTime":"1453344448787","compTime":"0","startUser":"dongkai3",
	 *"nodeCount":"1","status":"FAILED","hours":"24","startTime":"1453344446351","endTime":"0"
	 *}
	 *]
	 *}
	 */
	
	String id ;
	String name;
	String desc;
	String parentId;
	String projectId;
	String deptId;
	String graphInsId;
	long createTime;
	long compTime;
	String startUser;
	int nodeCount;
	String status;
	int hours;
	long startTime;
	long endTime;
	
	public static ErrorProject jsonObj2Node(JSONObject json){
		ErrorProject node = new ErrorProject();
		try {
			node.setId(json.getString("id"));
			node.setName(json.getString("name"));
			node.setDesc(json.getString("desc"));
			node.setParentId(json.getString("parentId"));
			node.setProjectId(json.getString("projectId"));
			node.setDeptId(json.getString("deptId"));
			node.setGraphInsId(json.getString("graphInsId"));
			node.setCreateTime(json.getLong("createTime"));
			node.setCompTime(json.getLong("compTime"));
			node.setStartUser(json.getString("startUser"));
			node.setNodeCount(json.getInt("nodeCount"));
			node.setStatus(json.getString("status"));
			node.setHours(json.getInt("hours"));
			node.setStartTime(json.getLong("startTime"));
			node.setEndTime(json.getLong("endTime"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getGraphInsId() {
		return graphInsId;
	}
	public void setGraphInsId(String graphInsId) {
		this.graphInsId = graphInsId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getCompTime() {
		return compTime;
	}
	public void setCompTime(long compTime) {
		this.compTime = compTime;
	}
	public String getStartUser() {
		return startUser;
	}
	public void setStartUser(String startUser) {
		this.startUser = startUser;
	}
	public int getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	
}
