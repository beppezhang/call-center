package bz.sunlight.exception;



public class SystemException extends AbstractException
{
    private static final long serialVersionUID = -9043510695379575443L;

    public SystemException(String code)
    {
        super(code);
    }

    public SystemException(String code, Throwable cause)
    {
        super(code, cause);
    }

    public SystemException(String code, String message)
    {
        super(code, message);
    }

    public SystemException(String code, String message, Throwable cause)
    {
        super(code, message, cause);
    }
}
