package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationList {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("sender")
    @Expose
    private String sender;

    @SerializedName("receiver")
    @Expose
    private String receiver;

    @SerializedName("doc_id")
    @Expose
    private String doc_id;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("doc_name")
    @Expose
    private String doc_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
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

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }
}
