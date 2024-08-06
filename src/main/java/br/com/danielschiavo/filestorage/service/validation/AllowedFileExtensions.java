package br.com.danielschiavo.filestorage.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedFileExtensions.AllowedFileExtensionsValidator.class)
@Documented
public @interface AllowedFileExtensions {

    String message() default "{AllowedFileExtensionsValidator.message}";

    String[] value() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public static class AllowedFileExtensionsValidator
            implements ConstraintValidator<AllowedFileExtensions, String> {

        private String[] allowedExtensions = {};

        public final void initialize(final AllowedFileExtensions annotation) {
            allowedExtensions = annotation.value();
        }

        public final boolean isValid(final String fileName,
                                     final ConstraintValidatorContext context) {

            if (fileName == null || fileName.isBlank()){
                return true;
            }

            if (allowedExtensions.length == 0) {
                return true;
            }

            for (String extension : this.allowedExtensions) {
                if (fileName.endsWith("."+extension)) {
                    return true;
                }
            }

            var contextImpl = (ConstraintValidatorContextImpl) context;
            contextImpl.addMessageParameter("extensions", String.join(", ", this.allowedExtensions));

            return false;
        }
    }
}