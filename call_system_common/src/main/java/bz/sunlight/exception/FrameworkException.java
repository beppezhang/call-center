package bz.sunlight.exception;



public class FrameworkException extends AbstractException
{
    private static final long serialVersionUID = -8085767228182381013L;

    public FrameworkException(String code)
    {
        super(code);
    }

    public FrameworkException(String code, Throwable cause)
    {
        super(code, cause);
    }

    public FrameworkException(String code, String message)
    {
        super(code, message);
    }

    public FrameworkException(String code, String message, Throwable cause)
    {
        super(code, message, cause);
    }
}
