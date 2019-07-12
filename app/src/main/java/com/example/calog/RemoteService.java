package com.example.calog;

import com.example.calog.VO.DietFourMealTotalVO;
import com.example.calog.VO.FitnessVO;
import com.example.calog.VO.MainHealthVO;
import com.example.calog.VO.UserDietViewVO;
import com.example.calog.VO.UserVO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemoteService {

    public static final String BASE_URL = "http://192.168.0.56:8088/calog/";

    /*//User
    @GET("user/list.jsp")
    Call<List<UserVO>> listUser();

    @GET("user/read.jsp")
    Call<UserVO> readUser(@Query("userId") String userId);

    @POST("user/insert.jsp")
    Call<Void> insertUser(@Body UserVO vo);

    @POST("user/delete.jsp")
    Call<Void> deleteUser(@Query("userId") String userId);

    @POST("user/update.jsp")
    Call<Void> updateUser(@Body UserVO vo);
*/

    //User Diet Total calories
    @GET("MainHealthVO")
    Call<MainHealthVO> userMainHealth(@Query("user_id") String user_id, @Query("select_date") String date);

    @GET("DietFourMealTotalVO")
    Call<List<DietFourMealTotalVO>> userDietDailyCalorie(@Query("user_id") String user_id, @Query("diet_date") String date);

    @GET("UserDietViewVO")
    Call<List<UserDietViewVO>> userDietDailyMenu(@Query("user_id") String user_id, @Query("diet_date") String date);

    /*//FitnessCardio
    @GET("fitnessCardio/list.jsp")
    Call<List<FitnessVO>> listFitness();

    @POST("fitnessCardio/insert.jsp")
    Call<ResponseBody> insertFitnessCardio(@Body FitnessVO vo);

    @POST("fitnessCardio/delete.jsp")
    Call<Void> deleteFitnessCardio(@Query("fitnessCardioId") int fitnessCardioId);*/

}
