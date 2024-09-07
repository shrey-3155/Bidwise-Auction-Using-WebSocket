package com.online.auction.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class used for handling service-related errors in the application.
 * <p>
 * This exception extends {@link Exception} and includes additional information about the HTTP status code and error message.
 * It is used to represent errors that occur during service operations and provides mechanisms to retrieve the status code and error message associated with the exception.
 * </p>
 */
public class ServiceException extends Exception {
    private final int statusCode;
    private final String errorMessage;

    /**
     * Constructs a new {@link ServiceException} with the specified HTTP status code and error message.
     *
     * @param statusCode The HTTP status code associated with the error.
     * @param message    The error message providing details about the error.
     */
    public ServiceException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode.value();
        this.errorMessage = message;
    }

    /**
     * Retrieves the HTTP status code associated with the exception.
     *
     * @return The HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Retrieves the error message associated with the exception.
     *
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
