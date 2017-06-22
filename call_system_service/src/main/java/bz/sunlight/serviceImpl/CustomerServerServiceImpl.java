package bz.sunlight.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bz.sunlight.dao.CustomerServerMapper;
import bz.sunlight.entity.CustomerServer;
import bz.sunlight.entity.CustomerServerExample;
import bz.sunlight.entity.CustomerTag;
import bz.sunlight.entity.CustomerTagExample;
import bz.sunlight.service.CustomerServerService;
@Service("customerServerService")
public class CustomerServerServiceImpl implements CustomerServerService{

	@Resource
	private CustomerServerMapper  customerServerMapper;
	
	@Override
	public CustomerServer getCustomerServer(String userName, String password,
			Short status) {
		CustomerServerExample example=new CustomerServerExample();
		example.createCriteria().andUserNameEqualTo(userName).andPasswordEqualTo(password).andStatusEqualTo(status);
		List<CustomerServer> customerServers = customerServerMapper.selectByExample(example);
		if(customerServers!=null&&customerServers.size()>0){
			return customerServers.get(0);
		}
		return null;
	}

	@Override
	public CustomerServer getCustomerServer(String customerServerId,Short status) {
		CustomerServerExample example=new CustomerServerExample();
		example.createCriteria().andIdEqualTo(customerServerId).andStatusEqualTo(status);
		List<CustomerServer> customerServers = customerServerMapper.selectByExample(example);
		if(customerServers!=null&&customerServers.size()>0){
			return customerServers.get(0);
		}
		return null;
	}

	
}
