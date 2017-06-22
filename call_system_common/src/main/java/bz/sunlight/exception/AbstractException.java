package bz.sunlight.exception;



public abstract class AbstractException extends RuntimeException
{
    private static final long serialVersionUID = -2886095369671343313L;

    private String code;

    public AbstractException(String code)
    {
        super(code);
        this.code = code;
    }

    public AbstractException(String code, Throwable cause)
    {
        super(code, cause);
        this.code = code;
    }

    public AbstractException(String code, String message)
    {
        super(message);
        this.code = code;
    }

    public AbstractException(String code, String message, Throwable cause)
    {
        super(message, cause);
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
