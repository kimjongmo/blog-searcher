package com.example.blogsearcher.presentation.api.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValueOfEnumValidator::class])
annotation class ValueOfEnum(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "must be {values}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
