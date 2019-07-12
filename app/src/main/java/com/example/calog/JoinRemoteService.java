package com.example.calog;

import com.example.calog.VO.UserVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JoinRemoteService {
    public static final String BASE_URL = "http://192.168.0.136:8088/";

    //User
    @GET("user/list.jsp")
    Call<List<UserVO>> listUser();

    @GET("join/read")
    Call<UserVO> readUser(@Query("user_id") String user_id, @Query("password") String password);

    @POST("join/insert")
    Call<Void> insertUser(@Body UserVO vo);

    @POST("user/delete.jsp")
    Call<Void> deleteUser(@Query("userId") String userId);

    @POST("user/update.jsp")
    Call<Void> updateUser(@Body UserVO vo);

}
