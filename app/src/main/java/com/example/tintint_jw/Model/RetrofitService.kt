package com.example.tintint_jw.Model

import GetProfileResponse
import com.example.tintint_jw.Model.Auth.CheckDuplicate.ID.DuplicateIdResponse
import com.example.tintint_jw.Model.Auth.CheckDuplicate.Nickname.DuplicateNameResponse
import com.example.tintint_jw.Model.Auth.Login.Kakao.LoginKakaoRequest
import com.example.tintint_jw.Model.Auth.Login.Kakao.LoginKakaoResponse
import com.example.tintint_jw.Model.Auth.Login.Local.LoginLocalRequest
import com.example.tintint_jw.Model.Auth.Login.Local.LoginLocalResponse
import com.example.tintint_jw.Model.Auth.School.*
import com.example.tintint_jw.Model.Auth.SignUp.SignUpRequest
import com.example.tintint_jw.Model.Auth.SignUp.SignUpResponse
import com.example.tintint_jw.Model.Profile.PatchProfileResponse
import com.example.tintint_jw.Model.Profile.PutProfile
import com.example.tintint_jw.Model.Profile.SignUpKakaoRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface RetrofitService{

    @Headers("Accept: application/json")
    @POST("/api-token-auth/")
    fun login(@Body user : LoginKakaoRequest) : Call<LoginLocalResponse>

    //header부분에 Auth 코드를 넣어줘야함.
    @Headers("Accept: application/json")
    @GET("/api/v1/me/profile")
    fun getProfile(@Header("Authorization") user : String) : Call<GetProfileResponse>

    @PATCH("/api/v1/me/profile")
    fun putProfile(@Header("Authorization") auth : String , @Body user : PutProfile) : Call<PatchProfileResponse>

    @GET("/api/v1/users/{id}/profile")
    fun getOtherProfile(@Header("Authentication") authentication:String, @Path("id") id : String) : Call<PutProfile>

    @Headers("Accept: application/json")

    @POST("/api/v1/auth/school")
    fun SchoolAuth(@Body user: SchoolAuthRequest) : Call<SchoolAuthResponse>

    @Headers("Accept: application/json")
    @POST("/api/v1/auth/school/confirm")
    fun SchoolAuthConfirm(@Body user: SchoolAuthConfirmRequest) : Call<SchoolAuthConfirmResponse>

    @Headers("Accept: application/json")
    @GET("/api/v1/auth/school/complete")
    fun SchoolAuthComplete(@Query("email") email:String) : Call<SchoolCompleteResponse>

    @Headers("Accept: application/json")
    @POST("/api/v1/auth/kakao/login")
    fun LoginKakao(@Header("Authorization") kakaoId : String) : Call<LoginKakaoResponse>

    @Headers("Accept: application/json")
    @POST("/api/v1/auth/kakao/login")
    fun SignUpKakao(@Header("Authorization") kakaoId : String, @Body user : SignUpKakaoRequest) : Call<LoginKakaoResponse>

    @Headers("Accept: application/json")
    @POST("/api/v1/auth/local/login")
    fun LoginLocal(@Body user: LoginLocalRequest) : Call<LoginLocalResponse>

    @Headers("Accept: application/json")
    @POST("/api/v1/auth/local/signup")
    fun SignUp(@Body user : SignUpRequest) : Call<SignUpResponse>

    @Headers("Accept: application/json")
    @GET("/api/v1/auth/duplicate-id")
    fun CheckDuplicateId(@Query("local_id") name:String) : Call<DuplicateIdResponse>

    @Headers("Accept: application/json")
    @GET("/api/v1/auth/duplicate-name")
    fun CheckDuplicateName(@Query("name") name: String) : Call<DuplicateNameResponse>


    @Headers("Accept: application/json")
    @GET("/api/v1/policy/rule/")
    fun GetPolicyRule() : Call<String>

    @Headers("Accept: application/json")
    @GET("/api/v1/policy/privacy/")
    fun GetPolicyPrivate() : Call<String>

  /*  @Headers("Accept: application/json")
    @GET("/api/v1/auth/logout/")
    fun Logout(@Body user: RequestLogout) : Call<LogoutResponse>
*/
}