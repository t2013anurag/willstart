package com.prince.android.willstart.Entity.Instances;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Prince Bansal Local on 9/17/2016.
 */

@Parcel
public class SuggestionResult {

    private float mean;
    @SerializedName("successRate")
    private float successRate;
    @SerializedName("results")
    private List<String> suggestions;
    @SerializedName("poorPercentage")
    private float poorPercentage;

    public float getSuccessRate() {
        return successRate;
    }
    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public float getPoorPercentage() {
        return poorPercentage;
    }

    public void setPoorPercentage(float poorPercentage) {
        this.poorPercentage = poorPercentage;
    }
}
