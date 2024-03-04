package expense.exp.internet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 30-07-2018.
 */

public class SearchFolderDataModel {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<FolderResult> results = null;
    @SerializedName("imageurl")
    @Expose
    private Object imageurl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FolderResult> getResults() {
        return results;
    }

    public void setResults(List<FolderResult> results) {
        this.results = results;
    }

    public Object getImageurl() {
        return imageurl;
    }

    public void setImageurl(Object imageurl) {
        this.imageurl = imageurl;
    }

}