package com.metistream.tools;

import java.time.LocalDateTime;

/**
 * Created by nathansalmon on 3/7/16.
 */
public class DemographicsDocument {
    /*
    {
       "id":"1234"
       "name":"Mr. Adam Everyman",
       "ethnicGroupCode":"2186-5",
       "ethnicGroupCode_displayName":"Not Hispanic or Latino",
       "gender":"M",
       "maritalCode":"M",
       "raceCode":"2106-3",
       "race":"White",
       "birthplace":"Kansas City, KS",
       "religiousAffiliationCode":"1013",
       "religiousAffiliation":"Christian (non-Catholic, non-specific)",
       "birthtime":"1954-11-25T00:00:00Z",
       "languageCode":"fr-CN",
       "language": "French",
       "phone":"555-555-5555"
    }
     */
    private String id;
    private String ingestDate;
    private String name;
    private String ethnicity;
    private String gender;
    private String maritalCode;
    private String race;
    private String birthplace;
    private String religion;
    private String birthDateTime;
    private String language;
    private String phone;
    private String condition;
    private int postalCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalCode() {
        return maritalCode;
    }

    public void setMaritalCode(String maritalCode) {
        this.maritalCode = maritalCode;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getBirthDateTime() {
        return birthDateTime;
    }

    public void setBirthDateTime(String birthDateTime) {
        this.birthDateTime = birthDateTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIngestDate() {
        return ingestDate;
    }

    public void setIngestDate(String ingestDate) {
        this.ingestDate = ingestDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
