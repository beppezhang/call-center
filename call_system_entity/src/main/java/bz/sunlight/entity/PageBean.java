package bz.sunlight.entity;
/**
 * 分页类：展示具体页数   上一页  下一页   
 * @author Administrator
 *
 */
public class PageBean {

	private int pageNo;           //第几页
	private int totalItemCount;   //总共的条数
	private int totalPageCount;   //总共的页数
	private int pageItemNo;       //每页第一条数据
	private int itemCount;        //每页的条数
	
	
	
	public PageBean(int pageNo, int itemCount) {
		super();
		this.pageNo = pageNo;
		this.itemCount = itemCount;
	}
	
	


	public PageBean(int totalItemCount, int itemCount,boolean flag) {
		super();
		this.totalItemCount = totalItemCount;
		this.itemCount = itemCount;
	}




	//  初始化时当传入的页码大于总页码时的逻辑处理
	public PageBean(int pageNo, int totalItemCount, int itemCount) {  //1,0,2
		super();
		if(totalItemCount%itemCount==0){
			if(pageNo>totalItemCount/itemCount){
				this.pageNo = totalItemCount/itemCount;
			}else{
				this.pageNo=pageNo;
			}
		}else{
			if(pageNo>totalItemCount/itemCount+1){
				this.pageNo = totalItemCount/itemCount+1;
			}else{
				this.pageNo=pageNo;
			}
		}
		
		this.totalItemCount = totalItemCount;
		this.itemCount = itemCount;
	}



	public int getPageNo() {
		
		return pageNo;
	}



	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}



	public int getTotalItemCount() {
		return totalItemCount;
	}



	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}



	public int getTotalPageCount() {
		if(totalItemCount%itemCount==0){
			return totalItemCount/itemCount;
		}else{
			return totalItemCount/itemCount+1;
		}
		
	}



	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}



	public int getPageItemNo() {
		if(pageNo==0){
			return 0;
		}
		return itemCount*(pageNo-1);
	}



	public void setPageItemNo(int pageItemNo) {
		this.pageItemNo = pageItemNo;
	}



	public int getItemCount() {
		return itemCount;
	}



	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}



	public static void main(String[] args) {
		System.out.println(30%10);
	}
}
