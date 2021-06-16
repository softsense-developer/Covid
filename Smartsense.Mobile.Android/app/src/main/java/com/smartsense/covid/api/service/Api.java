package com.smartsense.covid.api.service;

import com.smartsense.covid.api.model.requests.AddCompanionRequest;
import com.smartsense.covid.api.model.requests.AddInfoRequest;
import com.smartsense.covid.api.model.requests.AddQuarantineLocationRequest;
import com.smartsense.covid.api.model.requests.AddWarningRequest;
import com.smartsense.covid.api.model.requests.PatientConnectionRequest;
import com.smartsense.covid.api.model.requests.ResetPasswordRequest;
import com.smartsense.covid.api.model.requests.PutNowLocationRequest;
import com.smartsense.covid.api.model.requests.RefreshTokenRequest;
import com.smartsense.covid.api.model.responses.AddCompanionResponse;
import com.smartsense.covid.api.model.responses.AddDoctorResponse;
import com.smartsense.covid.api.model.responses.AddInfoResponse;
import com.smartsense.covid.api.model.requests.AddValueRequest;
import com.smartsense.covid.api.model.responses.AddQuarantineLocationResponse;
import com.smartsense.covid.api.model.responses.AddValueResponse;
import com.smartsense.covid.api.model.requests.PasswordChangeRequest;
import com.smartsense.covid.api.model.responses.AddWarningResponse;
import com.smartsense.covid.api.model.responses.DeleteCompanionResponse;
import com.smartsense.covid.api.model.responses.GetCompanionResponse;
import com.smartsense.covid.api.model.responses.PatientConnectionResponse;
import com.smartsense.covid.api.model.responses.GetDoctorResponse;
import com.smartsense.covid.api.model.responses.GetHospitalResponse;
import com.smartsense.covid.api.model.responses.GetPromotionsResponse;
import com.smartsense.covid.api.model.responses.PasswordChangeResponse;
import com.smartsense.covid.api.model.responses.PatientInfoResponse;
import com.smartsense.covid.api.model.responses.InsertResponse;
import com.smartsense.covid.api.model.responses.LoginResponse;
import com.smartsense.covid.api.model.responses.PutNowLocationResponse;
import com.smartsense.covid.api.model.responses.QuarantineStatusResponse;
import com.smartsense.covid.api.model.responses.RefreshTokenResponse;
import com.smartsense.covid.api.model.responses.ResetPasswordResponse;
import com.smartsense.covid.api.model.responses.SetLocationStatusResponse;
import com.smartsense.covid.api.model.responses.UserDataResponse;
import com.smartsense.covid.api.model.requests.UserInfoRequest;
import com.smartsense.covid.api.model.responses.UserInfoResponse;
import com.smartsense.covid.api.model.requests.UserLoginRequest;
import com.smartsense.covid.api.model.responses.UserLoginResponse;
import com.smartsense.covid.api.model.requests.UserRegisterRequest;
import com.smartsense.covid.api.model.responses.UserRegisterResponse;
import com.smartsense.covid.api.model.responses.VitalHistoryResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface Api {

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/auth/register")
    Call<UserRegisterResponse> register(@Body UserRegisterRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/auth/login")
    Call<UserLoginResponse> login(@Body UserLoginRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/auth/PasswordForgot")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/auth/refreshtoken")
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/auth/getinfo")
    Call<UserInfoResponse> getProfileInfo();

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/auth/putinfo")
    Call<UserInfoResponse> updateProfileInfo(@Body UserInfoRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/auth/changepassword")
    Call<PasswordChangeResponse> passwordChange(@Body PasswordChangeRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @PUT("api/patient/putinfo")
    Call<AddInfoResponse> updatePatientInfo(@Body AddInfoRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/patient/addinfo")
    Call<AddInfoResponse> addPatientInfo(@Body AddInfoRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/patient/getinfo")
    Call<PatientInfoResponse> getPatientInfo();

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/value/addvalue")
    Call<AddValueResponse> addValue(@Body AddValueRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/value/gethistory")
    Call<VitalHistoryResponse> getVitalHistory();

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/patient/quarantinestatus")
    Call<QuarantineStatusResponse> quarantineStatus();

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @PUT("api/patient/putnowlocation")
    Call<PutNowLocationResponse> sendLocationData(@Body PutNowLocationRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/patient/addquarantinelocation")
    Call<AddQuarantineLocationResponse> setQuarantineLocation(@Body AddQuarantineLocationRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/patient/addwarning")
    Call<AddWarningResponse> addWarning(@Body AddWarningRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/patient/adddoctor/{DoktorId}")
    Call<AddDoctorResponse> addDoctor(@Path("DoktorId") long doctorId);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/patient/gethospital")
    Call<GetHospitalResponse> getHospitals();

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/patient/getdoctor/{HospitalId}")
    Call<GetDoctorResponse> getDoctors(@Path("HospitalId") long hospitalId);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/patient/getpromotions")
    Call<GetPromotionsResponse> getPatientPromotions();

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/patient/connectionacceptrefuse")
    Call<PatientConnectionResponse> getPatientConnectionAcceptRefuse(@Body PatientConnectionRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("api/patient/getcompanions")
    Call<GetCompanionResponse> getCompanions();

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/patient/addcompanion")
    Call<AddCompanionResponse> addCompanion(@Body AddCompanionRequest request);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @GET("/api/patient/deletecompanion/{CompanionId}")
    Call<DeleteCompanionResponse> deleteCompanion(@Path("CompanionId") long companionId);

    @FormUrlEncoded
    @POST("api/Account/Register")
    Call<ResponseBody> createUser(
            @Field("Email") String email,
            @Field("Password") String password,
            @Field("ConfirmPassword") String confirmPassword,
            @Field("UserName") String emailSame,
            @Field("Number") String phone,
            @Field("Ad") String name,
            @Field("Soyad") String surname,
            @Field("dr_id") int doctorID
    );


    @FormUrlEncoded
    @POST("token")
    Call<LoginResponse> userLogin(
            @Field("grant_type") String grant_type,
            @Field("Username") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("api/values")
    Call<UserDataResponse> getUserData(
            @Field("Email") String email
    );

    @FormUrlEncoded
    @PUT("api/values")
    Call<ResponseBody> updateUser(
            @Field("Email") String email,
            @Field("Ad") String name,
            @Field("Soyad") String surname,
            @Field("PhoneNumber") String phoneNumber
    );


    @FormUrlEncoded
    @POST("api/Account/ChangePassword")
    Call<ResponseBody> updatePassword(
            @Field("OldPassword") String currentPassword,
            @Field("NewPassword") String newPassword,
            @Field("ConfirmPassword") String confirmPassword
    );

    @FormUrlEncoded
    @POST("api/hasta")
    Call<ResponseBody> insertData(
            @Field("ID") String ID,
            @Field("Name") String email,
            @Field("S1") float S1,
            @Field("S2") float S2,
            @Field("SP") int SP,
            @Field("Pa") int Pa,
            @Field("HR") int HR,
            @Field("DOJ") String DOJ
    );


    @FormUrlEncoded
    @POST("refreshtoken")
    Call<LoginResponse> refreshToken(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/info")
    Call<SetLocationStatusResponse> getUserLocationStatus(
            @Field("name") String email
    );


    @FormUrlEncoded
    @POST("api/locations")
    Call<ResponseBody> setUserHomeLocation(
            @Field("Name") String email,
            @Field("Lat_1") float lat1,
            @Field("Long_1") float long1,
            @Field("Lat_2") float lat2,
            @Field("Long_2") float long2
    );

    @FormUrlEncoded
    @PUT("api/locations")
    Call<ResponseBody> sendLocationData(
            @Field("Name") String email,
            @Field("Lat_1") float lat1,
            @Field("Long_1") float long1,
            @Field("Lat_2") float lat2,
            @Field("Long_2") float long2
    );

    @FormUrlEncoded
    @POST("api/uyari")
    Call<ResponseBody> sendWarning(
            @Field("Uyari_kisi") String email,
            @Field("Uyari_deger") String uyari
    );

    @FormUrlEncoded
    @POST("purchase")
    Call<InsertResponse> insertPurchase(
            @Field("user_id") long userID,
            @Field("order_id") String orderID,
            @Field("product_id") String productID,
            @Field("developer_payload") String developerPayload,
            @Field("purchase_time") String purchaseTime,
            @Field("purchase_token") String purchaseToken,
            @Field("signature") String signature,
            @Field("purchase_state") String purchaseState,
            @Field("status") boolean status
    );
}
