package haq.app.thirdapp.apiInterfaces;

import haq.app.thirdapp.models.PhotosetDTO;
import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApiInterface {

    @GET("services/rest/")
    Single<PhotosetDTO> getPhotoset(@Query("method") String method,
                                         @Query("api_key") String apiKey,
                                         @Query("photoset_id") String photosetID,
                                         @Query("user_id") String userID,
                                         @Query("extras") String extras,
                                         @Query("format") String format,
                                         @Query("nojsoncallback") String jsonCallback);
}
