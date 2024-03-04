package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("notifications")
    @Expose
    private String notifications;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
}