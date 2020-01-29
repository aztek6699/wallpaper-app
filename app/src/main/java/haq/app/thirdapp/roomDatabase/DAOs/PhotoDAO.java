package haq.app.thirdapp.roomDatabase.DAOs;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import haq.app.thirdapp.models.PhotoDTO;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface PhotoDAO {

    @Query("select * from photo")
    List<PhotoDTO> getWallpaperList();

    @Query("select * from photo where photosetID = :photosetID")
    Flowable<List<PhotoDTO>> getFlowableList(String photosetID);

    @Insert
    void insertPhotoDTO(PhotoDTO object);

    @Query("DELETE FROM photo")
    void nukeTable();

    @Query("select * from photo where photoID = :photoID")
    Single<PhotoDTO> getPhotoByID(int photoID);

    @Query("select * from photo where photosetID = :photosetID")
    Single<List<PhotoDTO>> getPhotosetByID(String photosetID);

    @Query("UPDATE photo SET favourite = :favourite WHERE photoID = :photoID")
    void updateFavourite( int photoID, boolean favourite);

    @Query("select * from photo where favourite = 1")
    Single<List<PhotoDTO>> getFavourites();

}
