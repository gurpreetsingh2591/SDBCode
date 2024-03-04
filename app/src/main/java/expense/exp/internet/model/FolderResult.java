package expense.exp.internet.model;

/**
 * Created by admin on 30-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FolderResult {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("owner_id")
    @Expose
    private String ownerId;
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("folder_name")
    @Expose
    private String folderName;
    @SerializedName("status")
    @Expose
    private String status;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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