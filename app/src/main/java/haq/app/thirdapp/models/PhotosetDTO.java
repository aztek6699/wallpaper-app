package haq.app.thirdapp.models;

import com.google.gson.annotations.SerializedName;

public class PhotosetDTO {

    @SerializedName("photoset")
    private PhotoListDTO photoListDTO;

    @SerializedName("stat")
    private String status;

    public PhotoListDTO getPhotoListDTO() {
        return photoListDTO;
    }

    public void setPhotoListDTO(PhotoListDTO photoset) {
        this.photoListDTO = photoset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
