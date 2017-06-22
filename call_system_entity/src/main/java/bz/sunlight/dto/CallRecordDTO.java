package bz.sunlight.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;
@Alias("callRecordDTO")
public class CallRecordDTO {

	
	
	
	private String importBatch;
	private Short pageNo;
	private Short itemCount;
	private String name;
	private String customerServerNickName;
	private Short callResult;
	private Short dialTime;
	private Short followDay;
	private Integer callTime;
	private Date startDate;
	private Date endDate;
	private Integer pageItemNo;
	private String choice;   //导出excel的标识，是否全部导出
	private Short status;
	public String getImportBatch() {
		return importBatch;
	}
	public void setImportBatch(String importBatch) {
		this.importBatch = importBatch;
	}
	public Short getPageNo() {
		return pageNo;
	}
	public void setPageNo(Short pageNo) {
		this.pageNo = pageNo;
	}
	public Short getItemCount() {
		return itemCount;
	}
	public void setItemCount(Short itemCount) {
		this.itemCount = itemCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCustomerServerNickName() {
		return customerServerNickName;
	}
	public void setCustomerServerNickName(String customerServerNickName) {
		this.customerServerNickName = customerServerNickName;
	}
	public Short getCallResult() {
		return callResult;
	}
	public void setCallResult(Short callResult) {
		this.callResult = callResult;
	}
	public Short getDialTime() {
		return dialTime;
	}
	public void setDialTime(Short dialTime) {
		this.dialTime = dialTime;
	}
	public Short getFollowDay() {
		return followDay;
	}
	public void setFollowDay(Short followDay) {
		this.followDay = followDay;
	}
	public Integer getCallTime() {
		return callTime;
	}
	public void setCallTime(Integer callTime) {
		this.callTime = callTime;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getPageItemNo() {
		return pageItemNo;
	}
	public void setPageItemNo(Integer pageItemNo) {
		this.pageItemNo = pageItemNo;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
	
	
	
}
