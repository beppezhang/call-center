package bz.sunlight.util;



public enum ExcelType {

	EXCEL_2003,EXCEL_2007;
	public String getSuffix(){
		String suffix = "";
		if(this.equals(EXCEL_2003)){
			suffix = ".xls";
		}else if(this.equals(EXCEL_2007)){
			suffix = ".xlsx";
		}else{
			suffix = "";
		}
		return suffix;
	}
	public String getResponseContentType(){
		String contentType = "";
		if(this.equals(EXCEL_2003)){
			contentType = "application/vnd.ms-excel";
		}else if(this.equals(EXCEL_2007)){
			contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		}else{
			contentType = "application/vnd.ms-excel";
		}
		return contentType;
	}
}
