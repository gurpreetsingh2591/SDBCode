package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFiles {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;
    @SerializedName("doc_path")
    @Expose
    private String docPath;
    @SerializedName("SubFolders")
    @Expose
    private List<SubFolder> subFolders;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public List<SubFolder> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<SubFolder> subFolders) {
        this.subFolders = subFolders;
    }
}

