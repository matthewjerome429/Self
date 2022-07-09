package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NomineeRecord {

    private String nomineeNumber;

    private String title;

    private String mixedTitle;

    private String surname;

    private String mixedSurname;

    private String givenName;

    private String mixedGivenName;

    private String nomineeMemberNumber;

    public String getNomineeNumber() {
        return nomineeNumber;
    }

    public void setNomineeNumber(final String nomineeNumber) {
        this.nomineeNumber = nomineeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getMixedTitle() {
        return mixedTitle;
    }

    public void setMixedTitle(final String mixedTitle) {
        this.mixedTitle = mixedTitle;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public String getMixedSurname() {
        return mixedSurname;
    }

    public void setMixedSurname(final String mixedSurname) {
        this.mixedSurname = mixedSurname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(final String givenName) {
        this.givenName = givenName;
    }

    public String getMixedGivenName() {
        return mixedGivenName;
    }

    public void setMixedGivenName(final String mixedGivenName) {
        this.mixedGivenName = mixedGivenName;
    }

    public String getNomineeMemberNumber() {
        return nomineeMemberNumber;
    }

    public void setNomineeMemberNumber(final String nomineeMemberNumber) {
        this.nomineeMemberNumber = nomineeMemberNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NomineeRecord{");
        sb.append("nomineeNumber='").append(nomineeNumber).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", mixedTitle='").append(mixedTitle).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", mixedSurname='").append(mixedSurname).append('\'');
        sb.append(", givenName='").append(givenName).append('\'');
        sb.append(", mixedGivenName='").append(mixedGivenName).append('\'');
        sb.append(", nomineeMemberNumber='").append(nomineeMemberNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
