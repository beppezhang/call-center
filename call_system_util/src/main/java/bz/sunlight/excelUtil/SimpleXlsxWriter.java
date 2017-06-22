package bz.sunlight.excelUtil;



import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bz.sunlight.exception.DataException;

public class SimpleXlsxWriter extends AbstractXlsxWriter
{
    private static final Log log = LogFactory.getLog(AbstractXlsWriter.class);

    private ExcelData excelData;

    public SimpleXlsxWriter(OutputStream out, ExcelData excelData)
    {
        setOut(out);
        this.excelData = excelData;
    }

    @Override
    public void generate()
    {
        String sheetName = null;
        List<Object[]> dataList = null;
        if(excelData.isMap())
        {
            for(Entry<String, List<Object[]>> entry : excelData.getExcelMapData().entrySet())
            {
                sheetName = entry.getKey();
                dataList = entry.getValue();
                if(dataList.size() >= 1048575)
                    throw new DataException("Excel2007 单个sheet最大行数限制  IllegalArgumentException if rowNum < 0 or greater than 1048575");
                createSheet(sheetName);
                beginSheet();
                addValidHead();
                for(Object[] obj : dataList)
                {
                    log.debug(obj);
                    addRow(obj);
                }
                endSheet();
            }
        }
        else
        {
            int count = 0;
            int sheetCount = 0;
            createSheet(String.valueOf(sheetCount + 1));
            beginSheet();
            addValidHead();
            for(Object[] obj : excelData.getExcelListData())
            {
                if(count == ((sheetCount + 1) * ExcelData.XLSX_SPLIT_ROW))
                {
                    sheetCount++;
                    endSheet();
                    createSheet(String.valueOf(sheetCount + 1));
                    beginSheet();
                    addValidHead();
                    log.debug(obj);
                    addRow(obj);
                }
                else
                {
                    log.debug(obj);
                    addRow(obj);

                }

                count++;

                if(count == excelData.getExcelListData().size())
                    endSheet();

            }
        }

    }

    private void addValidHead()
    {
        if(excelData.getExcelHeadData() != null && excelData.getExcelHeadData().length > 0)
            addRow(excelData.getExcelHeadData());
    }

    public ExcelData getExcelData()
    {
        return excelData;
    }

    public void setExcelData(ExcelData excelData)
    {
        this.excelData = excelData;
    }

    public static void main(String[] args) throws Exception
    {
        OutputStream os = new FileOutputStream(new File("D:/SimplePoiXlsxWriter.xlsx"));

        ExcelData excelListData = new ExcelData(SimpleXlsWriter.fillList(), SimpleXlsWriter.fillHead());
        AbstractXlsxWriter excel = new SimpleXlsxWriter(os, excelListData);
        excel.process();
    }

}
