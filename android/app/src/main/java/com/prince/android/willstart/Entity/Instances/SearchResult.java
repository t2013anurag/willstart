package com.prince.android.willstart.Entity.Instances;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Prince Bansal Local on 9/17/2016.
 */

public class SearchResult {

    private String category;
    private String worth;
    @SerializedName("total_companies")
    private String totalCompanies;
    @SerializedName("funding_raised")
    private String fundingRaised;
    private String location;
    @SerializedName("dsc")
    private HashMap<String,String> descriptions;
    @SerializedName("companies")
    private List<Company> companyList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWorth() {
        return worth;
    }

    public void setWorth(String worth) {
        this.worth = worth;
    }

    public String getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(String totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public String getFundingRaised() {
        return fundingRaised;
    }

    public void setFundingRaised(String fundingRaised) {
        this.fundingRaised = fundingRaised;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public HashMap<String, String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(HashMap<String, String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }


}
