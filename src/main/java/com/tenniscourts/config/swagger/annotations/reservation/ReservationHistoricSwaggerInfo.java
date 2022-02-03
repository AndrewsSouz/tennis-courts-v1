package com.tenniscourts.config.swagger.annotations.reservation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation("Return the all the reservations before the actual date")
@ApiResponses(@ApiResponse(code = 200, message = "Successfully retrieved the reservation history"))
public @interface ReservationHistoricSwaggerInfo {
}
