package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanList {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("pack_name")
    @Expose
    private String pack_name;

    @SerializedName("space")
    @Expose
    private String space;

    @SerializedName("pack_cost")
    @Expose
    private String pack_cost;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("status")
    @Expose
    private String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public void setPack_name(String pack_name) {
        this.pack_name = pack_name;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getPack_cost() {
        return pack_cost;
    }

    public void setPack_cost(String pack_cost) {
        this.pack_cost = pack_cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
