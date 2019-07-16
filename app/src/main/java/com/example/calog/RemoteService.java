package com.example.calog;

import com.example.calog.VO.DrinkingVO;
import com.example.calog.VO.FitnessVO;
import com.example.calog.VO.MainHealthVO;
import com.example.calog.VO.SleepingVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;
import com.example.calog.VO.UserVO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemoteService {

    public static final String BASE_URL = "http://210.89.188.230:8080/Calog/calog/";

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

    /*//FitnessCardio
    @GET("fitnessCardio/list.jsp")
    Call<List<FitnessVO>> listFitness();

    @POST("fitnessCardio/insert.jsp")
    Call<ResponseBody> insertFitnessCardio(@Body FitnessVO vo);

    @POST("fitnessCardio/delete.jsp")
    Call<Void> deleteFitnessCardio(@Query("fitnessCardioId") int fitnessCardioId);*/

    @GET("UserTotalCaloriesViewVOList")
    Call<List<UserTotalCaloriesViewVO>> UserTotalCaloriesViewVO(@Query("user_id") String user_id);

    @POST("UserDrinkInsert")
    Call<Void> UserDrinkInsert(@Body DrinkingVO vo);


    ////////////////// TODO 그래프용 restAPI ////////////////////////////////

    ///////////////////// 식단 ////////////////////////////////
    // New Version Graph
    @GET("GraphDietData")
    Call<List<UserTotalCaloriesViewVO>> GraphDietData(@Query("user_id") String user_id, @Query("start_date") String start_date, @Query("unit_date") String unit_date);
    /////////////////////

    @GET("LastWeekTotalCalorie") //최근 일주일간 데이터 //일
    Call<List<UserTotalCaloriesViewVO>> LastWeekTotalCalorie(@Query("user_id") String user_id);

    @GET("LastMonthTotalCalorie") //최근 한달간 데이터 //주
    Call<List<UserTotalCaloriesViewVO>> LastMonthTotalCalorie(@Query("user_id") String user_id);

    @GET("LastYearTotalCalorie") //최근 1년간 데이터 //월
    Call<List<UserTotalCaloriesViewVO>> LastYearTotalCalorie(@Query("user_id") String user_id);

    @GET("LastAllTotalCalorie") //최근 5년간 데이터 //년
    Call<List<UserTotalCaloriesViewVO>> LastAllTotalCalorie(@Query("user_id") String user_id);

    ///////////////////// 운동 ////////////////////////////////
    // New Version Graph
    @GET("GraphWeightCardioSumData")
    Call<List<FitnessVO>> GraphFitnessData(@Query("user_id") String user_id, @Query("start_date") String start_date, @Query("unit_date") String unit_date);
    ///////////////////

    @GET("LastWeekTotalBurnCal")
    Call<List<FitnessVO>> LastWeekTotalBurnCal(@Query("user_id") String user_id);

    @GET("LastMonthTotalBurnCal")
    Call<List<FitnessVO>> LastMonthTotalBurnCal(@Query("user_id") String user_id);

    @GET("LastYearTotalBurnCal")
    Call<List<FitnessVO>> LastYearTotalBurnCal(@Query("user_id") String user_id);

    @GET("LastAllTotalBurnCal")
    Call<List<FitnessVO>> LastAllTotalBurnCal(@Query("user_id") String user_id);

    ///////////////////// 음주 ////////////////////////////////
    // New Version Graph
    @GET("GraphDrinkingData")
    Call<List<DrinkingVO>> GraphDrinkingData(@Query("user_id") String user_id, @Query("start_date") String start_date, @Query("unit_date") String unit_date);
    ////////////////////////////////

    @GET("LastWeekTotalBac")
    Call<List<DrinkingVO>> LastWeekTotalBac(@Query("user_id") String user_id);

    @GET("LastMonthTotalBac")
    Call<List<DrinkingVO>> LastMonthTotalBac(@Query("user_id") String user_id);

    @GET("LastYearTotalBac")
    Call<List<DrinkingVO>> LastYearTotalBac(@Query("user_id") String user_id);

    @GET("LastAllTotalBac")
    Call<List<DrinkingVO>> LastAllTotalBac(@Query("user_id") String user_id);

    ///////////////////// 수면 ////////////////////////////////
    // New Version Graph
    @GET("GraphSleepingData")
    Call<List<SleepingVO>> GraphSleepingData(@Query("user_id") String user_id, @Query("start_date") String start_date, @Query("unit_date") String unit_date);
    ///////////////////
    @GET("LastWeekTotalSnoring")
    Call<List<SleepingVO>> LastWeekTotalSnoring(@Query("user_id") String user_id);

    @GET("LastMonthTotalSnoring")
    Call<List<SleepingVO>> LastMonthTotalSnoring(@Query("user_id") String user_id);

    @GET("LastYearTotalSnoring")
    Call<List<SleepingVO>> LastYearTotalSnoring(@Query("user_id") String user_id);

    @GET("LastAllTotalSnoring")
    Call<List<SleepingVO>> LastAllTotalSnoring(@Query("user_id") String user_id);
}
