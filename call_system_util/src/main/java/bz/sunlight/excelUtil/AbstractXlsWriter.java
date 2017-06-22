package bz.sunlight.excelUtil;



import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import bz.sunlight.exception.DataException;

public abstract class AbstractXlsWriter extends ExcelWriter
{
    private static final Log log = LogFactory.getLog(AbstractXlsWriter.class);
    private OutputStream out;
    private String encoding = null;
    private String dateFormat = "m/d/yy h:mm";
    private int rowNum = 0;
    private int cellNum = 0;
    private HSSFWorkbook wb;
    private HSSFFont headFont;
    private HSSFCellStyle headCellStyle;
    private HSSFFont dataFont;
    private HSSFCellStyle dataCellStyle;
    private HSSFCellStyle dateCellStyle;
    private short fontHeight = 10;
    private String fontName = "宋体";
    private HSSFSheet sheet;
    private HSSFRow row;

    @Override
    public void init()
    {
        wb = new HSSFWorkbook();
    }

    @Override
    public void start()
    {

        headFont = wb.createFont();
        headFont.setFontHeightInPoints(fontHeight);
        headFont.setFontName(fontName);
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 加粗

        headCellStyle = wb.createCellStyle();
        headCellStyle.setFont(headFont);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        dataFont = wb.createFont();
        dataFont.setFontHeightInPoints(fontHeight);
        dataFont.setFontName(fontName);
        dataFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        dataCellStyle = wb.createCellStyle();
        dataCellStyle.setFont(dataFont);
        dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);

        dateCellStyle = wb.createCellStyle();
        dateCellStyle.setFont(dataFont);
        dateCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(dateFormat));

    }

    @Override
    public abstract void generate();

    public void createSheet(String name)
    {
        sheet = wb.createSheet(name);
        rowNum = 0;
    }

    public void addRow(Object[] paramArray)
    {
        cellNum = paramArray.length;
        row = sheet.createRow(rowNum);
        for(int i = 0; i < cellNum; i++)
        {
            createCell(wb, row, i, paramArray[i]);
        }
        rowNum++;
    }

    public void createCell(HSSFWorkbook wb, HSSFRow row, int column, Object value)
    {
        HSSFCell cell = row.createCell(column);
        if(value instanceof Date)
        {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateCellStyle);
        }
        else if(value instanceof Double)
        {
            cell.setCellValue((Double) value);
            cell.setCellStyle(dataCellStyle);
        }
        else
        {
            if(value != null)
            {
                cell.setCellValue(new HSSFRichTextString(value.toString()));
            }
            else
            {
                cell.setCellValue("");
            }
            cell.setCellStyle(dataCellStyle);
        }
        if(row.getRowNum() == 0) // 第一行设置表头样式
        {
            cell.setCellStyle(headCellStyle);
        }
    }

    @Override
    public void end()
    {

    }

    @Override
    public void close()
    {
        try
        {
            wb.write(out); // HSSFWorkbook 写入到数据流
            if(out != null)
            {
                out.flush();
                out.close();
            }
        }
        catch(IOException e)
        {
            throw new DataException("HSSFWorkbook write 报错", e);
        }
    }

    @Override
    public void destory()
    {
        rowNum = 0;
        cellNum = 0;
        wb = null;
        headFont = null;
        headCellStyle = null;
        dataFont = null;
        dataCellStyle = null;
        sheet = null;
        row = null;
        log.info("AbstractXlsWriter 清空对象完成");
    }

    public OutputStream getOut()
    {
        return out;
    }

    public void setOut(OutputStream out)
    {
        this.out = out;
    }

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public int getRowNum()
    {
        return rowNum;
    }

    public void setRowNum(int rowNum)
    {
        this.rowNum = rowNum;
    }

    public int getCellNum()
    {
        return cellNum;
    }

    public void setCellNum(int cellNum)
    {
        this.cellNum = cellNum;
    }

    public HSSFWorkbook getWb()
    {
        return wb;
    }

    public void setWb(HSSFWorkbook wb)
    {
        this.wb = wb;
    }

    public HSSFFont getHeadFont()
    {
        return headFont;
    }

    public void setHeadFont(HSSFFont headFont)
    {
        this.headFont = headFont;
    }

    public HSSFCellStyle getHeadCellStyle()
    {
        return headCellStyle;
    }

    public void setHeadCellStyle(HSSFCellStyle headCellStyle)
    {
        this.headCellStyle = headCellStyle;
    }

    public HSSFFont getDataFont()
    {
        return dataFont;
    }

    public void setDataFont(HSSFFont dataFont)
    {
        this.dataFont = dataFont;
    }

    public HSSFCellStyle getDataCellStyle()
    {
        return dataCellStyle;
    }

    public void setDataCellStyle(HSSFCellStyle dataCellStyle)
    {
        this.dataCellStyle = dataCellStyle;
    }

    public short getFontHeight()
    {
        return fontHeight;
    }

    public void setFontHeight(short fontHeight)
    {
        this.fontHeight = fontHeight;
    }

    public String getDateFormat()
    {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat)
    {
        this.dateFormat = dateFormat;
    }

    public String getFontName()
    {
        return fontName;
    }

    public void setFontName(String fontName)
    {
        this.fontName = fontName;
    }

    public HSSFSheet getSheet()
    {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet)
    {
        this.sheet = sheet;
    }

    public HSSFRow getRow()
    {
        return row;
    }

    public void setRow(HSSFRow row)
    {
        this.row = row;
    }

}
