package bz.sunlight.excelUtil;



import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import bz.sunlight.exception.DataException;

//8/11/14 17:06 日期格式问题  m/d/yy h:mm 应为 2014/8/11  17:06:32
//目前返回数据格式为List<List<String>> int sheetIndex,String sheetName,int curRow, List<String> rowlist
public abstract class AbstractXlsReader extends ExcelReader implements HSSFListener
{
    private InputStream is;
    private int minColumns;
    private POIFSFileSystem fs;
    private PrintStream output = System.out;

    private int lastRowNumber;
    private int lastColumnNumber;

    private boolean outputFormulaValues = true;

    private SheetRecordCollectingListener workbookBuildingListener;
    private HSSFWorkbook stubWorkbook;

    private SSTRecord sstRecord;
    private FormatTrackingHSSFListener formatListener;

    private int sheetIndex = -1;
    private String sheetName;
    private BoundSheetRecord[] orderedBSRs;
    private List<BoundSheetRecord> boundSheetRecords;

    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;
    private int thisRow = -1;
    private int thisColumn = -1;
    private String thisStr = null;
    private List<List<String>> excelData;
    private List<String> rowList;
    private RowReader rowReader;
    private boolean callBack = false; // true RowReader ; false get excelData

    /* 解决 excel 空值问题 */
    // 当前列
    private int curCol = 0;
    // 总列数
    // private int totalCol = 0;
    // 是否第一行标题
    // private boolean isFirstRow = true;

    private String defaultStr = "";

    public AbstractXlsReader(InputStream is)
    {
        this(is, null, false);
    }

    public AbstractXlsReader(InputStream is, RowReader reader)
    {
        this(is, reader, false);
    }

    public AbstractXlsReader(InputStream is, RowReader reader, boolean callBack)
    {
        try
        {
            fs = new POIFSFileSystem(is);
        }
        catch(IOException e)
        {
            throw new DataException("new POIFSFileSystem 报错", e);
        }
        rowReader = reader;
        this.callBack = callBack;
    }

    @Override
    public void init()
    {
        boundSheetRecords = new ArrayList<BoundSheetRecord>();
        rowList = new ArrayList<String>();
        if(!callBack)
            excelData = new ArrayList<List<String>>();
    }

    @Override
    public void start()
    {
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        formatListener = new FormatTrackingHSSFListener(listener);

        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();

        if(outputFormulaValues)
        {
            request.addListenerForAllRecords(formatListener);
        }
        else
        {
            workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }

        try
        {
            factory.processWorkbookEvents(request, fs);
        }
        catch(IOException e)
        {
            throw new DataException("processWorkbookEvents 报错", e);
        }
    }

