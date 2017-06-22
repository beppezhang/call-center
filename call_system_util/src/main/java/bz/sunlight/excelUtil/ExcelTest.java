package bz.sunlight.excelUtil;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bz.sunlight.excelUtil.ExcelTools.ExcelType;

public final class ExcelTest
{
    private ExcelTest()
    {
    }

    public static Object[] fillHead(int cellNum)
    {
        Object[] cell = new Object[cellNum];
        for(int i = 0; i < cellNum; i++)
        {
            cell[i] = "Head_" + i;
        }
        return cell;
    }

    public static List<Object[]> fillList(int cellNum, int rowNum)
    {
        List<Object[]> lt = new ArrayList<Object[]>();
        Object[] cell = new Object[cellNum];
        for(int i = 0; i < rowNum; i++)
        {
            cell = new Object[cellNum];

            for(int j = 0; j < cellNum; j++)
            {
                if(j % 2 == 0)
                {
                    cell[j] = "第" + (i + 1) + "_" + (j + 1) + "列数据";
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

    public static Map<String, List<Object[]>> fillMap(int cellNum, int rowNum)
    {
        Map<String, List<Object[]>> m = new LinkedHashMap<String, List<Object[]>>();
        List<Object[]> lt = new ArrayList<Object[]>();
        Object[] cell = new Object[cellNum];
        for(int i = 0; i < rowNum; i++)
        {
            cell = new Object[cellNum];
            for(int j = 0; j < cellNum; j++)
            {
                if(j % 2 == 0)
                {
                    cell[j] = "第" + (i + 1) + "_" + (j + 1) + "列数据";
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
        // for(int i = 0; i < 100; i++)
        // {
        // m.put("sheet" + i, lt);
        // }
        m.put("sheet1", lt);
        m.put("sheet2", lt);
        m.put("sheet3", lt);
        // m.put("sheet4", lt);
        // m.put("sheet5", lt);
        // m.put("sheet6", lt);
        // m.put("sheet7", lt);
        // m.put("sheet8", lt);
        // m.put("sheet9", lt);
        // m.put("sheet10", lt);
        // m.put("sheet11", lt);
        // m.put("sheet12", lt);
        // m.put("sheet13", lt);
        // m.put("sheet14", lt);
        // m.put("sheet15", lt);
        // m.put("sheet16", lt);
        // m.put("sheet17", lt);
        // m.put("sheet18", lt);
        // m.put("sheet19", lt);
        // m.put("sheet20", lt);

        // m.put("第三个sheet", lt);
        return m;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception
    {
        Map<String, List<Object[]>> map = ExcelTest.fillMap(5, 6);
        List<Object[]> list = ExcelTest.fillList(5, 50001);
        Object[] head = ExcelTest.fillHead(5);
        ExcelData mapData = new ExcelData(map, head);
        ExcelData listData = new ExcelData(list, head);
        // String file03 = "D:/test-excel-03.xls";
        // String file07 = "D:/test-excel-07.xlsx";
        String file03 = "D:/副本销售线索模板.xls";
        String file07 = "D:/副本销售线索模板.xlsx";
        // List<String> rowList = new ArrayList<String>();
        // rowList.add(0, "测试");
        // rowList.add(1, "测试");
        // rowList.add(5, "测试");
        // file03 = "D:/9月通讯录.xls";
        // writer 2003
        // System.out.println("------------------------------ writer03 start");
        // writer03(listData, file03);
        // writer03(map, file03);
        // writer03(map, file03);
        // writer03(map, file03);
        // writer03(map, file03);
        // System.out.println("------------------------------ writer03 end");
        // writer 2007
        // System.out.println("------------------------------ writer07 start");
         writer07(listData, file07);
        // writer07(map, file07);
        // writer07(map, file07);
        // writer07(map, file07);
        // writer07(map, file07);
        // System.out.println("------------------------------ writer07 end");

        RowReader reader = new SimpleRowReader();
        //
        // // // reader 2003 callback
        // System.out.println("------------------------------ readerCallBack03 start");
        // readerCallBack03(file03, reader);
        // System.out.println("------------------------------ readerCallBack03 end");
        // // reader 2003 no_callback
        // System.out.println("------------------------------ reader03 start");
        // reader03(file03);
        // System.out.println("------------------------------ reader03 end");
        // // // reader 2007 callback
        // System.out.println("------------------------------ readerCallBack07 start");
        // readerCallBack07(file07, reader);
        // System.out.println("------------------------------ readerCallBack07 end");
        // // reader 2007 no_callback
        System.out.println("------------------------------ reader07 start");
//        reader07(file07);
        System.out.println("------------------------------ reader07 end");
    }

    /**
     *
     * @param file_07
     * @throws FileNotFoundException
     * @throws
     */
    @SuppressWarnings({"resource", "unused"})
    private static void reader07(String file07) throws FileNotFoundException
    {
        InputStream is = new FileInputStream(file07);
        List<List<String>> excelData = ExcelTools.readExcel(new File(file07), ExcelType.EXCEL2007);
        if(excelData != null)
        {
            for(List<String> list : excelData)
            {
                System.out.println(list.size());
                StringBuffer bf = new StringBuffer();
                for(String str : list)
                {
                    bf.append(str + ",");
                }

                System.out.println(bf.toString());
            }
        }
        else
        {
            System.out.println(excelData);
        }
    }

    /**
     *
     * @param file_03
     * @throws FileNotFoundException
     * @throws
     */
    @SuppressWarnings({})
    public static void reader03(String file03) throws FileNotFoundException
    {
        InputStream is = new FileInputStream(file03);
        List<List<String>> excelData = ExcelTools.readExcel(is, ExcelType.EXCEL2003);
        if(excelData != null)
        {
            for(List<String> list : excelData)
            {
                System.out.println(list.size());
                StringBuffer bf = new StringBuffer();
                for(String str : list)
                {
                    bf.append(str + ",");
                }
                System.out.println(bf.toString());
            }
        }
        else
        {
            System.out.println(excelData);
        }
    }

    /**
     *
     * @param file_07
     * @param reader
     * @throws FileNotFoundException
     * @throws
     */
    @SuppressWarnings({"unused", "resource"})
    private static void readerCallBack07(String file07, RowReader reader) throws FileNotFoundException
    {
        InputStream is = new FileInputStream(file07);
        ExcelTools.readExcel(new File(file07), reader, ExcelType.EXCEL2007);
    }

    /**
     *
     * @param file_03
     * @param reader
     * @throws FileNotFoundException
     * @throws
     */
    public static void readerCallBack03(String file03, RowReader reader) throws FileNotFoundException
    {
        InputStream is = new FileInputStream(file03);
        ExcelTools.readExcel(is, reader, ExcelType.EXCEL2003);
    }

    /**
     *
     * @param map
     * @throws FileNotFoundException
     * @throws
     */
    @SuppressWarnings("unused")
    private static void writer07(ExcelData data, String filePath) throws FileNotFoundException
    {
        OutputStream os = new FileOutputStream(new File(filePath));
        ExcelTools.writerExcel(os, data, ExcelType.EXCEL2007);
    }

    /**
     *
     * @return
     * @throws FileNotFoundException
     * @throws
     */
    @SuppressWarnings("unused")
    private static void writer03(ExcelData data, String filePath) throws FileNotFoundException
    {
        OutputStream os = new FileOutputStream(new File(filePath));
        ExcelTools.writerExcel(os, data, ExcelType.EXCEL2003);
    }
}
