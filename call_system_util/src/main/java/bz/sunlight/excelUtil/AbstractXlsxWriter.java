package bz.sunlight.excelUtil;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bz.sunlight.exception.DataException;

public abstract class AbstractXlsxWriter extends ExcelWriter
{
    private static final Log log = LogFactory.getLog(AbstractXlsxWriter.class);
    private static final String TEMP_EXCEL_PATH = "/temp/excel";
    private static final String XML_ENCODING = "UTF-8";
    private static final String TEMPLATE_FILE = "template.xlsx";
    private static final String TEMP_PREFIX = "sheet";
    private static final String TEMP_SUFFIX = ".xml";
    private SpreadsheetWriter sw;
    private XSSFWorkbook wb;
    private Map<String, XSSFCellStyle> styles;
    private OutputStream out;
    private XSSFSheet sheet;
    private String sheetRef; // name of the zip entry holding sheet data, e.g. /xl/worksheets/sheet1.xml
    private File template;
    private File tempFile;
    private Writer tempWriter;
    private int rowNum = 0;
    private int cellNum = 0;
    private short fontHeight = 10;
    private String fontName = "宋体";
    private String percentStyle = "percent";
    private String coeffStyle = "coeff";
    private String currencyStyle = "currency";
    private String dateStyle = "date";
    private String dataStyle = "data";
    private String headerStyle = "header";
    private List<File> tempFiles;
    private List<String> sheetRefs;
    private List<Writer> tempWriters;

    @Override
    public void init()
    {
        wb = new XSSFWorkbook();
        styles = createStyles(wb);
        tempFiles = new ArrayList<File>();
        sheetRefs = new ArrayList<String>();
        tempWriters = new ArrayList<Writer>();
    }

    @Override
    public void start()
    {
        // createTemplateExcel(); // save the template
        // createTempSheetXml();
    }

    private void createTemplateExcel()
    {
        try
        {
            FileOutputStream os = null;
            template = new File(TEMP_EXCEL_PATH + File.separator + TEMPLATE_FILE);
            try
            {
                os = new FileOutputStream(template);
                wb.write(os);
            }
            finally
            {
                os.flush();
                os.close();
            }
        }
        catch(FileNotFoundException e)
        {
            throw new DataException("template excel 文件未找到", e);
        }
        catch(IOException e)
        {
            throw new DataException("创建 excel 模板文件报错", e);
        }
    }

    private void createTempSheetXml()
    {
        try
        {
            // Generate XML file.
            tempFile = File.createTempFile(TEMP_PREFIX, TEMP_SUFFIX, new File(TEMP_EXCEL_PATH));
            tempWriter = new OutputStreamWriter(new FileOutputStream(tempFile), XML_ENCODING);
            tempWriters.add(tempWriter);
            sw = new SpreadsheetWriter(tempWriter);
        }
        catch(FileNotFoundException e)
        {
            throw new DataException("sheet*.xml 临时文件未找到", e);
        }
        catch(IOException e)
        {
            throw new DataException("创建 sheet*.xml 临时文件报错", e);
        }

    }

    public void addRow(Object[] paramArray)
    {
        try
        {
            cellNum = paramArray.length;
            insertRow(rowNum);
            for(int i = 0; i < cellNum; i++)
            {
                createCell(i, paramArray[i]);
            }
            endRow();
            rowNum++;
        }
        catch(Exception e)
        {
            throw new DataException("addRow 报错", e);
        }

    }

    @Override
    public abstract void generate();

    public void createSheet(String name)
    {
        sheet = wb.createSheet(name);
        sheetRef = sheet.getPackagePart().getPartName().getName();
        sheetRefs.add(sheetRef.substring(1));
        createTempSheetXml(); // 每次创建sheet,生成新sheet*.xml 临时文件
        tempFiles.add(tempFile);
        rowNum = 0;
    }

