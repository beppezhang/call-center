package bz.sunlight.excelUtil;



import java.util.List;
import java.util.Map;

public class ExcelData
{
    public static final int XLS_SPLIT_ROW = 50000;
    public static final int XLSX_SPLIT_ROW = 1000000;
    private Map<String, List<Object[]>> excelMapData;
    private boolean isMap = false;
    private List<Object[]> excelListData;
    private Object[] excelHeadData;

    public ExcelData(Map<String, List<Object[]>> excelMapData, Object[] excelHeadData)
    {
        this.excelMapData = excelMapData;
        this.excelHeadData = excelHeadData;
        isMap = true;
    }

    public ExcelData(List<Object[]> excelListData, Object[] excelHeadData)
    {
        this.excelListData = excelListData;
        this.excelHeadData = excelHeadData;
        isMap = false;
    }

    public Map<String, List<Object[]>> getExcelMapData()
    {
        return excelMapData;
    }

    public boolean isMap()
    {
        return isMap;
    }

    public List<Object[]> getExcelListData()
    {
        return excelListData;
    }

    public Object[] getExcelHeadData()
    {
        return excelHeadData;
    }

}
