package de.hsrm.mi.web.projekt.anzeige.api.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AnzeigeNotFoundException extends RuntimeException{

    public AnzeigeNotFoundException(){
        super();
    }

    public AnzeigeNotFoundException(String m){
        super(m);
    }

}