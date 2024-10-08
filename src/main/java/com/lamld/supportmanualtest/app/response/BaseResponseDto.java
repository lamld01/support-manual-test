package com.lamld.supportmanualtest.app.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic class to wrap responses.
 *
 * @param <T> the type of the data being returned.
 */
@Data
@NoArgsConstructor
public class BaseResponseDto<T> {

    /**
     * The data being returned.
     */
    private T data;

    /**
     * An optional message that may provide additional context.
     */
    private String message = "Success";

    /**
     * An optional status code representing the outcome of the request.
     */
    private Integer statusCode = 200;

    /**
     * Constructs a BaseResponseDto with data only.
     *
     * @param data the data to be returned.
     */
    public BaseResponseDto(T data) {
        this.data = data;
    }

    /**
     * Constructs a BaseResponseDto with data and a message.
     *
     * @param data the data to be returned.
     * @param message a message providing additional context.
     */
    public BaseResponseDto(T data, String message) {
        this.data = data;
        this.message = message;
    }

    /**
     * Constructs a BaseResponseDto with data, a message, and a status code.
     *
     * @param data the data to be returned.
     * @param message a message providing additional context.
     * @param statusCode a status code representing the outcome of the request.
     */
    public BaseResponseDto(T data, String message, Integer statusCode) {
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }
}
