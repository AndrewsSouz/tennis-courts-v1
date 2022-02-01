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
@ApiOperation("Cancel a reservation given a reservation Id")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully canceled the reservation"),
        @ApiResponse(code = 400, message = "Something wrong with the request"),
        @ApiResponse(code = 404, message = "Reservation not found")})
public @interface CancelReservationSwaggerInfo {
}
