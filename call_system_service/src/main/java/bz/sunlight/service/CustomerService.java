package bz.sunlight.service;

import java.util.List;
import java.util.Map;

import bz.sunlight.dto.CallRecordDTO;
import bz.sunlight.dto.CustomerTagDTO;
import bz.sunlight.entity.CallRecord;
import bz.sunlight.entity.CustomerServer;
import bz.sunlight.entity.CustomerTag;
import bz.sunlight.vo.CallRecordVo;

public interface CustomerService {

	List<CustomerTag> getCustomerTagWithCallRecord(String importBatch,int pageNo,int itemCount);
	
	List<CustomerTag> getCustomerTag(String importBatch,int pageNo,int itemCount);
	
	CustomerTag getCustomerTag(String id);
	
	void updateCustomerTag(CustomerTag customerTag);
	
	void insertCallRecord(CallRecordVo callRecordVo,CustomerServer customerServer);
	
	void updateCallRecord(CallRecordVo callRecordVo);
	
	CallRecord getCallRecord(String id);
	
	CallRecord getCallRecord(String customerTagId,Short status);
	
	List<CallRecord> getCallRecordByCustomerTagId(String customerTagId);
	
	List<String> selectionBatch();
	
	int getCount(String batch);
	
	int getDialTime(String customerTagId);
	
	int selectRecordCount(Map map);
	
	List<CallRecord> selectRecordByBatch(Map map);
	
	
	public List<CustomerTag> selectCustomerTagByCondition(CallRecordDTO customerTagDTO);

	
	public int selectCustomerTagCountByCondition(CallRecordDTO customerTagDTO);

	List<CustomerTag> selectCustomerTagByConditionExcel(CallRecordDTO customerTagDTO);
	
	List<CallRecord> getCallRecordByCustomerTagIdExcel(Map map);
	
	List<CustomerTag> selectCustomerTagByConditionTest(CallRecordDTO callRecordDTO);
	
	void updateByIdSelective(CallRecord record);
}
