package bz.sunlight.serviceTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import bz.sulight.common.BaseConstant;
import bz.sunlight.dto.CallRecordDTO;
import bz.sunlight.entity.CallRecord;
import bz.sunlight.entity.CustomerTag;
import bz.sunlight.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-service.xml","classpath:spring-mybatis.xml"})
public class CustomerServiceTest {

	@Autowired
	private CustomerService customerService;
	@Test
	public void selectRecordByBatchTest(){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("importBatch", "first_batch");
		map.put("status", BaseConstant.BASE_TRUE);
		List<CallRecord> list = customerService.selectRecordByBatch(map);
		System.out.println(list);
	}
	
	@Test
	public void selectCustomerTagByConditionTest(){
		
		CallRecordDTO dto=new CallRecordDTO();
//		dto.setCallResult((short)0);
		dto.setImportBatch("first_batch");
		dto.setItemCount((short)3);
		dto.setPageItemNo(0);
//		dto.setCustomerServerNickName("sale1");
		dto.setFollowDay((short)2);
		List<CustomerTag> tags = customerService.selectCustomerTagByCondition(dto);
		System.out.println(tags);
//		dto.setDialTime((short)0);
//		List<CustomerTag> customerTags = customerService.selectCustomerTagByCondition(dto);
//		System.out.println(customerTags);
//		List<CallRecord> list = customerService.selectRecordByBatch(map);
//		System.out.println(list);
	}
	
	@Test
	public void selectCustomerTagByConditionExcelTest(){
		
		CallRecordDTO dto=new CallRecordDTO();
//		dto.setCallResult((short)0);
		dto.setImportBatch("first_batch");
		dto.setItemCount((short)3);
		dto.setPageItemNo(0);
//		dto.setCustomerServerNickName("sale1");
		dto.setFollowDay((short)2);
		List<CustomerTag> tags = customerService.selectCustomerTagByConditionExcel(dto);
		for (CustomerTag customerTag : tags) {
			
		}
		System.out.println(tags);
//		dto.setDialTime((short)0);
//		List<CustomerTag> customerTags = customerService.selectCustomerTagByCondition(dto);
//		System.out.println(customerTags);
//		List<CallRecord> list = customerService.selectRecordByBatch(map);
//		System.out.println(list);
	}
}
