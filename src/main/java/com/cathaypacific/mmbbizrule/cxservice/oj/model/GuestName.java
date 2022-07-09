
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
    "GivenName",
    "MiddleName",
    "Surname",
    "PaxType",
    "Prefix"
})
public class GuestName implements Serializable {

	private static final long serialVersionUID = 7369501908725753605L;
	
	@JsonProperty("GivenName")
    private String givenName;
	
    @JsonProperty("MiddleName")
    private String middleName;
    
    @JsonProperty("Surname")
    private String surname;
    
    @JsonProperty("PaxType")
    private String paxType;
    
    @JsonProperty("Prefix")
    private String prefix;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("GivenName")
    public String getGivenName() {
        return givenName;
    }

    @JsonProperty("GivenName")
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @JsonProperty("MiddleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("MiddleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("Surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("Surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    @JsonProperty("PaxType")
    public String getPaxType() {
		return paxType;
	}

    @JsonProperty("PaxType")
	public void setPaxType(String paxType) {
		this.paxType = paxType;
	}

    @JsonProperty("Prefix")
	public String getPrefix() {
		return prefix;
	}

    @JsonProperty("Prefix")
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
