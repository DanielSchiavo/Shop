package br.com.danielschiavo.filestorage.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedContentTypes.AllowedContentTypesValidator.class)
@Documented
public @interface AllowedContentTypes {

    String message() default "{AllowedContentTypesValidator.message}";

    String[] value() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public static class AllowedContentTypesValidator
            implements ConstraintValidator<AllowedContentTypes, String> {

        private String[] allowedTypes = {};

        public final void initialize(AllowedContentTypes annotation) {
            allowedTypes = annotation.value();
        }

        public final boolean isValid(final String type,
                                     final ConstraintValidatorContext context) {

            if (type == null || type.isBlank()){
                return true;
            }

            if (allowedTypes.length == 0) {
                return true;
            }

            for (String allowedType : this.allowedTypes) {
                if (type.equals(allowedType)) {
                    return true;
                }
            }

            var contextImpl = (ConstraintValidatorContextImpl) context;
            contextImpl.addMessageParameter("types", String.join(", ", this.allowedTypes));

            return false;
        }
    }
}