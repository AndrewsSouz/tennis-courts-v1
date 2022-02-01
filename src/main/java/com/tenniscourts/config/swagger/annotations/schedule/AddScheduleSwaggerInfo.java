package com.tenniscourts.config.swagger.annotations.schedule;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation("Add a schedule to a tennis court")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully created a schedule"),
        @ApiResponse(code = 400, message = "Something wrong with the request")})
public @interface AddScheduleSwaggerInfo {
}
