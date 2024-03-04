package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanListResponse {

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("myplan")
    @Expose
    private String myplan;

    @SerializedName("packages")
    @Expose
    private List<PlanList> packages;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlanList> getPackages() {
        return packages;
    }

    public void setPackages(List<PlanList> packages) {
        this.packages = packages;
    }

    public String getMyplan() {
        return myplan;
    }

    public void setMyplan(String myplan) {
        this.myplan = myplan;
    }
}
