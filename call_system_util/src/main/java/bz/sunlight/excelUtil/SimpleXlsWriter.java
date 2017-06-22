package bz.sunlight.excelUtil;



import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bz.sunlight.exception.DataException;

public class SimpleXlsWriter extends AbstractXlsWriter
{
    private static final Log log = LogFactory.getLog(AbstractXlsWriter.class);
    private ExcelData excelData;

    /**
     * Object[]中尽量使用String, 如存在Date等其它类型，则性能下降大概20%
     * @param out
     * @param excelMap Map->LinkedHashMap 确保顺序
     *
     */
    public SimpleXlsWriter(OutputStream out, ExcelData excelData)
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
                if(dataList.size() >= 65535)
                    throw new DataException("Excel2003 单个sheet最大行数限制  IllegalArgumentException Invalid row number (65536) outside allowable range (0..65535)");
                log.debug("key = " + sheetName);

                createSheet(sheetName);
                addValidHead();
                for(Object[] obj : dataList)
                {
                    log.debug(obj);
                    addRow(obj);
                }

            }
        }
        else
        {
            int count = 0;
            int sheetCount = 0;
            for(Object[] obj : excelData.getExcelListData())
            {
                if(count == (sheetCount * ExcelData.XLS_SPLIT_ROW))
                {
                    sheetCount++;
                    createSheet(String.valueOf(sheetCount));
                    log.debug(obj);
                    addValidHead();
                    addRow(obj);
                }
                else
                {
                    log.debug(obj);
                    addRow(obj);
                }
                count++;
            }

            if(excelData.getExcelListData().size() == 0)
            {
                createSheet(String.valueOf(sheetCount));
                addValidHead();
            }
        }
    }

    private void addValidHead()
    {
        if(excelData.getExcelHeadData() != null && excelData.getExcelHeadData().length > 0)
            addRow(excelData.getExcelHeadData());
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception
    {

        OutputStream os = new FileOutputStream(new File("D:/SimplePoiXlsWriter.xls"));

        Map<String, List<Object[]>> map = SimpleXlsWriter.fillMap();
        ExcelData excelListData = new ExcelData(SimpleXlsWriter.fillList(), SimpleXlsWriter.fillHead());
        // ExcelData excelMapData = new ExcelData(SimpleXlsWriter.fillMap(), SimpleXlsWriter.fillHead());
        AbstractXlsWriter excel = new SimpleXlsWriter(os, excelListData);
        // excel.setDateFormat("h:mm:ss AM/PM");
        excel.process();
    }

    public ExcelData getExcelData()
    {
        return excelData;
    }

    public void setExcelData(ExcelData excelData)
    {
        this.excelData = excelData;
    }

    public static Map<String, List<Object[]>> fillMap()
    {
        int cellNum = 5;
        Map<String, List<Object[]>> m = new LinkedHashMap<String, List<Object[]>>();
        List<Object[]> lt = new ArrayList<Object[]>();
        Object[] cell = new Object[cellNum];
        for(int i = 0; i < 6; i++)
        {
            cell = new Object[cellNum];

            for(int j = 0; j < cellNum; j++)
            {
                if(j % 2 == 0)
                {
                    cell[j] = "第" + i + "_" + j + "列数据";
                }
                else if(j % 3 == 0)
                {
                    cell[j] = 987695.28;
                }
                else
                {
                    cell[j] = new Date(); // 88->4205ms date-> 5120ms
                }
            }
            lt.add(cell);

        }
        m.put("sheet1", lt);
        m.put("sheet2", lt);

        return m;
    }

    public static List<Object[]> fillList()
    {
        int cellNum = 5;
        List<Object[]> lt = new ArrayList<Object[]>();
        Object[] cell = new Object[cellNum];
        for(int i = 0; i < 6; i++)
        {
            cell = new Object[cellNum];

            for(int j = 0; j < cellNum; j++)
            {
                if(j % 2 == 0)
                {
                    cell[j] = "第" + i + "_" + j + "列数据";
                }
                else if(j % 3 == 0)
                {
                    cell[j] = 987695.28;
                }
                else
                {
                    cell[j] = new Date(); // 88->4205ms date-> 5120ms
                }
            }
            lt.add(cell);

        }
        return lt;
    }

    public static Object[] fillHead()
    {
        int cellNum = 5;
        Object[] cell = new Object[cellNum];
        for(int i = 0; i < cellNum; i++)
        {
            cell[i] = "Head_" + i;
        }
        return cell;
    }
}
