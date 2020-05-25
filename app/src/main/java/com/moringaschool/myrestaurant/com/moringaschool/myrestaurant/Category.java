
package com.moringaschool.myrestaurant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Category {

    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("title")
    @Expose
    private String title;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Category() {
    }

    /**
     * 
     * @param alias
     * @param title
     */
    public Category(String alias, String title) {
        super();
        this.alias = alias;
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(alias).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Category) == false) {
            return false;
        }
        Category rhs = ((Category) other);
        return new EqualsBuilder().append(title, rhs.title).append(alias, rhs.alias).isEquals();
    }

}
