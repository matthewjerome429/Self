package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class BaseResponse {

    private List<ErrorInfo> errors;

    public List<ErrorInfo> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
    }

    public void addError(ErrorInfo error) {
        if (CollectionUtils.isEmpty(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}
