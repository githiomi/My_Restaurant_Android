
package com.moringaschool.myrestaurant;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class YelpBusinessesSearchResponse {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("businesses")
    @Expose
    private List<Business> businesses = null;
    @SerializedName("region")
    @Expose
    private Region region;

    /**
     * No args constructor for use in serialization
     * 
     */
    public YelpBusinessesSearchResponse() {
    }

    /**
     * 
     * @param total
     * @param region
     * @param businesses
     */
    public YelpBusinessesSearchResponse(Integer total, List<Business> businesses, Region region) {
        super();
        this.total = total;
        this.businesses = businesses;
        this.region = region;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(total).append(region).append(businesses).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof YelpBusinessesSearchResponse) == false) {
            return false;
        }
        YelpBusinessesSearchResponse rhs = ((YelpBusinessesSearchResponse) other);
        return new EqualsBuilder().append(total, rhs.total).append(region, rhs.region).append(businesses, rhs.businesses).isEquals();
    }

}
