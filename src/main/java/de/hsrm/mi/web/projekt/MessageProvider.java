package de.hsrm.mi.web.projekt;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component
public class MessageProvider implements MessageSourceAware{

    private MessageSource messageSource;
    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String resolve(String key, Locale locale, String ... args){
        return messageSource.getMessage(key, args, locale);
    }
    
}
