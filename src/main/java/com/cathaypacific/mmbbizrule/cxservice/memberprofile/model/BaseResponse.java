package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import java.util.List;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseResponse {
    @JsonIgnore
    private List<ErrorInfo> errors;

    public List<ErrorInfo> getErrors() {
        return errors;
    }

    public void setErrors(final List<ErrorInfo> errors) {
        this.errors = errors;
    }
}
