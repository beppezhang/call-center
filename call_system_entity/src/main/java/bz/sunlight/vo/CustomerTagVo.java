package bz.sunlight.vo;

import java.util.Date;
import java.util.List;

import bz.sunlight.entity.CallRecord;


public class CustomerTagVo {

	private int totalPageNo;
	private int totalItemCount;

	
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

	public List<CustomerTag> getCustomerTags() {
		return customerTags;
	}
	public void setCustomerTags(List<CustomerTag> customerTags) {
		this.customerTags = customerTags;
	}
	
	public class CustomerTag{
		
		private String id;
		
		private String lastCallRecordId;
		
		private String customerAdId;
		
		private String customerTag;
		
		private String importBatch;
		
		private String name;
		
		private Short sex;
		
		private Short status;
		
		private Date createTime;
		
		private String decodeId;
		
		private Short isRepeate;
		
		private Date lastOperateTime;
		
		private String lastCustomerServerId;
		
		private Short dialTime;
		
		private String mobile;
		
		private String lastCustomerServerCode;
		
		private String lastCustomerServerName;
		
		private String lastCustomerServerNickName;
		
		private Short callResult;
		
		private String  remark;
		
		private Short hangUpPosition;
		
		private Short followStatus;
		
		private Date followDate;
		
		private Date startCallTime;
		
		private Date endCallTime;
		
		private String virtualMobile;
		
		private Short followDay;
		
		private List<CallRecord> callRecords;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCustomerAdId() {
			return customerAdId;
		}

		public void setCustomerAdId(String customerAdId) {
			this.customerAdId = customerAdId;
		}

		public String getCustomerTag() {
			return customerTag;
		}

		public void setCustomerTag(String customerTag) {
			this.customerTag = customerTag;
		}

		public String getImportBatch() {
			return importBatch;
		}

		public void setImportBatch(String importBatch) {
			this.importBatch = importBatch;
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

		public Short getStatus() {
			return status;
		}

		public void setStatus(Short status) {
			this.status = status;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getDecodeId() {
			return decodeId;
		}

		public void setDecodeId(String decodeId) {
			this.decodeId = decodeId;
		}

		public Short getIsRepeate() {
			return isRepeate;
		}

		public void setIsRepeate(Short isRepeate) {
			this.isRepeate = isRepeate;
		}

		public Date getLastOperateTime() {
			return lastOperateTime;
		}

		public void setLastOperateTime(Date lastOperateTime) {
			this.lastOperateTime = lastOperateTime;
		}

		public String getLastCustomerServerId() {
			return lastCustomerServerId;
		}

		public void setLastCustomerServerId(String lastCustomerServerId) {
			this.lastCustomerServerId = lastCustomerServerId;
		}

		public Short getDialTime() {
			return dialTime;
		}

		public void setDialTime(Short dialTime) {
			this.dialTime = dialTime;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getLastCustomerServerCode() {
			return lastCustomerServerCode;
		}

		public void setLastCustomerServerCode(String lastCustomerServerCode) {
			this.lastCustomerServerCode = lastCustomerServerCode;
		}

		public String getLastCustomerServerName() {
			return lastCustomerServerName;
		}

		public void setLastCustomerServerName(String lastCustomerServerName) {
			this.lastCustomerServerName = lastCustomerServerName;
		}

		public String getLastCustomerServerNickName() {
			return lastCustomerServerNickName;
		}

		public void setLastCustomerServerNickName(String lastCustomerServerNickName) {
			this.lastCustomerServerNickName = lastCustomerServerNickName;
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

		public List<CallRecord> getCallRecords() {
			return callRecords;
		}

		public void setCallRecords(List<CallRecord> callRecords) {
			this.callRecords = callRecords;
		}

		public Short getFollowDay() {
			return followDay;
		}

		public void setFollowDay(Short followDay) {
			this.followDay = followDay;
		}

		public String getLastCallRecordId() {
			return lastCallRecordId;
		}

		public void setLastCallRecordId(String lastCallRecordId) {
			this.lastCallRecordId = lastCallRecordId;
		}

		
		
		
		
		
	}
	
}
