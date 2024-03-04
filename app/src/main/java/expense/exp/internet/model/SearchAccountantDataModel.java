package expense.exp.internet.model;

/**
 * Created by admin on 30-07-2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchAccountantDataModel {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<AccountantResult> results = null;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AccountantResult> getResults() {
        return results;
    }

    public void setResults(List<AccountantResult> results) {
        this.results = results;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
