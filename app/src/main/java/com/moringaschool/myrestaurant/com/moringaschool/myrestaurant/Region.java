
package com.moringaschool.myrestaurant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Region {

    @SerializedName("center")
    @Expose
    private Center center;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Region() {
    }

    /**
     * 
     * @param center
     */
    public Region(Center center) {
        super();
        this.center = center;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(center).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Region) == false) {
            return false;
        }
        Region rhs = ((Region) other);
        return new EqualsBuilder().append(center, rhs.center).isEquals();
    }

}
