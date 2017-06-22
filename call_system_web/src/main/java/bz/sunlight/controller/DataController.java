package bz.sunlight.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bz.sulight.common.BaseConstant;
import bz.sunlight.entity.CustomerServer;
import bz.sunlight.entity.CustomerTag;
import bz.sunlight.entity.KeyValueCategory;
import bz.sunlight.service.CustomerServerService;
import bz.sunlight.util.GsonUtil;
import bz.sunlight.util.HttpUtil;
import bz.sunlight.util.PropertiesUtil;
import bz.sunlight.util.TelecomUtil;
import bz.sunlight.util.UUIDUtil;
@Controller
@RequestMapping(value="data")
public class DataController {

	
	private static final String QUERY_ALL_URL="http://116.228.236.206:8081/DP/getUsers/token/date";
	
	private static final String QUERY_BATCH_URL="http://116.228.236.206:8081/DP/getBatchUsers/token/size/date";
	
	private static final String BIND_URL="http://116.228.236.206:8081/DP/getVirtualMobileById/token/id";
	
	private static final String UN_BIND_URL="http://116.228.236.206:8081/DP/delVirtualMobileById/token/id";
	
	@Autowired
	private CustomerServerService customerServerService;
	
	@RequestMapping(value="test",method=RequestMethod.GET)
	@ResponseBody
	public Map selectBuild(){
		Map map=new HashMap<String, String>();
		map.put("name", "shanghai");
		return map;
	}
	
//	全量查询模型当天跑出的Users
	@RequestMapping(value="all/{date}",method=RequestMethod.GET)
	@ResponseBody
	public String allData(@PathVariable String date,@CookieValue("customerServerId") String customerServerId){
		CustomerServer customerServer = customerServerService.getCustomerServer(customerServerId, BaseConstant.BASE_TRUE);
		String token = PropertiesUtil.getInstanse().getString("token");
		QUERY_ALL_URL.replaceAll("token", token);
		QUERY_ALL_URL.replaceAll("date", date);
		String allData = HttpUtil.doGet(QUERY_ALL_URL, null);
		List<Map> list=GsonUtil.fromJson(allData, List.class);
		List<CustomerTag> customerTags=new ArrayList<CustomerTag>();
		for (Map map : list) {
			initImportData(date, customerTags, map,customerServer);
		}
	    return null;
	}
	
//	批量查询模型当天跑出的Users
	@RequestMapping(value="all/{date}/{size}",method=RequestMethod.GET)
	@ResponseBody
	public String sizeData(@PathVariable String date,@PathVariable String size,@CookieValue("customerServerId") String customerServerId){
		CustomerServer customerServer = customerServerService.getCustomerServer(customerServerId, BaseConstant.BASE_TRUE);
		String token = PropertiesUtil.getInstanse().getString("token");
		QUERY_BATCH_URL.replaceAll("token", token);
		QUERY_BATCH_URL.replaceAll("date", date);
		QUERY_BATCH_URL.replaceAll("size", size);
		String allData = HttpUtil.doGet(QUERY_BATCH_URL, null);
		List<Map> list=GsonUtil.fromJson(allData, List.class);
		List<CustomerTag> customerTags=new ArrayList<CustomerTag>();
		for (Map map : list) {
			initImportData(date, customerTags, map,customerServer);
		}
	    return null;
	}

	private void initImportData(String date, List<CustomerTag> customerTags,
			Map map,CustomerServer customerServer) {
		CustomerTag tag=new CustomerTag();
		String json=(String) map.get("values");
		Map dataMap = GsonUtil.fromJson(json, Map.class);
		String name = (String)dataMap.get("name");   //姓氏  name 为暂定名称
		String sex = (String)dataMap.get("sex");   //姓氏  name 为暂定名称
		String customerTag = (String)dataMap.get("tag");   //姓氏  name 为暂定名称
		String decodeId = (String)dataMap.get("decodeId");   //姓氏  name 为暂定名称
		tag.setId(UUIDUtil.getOrigUUID());
		tag.setName(name);
		tag.setSex((short)1);
		tag.setCustomerTag(customerTag);
		tag.setEnterpriseId(customerServer.getEnterpriseId());
		tag.setEnterpriseCode(customerServer.getEnterpriseCode());
		tag.setEnterpriseName(customerServer.getEnterpriseName());
		tag.setDecodeId(decodeId);
		tag.setImportBatch(date);
		tag.setCreateTime(new Date());
		customerTags.add(tag);
	}
}
