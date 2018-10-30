package com.demo.rbac.validators;



import com.demo.rbac.validators.validatorClass.GirlFormValidatorClass;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = GirlFormValidatorClass.class)
public @interface GirlFormIdValidator {

    String values();
    String message() default "girl的ID必须为纯数字";
    Class<?>[] groups() default {};
    Class<? extends Payload>[]  payload() default {};
}
