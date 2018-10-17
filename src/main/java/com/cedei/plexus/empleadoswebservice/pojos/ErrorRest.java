package com.cedei.plexus.empleadoswebservice.pojos;

import java.util.Date;

import org.springframework.http.HttpStatus;

/**
 * ErrorRest
 */
public class ErrorRest {

    private String message;
    private HttpStatus status;
    private Date date = new Date();

    public ErrorRest() {
    }

    public ErrorRest(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return this.status.toString() +" "+ this.status.name();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}