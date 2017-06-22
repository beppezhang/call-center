package bz.sunlight.dto;

import java.util.Date;

public class BindingDTO {

	private String customerTagId;
	private String lastCustomerServerId;
	private Date lastOperateTime;
	
	
	public String getCustomerTagId() {
		return customerTagId;
	}
	public void setCustomerTagId(String customerTagId) {
		this.customerTagId = customerTagId;
	}
	public String getLastCustomerServerId() {
		return lastCustomerServerId;
	}
	public void setLastCustomerServerId(String lastCustomerServerId) {
		this.lastCustomerServerId = lastCustomerServerId;
	}
	public Date getLastOperateTime() {
		return lastOperateTime;
	}
	public void setLastOperateTime(Date lastOperateTime) {
		this.lastOperateTime = lastOperateTime;
	}
	
	
}
