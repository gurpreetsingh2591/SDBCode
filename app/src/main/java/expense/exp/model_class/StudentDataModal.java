
package expense.exp.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentDataModal {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("stu_id")
    @Expose
    private String stuId;
    @SerializedName("college")
    @Expose
    private String college;
    @SerializedName("program")
    @Expose
    private String program;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

}
