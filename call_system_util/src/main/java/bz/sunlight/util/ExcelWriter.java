package bz.sunlight.util;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

	/**默认导出Excel为2003版*/
	private static final ExcelType DEFAULT_EXCEL_TYPE = ExcelType.EXCEL_2003;
	/**默认导出Excel的日期格式：yyyy/MM/dd HH:mm:ss*/
	private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

	private static final String DEFAULT_IMPORT_RESULT_PATH = "/temp/exportRepository";
	private String importResultPath;

	private ExcelType excelType;
	private String dateFormat;
	private Workbook workbook;
	private DataFormat dataFormat;
	private CellStyle defaultCellDateStyle;
	
	public ExcelWriter(){
		this(DEFAULT_EXCEL_TYPE, DEFAULT_DATE_FORMAT, DEFAULT_IMPORT_RESULT_PATH);
	}
	
	public ExcelWriter(ExcelType excelType){
		this(excelType, DEFAULT_DATE_FORMAT);
	}
	
	public ExcelWriter(ExcelType excelType, String dateFormat){
		setExcelType(excelType);
		setDateFormat(dateFormat);
	}

	public ExcelWriter(ExcelType excelType, String dateFormat, String importResultPath){
		setExcelType(excelType);
		setDateFormat(dateFormat);
		setImportResultPath(importResultPath);
	}
	
	public Workbook getWorkbook(){
		if(workbook == null){
			switch (excelType){
			case EXCEL_2003 :
				workbook = new HSSFWorkbook();
				break;
			case EXCEL_2007 :
				workbook = new XSSFWorkbook();
				break;
			default :
				throw new RuntimeException("不支持的文件类型！");
			}
		}
		return workbook;
	}
	
	public ExcelWriter initExcelData(List<List<List<Object>>> excelData){	
		if(excelData != null){
        	for(List<List<Object>> sheetData : excelData){
        		if(sheetData != null){
        			Sheet sheet = getWorkbook().createSheet();
        			int rowIndex = 0;
        			for(List<Object> rowData : sheetData){
        				if(rowData != null){
        					Row row = sheet.createRow(rowIndex);
        					int columnIndex = 0;
            				for(Object cellData : rowData){
            					if(cellData != null){
            						Cell cell = row.createCell(columnIndex);
            						if(cellData instanceof String){
            							cell.setCellValue((String)cellData);
            						}else if(cellData instanceof Date){
            							cell.setCellValue((Date)cellData);
            							cell.setCellStyle(getDefaultCellDateStyle());
            						}else if(cellData instanceof Boolean){
            							cell.setCellValue((Boolean)cellData);
            						}else if(cellData instanceof Calendar){
            							cell.setCellValue((Calendar)cellData);
            						}else if(cellData instanceof RichTextString){
            							cell.setCellValue((RichTextString)cellData);
            						}else if(cellData instanceof Integer || cellData instanceof Long || cellData instanceof Double){
            							cell.setCellValue((Double)cellData);
            						}else{
            							//不支持的数据类型
            							cell.setCellValue((String)cellData);
            						}
            					}
            					columnIndex++;
            				}
        				}
        				rowIndex++;
        			}
        		}
        	}
		}
		return this;
	}
	
	public void write(HttpServletResponse response, String fileName){
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName) + getExcelType().getSuffix());
		//response.addHeader("Content-Length", "");
		response.setContentType(getExcelType().getResponseContentType());
        response.setCharacterEncoding("UTF-8");
        try {
			write(response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("获取输出流或写入输出流出错！", e);
		}
	}

	public String write(){
		String filePath =  getImportResultPath() + File.separator + new Date().getTime() + getExcelType().getSuffix();
		try {
			write(new FileOutputStream(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	public String write(String partFilePath){
		String filePath =  getImportResultPath() + File.separator + partFilePath;
		try {
			write(new FileOutputStream(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	public void write(OutputStream outputStream){
		 try {
			 getWorkbook().write(outputStream);
			} catch (IOException e) {
				throw new RuntimeException("获取输出流或写入输出流出错！", e);
			} finally {
				try {
					outputStream.flush();
					outputStream.close();
					//getWorkbook().close();
				} catch (IOException e) {
					throw new RuntimeException("Workbook关闭出错！", e);
				}
			}
	}

	public void write(HttpServletResponse response, String fileName, List<List<List<Object>>> excelData){
		initExcelData(excelData);
		write(response, fileName);
	}
	
	public DataFormat getDataFormat(){
		if(dataFormat == null){
			dataFormat = getWorkbook().createDataFormat();
		}
		return dataFormat;
	}
	
	public CellStyle getDefaultCellDateStyle(){
		if(defaultCellDateStyle == null){
			defaultCellDateStyle = getWorkbook().createCellStyle();
			defaultCellDateStyle.setDataFormat(getDataFormat().getFormat(dateFormat));
		}
		return defaultCellDateStyle;
	}

	public ExcelType getExcelType() {
		return excelType;
	}

	public void setExcelType(ExcelType excelType) {
		if(excelType == null){
			excelType = DEFAULT_EXCEL_TYPE;
		}
		this.excelType = excelType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		if(dateFormat == null){
			dateFormat = DEFAULT_DATE_FORMAT;
		}
		this.dateFormat = dateFormat;
	}

	public String getImportResultPath() {
		return importResultPath;
	}

	public void setImportResultPath(String importResultPath) {
		if(importResultPath == null){
			importResultPath = DEFAULT_IMPORT_RESULT_PATH;
		}
		this.importResultPath = importResultPath;
	}
}
