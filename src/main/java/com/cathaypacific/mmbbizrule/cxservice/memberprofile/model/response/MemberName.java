package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberName {

    private String title;

    private String familyName;

    private String givenName;

    private String namingConvention;

    private String embossedName;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(final String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(final String givenName) {
        this.givenName = givenName;
    }

    public String getNamingConvention() {
        return namingConvention;
    }

    public void setNamingConvention(final String namingConvention) {
        this.namingConvention = namingConvention;
    }

    public String getEmbossedName() {
        return embossedName;
    }

    public void setEmbossedName(final String embossedName) {
        this.embossedName = embossedName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MemberName{");
        sb.append("title='").append(title).append('\'');
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", givenName='").append(givenName).append('\'');
        sb.append(", namingConvention='").append(namingConvention).append('\'');
        sb.append(", embossedName='").append(embossedName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