    @Override
    public void end()
    {
        try
        {
            if(tempWriters != null && tempWriters.size() > 0)
            {
                for(Writer $tempWriter : tempWriters)
                {
                    $tempWriter.flush();
                    $tempWriter.close();
                }
            }
            createTemplateExcel(); // save the template
            // Substitute the template entry with the generated data
            substitute(template, tempFiles.toArray(new File[tempFiles.size()]), sheetRefs.toArray(new String[sheetRefs.size()]), out);
        }
        catch(Exception e)
        {
            String msg = "substitute() method 替换生成excel数据报错";
            log.error(msg, e);
            throw new DataException(msg, e);
        }
    }

    @Override
    public void close()
    {
        try
        {
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
        sw = null;
        wb = null;
        styles = null;
        out = null;
        sheet = null;
        sheetRef = null; // name of the zip entry holding sheet data, e.g. /xl/worksheets/sheet1.xml
        template = null;
        tempFile = null;
        rowNum = 0;
        cellNum = 0;
    }

    public void createCell(int columnIndex, Object target) throws IOException
    {
        int styleIndex = styles.get(dataStyle).getIndex();
        if(target instanceof Date)
        {
            styleIndex = styles.get(dateStyle).getIndex();
            sw.createCell(columnIndex, (Date) target, styleIndex);
        }
        else if(target instanceof Double)
        {

            sw.createCell(columnIndex, Double.valueOf(target.toString()), styleIndex);
        }
        else
        {
            if(rowNum != 0)
            {
                sw.createCell(columnIndex, target.toString(), styleIndex);
            }
            else
            {
                styleIndex = styles.get(headerStyle).getIndex();
                sw.createCell(columnIndex, target.toString(), styleIndex);
            }

        }

    }

    public void beginSheet()
    {
        try
        {
            sw.beginSheet();
        }
        catch(IOException e)
        {
            throw new DataException("beginSheet 报错", e);
        }
    }

    public void insertRow(int rowNum)
    {
        try
        {
            sw.insertRow(rowNum);
        }
        catch(IOException e)
        {
            throw new DataException("insertRow 报错", e);
        }
    }

    public void createCell(int columnIndex, String value)
    {
        try
        {
            sw.createCell(columnIndex, value, -1);
        }
        catch(IOException e)
        {
            throw new DataException("createCell 报错", e);
        }
    }

    public void createCell(int columnIndex, double value)
    {
        try
        {
            sw.createCell(columnIndex, value, -1);
        }
        catch(IOException e)
        {
            throw new DataException("createCell 报错", e);
        }
    }

    public void endRow()
    {
        try
        {
            sw.endRow();
        }
        catch(IOException e)
        {
            throw new DataException("endRow 报错", e);
        }
    }

    public void endSheet()
    {
        try
        {
            sw.endSheet();
        }
        catch(IOException e)
        {
            throw new DataException("endSheet 报错", e);
        }
    }

    private Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb)
    {
        Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();
        XSSFDataFormat fmt = wb.createDataFormat();

        XSSFFont dataFont = wb.createFont();
        dataFont.setFontHeightInPoints(fontHeight);
        dataFont.setFontName(fontName);
        dataFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setAlignment(CellStyle.ALIGN_RIGHT);
        style1.setDataFormat(fmt.getFormat("0.0%"));
        style1.setFont(dataFont);
        styles.put(percentStyle, style1);

        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setAlignment(CellStyle.ALIGN_CENTER);
        style2.setDataFormat(fmt.getFormat("0.0X"));
        style2.setFont(dataFont);
        styles.put(coeffStyle, style2);

        XSSFCellStyle style3 = wb.createCellStyle();
        style3.setAlignment(CellStyle.ALIGN_RIGHT);
        style3.setDataFormat(fmt.getFormat("$#,##0.00"));
        style3.setFont(dataFont);
        styles.put(currencyStyle, style3);

        XSSFCellStyle style4 = wb.createCellStyle();
        style4.setAlignment(CellStyle.ALIGN_RIGHT);
        style4.setDataFormat((short) 22); // HSSFDataFormat.getBuiltinFormat fmt.getFormat("m/d/yy h:mm")
        style4.setFont(dataFont);
        styles.put(dateStyle, style4);

        XSSFCellStyle style5 = wb.createCellStyle();

        XSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints(fontHeight);
        headerFont.setFontName(fontName);

        style5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style5.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style5.setAlignment(CellStyle.ALIGN_CENTER);
        style5.setFont(headerFont);
        styles.put(headerStyle, style5);

        XSSFCellStyle style6 = wb.createCellStyle();
        style6.setAlignment(CellStyle.ALIGN_LEFT);
        style6.setFont(dataFont);
        styles.put(dataStyle, style6);

        return styles;
    }

