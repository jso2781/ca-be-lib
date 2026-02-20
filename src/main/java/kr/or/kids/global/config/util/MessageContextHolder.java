package kr.or.kids.global.config.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageContextHolder implements MessageSourceAware{

    private static MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        MessageContextHolder.messageSource = messageSource;
    }

    public static String getMessage(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }

    public static String getMessage(String msgCode,  Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, args, locale);
    }

    public static String getMessage(String msgCode,  Object[] args, Locale locale) {
        return messageSource.getMessage(msgCode, args, locale);
    }
}