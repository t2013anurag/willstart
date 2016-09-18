package com.prince.android.willstart.Entity.Instances;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prince Bansal Local on 9/17/2016.
 */

public class Company {
    private String name;
    @SerializedName("pic_url")
    private String picUrl;
    @SerializedName("services_available")
    private List<String> servicesOffered;
    @SerializedName("services_not_available")
    private List<String> servicesNotOffered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<String> getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(List<String> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public List<String> getServicesNotOffered() {
        return servicesNotOffered;
    }

    public void setServicesNotOffered(List<String> servicesNotOffered) {
        this.servicesNotOffered = servicesNotOffered;
    }
}
