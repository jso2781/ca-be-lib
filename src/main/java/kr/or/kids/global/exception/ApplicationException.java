package kr.or.kids.global.exception;

import java.util.Locale;

import kr.or.kids.global.config.util.MessageContextHolder;

public class ApplicationException extends RuntimeException
{
    private static final long serialVersionUID = 1202897784570477613L;
//    private static final String PREFIX = "error.application.";
    private String errorMessage;

    public ApplicationException(String msgCode)
    {
        super(MessageContextHolder.getMessage(msgCode));
        this.errorMessage = super.getMessage();
    }

    public ApplicationException(String msgCode, Throwable throwable)
    {
        super(MessageContextHolder.getMessage(msgCode), throwable);
        this.errorMessage = super.getMessage();
    }

    public ApplicationException(String msgCode, Object[] args)
    {
        super(MessageContextHolder.getMessage(msgCode, args));
        this.errorMessage = super.getMessage();
    }

    public ApplicationException(String msgCode, Object[] args, Throwable throwable)
    {
        super(MessageContextHolder.getMessage(msgCode, args), throwable);
        this.errorMessage = super.getMessage();
    }

    public ApplicationException(String msgCode, Object[] args, Locale locale)
    {
        super(MessageContextHolder.getMessage(msgCode, args, locale));
        this.errorMessage = super.getMessage();
    }

    public ApplicationException(String msgCode, Object[] args, Locale locale, Throwable throwable)
    {
        super(MessageContextHolder.getMessage(msgCode, args, locale), throwable);
        this.errorMessage = super.getMessage();
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    public String getCauseMessage()
    {
        Throwable t = getCause();
        if (t != null) {
            return t.getMessage();
        }
        return "";
    }

    public String getStackTraceMessage()
    {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] arrayOfStackTraceElement;
        int j = (arrayOfStackTraceElement = super.getStackTrace()).length;
        for (int i = 0; i < j; i++)
        {
            StackTraceElement s = arrayOfStackTraceElement[i];
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }
}
