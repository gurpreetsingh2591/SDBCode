package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import expense.exp.internet.model.User;

public class UserIfo {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("student")
    @Expose
    private List<String> student;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("referlink")
    @Expose
    private String referlink;


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

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getReferlink() {
        return referlink;
    }

    public void setReferlink(String referlink) {
        this.referlink = referlink;
    }
}
