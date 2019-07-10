package com.example.calog;

import com.example.calog.VO.UserVO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JoinRemoteService {
    public static final String BASE_URL = "http://192.168.0.27:8088/join";

    @POST("read.jsp")
    Call<UserVO> readUser(@Query("userId") String userId, @Query("password") String password);

}
