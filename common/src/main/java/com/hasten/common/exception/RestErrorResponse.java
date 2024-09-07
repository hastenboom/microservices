package com.hasten.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Student
 */
@Setter
@Getter
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

}