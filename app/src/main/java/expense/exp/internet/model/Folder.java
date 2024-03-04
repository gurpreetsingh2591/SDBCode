package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Folder {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("owner_id")
    @Expose
    private String ownerId;
    @SerializedName("accountant_id")
    @Expose
    private String accountantId;
    @SerializedName("folder_name")
    @Expose
    private String folderName;
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("FolderAsOrNot")
    @Expose
    private String folderAsOrNot;
    @SerializedName("NumOfDocs")
    @Expose
    private String numOfDocs;
    @SerializedName("subfolderlist")
    @Expose
    private List<SubFolder> subFolders;
    private boolean isSubfolder;

    public boolean isSubfolder() {
        return isSubfolder;
    }

    public void setSubfolder(boolean subfolder) {
        isSubfolder = subfolder;
    }

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

    public String getAccountantId() {
        return accountantId;
    }

    public void setAccountantId(String accountantId) {
        this.accountantId = accountantId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getFolderAsOrNot() {
        return folderAsOrNot;
    }

    public void setFolderAsOrNot(String folderAsOrNot) {
        this.folderAsOrNot = folderAsOrNot;
    }

    public String getNumOfDocs() {
        return numOfDocs;
    }

    public void setNumOfDocs(String numOfDocs) {
        this.numOfDocs = numOfDocs;
    }

    public List<SubFolder> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<SubFolder> subFolders) {
        this.subFolders = subFolders;
    }
}