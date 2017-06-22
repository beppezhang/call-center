package bz.sunlight.vo;

import java.util.Date;

public class CallRecordVo {

	
	private String id;
	
	private String customerTagId;
	
	private String mobile;
	
	private Date createTime;
	
	private Short callResult;
	
	private String remark;
	
	private Short hangUpPosition;
	
	private String customerServerId;
	
	private Short followStatus;
	
	private Date followDate;
	
	private Date startCallTime;
	
	private Date endCallTime;
	
	private String virtualMobile;
	
	private Short sex;
	
	private String name;
	
	private Short isRepeate;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerTagId() {
		return customerTagId;
	}

	public void setCustomerTagId(String customerTagId) {
		this.customerTagId = customerTagId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Short getCallResult() {
		return callResult;
	}

	public void setCallResult(Short callResult) {
		this.callResult = callResult;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Short getHangUpPosition() {
		return hangUpPosition;
	}

	public void setHangUpPosition(Short hangUpPosition) {
		this.hangUpPosition = hangUpPosition;
	}

	public String getCustomerServerId() {
		return customerServerId;
	}

	public void setCustomerServerId(String customerServerId) {
		this.customerServerId = customerServerId;
	}

	

	public Short getFollowStatus() {
		return followStatus;
	}

	public void setFollowStatus(Short followStatus) {
		this.followStatus = followStatus;
	}

	public Date getFollowDate() {
		return followDate;
	}

	public void setFollowDate(Date followDate) {
		this.followDate = followDate;
	}

	public Date getStartCallTime() {
		return startCallTime;
	}

	public void setStartCallTime(Date startCallTime) {
		this.startCallTime = startCallTime;
	}

	public Date getEndCallTime() {
		return endCallTime;
	}

	public void setEndCallTime(Date endCallTime) {
		this.endCallTime = endCallTime;
	}

	public String getVirtualMobile() {
		return virtualMobile;
	}

	public void setVirtualMobile(String virtualMobile) {
		this.virtualMobile = virtualMobile;
	}

	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public Short getIsRepeate() {
		return isRepeate;
	}

	public void setIsRepeate(Short isRepeate) {
		this.isRepeate = isRepeate;
	}

	
	
	
	
}
