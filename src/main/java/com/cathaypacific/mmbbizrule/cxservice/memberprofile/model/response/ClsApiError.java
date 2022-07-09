package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClsApiError {

    private String code;

    private String[] parameters;

    private String message;

    private ClsError clsError;

    public ClsApiError() {
        super();
    }

    public ClsApiError(String code, String message) {
        super();
        this.code = code;
        this.message = message;
        parameters = null;
    }

    public ClsApiError(String code, String message, String[] parameters) {
        super();
        this.code = code;
        this.parameters = parameters;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(final String[] parameters) {
        this.parameters = parameters;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public ClsError getClsError() {
        return clsError;
    }

    public void setClsError(final ClsError clsError) {
        this.clsError = clsError;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClsApiError{");
        sb.append("code='").append(code).append('\'');
        sb.append(", parameters=").append(parameters == null ? "null" : Arrays.asList(parameters).toString());
        sb.append(", message='").append(message).append('\'');
        sb.append(", clsError=").append(clsError);
        sb.append('}');
        return sb.toString();
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ClsError {

    private String errorStatusCode;

    private String errorMessage;

    private String errorMessageTextToInsert;

    private String errorStatusRollbackInc;

    private String errorStatusLocation;

    private String messageDisplayIndicator;

    private String errorSeverity;

    public String getErrorStatusCode() {
        return errorStatusCode;
    }

    public void setErrorStatusCode(final String errorStatusCode) {
        this.errorStatusCode = errorStatusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessageTextToInsert() {
        return errorMessageTextToInsert;
    }

    public void setErrorMessageTextToInsert(final String errorMessageTextToInsert) {
        this.errorMessageTextToInsert = errorMessageTextToInsert;
    }

    public String getErrorStatusRollbackInc() {
        return errorStatusRollbackInc;
    }

    public void setErrorStatusRollbackInc(final String errorStatusRollbackInc) {
        this.errorStatusRollbackInc = errorStatusRollbackInc;
    }

    public String getErrorStatusLocation() {
        return errorStatusLocation;
    }

    public void setErrorStatusLocation(final String errorStatusLocation) {
        this.errorStatusLocation = errorStatusLocation;
    }

    public String getMessageDisplayIndicator() {
        return messageDisplayIndicator;
    }

    public void setMessageDisplayIndicator(final String messageDisplayIndicator) {
        this.messageDisplayIndicator = messageDisplayIndicator;
    }

    public String getErrorSeverity() {
        return errorSeverity;
    }

    public void setErrorSeverity(final String errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClsError{");
        sb.append("errorStatusCode='").append(errorStatusCode).append('\'');
        sb.append(", errorMessage='").append(errorMessage).append('\'');
        sb.append(", errorMessageTextToInsert='").append(errorMessageTextToInsert).append('\'');
        sb.append(", errorStatusRollbackInc='").append(errorStatusRollbackInc).append('\'');
        sb.append(", errorStatusLocation='").append(errorStatusLocation).append('\'');
        sb.append(", messageDisplayIndicator='").append(messageDisplayIndicator).append('\'');
        sb.append(", errorSeverity='").append(errorSeverity).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
