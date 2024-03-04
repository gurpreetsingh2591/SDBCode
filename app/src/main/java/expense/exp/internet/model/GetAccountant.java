package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetAccountant {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("accs")
    @Expose
    private List<Acc> accs = null;
    @SerializedName("myaccountantId")
    @Expose
    private String myaccountantId;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Acc> getAccs() {
        return accs;
    }

    public void setAccs(List<Acc> accs) {
        this.accs = accs;
    }

    public String getMyaccountantId() {
        return myaccountantId;
    }

    public void setMyaccountantId(String myaccountantId) {
        this.myaccountantId = myaccountantId;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
