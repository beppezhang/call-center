package bz.sunlight.excelUtil;



import java.util.List;

public interface RowReader
{
    public void getRows(int sheetIndex, String sheetName, int curRow, List<String> rowlist);
}
