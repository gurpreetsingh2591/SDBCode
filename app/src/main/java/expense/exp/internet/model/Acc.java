package expense.exp.internet.model;

/**
 * Created by admin on 10-07-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Acc {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;

    private boolean isselect=false;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIsselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }
}