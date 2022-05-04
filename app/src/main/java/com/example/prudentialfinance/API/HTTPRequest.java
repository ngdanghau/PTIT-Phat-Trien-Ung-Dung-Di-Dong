package com.example.prudentialfinance.API;

import com.example.prudentialfinance.Container.AccountCreate;
import com.example.prudentialfinance.Container.AccountEdit;
import com.example.prudentialfinance.Container.AccountGetAll;
import com.example.prudentialfinance.Container.AccountGetById;
import com.example.prudentialfinance.Container.CategoryGetAll;
import com.example.prudentialfinance.Container.EmailSettingsResponse;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Container.ReportTotalBalance;
import com.example.prudentialfinance.Container.SiteSettingsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HTTPRequest {

    /*Login*/
    @FormUrlEncoded
    @POST("api/login")
    Call<Login> login(@Field("username") String username, @Field("password") String password);


    //Recovery password
    @FormUrlEncoded
    @POST("api/recovery")
    Call<Login> recovery(@Field("email") String email);

    //RESET password
    @FormUrlEncoded
    @POST("api/reset")
    Call<Login> reset_pass(@Field("email") String email,@Field("code") String otp);

    /*Register*/
    @FormUrlEncoded
    @POST("api/signup")
    Call<Login> signup(
            @Field("firstname") String firstname, @Field("lastname") String lastname,
            @Field("email") String email, @Field("password") String password,
            @Field("password-confirm") String passwordConfirm
    );


    // Profile
    @GET("api/profile")
    Call<Login> profile(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/profile")
    Call<Login> updateProfile(@HeaderMap Map<String, String> headers,
                              @Field("firstname") String firstname, @Field("lastname") String lastname);


    @FormUrlEncoded
    @POST("api/change-password")
    Call<Login> changePassword(@HeaderMap Map<String, String> headers,
                              @Field("password") String newPass, @Field("password-confirm") String confirmPass,
                              @Field("current-password") String currentPass);

    /** Application settings*/


    @GET("api/settings/site")
    Call<SiteSettingsResponse> getSiteSettings(@HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST("api/settings/site")
    Call<SiteSettingsResponse> saveSiteSettings(@HeaderMap Map<String, String> headers,
                                                @Field("action") String action,
                                                @Field("site_name") String site_name,
                                                @Field("site_slogan") String site_slogan,
                                                @Field("site_description") String site_description,
                                                @Field("site_keywords") String site_keywords,
                                                @Field("logotype") String logotype,
                                                @Field("logomark") String logomark,
                                                @Field("language") String language,
                                                @Field("currency") String currency);

    @GET("api/settings/smtp")
    Call<EmailSettingsResponse> getEmailSettings(@HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST("api/settings/smtp")
    Call<EmailSettingsResponse> saveEmailSettings(@HeaderMap Map<String, String> headers,
                                                @Field("action") String action,
                                                @Field("host") String host,
                                                @Field("port") String port,
                                                @Field("encryption") String encryption,
                                                @Field("auth") Boolean auth,
                                                @Field("username") String username,
                                                @Field("password") String password,
                                                @Field("from") String from);

    /***************************ACCOUNT***************************/
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @GET("api/accounts")
    Call<AccountGetAll> accountGetAll(@Header("Authorization") String authorization);

    @GET("api/accounts")
    Call<AccountGetAll> accountGetAll2(@HeaderMap Map<String, String> headers);

    @GET("api/accounts")
    Call<AccountGetAll> accountGetAll3(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> options);

    /*get by id*/
    @GET("api/accounts/{id}")
    Call<AccountGetById> accountGetById(@HeaderMap Map<String, String> headers, @Path("id") String id);

    /*create*/
    @FormUrlEncoded
    @POST("api/accounts")
    Call<AccountCreate> accountCreate(@HeaderMap Map<String, String> headers,
                                      @Field("name") String name,
                                      @Field("balance") int balance,
                                      @Field("description") String description,
                                      @Field("accountnumber") String accountnumber);

    /*edit*/
    @FormUrlEncoded
    @PUT("api/accounts/{id}")
    Call<AccountEdit> accountEdit(@HeaderMap Map<String, String> headers,
                                  @Path("id") String id,
                                  @Field("name") String name,
                                  @Field("balance") int balance,
                                  @Field("description") String description,
                                  @Field("accountnumber") String accountnumber);



    /***************************HOME*********************************/
    @GET("api/home/latestall")
    Call<HomeLatestTransactions> homeLatestTransactions(@HeaderMap Map<String, String> headers);



    /***************************CATEGORY*********************************/
    @GET("api/incomecategories")
    Call<CategoryGetAll> categoryGetAll(@HeaderMap Map<String, String> headers);


    /***************************REPORT***************************/
    @GET("/api/report/totalBalance")
    Call<ReportTotalBalance> reportTotalBalace(@HeaderMap Map<String, String> headers,
                                               @Query("date") String date);

}
