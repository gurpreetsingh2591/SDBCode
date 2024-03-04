package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdDetailsRes {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("addetails")
    @Expose
    private List<AdDetails> adsDetails = null;

    @SerializedName("imageurl")
    @Expose
    private String imageUrl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AdDetails> getAdsDetails() {
        return adsDetails;
    }

    public void setAdsDetails(List<AdDetails> adsDetails) {
        this.adsDetails = adsDetails;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}