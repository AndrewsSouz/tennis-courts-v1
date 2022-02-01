package com.tenniscourts.config.swagger.annotations.tenniscourt;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation("Create a new tennis court")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully added a tennis court"),
        @ApiResponse(code = 400, message = "Something wrong with the request")})
public @interface AddTennisCourtSwaggerInfo {
}
