package com.example.prudentialfinance.API;

import com.example.prudentialfinance.Container.AccountCreate;
import com.example.prudentialfinance.Container.AccountDelete;
import com.example.prudentialfinance.Container.AccountEdit;
import com.example.prudentialfinance.Container.AccountGetAll;
import com.example.prudentialfinance.Container.AccountGetById;
import com.example.prudentialfinance.Container.Settings.AvatarUpload;
import com.example.prudentialfinance.Container.CategoryGetAll;
import com.example.prudentialfinance.Container.Settings.EmailSettingsResponse;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Container.ReportTotalBalance;
import com.example.prudentialfinance.Container.Settings.SiteSettingsResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HTTPRequest {

    /**
     * Attention: we should utilize String data type, even parameters are passed to
     * function in API maybe as Integer, Double, Long, .. and so forth
     *
     * Using String data type helps us to reduce errors when Retrofit return result equals oh but
     * doesn't return error message
     * */

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
    Call<Login> process_reset(@Field("email") String email,@Field("code") String otp,@Field("action") String action);
    @FormUrlEncoded
    @POST("api/reset")
    Call<Login> reset_pass(@Field("email") String email, @Field("hash") String hash,@Field("password") String password,@Field("password-confirm") String password_confirm,@Field("action") String action);

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
                              @Field("action") String action,
                              @Field("firstname") String firstname,
                              @Field("lastname") String lastname);

    @Multipart
    @POST("api/profile")
    Call<AvatarUpload> uploadAvatar(@Header("Authorization") String authorization,
                                    @Part("action") RequestBody action,
                                    @Part MultipartBody.Part file);


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
    Call<AccountEdit> accountUpdate(@HeaderMap Map<String, String> headers,
                                  @Path("id") int id,
                                  @Field("name") String name,
                                  @Field("balance") String balance,
                                  @Field("description") String description,
                                  @Field("accountnumber") String accountnumber);

    @FormUrlEncoded
    @PUT("api/accounts/{id}")
    Call<AccountEdit> accountUpdate2(@HeaderMap Map<String, String> headers,
                                    @QueryMap Map<String, String> parameters);


    @DELETE("api/accounts/{id}")
    Call<AccountDelete> accountDelete(@HeaderMap Map<String, String> headers,@Path("id") int id);


    /***************************HOME*********************************/
    @GET("api/home/latestall")
    Call<HomeLatestTransactions> homeLatestTransactions(@HeaderMap Map<String, String> headers);



    /***************************CATEGORY*********************************/
    @GET("api/incomecategories")
    Call<CategoryGetAll> searchIncomeCategories(@HeaderMap Map<String, String> headers,
                                                @Query("search") String search,
                                                @Query("start") int start,
                                                @Query("length") int length,
                                                @Query("order[column]") String column,
                                                @Query("order[dir]") String dir);

    @DELETE("api/incomecategories/{id}")
    Call<AccountDelete> removeIncomeCategories(@HeaderMap Map<String, String> headers, @Path("id") int id);

    @GET("api/expensecategories")
    Call<CategoryGetAll> searchExpenseCategories(@HeaderMap Map<String, String> headers,
                                                 @Query("search") String search,
                                                 @Query("start") int start,
                                                 @Query("length") int length,
                                                 @Query("order[column]") String column,
                                                 @Query("order[dir]") String dir);

    @DELETE("api/expensecategories/{id}")
    Call<AccountDelete> removeExpenseCategories(@HeaderMap Map<String, String> headers, @Path("id") int id);

    /***************************REPORT***************************/
    @GET("/api/report/totalBalance")
    Call<ReportTotalBalance> reportTotalBalace(@HeaderMap Map<String, String> headers,
                                               @Query("date") String date);

}
