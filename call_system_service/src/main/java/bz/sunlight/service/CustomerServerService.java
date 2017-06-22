package bz.sunlight.service;

import bz.sunlight.entity.CustomerServer;

public interface CustomerServerService {

	CustomerServer getCustomerServer(String userName,String password,Short status);
	
	CustomerServer getCustomerServer(String customerServerId,Short status);
	
}
