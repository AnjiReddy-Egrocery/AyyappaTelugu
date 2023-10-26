package com.example.ayyapatelugu.Services;

import com.example.ayyapatelugu.Model.BajanaManadaliListModel;
import com.example.ayyapatelugu.Model.BajanaMandaliList;
import com.example.ayyapatelugu.Model.BooksListModel;
import com.example.ayyapatelugu.Model.GuruSwamiList;
import com.example.ayyapatelugu.Model.KaryakarmamList;
import com.example.ayyapatelugu.Model.UserDataResponse;
import com.example.ayyapatelugu.Model.YatraList;
import com.example.ayyapatelugu.Model.decoratorListModel;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APiInterface {

    @GET("APICalls/Decorators/index")
    Call<decoratorListModel> getDecoratorsList();

    @GET("APICalls/Books/index")
    Call<BooksListModel> getBookList();

    @GET("APICalls/Guruswami/index")
    Call<GuruSwamiList> getGuruSwamiList();

    @GET("APICalls/Yatralu/index")
    Call<YatraList> getYatraList();

    @GET("APICalls/Bajanamandali/index")
    Call<BajanaMandaliList> getBajamandaliList();

    @GET("APICalls/Activities/index")
    Call<KaryakarmamList> getKaryakaramamList();

    @POST("APICalls/Bajanamandali/info")
    Call<BajanaMandaliList> postBajanaMandali(@Body BajanaManadaliListModel bajanaMandaliList);


    @Multipart
    @POST("APICalls/Users/userRegistration")
    Call<UserDataResponse> postData(
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("emailId") RequestBody emailId,
            @Part("mobileNumber") RequestBody mobileNumber,
            @Part("pwd") RequestBody pwd
    );



}
