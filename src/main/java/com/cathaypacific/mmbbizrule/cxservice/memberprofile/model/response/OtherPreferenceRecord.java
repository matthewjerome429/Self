package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherPreferenceRecord {

    private String additionalText;

    private String category;

    private String type;

    private String preferenceDescription;

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(final String additionalText) {
        this.additionalText = additionalText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPreferenceDescription() {
        return preferenceDescription;
    }

    public void setPreferenceDescription(final String preferenceDescription) {
        this.preferenceDescription = preferenceDescription;
    }
}
