package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFolders {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("folders")
    @Expose
    private List<Folder> folders = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

}