package bz.sunlight.excelUtil;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import bz.sunlight.exception.DataException;

//8/11/14 17:06 日期格式问题  m/d/yy h:mm 应为 2014/8/11  17:06:32
//目前返回数据格式为List<List<String>> int sheetIndex,String sheetName,int curRow, List<String> rowlist
//10000000 条数据 通过InputStream 内存溢出, File 则可以正常读取
public abstract class AbstractXlsxReader extends ExcelReader
{
    // /////////////////////////////////////
    enum xssfDataType
    {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
    }
    private OPCPackage xlsxPackage;
    private int minColumns;
    private PrintStream output = System.out;
    private InputStream is;
    private File file;

    private List<List<String>> excelData;
    private List<String> rowList;
    private RowReader rowReader;
    private boolean callBack = false; // true RowReader ; false get excelData

    private int sheetIndex = -1;
    private String sheetName;

    public AbstractXlsxReader(InputStream is)
    {
        this.is = is;
    }

    public AbstractXlsxReader(InputStream is, RowReader reader)
    {
        this.is = is;
        rowReader = reader;
    }

    public AbstractXlsxReader(InputStream is, RowReader reader, boolean callBack)
    {
        this.is = is;
        rowReader = reader;
        this.callBack = callBack;
    }

    public AbstractXlsxReader(File sourceFile)
    {
        file = sourceFile;
    }

    public AbstractXlsxReader(File sourceFile, RowReader reader)
    {
        file = sourceFile;
        rowReader = reader;
    }

    public AbstractXlsxReader(File sourceFile, RowReader reader, boolean callBack)
    {
        file = sourceFile;
        rowReader = reader;
        this.callBack = callBack;
    }

    public List<List<String>> getExcelData()
    {
        return excelData;
    }

    @Override
    public void init()
    {
        try
        {
            if(file != null)
            {
                xlsxPackage = OPCPackage.open(file, PackageAccess.READ);
            }
            else
            {
                xlsxPackage = OPCPackage.open(is);
            }
        }
        catch(InvalidFormatException e)
        {
            throw new DataException("OPCPackage 创建失败 文件格式错误", e);
        }
        catch(Exception e)
        {
            throw new DataException("OPCPackage 创建失败", e);
        }
        rowList = new ArrayList<String>();
        if(!callBack)
            excelData = new ArrayList<List<String>>();
    }

    @Override
    public void start()
    {
        try
        {
            processMaster();
        }
        catch(IOException e)
        {
            throw new DataException("processMaster ReadOnlySharedStringsTable 报错", e);
        }
        catch(OpenXML4JException e)
        {
            throw new DataException("processMaster ReadOnlySharedStringsTable 报错", e);
        }
        catch(ParserConfigurationException e)
        {
            throw new DataException("processMaster ReadOnlySharedStringsTable 报错", e);
        }
        catch(SAXException e)
        {
            throw new DataException("processMaster ReadOnlySharedStringsTable 报错", e);
        }
    }

