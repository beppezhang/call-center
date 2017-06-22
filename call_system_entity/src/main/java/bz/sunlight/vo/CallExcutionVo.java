package bz.sunlight.vo;

import java.util.List;

import bz.sunlight.entity.CustomerServer;
import bz.sunlight.entity.CustomerTag;

public class CallExcutionVo {

	private int totalPageNo;
	private int totalItemCount;
	private CustomerServer currentCustomerServer;
	private List<CustomerTag> customerTags;
	public int getTotalPageNo() {
		return totalPageNo;
	}
	public void setTotalPageNo(int totalPageNo) {
		this.totalPageNo = totalPageNo;
	}
	public int getTotalItemCount() {
		return totalItemCount;
	}
	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
	
	public CustomerServer getCurrentCustomerServer() {
		return currentCustomerServer;
	}
	public void setCurrentCustomerServer(CustomerServer currentCustomerServer) {
		this.currentCustomerServer = currentCustomerServer;
	}
	public List<CustomerTag> getCustomerTags() {
		return customerTags;
	}
	public void setCustomerTags(List<CustomerTag> customerTags) {
		this.customerTags = customerTags;
	}
	
}
