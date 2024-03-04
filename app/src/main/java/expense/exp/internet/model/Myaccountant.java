package expense.exp.internet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 11-07-2018.
 */

public class Myaccountant {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uID")
    @Expose
    private String uID;
    @SerializedName("aID")
    @Expose
    private String aID;
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

    public String getUID() {
        return uID;
    }

    public void setUID(String uID) {
        this.uID = uID;
    }

    public String getAID() {
        return aID;
    }

    public void setAID(String aID) {
        this.aID = aID;
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