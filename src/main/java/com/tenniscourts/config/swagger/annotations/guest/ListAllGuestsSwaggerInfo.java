package com.tenniscourts.config.swagger.annotations.guest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation("List all guests")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Return all guests"),
        @ApiResponse(code = 204, message = "No guests found"),
        @ApiResponse(code = 400, message = "Something wrong with the request")})
public @interface ListAllGuestsSwaggerInfo {
}
