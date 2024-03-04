package expense.exp.internet.model;

/**
 * Created by admin on 30-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentResult {




    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("doc_folder_id")
    @Expose
    private String docFolderId;
    @SerializedName("doc_owner_id")
    @Expose
    private String docOwnerId;
    @SerializedName("doc_name")
    @Expose
    private String docName;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("doc_size")
    @Expose
    private String docSize;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocFolderId() {
        return docFolderId;
    }

    public void setDocFolderId(String docFolderId) {
        this.docFolderId = docFolderId;
    }

    public String getDocOwnerId() {
        return docOwnerId;
    }

    public void setDocOwnerId(String docOwnerId) {
        this.docOwnerId = docOwnerId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDocSize() {
        return docSize;
    }

    public void setDocSize(String docSize) {
        this.docSize = docSize;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

}