    @SuppressWarnings("unused")
    private static void substitute(File zipfile, File tmpfile, String entry, OutputStream out) throws IOException
    {
        ZipFile zip = new ZipFile(zipfile);

        ZipOutputStream zos = new ZipOutputStream(out);

        @SuppressWarnings("unchecked")
        Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
        while(en.hasMoreElements())
        {
            ZipEntry ze = en.nextElement();
            if(!ze.getName().equals(entry))
            {
                copyStreamZip(zip, zos, ze);
            }
        }
        copyStreamFile(tmpfile, entry, zos);
        zos.close();
    }

    private static void substitute(File zipfile, File[] tmpfile, String[] entry, OutputStream out) throws IOException
    {
        ZipFile zip = new ZipFile(zipfile);

        ZipOutputStream zos = new ZipOutputStream(out);

        @SuppressWarnings("unchecked")
        Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
        while(en.hasMoreElements())
        {
            ZipEntry ze = en.nextElement();
            if(!exist(ze.getName(), entry))
            {
                copyStreamZip(zip, zos, ze);
            }
        }
        int size = tmpfile.length;
        for(int i = 0; i < size; i++)
        {
            copyStreamFile(tmpfile[i], entry[i], zos);
        }
        zos.close();
    }

    private static void copyStreamFile(File tmpfile, String entry, ZipOutputStream zos) throws IOException, FileNotFoundException
    {
        zos.putNextEntry(new ZipEntry(entry));
        InputStream is = new FileInputStream(tmpfile);
        try
        {
            copyStream(is, zos);
        }
        finally
        {
            is.close();
        }
    }

    private static void copyStreamZip(ZipFile zip, ZipOutputStream zos, ZipEntry ze) throws IOException
    {
        zos.putNextEntry(new ZipEntry(ze.getName()));
        InputStream is = zip.getInputStream(ze);
        try
        {
            copyStream(is, zos);
        }
        finally
        {
            is.close();
        }
    }

