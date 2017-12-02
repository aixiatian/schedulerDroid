package com.day.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import scheduler.entity.Node;
//import scheduler.entity.Project;
//import scheduler.entity.SmallFileItem;


/**
 * Created by dongkai3 on 2017/8/23.
 */
public class ServiceSchadualerNewHpm extends ServiceSchadualer implements Constants{

    CloseableHttpClient client;
    String lt = "";
    String token = "";
    String host = HPM_NEW;

    public ServiceSchadualerNewHpm(int idx){
        this.hostIdx = idx;
    }

    public ServiceSchadualerNewHpm(){}

//    public void setOv(TextArea ov) {
//        this.ov = ov;
//    }

    public static void main(String[] args) {
        ServiceSchadualerNewHpm scn = new ServiceSchadualerNewHpm();
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            System.out.println(sdf.format(1509351644803l));
            String username = "dongkai3";
            String pwd = "dk@#0905";
            scn.login(username,pwd);
            String s = scn.postUserDep();
            System.out.println(s);
//            String defid = "33D634F0-F9C3-47A7-85ED-2F8A9BEF2127";
//            String datestr = "20170823";
//            boolean test = true;
//            scn.startNode(datestr,defid,test);
//            scn.startByDefnode(datestr,defid);
//            Map<String,Project> projs = scn.getAllProjects();
//            System.out.println(projs.size());
//            Node s = scn.getDefids("log_mbportal_wap_basic_hour","702022_finance_singleurl_hour");
//            System.out.println(s);
//            String res = scn.getProjectsForStatus(STATUS_FAIL);
//            System.out.println(res);
           /* Map<String,Node> nds = scn.getInfluncedNodes("loganalysis_ods_video_playlog","ods_chnl_video_playlog");
            System.out.println(nds.size());
            for (String s : nds.keySet()
                 ) {
                Node n = nds.get(s);
                if(n.getOnline()){

                    System.out.println(s + "\t\t\t" + n.getContent());
                }
            }*/

            /*String name = "dw/mds/mds_mbportal_client_bhv_event";
		    String m = URLEncoder.encode(name);
//            scn.getSmallFiles(m);
            String bd = scn.getSmallFiles(m);
            Map<String,String> map = SmallFileItem.parseItemMap(bd);
            scn.getPartitionInfo(name, map,"e:/work/2017/09/04","org");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ServiceSchadualerNewHpm(String username,String pwd,int hostidx){
		this.username = username;
		this.pwd = pwd;
		this.hostIdx = hostidx;
	}
    public String login(String username,String pwd) {
        this.username = username;
        this.pwd = pwd;
        client = HttpClients.createDefault();
        lt = getLt();
        String pbody = postCasa(username,pwd);
        String loginUrl = pbody.substring(pbody.indexOf("\"")+1, pbody.lastIndexOf("\""));
        getTokenStr(loginUrl);
        String secbody = secondGet(loginUrl);
        loginUrl = secbody.substring(secbody.indexOf("\"")+1, secbody.lastIndexOf("\""));
        lastLogin(loginUrl);
        return token;
    }

    String getTokenStr(String loginurl){
        if(token == null || "".equals(token)){
            token = loginurl.substring(loginurl.indexOf("ticket=")+7,loginurl.indexOf("&"));
        }
        return token;
    }

    public String getLt() {
        HttpGet get = new HttpGet("http://cas.erp.sina.com.cn/cas/login?ext=&service=http://"+host+"/hpm/index.php/scheduler/project_instances");
        get.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        get.addHeader("Accept-Encoding", "gzip, deflate");
        get.addHeader("Accept-Language", "zh-CN");
        get.addHeader("Connection", "Keep-Alive");
        get.addHeader("Host", "cas.erp.sina.com.cn");
        get.addHeader("User-Agent", UA);
        CloseableHttpResponse resp = null;
        try {
            resp = client.execute(get);
            String body = EntityUtils.toString(resp.getEntity(), "utf-8");
            String lt = body.split("name=\"lt\" value=\"")[1];
            lt = lt.substring(0,lt.indexOf("\""));
            return lt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String postCasa(String username,String pwd) {
        HttpPost post = new HttpPost("https://cas.erp.sina.com.cn/cas/login?ext=&service=http://"+host+"/hpm/index.php/scheduler/project_instances");
        post.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Accept-Language", "zh-CN");
        post.addHeader("Cache-Control", "no-cache");
        post.addHeader("Connection", "Keep-Alive");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.addHeader("Host", "cas.erp.sina.com.cn");
        post.addHeader("Referer", "https://cas.erp.sina.com.cn/cas/login?ext=&service=http://"+host+"/hpm/index.php/scheduler/project_instances");
        post.addHeader("User-Agent", UA);

        Map<String,String> parameterMap = new HashMap<String,String>();
        parameterMap.put("auth_type", "ldap");
//        parameterMap.put("auth_type", auth_type);
        parameterMap.put("ext", "");
        parameterMap.put("lt", lt);
        parameterMap.put("password", pwd);
        parameterMap.put("qrc_email", "");
        parameterMap.put("qrc_status", "");
        parameterMap.put("username", username);

        UrlEncodedFormEntity postEntity = null;
        try {
            postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
            post.setEntity(postEntity);

            CloseableHttpResponse presp = client.execute(post);
//            int code = presp.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(presp.getEntity(), "utf-8");
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String secondGet(String refer) {
        HttpGet getred = new HttpGet("http://cas.erp.sina.com.cn/cas/login?ext=&service="+refer);
        getred.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        getred.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        getred.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        getred.addHeader("Connection", "keep-alive");
        getred.addHeader("Host", "cas.erp.sina.com.cn");
        getred.addHeader("Referer", refer);
        getred.addHeader("User-Agent", UA);
        CloseableHttpResponse resp = null;
        try {
            resp = client.execute(getred);
            String bodyred = EntityUtils.toString(resp.getEntity(), "utf-8");
            return bodyred;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String lastLogin(String url){

        HttpGet getred = new HttpGet("http://"+host+"/hpm/index.php/login?referrer="+url);
        getred.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        getred.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        getred.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        getred.addHeader("Connection", "keep-alive");
        getred.addHeader("Host", host);
        getred.addHeader("Referer", url);
        getred.addHeader("User-Agent", UA);
        String bodyred = "";
        try {
            CloseableHttpResponse resp = client.execute(getred);
            bodyred = EntityUtils.toString(resp.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bodyred;

    }

    public String postUserDep() throws IOException {
        String refer = "http://"+host+"/hpm/index.php/scheduler/project_instances";
        HttpPost post = new HttpPost("http://"+host+"/hpm/index.php/scheduler/depts/userDept");
        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        post.addHeader("Origin", "http://"+host+"");
        post.addHeader("Connection", "keep-alive");
        post.addHeader("X-Requested-With", "XMLHttpRequest");
//        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.addHeader("Host", host);
        post.addHeader("Referer", refer);
        post.addHeader("User-Agent", UA);
        CloseableHttpResponse presp = client.execute(post);
//        int code = presp.getStatusLine().getStatusCode();
        String body = EntityUtils.toString(presp.getEntity(), "utf-8");
        return body;
    }

//    public String postListPages(String name) {
//
//        HttpPost post = new HttpPost("http://"+host+"/hpm/index.php/scheduler/projects/list_pages");
//        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//        post.addHeader("Accept-Encoding", "gzip, deflate");
//        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//        post.addHeader("Origin", "http://"+host+"");
//        post.addHeader("Connection", "keep-alive");
//        post.addHeader("X-Requested-With", "XMLHttpRequest");
//        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
//        post.addHeader("Host", host);
////        post.addHeader("Referer", refer);
//        post.addHeader("User-Agent", UA);
//
//        Map<String,String> parameterMap = new HashMap<String,String>();
//        parameterMap.put("sEcho","1");
//        parameterMap.put("iColumns","6");
//        parameterMap.put("sColumns","id%2C%2Cname%2Cdesc%2CdeptId%2C");
//        parameterMap.put("iDisplayStart","0");
//        parameterMap.put("iDisplayLength","500");
//        parameterMap.put("mDataProp_0","id");
//        parameterMap.put("bSortable_0","false");
//        parameterMap.put("mDataProp_1","1");
//        parameterMap.put("bSortable_1","true");
//        parameterMap.put("mDataProp_2","name");
//        parameterMap.put("bSortable_2","true");
//        parameterMap.put("mDataProp_3","desc");
//        parameterMap.put("bSortable_3","false");
//        parameterMap.put("mDataProp_4","deptId");
//        parameterMap.put("bSortable_4","false");
//        parameterMap.put("mDataProp_5","id");
//        parameterMap.put("bSortable_5","false");
//        parameterMap.put("iSortCol_0","1");
//        parameterMap.put("sSortDir_0","desc");
//        parameterMap.put("iSortingCols","1");
//        parameterMap.put("name",name);
//
//        UrlEncodedFormEntity postEntity = null;
//        try {
//            postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
//            post.setEntity(postEntity);
//
//            CloseableHttpResponse presp = client.execute(post);
//            String body = EntityUtils.toString(presp.getEntity(), "utf-8");
//            return body ;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

//    Map<String,String> cache = new HashMap<String,String>();


//    public String startByDefnode(String dateStr,String defids){
//        try {
//
//            HttpPost post = new HttpPost("http://"+host+HPM_REST_URL);
//            post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//            post.addHeader("Accept-Encoding", "gzip, deflate");
//            post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//            post.addHeader("Cache-Control", "no-cache");
//            post.addHeader("Proxy-Connection", "keep-alive");
//            post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            post.addHeader("Host", host);
//            post.addHeader("Origin", "http://"+host);
//            post.addHeader("Referer", "http://"+host+"/hpm/index.php/scheduler/projects/list_pages");
//            post.addHeader("User-Agent", UA);
//            post.addHeader("X-Requested-With", "XMLHttpRequest");
//            StringBuilder sb = new StringBuilder();
////            if(defids.contains(","))
////                defids = defids.replaceAll(",", "\",\"");
//            sb.append("defIDs="+URLEncoder.encode(defids,"UTF-8")).append("&");
//            String paraymd = "YYYYMMDD="+dateStr+";HH=";
//            sb.append("paramMap="+ URLEncoder.encode(paraymd,"UTF-8"));
//            if(isStartTest)
//                sb.append(URLEncoder.encode(";test=1","UTF-8")).append("&");
//            else
//                sb.append("&");
//            sb.append("rdurl="+URLEncoder.encode(startByDefnode,"UTF-8"));
//
//            String entity = sb.toString();
////            System.out.println(entity);
//            post.setEntity(new StringEntity(entity));
//            CloseableHttpResponse presp = client.execute(post);
//            int code = presp.getStatusLine().getStatusCode();
//            String body = EntityUtils.toString(presp.getEntity(), "utf-8");
////            body = body.replace("{", "");
////            body = body.replace("}", "");
////            body = body.replace(",\"", "\n");
////            body = body.replace("\"", "");
////            System.out.println(code + "-->" + body);
//            String res = getReturnMessage(body);
//            if(res != null){
//                refreshCacheMap(dateStr,defids);
//            }
//            return res;
//        }catch (Exception e){
//            return e.getMessage();
//        }
//    }
//
//    public void refreshCacheMap(String dateStr,String defids){
//        if(isOrderExe){
//            String[] dsa = defids.split(",");
//            for (int i = 0; i < dsa.length; i++) {
//
//                cache.put(dsa[i]+"`"+dateStr,"");
//            }
//        }
//    }
//
//    private void initCacheMap() throws JSONException {
//        String[] status = new String[]{STATUS_FAIL,STATUS_RUNNING};
//        for(int h = 0;h<status.length;h++){
//
//            String projRunning = getProjectsForStatus(status[h]);
//            List<JSONObject> items = getResponseMap(projRunning);
//            if(items == null){
//                break;
//            }
//            for (int i = 0; i < items.size(); i++) {
//                JSONObject item = items.get(i);
////                String startTime = item.getString("startTime");
////                if(Long.parseLong(startTime) < startTaskTime){
////                    continue;
////                }
//                String projid = item. getString("projectId");
//                String insgraid = item.getString("graphInsId");
//                String nodesStr = getNodesForProject(projid, insgraid);
//                JSONObject nodesJs = new JSONObject(nodesStr);
//                JSONObject data = nodesJs.getJSONObject("data");
//                JSONArray nodes = data.getJSONArray("nodes");
//                for (int j=0;j<nodes.length();j++){
//                    JSONObject o = nodes.getJSONObject(j);
//                    String date = o.getJSONObject("paramMap").getString("YYYYMMDD");
//                    String id = o.getString("id");
//                    cache.put(id + "`" + date,"");
//                }
//            }
//        }
//    }
//
//    public String getReturnMessage(String json) throws Exception {
//        JSONObject jo = new JSONObject(json);
//        String code = SchedulerUtil.getJsonObjectString(jo,"code");
////        String id = "";
//        String date = "";
//        String res = null;
//        if("000".equals(code)){
//            JSONObject data = jo.getJSONObject("data");
////            id = SchedulerUtil.getJsonObjectString(data,"id");
//            date = SchedulerUtil.format2ymdhms(SchedulerUtil.getJsonObjectString(data,"createTime"));
//            res = "�ύʱ�䣺"+date+"\n";
//        }
//        return res;
//    }

//    public Node getDefids(String projName, String nodeName)  {
//        Map<String,Project> projMap = getAllProjects();
//        Node id = null;
//        projName = projName + "`";
//        if(projMap != null && projMap.containsKey(projName)){
//            Project p = projMap.get(projName);
//            Map<String,Node> nodeMap = searchNodesForProjectIDs(p);
//            if(nodeName.contains("@")){
//                nodeName = nodeName.substring(0,nodeName.indexOf("@"));
//            }
//            if(nodeMap != null && nodeMap.containsKey(nodeName)){
//                id = nodeMap.get(nodeName);
//            }
//        }
//        return id;
//    }

//    public Map<String,Project> getAllProjects()  {
//        String pages = postListPages("");
//        return Project.getProjectsFromJsonStr(pages);
//    }
//
//    public Map<String,Node> searchNodesForProjectIDs(Project p){
//        String interface_url = "http://"+host+"/hpm/index.php/scheduler/rest";
//        String res = "";
//        HttpPost post = new HttpPost(interface_url);
//
//        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//        post.addHeader("Accept-Encoding", "gzip, deflate");
//        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//        post.addHeader("Connection", "keep-alive");
//        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        post.addHeader("Host", host);
//        post.addHeader("Origin", "http://"+host);
//        String projid = p.getId();
//        String count = p.getNodeCount();
//        post.addHeader("Referer", "http://"+host+"/hpm/index.php/scheduler/project_flow?id="+projid+"&nodeCount="+count+"&from=list");
//        post.addHeader("X-Requested-With", "XMLHttpRequest");
//        post.addHeader("User-Agent", UA);
////        projid = projid.replace(",", "\",\"");
////        System.out.println("��Ŀid��"+projid);
//        Map<String,Node> nodeMap = null;
//        try {
//            String entity = "projectIDs="+projid+"&nodeCount="+count+"&rdurl="+ URLEncoder.encode(searchNodesForProjectIDs,"utf-8");
//
//            post.setEntity(new StringEntity(entity));
//            CloseableHttpResponse resp = client.execute(post);
//            if(resp.getStatusLine().getStatusCode() == 200){
//                res = EntityUtils.toString(resp.getEntity(),"utf-8");
//                JSONObject dataJson = null;
//                try {
//                    dataJson = new JSONObject(res).getJSONObject("data");
//
//                } catch (Exception e) {
//                    System.err.println("getDataʧ��!");
//                    return new HashMap<String,Node>();
//                }
////				System.out.println(dataJson.getString("nodes"));
//                nodeMap = Node.getNodeMapFromJson(dataJson.getString("nodes"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return nodeMap;
//    }

    public String getProjectsForStatus(String status){
        return getProjectsForStatus("",status);
    }

    String getProjectsForStatus(String name,String status) {
        HttpPost post = new HttpPost("http://"+host+GET_PROJECT_FROM_STATUS);
        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        post.addHeader("Origin", "http://"+host+"");
        post.addHeader("Connection", "keep-alive");
        post.addHeader("X-Requested-With", "XMLHttpRequest");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.addHeader("Host", host);
//        post.addHeader("Referer", refer);
        post.addHeader("User-Agent", UA);

        Map<String,String> parameterMap = new HashMap<String,String>();
        parameterMap.put("sEcho","2");
        parameterMap.put("iColumns","8");
        parameterMap.put("sColumns","id,name,desc,deptId,startTime,nodeCount,status,");
        parameterMap.put("iDisplayStart","0");
        parameterMap.put("iDisplayLength","200");
        parameterMap.put("mDataProp_0","id");
        parameterMap.put("bSortable_0","false");
        parameterMap.put("mDataProp_1","name");
        parameterMap.put("bSortable_1","true");
        parameterMap.put("mDataProp_2","desc");
        parameterMap.put("bSortable_2","false");
        parameterMap.put("mDataProp_3","deptId");
        parameterMap.put("bSortable_3","false");
        parameterMap.put("mDataProp_4","startTime");
        parameterMap.put("bSortable_4","true");
        parameterMap.put("mDataProp_5","nodeCount");
        parameterMap.put("bSortable_5","false");
        parameterMap.put("mDataProp_6","status");
        parameterMap.put("bSortable_6","false");
        parameterMap.put("mDataProp_7","id");
        parameterMap.put("bSortable_7","false");
        parameterMap.put("iSortCol_0","1");
        parameterMap.put("sSortDir_0","desc");
        parameterMap.put("iSortingCols","1");
        parameterMap.put("name",name);
        parameterMap.put("nodestatus",status);

        UrlEncodedFormEntity postEntity ;
        try {
            postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
            post.setEntity(postEntity);

            CloseableHttpResponse presp = client.execute(post);
            String body = EntityUtils.toString(presp.getEntity(), "utf-8");
            return body ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    String setStatus(String insGraphID,String nodeInsID,String status) {

        String interface_url = "http://"+host+Constants.HPM_REST_URL;

        HttpPost post = new HttpPost(interface_url);
        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        post.addHeader("Proxy-Connection", "keep-alive");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.addHeader("Host", host);
        post.addHeader("Origin", "http://"+host);
//        post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
        post.addHeader("X-Requested-With", "XMLHttpRequest");

        StringBuilder sb = new StringBuilder();
        sb.append("nodeInsID=").append(nodeInsID).append("&");
        sb.append("status=").append(status).append("&");
        try {
            sb.append("rdurl=").append(URLEncoder.encode(setStatus,"utf-8")).append("&");
            sb.append("insGraphID=").append(insGraphID);

            post.setEntity(new StringEntity(sb.toString()));
            CloseableHttpResponse resp = client.execute(post);
            String res = EntityUtils.toString(resp.getEntity(),"utf-8");

            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

 //    private  int heartBeat(int exp) throws InterruptedException {
//        Thread.sleep(exp * 60 * 1000);
//        int nodecount = getRunningTaskNum();
//        return nodecount;
//    }
//
//    private int heartBeat() throws InterruptedException {
//        return heartBeat(1);
//    }

    public String retryNode(String insGraphID,String nodeInsID) {
        return setStatus(insGraphID,nodeInsID,STATUS_RETRY);
    }

    public String passNode(String insGraphID,String nodeInsID) {
        return setStatus(insGraphID,nodeInsID,STATUS_SUCCESS);
    }
//    boolean isMonitorStart = false;
//    boolean monitorStop = false;
//    public void startNodeBetweenDates(int num_per_time,int task_timespan_sec,int sleep_for_next,String datefrom,String dateto,String defids,String dwm,String hour) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        try {
//            long timestart = sdf.parse(datefrom).getTime();
//            long timeend = sdf.parse(dateto).getTime();
//            int count = 0;
//            int canrunNum = num_per_time;
//            startTaskTime = System.currentTimeMillis();
//            if(isOrderExe){
//                initCacheMap();
//                if(!isMonitorStart){
//                    new Thread(new MonitorThread()).start();
//                }
//            }
//            for (long i = timestart; i <= timeend; i = i + 86400000) {
//                if (!SchedulerUtil.isDayWeekMonthExe(dwm, new Date(i)))
//                    continue;
//                if (count == 0) {
//
//                    int nodecount = getRunningTaskNum();
//                    if (nodecount >= num_per_time) {
//                        i = i - 86400000;
//                        Thread.sleep(sleep_for_next * 60 * 1000);
//                        continue;
//                    } else {
//                        canrunNum = num_per_time - nodecount;
//                    }
//                }
//                if (canrunNum <= 0 || canrunNum > num_per_time) {
//                    i = i - 86400000;
////                    Thread.sleep(sleep_for_next * 60 * 1000);
////                    int nodecount = getRunningTaskNum();
//                    int nodecount = heartBeat(sleep_for_next);
//                    canrunNum = num_per_time - nodecount;
//                    continue;
//                }
//                String dateStr = sdf.format(i);
//                if (!SchedulerUtil.isStrNull(hour))
//                    dateStr += "@" + hour;
//                waitForLastSuccess(defids,i,sdf);
//                String res = startByDefnode(dateStr, defids);
//                ov.append(dateStr + "�ɹ���\n" + res + "\n");
//                Thread.sleep(task_timespan_sec * 1000);
//                count++;
//                canrunNum--;
//            }
//            ov.append("------------�������--------------\n");
//            ov.append("������������"+count+"\n");
//            String tt = ov.getText();
//            SchedulerUtil.writeLog(tt, defids);
//            monitorStop = true;
//        } catch (Exception e) {
//            ov.append(e.getMessage());
//        }
//    }

//    private void waitForLastSuccess(String defids,long i,SimpleDateFormat sdf) throws InterruptedException {
//        while (isOrderExe && true){
//            if(cache.containsKey(defids + "`" + sdf.format(i-86400000))){
//                ov.append("�ȴ�"+sdf.format(i-86400000)+"ִ����ϣ�");
//                heartBeat();
//            }else{
//                break;
//            }
//        }
//    }

//    public class MonitorThread implements Runnable{
//
//        @Override
//        public void run() {
//            try {
//                while(!monitorStop) {
//                    execute(STATUS_FAIL);
//                    execute(STATUS_SUCCESS);
//                }
//            }catch (Exception e){
//                ov.append("\n"+e.getMessage());
//            }
//        }
//
//        private void execute(String stauts) throws JSONException, ParseException {
//            String projRunning = getProjectsForStatus(stauts);
//            List<JSONObject> items = getResponseMap(projRunning);
//            for (int i = 0; i < items.size(); i++) {
//                JSONObject item = items.get(i);
//                String startTime = item.getString("startTime");
//                if(Long.parseLong(startTime) < startTaskTime){
//                    continue;
//                }
//                String projid = item. getString("projectId");
//                String insgraid = item.getString("graphInsId");
//                String nodesStr = getNodesForProject(projid, insgraid);
//                JSONObject nodesJs = new JSONObject(nodesStr);
//                JSONObject data = nodesJs.getJSONObject("data");
//                JSONArray nodes = data.getJSONArray("nodes");
//                for (int j=0;j<nodes.length();j++){
//                    JSONObject o = nodes.getJSONObject(i);
//                    String date = o.getJSONObject("paramMap").getString("YYYYMMDD");
//                    String id = o.getString("id");
//                    if(STATUS_SUCCESS.equals(stauts)&&cache.containsKey(id + "`" + date)){
//                        cache.remove(id + "`" + date);
//                    }else if(STATUS_FAIL.equals(stauts)&&cache.containsKey(id + "`" + date)){
//                        String info = readyMap.get(id);
//                        String[] infoarr = info.split("`");
//                        String content = "��Ŀ����"+infoarr[0]+"\n��������"+infoarr[1]+"\n���ڣ�"+date;
//                        SchedulerUtil.sendMail(username,"��˳�������ڵ�ʧ�ܡ�",content);
//                    }
//                }
//            }
//        }
//    }

//    public int getRunningTaskNum() {
//        String projs = getProjectsForStatus("RUNNING");
//        if(SchedulerUtil.isStrNull(projs)){
//            return -1;
//        }else{
//            try {
//                JSONObject jo = new JSONObject(projs);
//                if (!SchedulerUtil.isJsonReturnSuccess(jo))
//                    return -1;
//                JSONArray jarr = jo.getJSONArray("data");
//                int nodeCount = 0;
//                if(jarr == null)
//                    nodeCount = 0;
//                else
//                    nodeCount = jarr.length();
//                return nodeCount;
//            } catch (Exception e) {
//                if(ov != null)
//                    ov.append("��ѯ��ǰִ�нڵ��������\n"+e.getMessage()+"\n");
//            }
//        }
//        return -1;
//    }

//    Map<String,String> readyMap = new HashMap<String,String>();
//
//    public void startNodeBetweenDates(int num_per_time,int task_timespan_sec,int sleep_for_next,String datefrom,String dateto,String defids,String dwm){
//        startNodeBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefrom, dateto, defids, dwm, null);
//    }
//    long startTaskTime = 0l;
//    public void startMutiTaskBetweenDates(int num_per_time,int task_timespan_sec,int sleep_for_next,String datefrom,String dateto,String infos,String dwm) {
//        if(SchedulerUtil.isStrNull(infos))
//            return;
//        String[] projs = infos.split(";");
//        if(projs.length < 1)
//            return;
//        String defidsStr = "";
//        boolean isHasHour = false;
//        startTaskTime = System.currentTimeMillis();
//        for (int i = 0; i < projs.length; i++) {
//            String proj = projs[i].trim();
//            if(SchedulerUtil.isStrNull(proj)) {
//                continue;
//            }
//            String[] info = proj.split(":");
//            if(info.length==5 || info.length==3){
//                String[] tasknames = info[1].trim().split(",");
//                String[] shells = info[2].trim().split(",");
//                if(tasknames.length != shells.length){
//                    ov.append("������Ϣ�����������ͽű�������ƥ�䣡\n��������"+info[1]+"\n�ű���"+info[2]+"\n");
//                    continue;
//                }
//
//                String datefromStr = datefrom;
//                String datetoStr = dateto;
//                if(info.length==5){
//                    datefromStr = info[3].trim();
//                    datetoStr = info[4].trim();
//                }
//
//                String hour = "";
//                boolean findAllDefids = true;
//                for (int j = 0; j < tasknames.length; j++) {
//                    String tkname = tasknames[j].trim();
//                    if(tkname.contains("@")){
//                        String[] tkeles = tkname.split("@");
//                        tkname = tkeles[0];
//                        if(tkeles.length==2)
//                            hour = tkeles[1];
//                        else
//                            hour = "00";
//                        isHasHour = true;
//                    }
//
//                    Node node = getDefids(info[0].trim(), tkname);
//                    if(node == null){
//                        ov.append("δ�ҵ��ڵ���Ϣ��\n��Ŀ����"+info[0]+"����������"+tkname+"\n");
//                        findAllDefids = false;
//                        break;
//                    }
//                    String content = node.getContent();
//                    content = content.substring(content.lastIndexOf("/")+1);
//                    if(content.trim().equals(shells[j].trim())){
//                        ov.append("�ڵ���Ϣƥ��ɹ���\n��Ŀ����"+info[0]+"\n��������"+tkname+"\n�ű�����"+shells[j]+"\n");
//                        String defids = node.getId();
//                        if(!"".equals(defidsStr)){
//                            defidsStr += ","+defids;
//                            readyMap.put(defids,info[0]+"`"+tkname);
//                        }
//                        else{
//                            defidsStr += defids;
//                        }
//
//                    }
//                }
//                if(!findAllDefids){
//                    ov.append("��Ϣδ��ȫƥ�䣺"+proj+"ִ����һ���� -_-!!!\n");
//                    continue;
//                }
//                if(isHasHour){
//                    startNodeBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefromStr, datetoStr, defidsStr,dwm,hour);
//                    defidsStr = "";
//                }
//            }
//        }
//        startNodeBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefrom, dateto, defidsStr,dwm);
//    }

    public String getNodesForProject(String projid,String insgraid){
        String interface_url = "http://"+host+Constants.HPM_REST_URL;
        String res = "";
        HttpPost post = new HttpPost(interface_url);

        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        post.addHeader("Connection", "keep-alive");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.addHeader("Host", host);
        post.addHeader("Origin", "http://"+host);
//        post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
        post.addHeader("X-Requested-With", "XMLHttpRequest");

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("projectIDs=").append(projid).append("&");
            sb.append("insGraphID=").append(insgraid).append("&");
            sb.append("rdurl=").append(URLEncoder.encode(getNodesForProject,"utf-8"));

            post.setEntity(new StringEntity(sb.toString()));
            CloseableHttpResponse resp = client.execute(post);
            res = EntityUtils.toString(resp.getEntity(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

//    @Override
//    public Map<String,Node> getInfluncedNodes(String projName,String nodeName){
//        Node node = getDefids(projName, nodeName);
//        String defid = node.getId();
//        Map<String,Project> influProjs = getInfluencedProjects(defid);
//        Map<String,Node> res = null;
//        if(influProjs != null && influProjs.size() > 0){
//            res = new HashMap<String,Node>();
//            for (Project p: influProjs.values()) {
//                if(p == null){
//                    continue;
//                }
//                Map<String,Node> nm = getInfluncedNodesForProject(p.getId(),defid);
//                if(nm != null && nm.size() > 0){
//                    res.putAll(nm);
//                }
//            }
//        }
//        return res;
//    }

//    public Map<String,Node> getInfluncedNodesForProject(String projid,String defid){
//        try {
////            Node node = getDefids(projName, nodeName);
////            String defid = node.getId();
//
//            HttpPost method = new HttpPost("http://"+host+HPM_REST_URL);
//            method.addHeader("Accept-Encoding", "gzip, deflate");
//            method.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//            method.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//            method.addHeader("Connection", "keep-alive");
//            method.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            method.addHeader("Host", host);
//            method.addHeader("Origin", "http://"+host);
////            method.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/index.php");
//            method.addHeader("User-Agent", UA );
//            method.addHeader("X-Requested-With", "XMLHttpRequest");
//
//            Map<String,String> parameterMap = new HashMap<String,String>();
//            parameterMap.put("sEcho","1");
//            parameterMap.put("iColumns","6");
//            parameterMap.put("sColumns","depth,name,runIP,content,deptID,");
//            parameterMap.put("iDisplayStart","0");
//            parameterMap.put("iDisplayLength","100");
//            parameterMap.put("mDataProp_0","depth");
//            parameterMap.put("bSortable_0","false");
//            parameterMap.put("mDataProp_1","name");
//            parameterMap.put("bSortable_1","true");
//            parameterMap.put("mDataProp_2","runIP");
//            parameterMap.put("bSortable_2","true");
//            parameterMap.put("mDataProp_3","content");
//            parameterMap.put("bSortable_3","true");
//            parameterMap.put("mDataProp_4","deptID");
//            parameterMap.put("bSortable_4","false");
//            parameterMap.put("mDataProp_5","id");
//            parameterMap.put("bSortable_5","false");
//            parameterMap.put("iSortCol_0","1");
//            parameterMap.put("sSortDir_0","desc");
//            parameterMap.put("iSortingCols","1");
//            parameterMap.put("name","");
//            parameterMap.put("project_id",projid);
//            parameterMap.put("rdurl",HPM_INFLUNCE_URL);
//            parameterMap.put("nodeDefIDs",defid);
//
//            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
//            method.setEntity(postEntity);
//            CloseableHttpResponse presp = client.execute(method);
//            String body = EntityUtils.toString(presp.getEntity(), "utf-8");
//            JSONObject jo = new JSONObject(body);
//            Map<String,Node> nm = Node.getNodeMapFromJson(jo.getString("data"));
//
//            return nm;
//
//        } catch (Exception e) {
//            if(ov != null)
//                ov.setText(e.getMessage());
//        }
//        return null;
//    }
//
//    public Map<String,Project> getInfluencedProjects(String nodeDefIDs){
//        try {
//
//            HttpPost method = new HttpPost("http://"+host+HPM_REST_URL);
//            method.addHeader("Accept-Encoding", "gzip, deflate");
//            method.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//            method.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//            method.addHeader("Connection", "keep-alive");
//            method.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            method.addHeader("Host", host);
//            method.addHeader("Origin", "http://"+host);
//            method.addHeader("Referer", "http://"+host+PAGE_AFFACTED_PROJECTS_URL);
//            method.addHeader("User-Agent", UA );
//            method.addHeader("X-Requested-With", "XMLHttpRequest");
//            String entity = "rdurl=" + URLEncoder.encode(BY_FOLLOWING_PROJECTS_IDS_URL,"UTF-8");
//            entity = entity + "&nodeDefIDs=" + nodeDefIDs;
//
//            method.setEntity(new StringEntity(entity));
//            CloseableHttpResponse resp = client.execute(method);
//            String res = EntityUtils.toString(resp.getEntity(),"utf-8");
//            Map<String,Project> pjMap = Project.getProjectsFromJsonStr(res);
//            return pjMap;
//        }catch (Exception e){
//            return null;
//        }
//    }

//    String getSmallFiles(String name){
//        String url = "http://"+host+"/hpm/common/js/AdminLTE/dirtee/data/data.php?";
//        url += "name="+name;
//        url += "&count=1000";
//        url += "&owner=loganalysis";
//        url += "&method="+Constants.method_smallFileNum;
//
//        HttpGet get = new HttpGet(url);
//
//        get.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//        get.addHeader("Accept-Encoding", "gzip, deflate, sdch");
//        get.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//        get.addHeader("Connection", "keep-alive");
//        get.addHeader("Host", "dp.hadoop.data.sina.com.cn");
//        get.addHeader("Referer", "http://dp.hadoop.data.sina.com.cn/hpm/index.php/statistic/personal_small/detail/loganalysis/50");
//        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
//        get.addHeader("X-Requested-With", "XMLHttpRequest");
//
//
//        String body = "";
//        try {
//            CloseableHttpResponse resp = client.execute(get);
//            body = EntityUtils.toString(resp.getEntity(),"utf-8");
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return body;
//    }
//    String encodestr = "/";
//    public void getPartitionInfo(String name,Map<String,String> map,String outPath,String otherpath){
//        String base = encodestr;
//        if(!SchedulerUtil.isStrNull(otherpath))
//            base = encodestr+otherpath;
//        TreeMap<String,String> resmap = new TreeMap<String,String>();
//        StringBuffer res = new StringBuffer();
//        Map<String,String> maps = null;
//        for (String key : map.keySet()) {
//            if (!base.equals("/")) {
//                String tem = name + encodestr + key + base;
//                String bd = getSmallFiles(URLEncoder.encode(tem));
//                maps = SmallFileItem.parseItemMap(bd);
//                for (String k : maps.keySet()) {
//                    resmap.put(key + "/channel=" + k,maps.get(k));
//                }
//            } else
//                resmap.put(key ,map.get(key));
//        }
//
//        for (String key : resmap.keySet()) {
//            res.append(key + "\n");
//        }
//        SchedulerUtil.writeFile(outPath+"/partitionInfo", res.toString());
//    }

}
