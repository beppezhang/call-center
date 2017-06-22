package bz.sunlight.excelUtil;



import java.util.List;

public class SimpleRowReader implements RowReader
{
    @Override
    public void getRows(int sheetIndex, String sheetName, int curRow, List<String> rowlist)
    {
        StringBuffer bf = new StringBuffer();
        for(String str : rowlist)
        {
            bf.append(str + ",");
        }
        System.out.println("--RowReader|sheetIndex=" + sheetIndex + "|sheetName=" + sheetName + "|curRow=" + curRow + "  " + bf.toString());
    }
}
