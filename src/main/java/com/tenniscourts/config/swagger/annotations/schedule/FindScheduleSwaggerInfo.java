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
@ApiOperation("Find schedule by id")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found the schedule"),
        @ApiResponse(code = 400, message = "Something wrong with the request"),
        @ApiResponse(code = 404, message = "Schedule not found")})
public @interface FindScheduleSwaggerInfo {
}
