package expense.exp.internet.model;

/**
 * Created by admin on 11-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import expense.exp.model_class.StudentDataModal;

public class UserInfo {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("student")
    @Expose
    private StudentDataModal student;
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

    public StudentDataModal getStudent() {
        return student;
    }

    public void setStudent(StudentDataModal student) {
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