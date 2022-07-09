
package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "SelectedBedType",
    "Code",
    "Name",
    "Description",
    "Meals",
    "SmokingPreference",
    "GuestDetails"
})
public class RoomOption implements Serializable{

	private static final long serialVersionUID = 6548669589821918315L;
	
	@JsonProperty("SelectedBedType")
    private String selectedBedType;
	
    @JsonProperty("Code")
    private String code;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("Description")
    private String description;
    
    @JsonProperty("Meals")
    private Meals meals;
    
    @JsonProperty("SmokingPreference")
    private SmokingPreference smokingPreference;
    
    @JsonProperty("GuestDetails")
    private GuestDetails guestDetails;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("SelectedBedType")
    public String getSelectedBedType() {
        return selectedBedType;
    }

    @JsonProperty("SelectedBedType")
    public void setSelectedBedType(String selectedBedType) {
        this.selectedBedType = selectedBedType;
    }

    @JsonProperty("Code")
    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Meals")
    public Meals getMeals() {
        return meals;
    }

    @JsonProperty("Meals")
    public void setMeals(Meals meals) {
        this.meals = meals;
    }

    @JsonProperty("SmokingPreference")
    public SmokingPreference getSmokingPreference() {
        return smokingPreference;
    }

    @JsonProperty("SmokingPreference")
    public void setSmokingPreference(SmokingPreference smokingPreference) {
        this.smokingPreference = smokingPreference;
    }

    @JsonProperty("GuestDetails")
    public GuestDetails getGuestDetails() {
        return guestDetails;
    }

    @JsonProperty("GuestDetails")
    public void setGuestDetails(GuestDetails guestDetails) {
        this.guestDetails = guestDetails;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
