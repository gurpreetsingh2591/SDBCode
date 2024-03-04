package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ads implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

    @SerializedName("subcategory_id")
    @Expose
    private Integer subcategoryId;

    @SerializedName("contact")
    @Expose
    private String contact;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("pincode")
    @Expose
    private String pinCode;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("province")
    @Expose
    private String province;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Integer subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}