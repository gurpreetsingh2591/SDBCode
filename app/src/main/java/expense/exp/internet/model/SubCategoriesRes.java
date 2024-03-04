package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoriesRes {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("subcategories")
    @Expose
    private List<SubCategory> subcategories = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }
}