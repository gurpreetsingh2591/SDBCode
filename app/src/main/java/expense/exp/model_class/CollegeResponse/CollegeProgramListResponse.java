package expense.exp.model_class.CollegeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollegeProgramListResponse {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("list")
    @Expose
    private java.util.List<CollProgramList> list = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.util.List<CollProgramList> getList() {
        return list;
    }

    public void setList(java.util.List<CollProgramList> list) {
        this.list = list;
    }
}
