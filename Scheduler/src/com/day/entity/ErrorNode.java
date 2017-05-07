package com.day.entity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class ErrorNode {

	
	/**
	 * "progress": 0,
			"createTime": 1422442544000,
			"outPath": "10.39.3.98:8081/viewLog?log_path=/tmp/update_mobile_perl6652946799639716429.out",
			"runUser": "loganalysis",
			"desc": "更新rpt\\mbportal\\perl目录",
			"errPath": "10.39.3.98:8081/viewLog?log_path=/tmp/update_mobile_perl4693549842876427534.err",
			"creator": "duyu1",
			"nameCN": "更新rpt\\mbportal\\perl目录",
			"id": "38C8360A-B4A8-ADA8-E150-602C2E65782A",
			"startTime": "",
			"projectID": "ae003556-334d-11e4-bfec-14feb5d379aa",
			"hadoopVersion": "v2",
			"priority": 0,
			"maxRetry": 0,
			"paramMap": {
				"mm": "08",
				"HH": "00",
				"YYYYMMDD": "20160128"
			},
			"name": "update_mobile_perl",
			"deptID": "17",
			"outputPath": [],
			"runIP": "10.39.3.98",
			"updateTime": 1422442544000,
			"status": "FAILED",
			"exitCode": 255,
			"svnUrl": "https://svn.intra.sina.com.cn/data/myprog/home_yz/loganalysis/rpt/mbportal/perl/",
			"outMsg": "svn checkout and mvn package log:/mnt/mfs/SinaScheduler/svnbase//data/myprog/home_yz/loganalysis/rpt/mbportal/perl//log/update_mobile_perl1453997310274.log\n",
			"params": "YYYYMMDD",
			"depth": 0,
			"content": "1.txt",
			"defID": "F893FFC1-667C-440B-ADE7-AA8EBC11F336",
			"inputPath": [],
			"actualStartTime": 1453997305453,
			"errMsg": "Error trying to checking and building: com.sina.data.scheduler.agent.AgentTaskException: Task file [/mnt/mfs/SinaScheduler/svnbase//data/myprog/home_yz/loganalysis/rpt/mbportal/perl//1.txt] not exists.",
			"owner": "duyu1",
			"actualEndTime": 0,
			"warningCond": ""
	 */
	String progress;
	long createTime;
	String outPath;
	String runUser;
	String desc;
	String errPath;
	String creator;
	String nameCN;
	String id;
	long startTime;
	String projectID;
	String hadoopVersion;
	String priority;
	String maxRetry;
	Map<String,Object> paramMap;
	String name;
	String deptID;
	String[] outputPath;
	String runIP;
	long updateTime;
	String status;
	String exitCode;
	String svnUrl;
	String outMsg;
	String params;
	String depth;
	String content;
	String defID;
	String[] inputPath;
	long actualStartTime;
	String errMsg;
	String owner;
	long actualEndTime;
	String warningCond;
	
	public static ErrorNode getErrNodeFromJson(JSONObject json){
		ErrorNode node = new ErrorNode();
		try {
			node.setProgress(json.getString("progress"));
			node.setStatus(json.getString("status"));
			node.setCreateTime(json.getLong("createTime"));
			node.setOutPath(json.getString("outPath"));
			node.setRunUser(json.getString("runUser"));
			node.setDesc(json.getString("desc"));
			node.setErrPath(json.getString("errPath"));
			node.setCreator(json.getString("creator"));
			node.setNameCN(json.getString("nameCN"));
			node.setId(json.getString("id"));
			node.setRunIP(json.getString("runIP"));
//			node.setStartTime(json.getLong("startTime"));
			node.setProjectID(json.getString("projectID"));
			node.setHadoopVersion(json.getString("hadoopVersion"));
			node.setPriority(json.getString("priority"));
			node.setMaxRetry(json.getString("maxRetry"));
			JSONObject paramap = json.getJSONObject("paramMap");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("mm", paramap.getString("mm"));
			map.put("HH", paramap.getString("HH"));
			map.put("YYYYMMDD", paramap.getString("YYYYMMDD"));
			node.setParamMap(map);
			node.setName(json.getString("name"));
			node.setDeptID(json.getString("deptID"));
			
			node.setOutMsg(json.getString("outMsg"));
			node.setErrMsg(json.getString("errMsg"));
			node.setDefID(json.getString("defID"));
			node.setOwner(json.getString("owner"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return node;
	}
	
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getOutPath() {
		return outPath;
	}
	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}
	public String getRunUser() {
		return runUser;
	}
	public void setRunUser(String runUser) {
		this.runUser = runUser;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getErrPath() {
		return errPath;
	}
	public void setErrPath(String errPath) {
		this.errPath = errPath;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getNameCN() {
		return nameCN;
	}
	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getHadoopVersion() {
		return hadoopVersion;
	}
	public void setHadoopVersion(String hadoopVersion) {
		this.hadoopVersion = hadoopVersion;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getMaxRetry() {
		return maxRetry;
	}
	public void setMaxRetry(String maxRetry) {
		this.maxRetry = maxRetry;
	}
	public Map<String, Object> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public String getRunIP() {
		return runIP;
	}
	public void setRunIP(String runIP) {
		this.runIP = runIP;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExitCode() {
		return exitCode;
	}
	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	public String getOutMsg() {
		return outMsg;
	}
	public void setOutMsg(String outMsg) {
		this.outMsg = outMsg;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDefID() {
		return defID;
	}
	public void setDefID(String defID) {
		this.defID = defID;
	}
	public String[] getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String[] outputPath) {
		this.outputPath = outputPath;
	}
	public String[] getInputPath() {
		return inputPath;
	}
	public void setInputPath(String[] inputPath) {
		this.inputPath = inputPath;
	}
	public long getActualStartTime() {
		return actualStartTime;
	}
	public void setActualStartTime(long actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public long getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(long actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public String getWarningCond() {
		return warningCond;
	}
	public void setWarningCond(String warningCond) {
		this.warningCond = warningCond;
	}
	
	
}
