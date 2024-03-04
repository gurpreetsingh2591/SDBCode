package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAds {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("ads")
    @Expose
    private List<Ads> ads = null;

    @SerializedName("imageurl")
    @Expose
    private String imageUrl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Ads> getAds() {
        return ads;
    }

    public void setAds(List<Ads> folders) {
        this.ads = folders;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}