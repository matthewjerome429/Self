package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class Services {

    private String psCode;

    private String code;

    private String name;

    private List<Courses> courses;

    public String getPsCode() {
        return psCode;
    }

    public void setPsCode(String psCode) {
        this.psCode = psCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public void addCourses(Courses course) {
        if (CollectionUtils.isEmpty(this.courses)) {
            this.courses = new ArrayList<>();
        }
        this.courses.add(course);
    }
}
