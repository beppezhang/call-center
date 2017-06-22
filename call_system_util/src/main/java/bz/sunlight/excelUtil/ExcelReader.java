package bz.sunlight.excelUtil;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

public abstract class ExcelReader
{
    private static final Log log = LogFactory.getLog(ExcelReader.class);

    public abstract void init();

    public abstract void start();

    public void process()
    {
        if(log.isDebugEnabled())
        {
            StopWatch stopWatch = new StopWatch(ExcelReader.class.getName());
            stopWatch.start("init");
            init();
            stopWatch.stop();

            stopWatch.start("start");
            start();
            stopWatch.stop();

            stopWatch.start("generate");
            generate();
            stopWatch.stop();

            stopWatch.start("end");
            end();
            stopWatch.stop();

            stopWatch.start("close");
            close();
            stopWatch.stop();

            stopWatch.start("destory");
            destory();
            stopWatch.stop();
            log.debug(stopWatch.prettyPrint());
            System.out.println(stopWatch.prettyPrint());
        }
        else
        {
            init();
            start();
            generate();
            end();
            close();
            destory();
        }

    }

    public abstract void generate();

    public abstract void end();

    public abstract void close();

    public abstract void destory();
}
