package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("User")
    @Expose
    private User user;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("Message")
    @Expose
    private String Message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}