package com.example.blogsearcher.presentation.api.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, CharSequence?> {
    private lateinit var values: List<String>
    private lateinit var enumClassName: String
    private lateinit var notValidMessage: String

    override fun initialize(annotation: ValueOfEnum) {
        values = annotation.enumClass.java.enumConstants.map { it.toString() }
        enumClassName = annotation.enumClass.simpleName!!
        notValidMessage = annotation.message
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        value ?: return true

        val valid = value.toString() in values
        if (!valid) {
            val message = notValidMessage.replace("{values}", values.toString())
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
        }
        return valid
    }
}
