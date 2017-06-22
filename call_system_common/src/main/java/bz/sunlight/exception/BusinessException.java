package bz.sunlight.exception;



public class BusinessException extends AbstractException
{
    private static final long serialVersionUID = 2234207247629828430L;

    public BusinessException(String code)
    {
        super(code);
    }

    public BusinessException(String code, Throwable cause)
    {
        super(code, cause);
    }

    public BusinessException(String code, String message)
    {
        super(code, message);
    }

    public BusinessException(String code, String message, Throwable cause)
    {
        super(code, message, cause);
    }
}