    @Override
    public void processRecord(Record record)
    {
        thisRow = -1;
        thisColumn = -1;
        thisStr = null;
        paddingNullCell();
        curCol++;
        switch(record.getSid())
        {
            case BoundSheetRecord.sid:
                boundSheetRecords.add((BoundSheetRecord) record);
                break;
            case BOFRecord.sid:
                bofRecordHandler(record);
                break;
            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;
            case BlankRecord.sid:
                blankRecordHandler(record);
                break;
            case BoolErrRecord.sid:
                boolErrRecordHandler(record);
                break;
            case FormulaRecord.sid:
                formulaRecordHandler(record);
                break;
            case StringRecord.sid:
                stringRecordHandler(record);
                break;
            case LabelRecord.sid:
                labelRecordHandler(record);
                break;
            case LabelSSTRecord.sid:
                labelSSTRecordHandler(record);
                break;
            case NoteRecord.sid:
                noteRecordHandler(record);
                break;
            case NumberRecord.sid:
                numberRecordHandler(record);
                break;
            case RKRecord.sid:
                rkRecordHandler(record);
                break;
            default:
                break;
        }

        rowColumnReset(record);

        lastCellOfRowDummyRecordHandler(record);

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

    private void lastCellOfRowDummyRecordHandler(Record record)
    {
        if(record instanceof LastCellOfRowDummyRecord)
        {
            if(minColumns > 0)
            {
                if(lastColumnNumber == -1)
                {
                    lastColumnNumber = 0;
                }
                for(int i = lastColumnNumber; i < (minColumns); i++)
                {
                    print(",", false);
                }
            }
            LastCellOfRowDummyRecord lcodRec = (LastCellOfRowDummyRecord) record;
            thisRow = lcodRec.getRow();

            lastColumnNumber = -1;

            excelDataHandler();

            print(null, true);

            curCol = 0;
        }
    }

    private void excelDataHandler()
    {
        if(callBack)
        {
            if(rowReader == null)
                throw new DataException("AbstractXlsReader --> RowReader is null");
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

    private void addRowData(int column, String value)
    {
        // rowList.add(column,value); // 复杂excel,潜在数组越界bug
        rowList.add(value);
    }

    private void rowColumnReset(Record record)
    {
        if(thisRow != -1 && thisRow != lastRowNumber)
        {
            lastColumnNumber = -1;
        }
        if(record instanceof MissingCellDummyRecord)
        {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            thisRow = mc.getRow();
            thisColumn = mc.getColumn();
            thisStr = "";
            addRowData(thisColumn, thisStr);//空值也要填充,否则会引起此行数据与其他行不对应的问题
        }
        if(thisStr != null)
        {
            if(thisColumn > 0)
            {
                print(",", false);
            }
            print(thisStr, false);
        }

        if(thisRow > -1)
            lastRowNumber = thisRow;
        if(thisColumn > -1)
            lastColumnNumber = thisColumn;

    }

    private void rkRecordHandler(Record record)
    {
        RKRecord rkrec = (RKRecord) record;
        thisRow = rkrec.getRow();
        thisColumn = rkrec.getColumn();
        thisStr = "(TODO)";
    }

    private void numberRecordHandler(Record record)
    {
        NumberRecord numrec = (NumberRecord) record;
        thisRow = numrec.getRow();
        thisColumn = numrec.getColumn();
        thisStr = formatListener.formatNumberDateCell(numrec);
        addRowData(thisColumn, thisStr);
    }

    private void noteRecordHandler(Record record)
    {
        NoteRecord nrec = (NoteRecord) record;
        thisRow = nrec.getRow();
        thisColumn = nrec.getColumn();
        thisStr = "(TODO)";
    }

    private void labelSSTRecordHandler(Record record)
    {
        LabelSSTRecord lsrec = (LabelSSTRecord) record;
        thisRow = lsrec.getRow();
        thisColumn = lsrec.getColumn();
        if(sstRecord == null)
        {
            thisStr = "(No SST Record, can't identify string)";
            addRowData(thisColumn, "");
        }
        else
        {
            thisStr = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
            addRowData(thisColumn, thisStr);
        }
    }

    private void labelRecordHandler(Record record)
    {
        LabelRecord lrec = (LabelRecord) record;
        thisRow = lrec.getRow();
        thisColumn = lrec.getColumn();
        thisStr = lrec.getValue();
        addRowData(thisColumn, thisStr);
    }

    private void stringRecordHandler(Record record)
    {
        if(outputNextStringRecord)
        {
            StringRecord srec = (StringRecord) record;
            thisStr = srec.getString();
            thisRow = nextRow;
            thisColumn = nextColumn;
            outputNextStringRecord = false;
        }
    }

    private void formulaRecordHandler(Record record)
    {
        FormulaRecord frec = (FormulaRecord) record;
        thisRow = frec.getRow();
        thisColumn = frec.getColumn();
        if(outputFormulaValues)
        {
            if(Double.isNaN(frec.getValue()))
            {
                outputNextStringRecord = true;
                nextRow = frec.getRow();
                nextColumn = frec.getColumn();
            }
            else
            {
                thisStr = formatListener.formatNumberDateCell(frec);
            }
        }
        else
        {
            thisStr = HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression());
        }
        addRowData(thisColumn, thisStr);
    }

    private void boolErrRecordHandler(Record record)
    {
        BoolErrRecord berec = (BoolErrRecord) record;
        thisRow = berec.getRow();
        thisColumn = berec.getColumn();
        thisStr = berec.getBooleanValue() + "";
        addRowData(thisColumn, thisStr);
    }

    private void blankRecordHandler(Record record)
    {
        BlankRecord brec = (BlankRecord) record;
        thisRow = brec.getRow();
        thisColumn = brec.getColumn();
        thisStr = "";
        addRowData(thisColumn, thisStr);
    }

    private void bofRecordHandler(Record record)
    {
        BOFRecord br = (BOFRecord) record;
        if(br.getType() == BOFRecord.TYPE_WORKSHEET)
        {
            if(workbookBuildingListener != null && stubWorkbook == null)
            {
                stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
            }
            sheetIndex++;
            if(orderedBSRs == null)
            {
                orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
            }
            print(null, true);
            print(orderedBSRs[sheetIndex].getSheetname() + " [" + (sheetIndex + 1) + "]:", true);
            sheetName = orderedBSRs[sheetIndex].getSheetname();
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
            if(is != null)
            {
                is.close();
            }
        }
        catch(IOException e)
        {
            throw new DataException("InputStream close 报错", e);
        }
    }

    @Override
    public void destory()
    {
        is = null;
        fs = null;
        output = null;
        workbookBuildingListener = null;
        stubWorkbook = null;
        sstRecord = null;
        formatListener = null;
        sheetName = null;
        orderedBSRs = null;
        boundSheetRecords = null;
        thisStr = null;
        rowList = null;
        rowReader = null;

    }

    public List<List<String>> getExcelData()
    {
        return excelData;
    }

    public boolean isCallBack()
    {
        return callBack;
    }

    public void setCallBack(boolean callBack)
    {
        this.callBack = callBack;
    }

    public int getMinColumns()
    {
        return minColumns;
    }

    public void setMinColumns(int minColumns)
    {
        this.minColumns = minColumns;
    }

    public static void main(String[] args) throws Exception
    {
        Date d;
        d = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse("8/11/14 17:06:32");
        System.out.println(d);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
    }
}
