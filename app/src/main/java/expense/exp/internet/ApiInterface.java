package expense.exp.internet;

/**
 * Created by admin on 10-07-2018.
 */

import expense.exp.internet.model.AdDetailsRes;
import expense.exp.internet.model.CategoriesRes;
import expense.exp.internet.model.GetAccountant;
import expense.exp.internet.model.GetAds;
import expense.exp.internet.model.GetFiles;
import expense.exp.internet.model.GetFolders;
import expense.exp.internet.model.Login;
import expense.exp.internet.model.SearchAccountantDataModel;
import expense.exp.internet.model.SearchDocumentDataModel;
import expense.exp.internet.model.SearchFolderDataModel;
import expense.exp.internet.model.Status;
import expense.exp.internet.model.Status_model;
import expense.exp.internet.model.SubCategoriesRes;
import expense.exp.internet.model.UserInfo;
import expense.exp.model_class.AdsPackageResponse;
import expense.exp.model_class.CollegeResponse.CollegeProgramListResponse;
import expense.exp.model_class.Delete_Folfer;
import expense.exp.model_class.Delete_Multi_Folder;
import expense.exp.model_class.MoveFolder;

import java.util.List;

import expense.exp.model_class.MoveMultiDoc;
import expense.exp.model_class.NotificationListResponse;
import expense.exp.model_class.PlanListResponse;
import expense.exp.model_class.UserIfo;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {


    @GET("Registerme")
    Single<Status> userRegistration(@Query("email") String email, @Query("password") String password, @Query("type") String type, @Query("code") String code);

    @FormUrlEncoded
    @POST("loginme")
    Single<Login> userLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("ForgotMyPass")
    Single<Status> userForgotPass(@Field("email") String email);


    @POST("Profile?userid=")
    Single<UserInfo> getuserInfo(@Query("userid") String userid);

    @POST("Profile?userid=")
    Single<UserIfo> getuserIfo(@Query("userid") String userid);


    @GET("MyFolders")
    Single<GetFolders> getFolders(@Query("user_id") int userid, @Query("year") String selected_year);

    @GET("GetPPlansList")
    Single<PlanListResponse> getPlans(@Query("user_type") String user_type,@Query("user_id") String user_id);

    @GET("GetNotifications")
    Single<NotificationListResponse> getNotifications(@Query("email") String email);

    @FormUrlEncoded
    @POST("AddFolder")
    Single<Status_model> addFolders(@Field("parent_id") int parentid,@Field("user_id") int userid, @Field("foldername") String folder_name);

    @GET("AccList")
    Single<GetAccountant> getAccountant(@Query("user_id") int id);

    @FormUrlEncoded
    @POST("AssignUnAsAcc")
    Single<Status> assignUnAsAcc(@Field("user_id") int userid, @Field("accountant_id") int accountant_id, @Field("purpose") String purpose);


    @FormUrlEncoded
    @POST("FolderDocs")
    Single<GetFiles> getFiles(@Field("folderId") String folderId, @Field("year") String year);

    @FormUrlEncoded
    @POST("RenameFolder")
    Single<Status> renameFile(@Field("user_id") String userId, @Field("folder_id") String folderId, @Field("newfoldername") String newName);


    @FormUrlEncoded
    @POST("RecentDocs")
    Single<GetFiles> getRecentDocs(@Field("user_id") int user_Id, @Field("limit") String limit);

    @Multipart
    @POST("UploadDoc")
    Single<Status> uploadFile(@Query("doc_owner_id") int doc_owner_id,
                              @Query("doc_folder_id") String doc_folder_id,
                              @Query("savedocas") int savedocas,
                              @Query("cost") String cost,
                              @Query("doc_name") String doc_name,
                              @Query("year") String year,
                              @Query("doc_type") String type,
                              @Part List<MultipartBody.Part> file,
                              @Part MultipartBody.Part signature);

    @Multipart
    @POST("UpdateProfile")
    Single<Status> updateProfile(@Part("user_id") RequestBody user_id,
                                 @Part("email") RequestBody email,
                                 @Part("name") RequestBody name,
                                 @Part("province") RequestBody province,
                                 @Part("mobile") RequestBody mobile,
                                 @Part("college") RequestBody college,
                                 @Part("year") RequestBody year,
                                 @Part("program") RequestBody program,
                                 @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("Changepass")
    Single<Status> changepass(@Field("user_id") int userid, @Field("oldpass") String oldpass, @Field("newpass") String newpass);


    @GET("SearchEM")
    Single<SearchFolderDataModel> searching(@Query("searchtxt") String searchtxt, @Query("type") String type, @Query("user_id") int user_id);


    @GET("SearchEM")
    Single<SearchAccountantDataModel> accountantsearching(@Query("searchtxt") String searchtxt, @Query("type") String type, @Query("user_id") int user_id);


    @GET("SearchEM")
    Single<SearchDocumentDataModel> documentsearching(@Query("searchtxt") String searchtxt, @Query("type") String type, @Query("user_id") int user_id);

    @GET("DeleteDoc")
    Single<SearchDocumentDataModel> deleteDoc(@Query("DocId") String DocId, @Query("doc_owner_id") String doc_owner_id);


    @FormUrlEncoded
    @POST("AssignUnAsFolder")
    Single<Status> AssignUnAsFolder(@Field("user_id") int userid, @Field("accountant_id") int accountant_id, @Field("folder_id") int folder_id, @Field("purpose") String purpose);


    @GET("GetNotificationsCount")
    Single<Status> getNotificationCount(@Query("email") String email);


    @FormUrlEncoded
    @POST("MoveDoc")
    Single<MoveFolder> MoveDoc(@Field("DocId") String DocId, @Field("doc_owner_id") String doc_owner_id, @Field("TargetFolderId") String folder_id);

    @FormUrlEncoded
    @POST("DeleteFolder")
    Single<Delete_Folfer> DeleteFolder(@Field("FolderId") String FolderId, @Field("Folder_owner_id") String Folder_owner_id);

    @FormUrlEncoded
    @POST("DeleteMulDocs")
    Single<Delete_Multi_Folder> DeleteMultipleDoc(@Field("DocId") String DocId, @Field("Doc_owner_id") String Doc_owner_id);

    @FormUrlEncoded
    @POST("MoveMulDocs")
    Single<MoveMultiDoc> MoveMultipleDoc(@Field("DocId") String DocId, @Field("Doc_owner_id") String Doc_owner_id,@Field("TargetFolderId") String TargetFolderId);

    @FormUrlEncoded
    @POST("GetColCorList")
    Single<CollegeProgramListResponse> CollProgList(@Field("field") String field);

    @GET("GetAdsPlansList")
    Single<AdsPackageResponse> getAdsPackages(@Query("user_id") String user_id);

    @GET("GetAdsList")
    Single<GetAds> getAdsList(@Query("user_id") int userid);
    @GET("DeleteUser")
    Single<Delete_Folfer> getDeleteUser(@Query("userid") int userid);

    @GET("GetAdDetails")
    Single<AdDetailsRes> getAdDetails(@Query("ad_id") String adid);

    @GET("PostAd")
    Single<CategoriesRes> getAdCategories();

    @GET("AdSubCat")
    Single<SubCategoriesRes> getAdSuCategories(@Query("category_id") String catId);

    @Multipart
    @POST("SaveAd")
    Single<Status> postAd(@Part("user_id") RequestBody userId,
                          @Part("title") RequestBody title,
                          @Part("category_id") RequestBody catId,
                          @Part("subcategory_id") RequestBody subCatId,
                          @Part("url") RequestBody url,
                          @Part("contact") RequestBody contact,
                          @Part("address") RequestBody address,
                          @Part("status") RequestBody status,
                          @Part MultipartBody.Part file,
                          @Part("city") RequestBody city,
                          @Part("pincode") RequestBody pincode,
                          @Part("province") RequestBody province);

    @Multipart
    @POST("UpdateAd")
    Single<Status> updateAd(@Part("ad_id") RequestBody adId,
                            @Part("title") RequestBody title,
                            @Part("user_id") RequestBody userId,
                            @Part("category_id") RequestBody catId,
                            @Part("subcategory_id") RequestBody subCatId,
                            @Part("url") RequestBody url,
                            @Part MultipartBody.Part file,
                            @Part("contact") RequestBody contact,
                            @Part("address") RequestBody address,
                            @Part("status") RequestBody status,
                            @Part("city") RequestBody city,
                            @Part("pincode") RequestBody pincode,
                            @Part("province") RequestBody province);

}
