package com.dst.ayyapatelugu.Services;

import com.dst.ayyapatelugu.Model.BajanaManadaliListModel;
import com.dst.ayyapatelugu.Model.BajanaMandaliList;
import com.dst.ayyapatelugu.Model.BooksListModel;
import com.dst.ayyapatelugu.Model.GuruSwamiList;
import com.dst.ayyapatelugu.Model.KaryakarmamList;
import com.dst.ayyapatelugu.Model.UserDataResponse;
import com.dst.ayyapatelugu.Model.YatraList;
import com.dst.ayyapatelugu.Model.decoratorListModel;


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
