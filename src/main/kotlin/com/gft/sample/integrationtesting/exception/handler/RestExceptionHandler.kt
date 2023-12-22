package com.gft.sample.integrationtesting.exception.handler

import com.gft.sample.integrationtesting.exception.RestException
import com.gft.sample.openapi.integrationtesting.v1.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RestException::class)
    fun handleRestException(ex: RestException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(ex.statusCode)
            .body(ErrorResponse(ex.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("Unexpected exception: ${ex.message}"))
    }

}
