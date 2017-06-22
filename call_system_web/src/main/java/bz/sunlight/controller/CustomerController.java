package bz.sunlight.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bz.sulight.common.BaseConstant;
import bz.sunlight.dto.BindingDTO;
import bz.sunlight.dto.CallRecordDTO;
import bz.sunlight.dto.CustomerTagDTO;
import bz.sunlight.entity.CallRecord;
import bz.sunlight.entity.CustomerServer;
import bz.sunlight.entity.CustomerTag;
import bz.sunlight.entity.KeyValueCategory;
import bz.sunlight.entity.KeyValueItem;
import bz.sunlight.entity.PageBean;
import bz.sunlight.excelUtil.ExcelData;
import bz.sunlight.excelUtil.ExcelTools;
import bz.sunlight.excelUtil.ExcelTools.ExcelType;
import bz.sunlight.service.CustomerServerService;
import bz.sunlight.service.CustomerService;
import bz.sunlight.service.KeyValueService;
import bz.sunlight.util.BeanUtil;
import bz.sunlight.util.DateUtil;
import bz.sunlight.util.GsonUtil;
import bz.sunlight.util.HttpUtil;
import bz.sunlight.util.PropertiesUtil;
import bz.sunlight.util.StringUtil;
import bz.sunlight.vo.CallExcutionVo;
import bz.sunlight.vo.CallRecordVo;
import bz.sunlight.vo.CustomerTagVo;

@Controller
@RequestMapping("/customerInfo/")
public class CustomerController {

	private static final String BIND_URL="http://116.228.236.206:8081/DP/getVirtualMobileById/token/id";
	
	private static final String UN_BIND_URL="http://116.228.236.206:8081/DP/delVirtualMobileById/token/id";
	
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerServerService customerServerService;
	
