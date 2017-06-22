package bz.sunlight.excelUtil;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import bz.sunlight.exception.DataException;

public final class ExcelTools
{
    public enum ExcelType
    {
        EXCEL2003, EXCEL2007,
    }

    private static AbstractXlsReader readerXls;
    private static AbstractXlsxReader readerXlsx;
    private static AbstractXlsWriter writerXls;
    private static AbstractXlsxWriter writerXlsx;

    private ExcelTools()
    {

    }

    public static List<List<String>> readExcel(InputStream is, ExcelType excelType)
    {
        List<List<String>> excelData = null;
        switch(excelType)
        {
            case EXCEL2003:
                excelData = readXlsExcel(is);
                break;
            case EXCEL2007:
                excelData = readXlsxExcel(is);
                break;
            default:
                break;
        }
        return excelData;
    }

    public static List<List<String>> readExcel(File sourceFile, ExcelType excelType)
    {
        List<List<String>> excelData = null;
        switch(excelType)
        {
            case EXCEL2003:
                try
                {
                    excelData = readXlsExcel(new FileInputStream(sourceFile));
                }
                catch(FileNotFoundException e)
                {
                    throw new DataException(String.format("%s 文件不存在", sourceFile.getName()), e);
                }
                break;
            case EXCEL2007:
                excelData = readXlsxExcel(sourceFile);
                break;
            default:
                break;
        }
        return excelData;
    }

    public static void readExcel(InputStream is, RowReader reader, ExcelType excelType)
    {
        switch(excelType)
        {
            case EXCEL2003:
                readXlsExcel(is, reader);
                break;
            case EXCEL2007:
                readXlsxExcel(is, reader);
                break;
            default:
                break;
        }
    }

    public static void readExcel(File sourceFile, RowReader reader, ExcelType excelType)
    {
        switch(excelType)
        {
            case EXCEL2003:
                try
                {
                    readXlsExcel(new FileInputStream(sourceFile), reader);
                }
                catch(FileNotFoundException e)
                {
                    throw new DataException(String.format("%s 文件不存在", sourceFile.getName()), e);
                }
                break;
            case EXCEL2007:
                readXlsxExcel(sourceFile, reader);
                break;
            default:
                break;
        }
    }

    private static void readXlsxExcel(InputStream is, RowReader reader)
    {
        readerXlsx = new SimpleXlsxReader(is, reader, true);
        readerXlsx.process();
    }

    private static void readXlsxExcel(File sourceFile, RowReader reader)
    {
        readerXlsx = new SimpleXlsxReader(sourceFile, reader, true);
        readerXlsx.process();
    }

    private static void readXlsExcel(InputStream is, RowReader reader)
    {
        readerXls = new SimpleXlsReader(is, reader, true);
        readerXls.process();
    }

    private static List<List<String>> readXlsExcel(InputStream is)
    {
        readerXls = new SimpleXlsReader(is, null, false);
        readerXls.process();
        return readerXls.getExcelData();
    }

    private static List<List<String>> readXlsxExcel(InputStream is)
    {
        readerXlsx = new SimpleXlsxReader(is, null, false);
        readerXlsx.process();
        return readerXlsx.getExcelData();
    }

    private static List<List<String>> readXlsxExcel(File sourceFile)
    {
        readerXlsx = new SimpleXlsxReader(sourceFile, null, false);
        readerXlsx.process();
        return readerXlsx.getExcelData();
    }

    public static void writerExcel(OutputStream os, ExcelData data, ExcelType excelType)
    {
        switch(excelType)
        {
            case EXCEL2003:
                writeXlsExcel(os, data);
                break;
            case EXCEL2007:
                writeXlsxExcel(os, data);
                break;
            default:
                break;
        }
    }

    public static void writerExcel(File sourceFile, ExcelData data, ExcelType excelType)
    {
        try
        {
            writerExcel(new FileOutputStream(sourceFile), data, excelType);
        }
        catch(FileNotFoundException e)
        {
            throw new DataException(String.format("%s 文件不存在", sourceFile.getName()), e);
        }

    }

    private static void writeXlsExcel(OutputStream os, ExcelData data)
    {
        writerXls = new SimpleXlsWriter(os, data);
        writerXls.process();
    }

    private static void writeXlsxExcel(OutputStream os, ExcelData data)
    {
        writerXlsx = new SimpleXlsxWriter(os, data);
        writerXlsx.process();
    }
}
