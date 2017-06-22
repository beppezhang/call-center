package bz.sunlight.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bz.sulight.common.BaseConstant;
import bz.sunlight.dao.CallRecordMapper;
import bz.sunlight.dao.CustomerTagMapper;
import bz.sunlight.dto.CallRecordDTO;
import bz.sunlight.entity.CallRecord;
import bz.sunlight.entity.CallRecordExample;
import bz.sunlight.entity.CustomerServer;
import bz.sunlight.entity.CustomerTag;
import bz.sunlight.entity.CustomerTagExample;
import bz.sunlight.entity.PageBean;
import bz.sunlight.service.CustomerService;
import bz.sunlight.util.BeanUtil;
import bz.sunlight.util.UUIDUtil;
import bz.sunlight.vo.CallRecordVo;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

	@Resource
	private CustomerTagMapper customerTagMapper;
	
	@Resource
	private CallRecordMapper callRecordMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerTag> getCustomerTagWithCallRecord(String importBatch,int pageNo,int itemCount) {
		Map paraMap=new HashMap<String,Object>();
		PageBean pageBean = new PageBean(pageNo, itemCount);
		int pageItemNo = pageBean.getPageItemNo();
		paraMap.put("importBatch", importBatch);
		paraMap.put("pageItemNo", pageItemNo);
		paraMap.put("itemCount", itemCount);
		return customerTagMapper.selectByPage(paraMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerTag> getCustomerTag(String importBatch,int pageNo,int itemCount) {
		Map paraMap=new HashMap<String,Object>();
		PageBean pageBean = new PageBean(pageNo, itemCount);
		int pageItemNo = pageBean.getPageItemNo();
		paraMap.put("importBatch", importBatch);
		paraMap.put("pageItemNo", pageItemNo);
		paraMap.put("itemCount", itemCount);
		return customerTagMapper.selectCustomerTagByPage(paraMap);
	}
	
	@Override
	public void updateCustomerTag(CustomerTag customerTag) {
		customerTagMapper.updateByPrimaryKeySelective(customerTag);
		
	}

	@Transactional
	@Override
	public void insertCallRecord(CallRecordVo callRecordVo,CustomerServer customerServer) {
//		增加呼叫记录   更新呼叫执行的呼叫次数   更新之前通话记录的状态无效
		CallRecord callRecord=getCallRecord(callRecordVo.getCustomerTagId(), BaseConstant.BASE_TRUE);
		if(callRecord!=null){
			callRecord.setStatus(BaseConstant.BASE_FALSE);
			callRecordMapper.updateByPrimaryKeySelective(callRecord);
		}
		callRecord=new CallRecord();
		BeanUtil.copyProperties(callRecord, callRecordVo);
		callRecord.setCustomerServerName(customerServer.getName());
		callRecord.setCustomerServerCode(customerServer.getCode());
		callRecord.setCustomerServerNickName(customerServer.getNickName());
		callRecord.setId(UUIDUtil.getOrigUUID());
		callRecord.setStatus(BaseConstant.BASE_TRUE);
		Date now=new Date();
		callRecord.setCreateTime(now);
		callRecordMapper.insert(callRecord);
		short dialTime = (short)getDialTime(callRecord.getCustomerTagId());
		CustomerTag customerTag=new CustomerTag();
		customerTag.setId(callRecordVo.getCustomerTagId());
		customerTag.setDialTime(dialTime);
		customerTag.setSex(callRecordVo.getSex());
		customerTag.setName(callRecordVo.getName());
		customerTag.setMobile(callRecordVo.getMobile());
		customerTag.setIsRepeate(callRecordVo.getIsRepeate());
		customerTag.setLastCustomerServerId(customerServer.getId());
		customerTag.setLastCustomerServerCode(customerServer.getCode());
		customerTag.setLastCustomerServerName(customerServer.getName());
		customerTag.setLastCustomerServerNickName(customerServer.getNickName());
		customerTag.setLastOperateTime(now);
		customerTagMapper.updateByPrimaryKeySelective(customerTag);
	}

	@Override
	public List<String> selectionBatch() {
		// TODO Auto-generated method stub
		return customerTagMapper.selectionBatch();
	}

	@Override
	public int getCount(String batch) {
		return customerTagMapper.getCount(batch);
	}

	@Override
	public CustomerTag getCustomerTag(String id) {
		return customerTagMapper.selectByPrimaryKey(id);
	}

	

	@Override
	public int getDialTime(String customerTagId) {
		CallRecordExample example=new CallRecordExample();
		example.createCriteria().andCustomerTagIdEqualTo(customerTagId);
		return callRecordMapper.countByExample(example);
	}

	@Transactional
	@Override
	public void updateCallRecord(CallRecordVo callRecordVo) {
		CallRecord callRecord=new CallRecord();
		BeanUtil.copyProperties(callRecord, callRecordVo);
//		callRecord.setId(UUIDUtil.getOrigUUID());
//		callRecord.setCreateTime(new Date());
		callRecordMapper.updateByIdSelective(callRecord);
		CustomerTag customerTag=new CustomerTag();
		customerTag.setId(callRecordVo.getCustomerTagId());
		customerTag.setSex(callRecordVo.getSex());
		customerTag.setName(callRecordVo.getName());
		customerTag.setMobile(callRecordVo.getMobile());
		customerTagMapper.updateByPrimaryKeySelective(customerTag);
		

	}

	@Override
	public CallRecord getCallRecord(String id) {
		return callRecordMapper.selectByPrimaryKey(id);
		
	}

	@Override
	public int selectRecordCount(Map map) {
		return callRecordMapper.selectRecordCount(map);
		
	}

	@Override
	public List<CallRecord> getCallRecordByCustomerTagId(String customerTagId) {
		return callRecordMapper.getCallRecordByCustomerTagId(customerTagId);
	}

	@Override
	public List<CallRecord> selectRecordByBatch(Map map) {
		// TODO Auto-generated method stub
		return callRecordMapper.selectRecordByBatch(map);
	}

	@Override
	public List<CustomerTag> selectCustomerTagByCondition(CallRecordDTO callRecordDTO) {
		// TODO Auto-generated method stub
		return customerTagMapper.selectCustomerTagByCondition(callRecordDTO);
	}

	@Override
	public int selectCustomerTagCountByCondition(CallRecordDTO callRecordDTO) {
		return customerTagMapper.selectCustomerTagCountByCondition(callRecordDTO);
		
	}

	@Override
	public List<CustomerTag> selectCustomerTagByConditionExcel(
			CallRecordDTO callRecordDTO) {
		// TODO Auto-generated method stub
		return customerTagMapper.selectCustomerTagByConditionExcel(callRecordDTO);
	}

	@Override
	public List<CustomerTag> selectCustomerTagByConditionTest(
			CallRecordDTO callRecordDTO) {
		// TODO Auto-generated method stub
		return customerTagMapper.selectCustomerTagByConditionTest(callRecordDTO);
	}

	@Override
	public CallRecord getCallRecord(String customerTagId, Short status) {
		CallRecordExample example=new CallRecordExample();
		example.createCriteria().andCustomerTagIdEqualTo(customerTagId).andStatusEqualTo(status);
		List<CallRecord> callRecords = callRecordMapper.selectByExample(example);
		if(callRecords!=null&&callRecords.size()>0){
			return callRecords.get(0);
		}
		return null;
	}

	@Override
	public List<CallRecord> getCallRecordByCustomerTagIdExcel(
			Map map) {
		// TODO Auto-generated method stub
		return callRecordMapper.getCallRecordByCustomerTagIdExcel(map);
	}

	@Override
	public void updateByIdSelective(CallRecord record) {
		// TODO Auto-generated method stub
		callRecordMapper.updateByIdSelective(record);
	}
	
	
	
}