	@Autowired
	private KeyValueService keyValueService;
	

	
//	外呼执行
	@RequestMapping(value="callExcution/{importBatch}/{pageNo}/{itemCount}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<CallExcutionVo> callExcution(@PathVariable("importBatch") String importBatch,
			@PathVariable("pageNo") int pageNo,@PathVariable("itemCount") int itemCount,
			@CookieValue("customerServerId") String customerServerId){
		CallExcutionVo callExcutionVo=new CallExcutionVo();
		int totalItemCount = customerService.getCount(importBatch);
		PageBean pageBean = new PageBean(totalItemCount, itemCount, false);
		callExcutionVo.setTotalItemCount(totalItemCount);
		callExcutionVo.setTotalPageNo(pageBean.getTotalPageCount());
		CustomerServer customerServer = customerServerService.getCustomerServer(customerServerId, BaseConstant.BASE_TRUE);
		callExcutionVo.setCurrentCustomerServer(customerServer);
		List<CustomerTag> customerTags = customerService.getCustomerTag(importBatch, pageNo, itemCount);
		callExcutionVo.setCustomerTags(customerTags);
		if(customerTags!=null&&customerTags.size()>0){
			return new ResponseEntity<CallExcutionVo>(callExcutionVo,HttpStatus.OK);
		}
		return new ResponseEntity<CallExcutionVo>(HttpStatus.NO_CONTENT);
	}
	
//	外呼执行
	@RequestMapping(value="callExcution",method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Void> callExcution(@RequestBody CustomerTag customerTag){
		customerService.updateCustomerTag(customerTag);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	

	
//	绑定号码
	@RequestMapping(value="bind/{decodeId}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> binding(@PathVariable String decodeId){
//		TODO 请求电信绑定手机号码 
		String token = PropertiesUtil.getInstanse().getString("token");
		BIND_URL.replaceAll("token", token);
		BIND_URL.replaceAll("decodeId", decodeId);
		String json = HttpUtil.doGet(BIND_URL, null);
		Map map = GsonUtil.fromJson(json, Map.class);
		String virtualMobile=(String)map.get("virtualMobile");
		return new ResponseEntity<String>(virtualMobile,HttpStatus.OK);
	}
	
	
//	解绑号码
	@RequestMapping(value="unbind/{decodeId}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> unbind(@PathVariable String decodeId){
//		TODO 请求电信绑定手机号码 
		String token = PropertiesUtil.getInstanse().getString("token");
		UN_BIND_URL.replaceAll("token", token);
		UN_BIND_URL.replaceAll("decodeId", decodeId);
		String json = HttpUtil.doGet(UN_BIND_URL, null);
		return new ResponseEntity<String>(json,HttpStatus.OK);
	}
	
//	外呼记录  选择批次后查出所有的通话记录
	@RequestMapping(value="callRecord/{importBatch}/{pageNo}/{itemCount}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<CustomerTagVo> customerInfo(@PathVariable("importBatch") String importBatch,
			@PathVariable("pageNo") int pageNo,@PathVariable("itemCount") int itemCount,
			@CookieValue("customerServerId") String customerServerId){
		//获取外呼记录条数
		CustomerTagVo customerTagVo=new CustomerTagVo();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("importBatch", importBatch);
		map.put("status", BaseConstant.BASE_TRUE);
		int totalItemCount = customerService.selectRecordCount(map);
		PageBean pageBean = new PageBean(totalItemCount, itemCount, false);
		customerTagVo.setTotalItemCount(totalItemCount);
		customerTagVo.setTotalPageNo(pageBean.getTotalPageCount());
		CustomerTagVo.CustomerTag tag=null;
//		获取通话记录
		List<CustomerTagVo.CustomerTag> tags=new ArrayList<CustomerTagVo.CustomerTag>();
		List<CustomerTag> customerTags = customerService.getCustomerTagWithCallRecord(importBatch, pageNo, itemCount);
		if(customerTags!=null&&customerTags.size()>0){
			for (CustomerTag customerTag : customerTags) {
				List<CallRecord> callRecords = customerService.getCallRecordByCustomerTagId(customerTag.getId());
//				获取最新一次通话记录的信息
				if(callRecords!=null&&callRecords.size()>0){
					tag=customerTagVo.new CustomerTag();
					initTag(tag, customerTag, callRecords);
				}
				tag.setCallRecords(callRecords);
				tags.add(tag);
			}
			customerTagVo.setCustomerTags(tags);
			return new ResponseEntity<CustomerTagVo>(customerTagVo,HttpStatus.OK);
		}
		return new ResponseEntity<CustomerTagVo>(HttpStatus.NO_CONTENT);
	}

	private void initTag(CustomerTagVo.CustomerTag tag,
			CustomerTag customerTag, List<CallRecord> callRecords) {
		CallRecord callRecord = callRecords.get(0);
		BeanUtil.copyProperties(tag, customerTag);
		tag.setCallResult(callRecord.getCallResult());
		tag.setRemark(callRecord.getRemark());
		tag.setHangUpPosition(callRecord.getHangUpPosition());
		tag.setFollowStatus(callRecord.getFollowStatus());
		tag.setFollowDate(callRecord.getFollowDate());
		tag.setFollowDay(callRecord.getFollowDay());
		tag.setStartCallTime(callRecord.getStartCallTime());
		tag.setEndCallTime(callRecord.getEndCallTime());
		tag.setVirtualMobile(callRecord.getVirtualMobile());
		tag.setLastCallRecordId(callRecord.getId());
		tag.setLastOperateTime(callRecord.getCreateTime());
		tag.setLastCustomerServerId(callRecord.getCustomerServerId());
		tag.setLastCustomerServerCode(callRecord.getCustomerServerCode());
		tag.setLastCustomerServerName(callRecord.getCustomerServerName());
		tag.setLastCustomerServerNickName(callRecord.getCustomerServerNickName());
	}
	
//	新增通话记录  + 更新客户标签   TODO:新增时需要将导入批次提交过来
	@RequestMapping(value="callRecord",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> inserting(@RequestBody CallRecordVo callRecordVo){
		CustomerServer customerServer = customerServerService.getCustomerServer(callRecordVo.getCustomerServerId(), BaseConstant.BASE_TRUE);
		customerService.insertCallRecord(callRecordVo,customerServer);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
//	更改通话记录
	@RequestMapping(value="callRecord",method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Void> updating(@RequestBody CallRecordVo callRecordVo){
		CallRecord callRecord = customerService.getCallRecord(callRecordVo.getId());
		if(callRecord!=null){
			customerService.updateCallRecord(callRecordVo);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	
//	客户选择导入数据的批次
	@RequestMapping(value="batch",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<String>> selectionBatch(){
		
		List<String> selectionBatchs = customerService.selectionBatch();
		if(selectionBatchs!=null&&selectionBatchs.size()>0){
			return new ResponseEntity<List<String>>(selectionBatchs,HttpStatus.OK);
		}
		return new ResponseEntity<List<String>>(HttpStatus.NO_CONTENT);
	}
	
//	多条件搜索
	@RequestMapping(value="condition",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<CustomerTagVo> conditon(@RequestBody CallRecordDTO callRecordDTO){
		CustomerTagVo customerTagVo=new CustomerTagVo();
		callRecordDTO.setStatus(BaseConstant.BASE_TRUE);
		int totalItemCount=customerService.selectCustomerTagCountByCondition(callRecordDTO);
		PageBean pageBean = new PageBean(callRecordDTO.getPageNo(), totalItemCount, callRecordDTO.getItemCount());
		callRecordDTO.setPageItemNo(pageBean.getPageItemNo());
		List<CustomerTag> customerTags = customerService.selectCustomerTagByCondition(callRecordDTO);
		CustomerTagVo.CustomerTag tag=null;
		List<CustomerTagVo.CustomerTag> tags=new ArrayList<CustomerTagVo.CustomerTag>();
		if(customerTags!=null&&customerTags.size()>0){
			customerTagVo.setTotalItemCount(totalItemCount);
			customerTagVo.setTotalPageNo(pageBean.getTotalPageCount());
			for (CustomerTag customerTag : customerTags) {
				List<CallRecord> callRecords = customerService.getCallRecordByCustomerTagId(customerTag.getId());
//						获取最新一次通话记录的信息
				if(callRecords!=null&&callRecords.size()>0){
					tag=customerTagVo.new  CustomerTag();
					initTag(tag, customerTag, callRecords);
				}
				tag.setCallRecords(callRecords);
				tags.add(tag);
				
			}
			customerTagVo.setCustomerTags(tags);
			return new ResponseEntity<CustomerTagVo>(customerTagVo,HttpStatus.OK);
		}
		return new ResponseEntity<CustomerTagVo>(HttpStatus.NO_CONTENT);
		}
		
	
	
//	请求导出到excel
	@RequestMapping(value="excel",method=RequestMethod.GET)
	@ResponseBody
    public ResponseEntity<Void> excel(String importBatch, String name, String customerServerNickName,
    		 Short callResult, Short dialTime, Short followDay,
    		 Integer callTime, Date startDate, Date endDate,
    		HttpServletResponse response) throws IOException{
		//get方式乱码
		importBatch = new String(importBatch.getBytes(BaseConstant.ISO_8859_1),BaseConstant.UTF_8);
		if(StringUtils.isNotBlank(name)){
			name = new String(name.getBytes(BaseConstant.ISO_8859_1),BaseConstant.UTF_8);
		}
		if(StringUtils.isNotBlank(customerServerNickName)){
			customerServerNickName = new String(customerServerNickName.getBytes(BaseConstant.ISO_8859_1),BaseConstant.UTF_8);
		}
		CallRecordDTO callRecordDTO = new CallRecordDTO();
		callRecordDTO.setStatus(BaseConstant.BASE_TRUE);
		callRecordDTO.setImportBatch(importBatch);
		callRecordDTO.setName(name);
		callRecordDTO.setCustomerServerNickName(customerServerNickName);
		callRecordDTO.setCallResult(callResult);
		callRecordDTO.setDialTime(dialTime);
		callRecordDTO.setFollowDay(followDay);
		callRecordDTO.setCallTime(callTime);
		callRecordDTO.setStartDate(startDate);
		callRecordDTO.setEndDate(endDate);
//		标题
		Object[] title={"客户ID","姓名","性别","标签","手机号码","开始时间","结束时间","客服昵称","呼叫结果","标签重复","拨打次数","备注","操作时间","挂断位置","跟进状态","跟进日期"};
	    List<Object[]> contents=new ArrayList<Object[]>();
	    Map<String,Object> map=new HashMap<String, Object>();
		if("all".equals(callRecordDTO.getChoice())){
//	    	导出所有 包括主单和子单据
	    	//正文
			List<CustomerTag> customerTags = customerService.selectCustomerTagByConditionExcel(callRecordDTO);
			if(customerTags!=null&&customerTags.size()>0){
				for (CustomerTag customerTag : customerTags) {
					map.put("customerTagId", customerTag.getId());
					List<CallRecord> callRecords = customerService.getCallRecordByCustomerTagIdExcel(map);
				    if(callRecords!=null&&callRecords.size()>0){
				    	for (CallRecord callRecord : callRecords) {
							Object[] content = initContent(customerTag,
									callRecord);
							contents.add(content);
						}
				    }
				}
			}
		}else{
//	    	导出主单据
			List<CustomerTag> customerTags = customerService.selectCustomerTagByConditionExcel(callRecordDTO);
			if(customerTags!=null&&customerTags.size()>0){
				for (CustomerTag customerTag : customerTags) {
					map.put("customerTagId", customerTag.getId());
					map.put("status", BaseConstant.BASE_TRUE);
					List<CallRecord> callRecords = customerService.getCallRecordByCustomerTagIdExcel(map);
				    if(callRecords!=null&&callRecords.size()>0){
				    	CallRecord callRecord=callRecords.get(0);
							Object[] content = initContent(customerTag,
									callRecord);
							contents.add(content);
						
				    }
				}
			}
	    	
	    	
	    }
		ExcelData data = new ExcelData(contents, title);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename="+DateUtil.dateToString(new Date(), "yyyy-MM-dd hh:mm:ss")+".xlsx");
		response.setContentType("application/vnd.ms-excel; charset=utf-8") ;
		ExcelTools.writerExcel(response.getOutputStream(), data, ExcelType.EXCEL2007);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private Object[] initContent(CustomerTag customerTag, CallRecord callRecord) {
		Object[] content={callRecord.getId(),callRecord.getName(),keyValueService.getKeyValueItemByCode(callRecord.getSex(), BaseConstant.CATEGORY_CODE_SEX),
				customerTag.getCustomerTag(),callRecord.getMobile(),DateUtil.dateToString(callRecord.getStartCallTime(), BaseConstant.DATE_FORMAT_YEAR_TO_SECOND),DateUtil.dateToString(callRecord.getEndCallTime(), BaseConstant.DATE_FORMAT_YEAR_TO_SECOND),
				callRecord.getCustomerServerNickName(),
				keyValueService.getKeyValueItemByCode(callRecord.getCallResult(), 
						BaseConstant.CATEGORY_CODE_CALL_RESULT),
						keyValueService.getKeyValueItemByCode(customerTag.getIsRepeate(), BaseConstant.CATEGORY_CODE_IS_REPEATE),
						StringUtil.shortToString(customerTag.getDialTime()),
				callRecord.getRemark(),DateUtil.dateToString(callRecord.getCreateTime(), BaseConstant.DATE_FORMAT_YEAR_TO_SECOND),	keyValueService.getKeyValueItemByCode(callRecord.getHangUpPosition(), BaseConstant.CATEGORY_CODE_HANGUP_POSITION),	keyValueService.getKeyValueItemByCode(callRecord.getFollowStatus(), BaseConstant.CATEGORY_CODE_IS_FOLLOW),
				DateUtil.dateToString(callRecord.getFollowDate(), BaseConstant.DATE_FORMAT_YEAR_TO_DAY)
		};
		return content;
	}
}
