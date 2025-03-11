package com.dino.Mega_City_Cabs.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorDetail {

    private Date timestamp;
    private String message;
    private String errorCode;

    public ErrorDetail(Date timestamp, String message, String errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.errorCode = errorCode;
    }
}
