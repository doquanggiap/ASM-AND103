package fpoly.giapdqph34273.asm_ph34273_md18306;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    String DOMAIN = "http://192.168.0.103:3000/";

    @GET("/api/list")
    Call<List<CarModel>> getCars();

    @DELETE("/xoa/{id}")
    Call<Void> deleteCar(@Path("id") String id);
//
//    @PUT("/api/update-car/{id}")
//    Call<Void> updateCar(@Path("id") String id, @Body CarModel carModel);

    @POST("/add_xe")
    Call<Void> addCar(@Body CarModel carModel);
}
