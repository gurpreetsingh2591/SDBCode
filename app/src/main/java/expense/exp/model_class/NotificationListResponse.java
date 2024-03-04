package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationListResponse {

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("notifications")
    @Expose
    private List<NotificationList> notifications;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<NotificationList> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationList> notifications) {
        this.notifications = notifications;
    }
}
