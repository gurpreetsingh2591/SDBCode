package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FolderFile {
    private String id;
    private String docFolderId;
    private String docOwnerId;
    private String docName;
    private String cost;
    private String docSize;
    private String created;
    private String modified;
    private boolean isSelect;
    private String attachment;

    private String ownerId;
    private String accountantId;
    private String folderName;
    private Integer days;
    private String folderAsOrNot;
    private String numOfDocs;
    private boolean isFolder;

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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }
}
