package de.hsrm.mi.web.projekt.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
@Target({ ElementType.FIELD }) // Worauf ist Annotation anwendbar?
@Retention(RetentionPolicy.RUNTIME) // zur Laufzeit vorhanden
// Verweis auf Validator-Implementierung
@Constraint(validatedBy=GutesPasswortValidator.class)
// @Min(1) // anderere Constraints optional "dazukombinierbar"
@Documented // z.B. in JavaDoc des Zielelements aufnehmen
public @interface GutesPasswort {
    String message() default "{benutzer.fehler.passwortungeeignet}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] values() default {};
}
