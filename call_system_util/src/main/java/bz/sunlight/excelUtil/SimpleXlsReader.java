package bz.sunlight.excelUtil;



import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class SimpleXlsReader extends AbstractXlsReader
{
    public SimpleXlsReader(InputStream is)
    {
        super(is);
    }

    public SimpleXlsReader(InputStream is, RowReader reader)
    {
        super(is, reader);
    }

    public SimpleXlsReader(InputStream is, RowReader reader, boolean callBack)
    {
        super(is, reader, callBack);
    }

    public static void main(String[] args) throws Exception
    {
        AbstractXlsReader reader;
        InputStream is = null;
        RowReader rowReader = new SimpleRowReader();
        is = new FileInputStream("D:/SimplePoiXlsWriter.xls");
        reader = new SimpleXlsReader(is, rowReader, false);
        reader.process();

        List<List<String>> excelData = reader.getExcelData();
        if(excelData != null)
        {
            for(List<String> list : excelData)
            {
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
}
