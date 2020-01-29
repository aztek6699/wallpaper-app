package haq.app.thirdapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo")
public class PhotoDTO implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int photoID;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "url")
    @SerializedName("url_o")
    @Expose
    private String photoURL;

    @ColumnInfo(name = "photosetID")
    private String photosetID;

    @ColumnInfo(name = "favourite")
    private boolean favourite;

    public PhotoDTO() {}

    protected PhotoDTO(Parcel in) {
        photoID = in.readInt();
        title = in.readString();
        photoURL = in.readString();
        photosetID = in.readString();
        favourite = in.readByte() != 0;
    }

    public static final Creator<PhotoDTO> CREATOR = new Creator<PhotoDTO>() {
        @Override
        public PhotoDTO createFromParcel(Parcel in) {
            return new PhotoDTO(in);
        }

        @Override
        public PhotoDTO[] newArray(int size) {
            return new PhotoDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photoID);
        dest.writeString(title);
        dest.writeString(photoURL);
        dest.writeString(photosetID);
        dest.writeByte((byte) (favourite ? 1 : 0));
    }

    //region getters/setters
    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int id) {
        this.photoID = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotosetID() {
        return photosetID;
    }

    public void setPhotosetID(String photosetID) {
        this.photosetID = photosetID;
    }

    public boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
    //endregion
}