    private static boolean exist(String str, String[] invalidStrs)
    {
        for(String invalid : invalidStrs)
        {
            if(str.equals(invalid))
                return true;
        }
        return false;
    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException
    {
        byte[] chunk = new byte[1024];
        int count;
        while((count = in.read(chunk)) >= 0)
        {
            out.write(chunk, 0, count);
        }
    }

    public static class SpreadsheetWriter
    {
        private final Writer _out;
        private int _rownum;

        public SpreadsheetWriter(Writer out)
        {
            _out = out;
        }

        public void beginSheet() throws IOException
        {
            _out.write("<?xml version=\"1.0\" encoding=\"" + XML_ENCODING + "\"?>" + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
            _out.write("<sheetData>\n");
        }

        public void endSheet() throws IOException
        {
            _out.write("</sheetData>");
            _out.write("</worksheet>");
        }

        public void insertRow(int rownum) throws IOException
        {
            _out.write("<row r=\"" + (rownum + 1) + "\">\n");
            _rownum = rownum;
        }

        public void endRow() throws IOException
        {
            _out.write("</row>\n");
        }

        public void createCell(int columnIndex, String value, int styleIndex) throws IOException
        {
            String ref = new CellReference(_rownum, columnIndex).formatAsString();
            _out.write("<c r=\"" + ref + "\" t=\"inlineStr\"");
            if(styleIndex != -1)
                _out.write(" s=\"" + styleIndex + "\"");
            _out.write(">");
            _out.write("<is><t>" + XMLEncoder.encode(value) + "</t></is>");
            _out.write("</c>");
        }

        public void createCell(int columnIndex, String value) throws IOException
        {
            createCell(columnIndex, value, -1);
        }

        public void createCell(int columnIndex, double value, int styleIndex) throws IOException
        {
            String ref = new CellReference(_rownum, columnIndex).formatAsString();
            _out.write("<c r=\"" + ref + "\" t=\"n\"");
            if(styleIndex != -1)
                _out.write(" s=\"" + styleIndex + "\"");
            _out.write(">");
            _out.write("<v>" + value + "</v>");
            _out.write("</c>");
        }

        public void createCell(int columnIndex, double value) throws IOException
        {
            createCell(columnIndex, value, -1);
        }

        public void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException
        {
            createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
        }

        public void createCell(int columnIndex, Date value, int styleIndex) throws IOException
        {
            createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
        }
    }

    public SpreadsheetWriter getSw()
    {
        return sw;
    }

    public void setSw(SpreadsheetWriter sw)
    {
        this.sw = sw;
    }

    public XSSFWorkbook getWb()
    {
        return wb;
    }

    public void setWb(XSSFWorkbook wb)
    {
        this.wb = wb;
    }

    public Map<String, XSSFCellStyle> getStyles()
    {
        return styles;
    }

    public void setStyles(Map<String, XSSFCellStyle> styles)
    {
        this.styles = styles;
    }

    public OutputStream getOut()
    {
        return out;
    }

    public void setOut(OutputStream out)
    {
        this.out = out;
    }

    public XSSFSheet getSheet()
    {
        return sheet;
    }

    public void setSheet(XSSFSheet sheet)
    {
        this.sheet = sheet;
    }

    public String getSheetRef()
    {
        return sheetRef;
    }

    public void setSheetRef(String sheetRef)
    {
        this.sheetRef = sheetRef;
    }

    public File getTemplate()
    {
        return template;
    }

    public void setTemplate(File template)
    {
        this.template = template;
    }

    public File getTempData()
    {
        return tempFile;
    }

    public void setTempData(File tempData)
    {
        tempFile = tempData;
    }

    public short getFontHeight()
    {
        return fontHeight;
    }

    public void setFontHeight(short fontHeight)
    {
        this.fontHeight = fontHeight;
    }

    public String getFontName()
    {
        return fontName;
    }

    public void setFontName(String fontName)
    {
        this.fontName = fontName;
    }

    public String getPercentStyle()
    {
        return percentStyle;
    }

    public void setPercentStyle(String percentStyle)
    {
        this.percentStyle = percentStyle;
    }

    public String getCoeffStyle()
    {
        return coeffStyle;
    }

    public void setCoeffStyle(String coeffStyle)
    {
        this.coeffStyle = coeffStyle;
    }

    public String getCurrencyStyle()
    {
        return currencyStyle;
    }

    public void setCurrencyStyle(String currencyStyle)
    {
        this.currencyStyle = currencyStyle;
    }

    public String getDateStyle()
    {
        return dateStyle;
    }

    public void setDateStyle(String dateStyle)
    {
        this.dateStyle = dateStyle;
    }

    public String getDataStyle()
    {
        return dataStyle;
    }

    public void setDataStyle(String dataStyle)
    {
        this.dataStyle = dataStyle;
    }

    public String getHeaderStyle()
    {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle)
    {
        this.headerStyle = headerStyle;
    }

}
