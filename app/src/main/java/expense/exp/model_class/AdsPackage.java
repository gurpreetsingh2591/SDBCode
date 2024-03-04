package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsPackage {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String packName;

    @SerializedName("cost")
    @Expose
    private String packCost;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("total_ads")
    @Expose
    private String totalAds;

    @SerializedName("status")
    @Expose
    private String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getPackCost() {
        return packCost;
    }

    public void setPackCost(String packCost) {
        this.packCost = packCost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAds() {
        return totalAds;
    }

    public void setTotalAds(String totalAds) {
        this.totalAds = totalAds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