    @Override
    public void generate()
    {

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
            if(xlsxPackage != null)
                xlsxPackage.close();
        }
        catch(IOException e)
        {
            throw new DataException("OPCPackage close 报错", e);
        }
    }

    @Override
    public void destory()
    {
        rowList = null;
        rowReader = null;
        xlsxPackage = null;
    }

    public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException
    {

        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        ContentHandler handler = new MyXSSFSheetHandler(styles, strings, minColumns);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
    }

    public void processMaster() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException
    {

        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while(iter.hasNext())
        {
            InputStream stream = iter.next();
            sheetName = iter.getSheetName();
            sheetIndex = index;
            print(null, true);
            print(sheetName + " [index=" + index + "]:", true);
            processSheet(styles, strings, stream);
            stream.close();
            ++index;
        }
    }

    private void print(String value, boolean ln)
    {
        if(ln)
        {
            if(value == null)
            {
                output.println();
            }
            else
            {
                output.println(value);
            }
        }
        else
        {
            output.print(value);
        }
    }
    class MyXSSFSheetHandler extends DefaultHandler
    {

        private StylesTable stylesTable;
        private ReadOnlySharedStringsTable sharedStringsTable;
        // private final PrintStream output;
        private final int minColumnCount;
        private boolean vIsOpen;
        private xssfDataType nextDataType;
        private short formatIndex;
        private String formatString;
        private final DataFormatter formatter;
        private int thisColumn = -1;
        private int lastColumnNumber = -1;
        private StringBuffer value;
        private String thisStr = null;
        private int thisRow;
        // 当前列
        private int curCol = 0;
        // 总列数
        private int totalCol = 0;
        // 是否第一行标题
        private boolean isFirstRow = true;

        private String defaultStr = "";

        public MyXSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings, int cols)
        {
            stylesTable = styles;
            sharedStringsTable = strings;
            minColumnCount = cols;
            // this.output = target;
            value = new StringBuffer();
            nextDataType = xssfDataType.NUMBER;
            formatter = new DataFormatter();
        }

        /*
           * (non-Javadoc)
           * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
           */
        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
        {

            if("inlineStr".equals(name) || "v".equals(name) || "t".equals(name))
            {
                vIsOpen = true;
                // Clear contents cache
                value.setLength(0);
            }
            else if("c".equals(name)) // c => cell
            {
                String r = attributes.getValue("r");
                int firstDigit = -1;
                for(int c = 0; c < r.length(); ++c)
                {
                    if(Character.isDigit(r.charAt(c)))
                    {
                        firstDigit = c;
                        break;
                    }
                }
                thisColumn = nameToColumn(r.substring(0, firstDigit));

                nextDataType = xssfDataType.NUMBER;
                formatIndex = -1;
                formatString = null;
                String cellType = attributes.getValue("t");
                String cellStyleStr = attributes.getValue("s");
                if("b".equals(cellType))
                    nextDataType = xssfDataType.BOOL;
                else if("e".equals(cellType))
                    nextDataType = xssfDataType.ERROR;
                else if("inlineStr".equals(cellType))
                    nextDataType = xssfDataType.INLINESTR;
                else if("s".equals(cellType))
                    nextDataType = xssfDataType.SSTINDEX;
                else if("str".equals(cellType))
                    nextDataType = xssfDataType.FORMULA;
                else if(cellStyleStr != null)
                {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    formatIndex = style.getDataFormat();
                    formatString = style.getDataFormatString();
                    if(formatString == null)
                        formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
                }
            }
            else if(name.equals("row"))
            {
                // 设置行号
                if(isFirstRow)
                {
                    totalCol = getColumns(attributes.getValue("spans"));
                    isFirstRow = false;
                }
            }

        }

        /*
           * (non-Javadoc)
           * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
           */
        @Override
        public void endElement(String uri, String localName, String name) throws SAXException
        {

            thisStr = null;

            // v => contents of a cell
            if("v".equals(name))
            {
                paddingNullCell();

                endElementSwitch();

                curCol++;

                rowColumnReset();

            }
            else if("t".equals(name))
            {
                endElementSwitch();

                rowColumnReset();
            }
            else if("row".equals(name))
            {

                if(minColumns > 0)
                {
                    if(lastColumnNumber == -1)
                    {
                        lastColumnNumber = 0;
                    }
                    for(int i = lastColumnNumber; i < (minColumnCount); i++)
                    {
                        print(",", false);
                    }
                }

                // 对最后的空列做填充
                for(int i = rowList.size(); i < totalCol; i++)
                {
                    print(",", false);
                    rowList.add(i, defaultStr);
                }

                print(null, true);
                lastColumnNumber = -1;
                excelDataHandler();
                thisRow++;
                curCol = 0;
            }
            else if("worksheet".equals(name))
            {
                thisRow = 0;
            }

        }

        private void excelDataHandler()
        {
            if(callBack)
            {
                if(rowReader == null)
                    throw new DataException("AbstractXlsxReader --> RowReader is null");
                rowReader.getRows(sheetIndex, sheetName, thisRow, rowList);
            }
            else
            {
                List<String> rowListCopy = new ArrayList<String>();
                for(int i = 0; i < rowList.size(); i++)
                {
                    rowListCopy.add(rowList.get(i));
                }
                excelData.add(rowListCopy);
            }
            rowList.clear();
        }

        private void rowColumnReset()
        {

            if(lastColumnNumber == -1)
            {
                lastColumnNumber = 0;
            }
            for(int i = lastColumnNumber; i < thisColumn; ++i)
            {
                print(",", false);
            }

            print(thisStr, false);

            if(thisColumn > -1)
                lastColumnNumber = thisColumn;
        }

        /**
         * 空的单元个填充
         */
        private void paddingNullCell()
        {
            int index = curCol;
            if(thisColumn > index)
            {
                for(int i = index; i < thisColumn; i++)
                {
                    addRowData(curCol, defaultStr);
                    curCol++;
                }
            }
        }

        private int getColumns(String spans)
        {
            String number = spans.substring(spans.lastIndexOf(':') + 1, spans.length());
            return Integer.parseInt(number);
        }

        private void addRowData(int column, String value)
        {
            // rowList.add(column, value); // 面对复杂excel,潜在数组越界bug
            rowList.add(value);
        }

        private String endElementSwitch()
        {
            switch(nextDataType)
            {

                case BOOL:
                    char first = value.charAt(0);
                    thisStr = first == '0' ? "FALSE" : "TRUE";
                    addRowData(thisColumn, thisStr);
                    break;

                case ERROR:
                    thisStr = "\"ERROR:" + value.toString() + '"';
                    addRowData(thisColumn, thisStr);
                    break;

                case FORMULA:
                    thisStr = value.toString();
                    addRowData(thisColumn, thisStr);
                    break;

                case INLINESTR:
                    XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                    thisStr = rtsi.toString();
                    addRowData(thisColumn, thisStr);
                    break;

                case SSTINDEX:
                    String sstIndex = value.toString();
                    try
                    {
                        int idx = Integer.parseInt(sstIndex);
                        XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                        thisStr = rtss.toString();
                        addRowData(thisColumn, thisStr);
                    }
                    catch(NumberFormatException ex)
                    {
                        print("Failed to parse SST index '" + sstIndex + "': " + ex.toString(), true);
                        addRowData(thisColumn, "Failed to parse SST index '" + sstIndex + "': " + ex.toString());
                    }
                    break;

                case NUMBER:
                    String n = value.toString();
                    if(formatString != null)
                        thisStr = formatter.formatRawCellContents(Double.parseDouble(n), formatIndex, formatString);
                    else
                        thisStr = n;

                    addRowData(thisColumn, thisStr);
                    break;

                default:
                    thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
                    addRowData(thisColumn, thisStr);
                    break;
            }
            return thisStr;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException
        {
            if(vIsOpen)
                value.append(ch, start, length);
        }

        private int nameToColumn(String name)
        {
            int column = -1;
            for(int i = 0; i < name.length(); ++i)
            {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }
    }

}
