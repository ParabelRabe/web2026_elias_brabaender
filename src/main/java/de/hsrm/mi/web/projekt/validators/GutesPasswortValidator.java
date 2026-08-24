package de.hsrm.mi.web.projekt.validators;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerController;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GutesPasswortValidator implements ConstraintValidator<GutesPasswort, String>{
    protected String [] falseValues;
    Logger logger = LoggerFactory.getLogger(BenutzerController.class);
    @Override
    public void initialize(GutesPasswort annotationGutesPasswort){
        this.falseValues = annotationGutesPasswort.values();
        logger.info(falseValues[0] + " " + falseValues[1]);

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.equals("")){
            return true;
        }
        else{
            for (String fvalue : falseValues){
                if(value.toLowerCase().contains(fvalue.toLowerCase())){
                    return false;
                }
            }
            return true;
        }
    }

}
