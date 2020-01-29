package haq.app.thirdapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoListDTO {

    @SerializedName("photo")
    private List<PhotoDTO> list;

    public List<PhotoDTO> getList() {
        return list;
    }

    public void setList(List<PhotoDTO> list) {
        this.list = list;
    }
}
