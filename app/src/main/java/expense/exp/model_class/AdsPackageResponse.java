package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdsPackageResponse {

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("myplan")
    @Expose
    private String myplan;

    @SerializedName("packages")
    @Expose
    private List<AdsPackage> packages;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AdsPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<AdsPackage> packages) {
        this.packages = packages;
    }

    public String getMyplan() {
        return myplan;
    }

    public void setMyplan(String myplan) {
        this.myplan = myplan;
    }
}
