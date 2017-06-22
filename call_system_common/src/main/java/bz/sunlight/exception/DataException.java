package bz.sunlight.exception;



public class DataException extends AbstractException
{
    private static final long serialVersionUID = -3350447753949291206L;

    public DataException(String code)
    {
        super(code);
    }

    public DataException(String code, Throwable cause)
    {
        super(code, cause);
    }

    public DataException(String code, String message)
    {
        super(code, message);
    }

    public DataException(String code, String message, Throwable cause)
    {
        super(code, message, cause);
    }
}
