package expense.exp.internet.model;

/**
 * Created by admin on 30-07-2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchDocumentDataModel {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<Document> results = null;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("folders")
    @Expose
    private List<SubFolder> folders = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Document> getResults() {
        return results;
    }

    public void setResults(List<Document> results) {
        this.results = results;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public List<SubFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<SubFolder> folders) {
        this.folders = folders;
    }
}
