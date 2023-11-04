package com.example.titulaundry.API;

import com.example.titulaundry.Model.CheckEmail;
import com.example.titulaundry.Model.ResponeBarang;
import com.example.titulaundry.Model.ResponseAlamat;
import com.example.titulaundry.Model.ResponseAlamatLain;
import com.example.titulaundry.Model.ResponseBanner;
import com.example.titulaundry.Model.ResponseCod;
import com.example.titulaundry.Model.ResponseEditUser;
import com.example.titulaundry.Model.ResponseEmail;
import com.example.titulaundry.Model.ResponseHapusFoto;
import com.example.titulaundry.Model.ResponseImg;
import com.example.titulaundry.Model.ResponseInsertPesanan;
import com.example.titulaundry.Model.ResponseLogin;
import com.example.titulaundry.Model.ResponsePesanan;
import com.example.titulaundry.Model.ResponsePesananUser;
import com.example.titulaundry.Model.ResponseRegister;
import com.example.titulaundry.Model.ResponseRubahPw;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.Model.ResponseVoucher;
import com.example.titulaundry.Model.SemuaPesanan;
import com.example.titulaundry.Model.UpdatePassword;
import com.example.titulaundry.Model.VerifEmail;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login2.php")
    Call<ResponseLogin> loginResponse(
            @Field("email") String email,
            @Field("password") String pass
    );
    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseRegister> registerResponse(
            @Field("nama") String nama,
            @Field("no_telpon") String telp,
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("DataVerif_Email.php")
    Call<ResponseEmail> getVerifEmail(
            @Field("id_user") String id_user
    );
    @FormUrlEncoded
    @POST("UpdateEmail.php")
    Call<VerifEmail> setUpdateEmail(
            @Field("id_user") String id_user
    );
    @FormUrlEncoded
    @POST("CheckEmail.php")
    Call<CheckEmail> getCheckEmail(
            @Field("email") String gmail
    );
    @FormUrlEncoded
    @POST("LupaPassword.php")
    Call<UpdatePassword> setNewPassword(
            @Field("email") String gmail,
            @Field("password") String pass
    );
    @FormUrlEncoded
    @POST("SetAlamat.php")
    Call<ResponseAlamat> setAlamat(
            @Field("id_user") String user,
            @Field("alamat") String alamat
    );
    @FormUrlEncoded
    @POST("AddAlamatLain.php")
    Call<ResponseAlamat> TambahAlamat(
            @Field("id_user") String user,
            @Field("alamat") String alamat
    );
    @FormUrlEncoded
    @POST("EditAlamatUtama.php")
    Call<ResponseAlamat> EditAlamatUtama(
            @Field("id_alamat") String id_alamat,
            @Field("alamat") String alamat
    );
    @FormUrlEncoded
    @POST("DeleteAddress.php")
    Call<ResponseAlamat> DeleteAlamat(
            @Field("id_alamat") String id_alamat
    );
    @GET("GetMenu.php")
    Call<ResponeBarang> getRetrive();

    @GET("Voucher.php")
    Call<ResponseVoucher> getVoucher();

    @GET("Banner.php")
    Call<ResponseBanner> getBanner();

    @FormUrlEncoded
    @POST("GetPesanan.php")
    Call<ResponsePesanan> getPesanan(
            @Field("id_user") String id_user
    );

    @FormUrlEncoded
    @POST("DataUser.php")
    Call<ResponseUser> getDataUser(
            @Field("id_user") String id_user
    );

    @FormUrlEncoded
    @POST("InsertPesanan.php")
    Call<ResponseInsertPesanan> getDataPesanan(
            @Field("total_berat") String total_berat,
            @Field("total_harga") String total_harga,
            @Field("harga_diskon") String harga_diskon,
            @Field("waktu_penjemputan") String waktu_penjemputan,
            @Field("waktu_antar") String waktu_antar,
            @Field("id_voucher") String id_voucher,
            @Field("id_jasa") String id_jasa,
            @Field("id_user") String id_user,
            @Field("alamat_penjemputan") String alamat_penjemputan,
            @Field("alamat_pengiriman") String alamat_pengiriman
    );
    @FormUrlEncoded
    @POST("PesananSemua.php")
    Call<SemuaPesanan> getPesananSemua(
            @Field("id_user") String id_user
    );
    @FormUrlEncoded
    @POST("PesananSaatIni.php")
    Call<SemuaPesanan> getPesananSaatIni(
            @Field("id_user") String id_user
    );
    @FormUrlEncoded
    @POST("PesananSelesai.php")
    Call<SemuaPesanan> getPesananSelesai(
            @Field("id_user") String id_user
    );

    @Multipart
    @POST("UpImage.php")
    Call<ResponseImg> uploadImage (
            @Part MultipartBody.Part image,
            @Part("id_user")RequestBody idUser,
            @Part("nama")RequestBody nama,
            @Part("email")RequestBody email,
            @Part("no_telpon")RequestBody no_telpon
            );

    @FormUrlEncoded
    @POST("editDataSaja.php")
    Call<ResponseEditUser> getUpdateDataUser(
            @Field("id_user") String id_user,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("no_telpon") String no_telpon
    );

    @FormUrlEncoded
    @POST("DeleteImage.php")
    Call<ResponseHapusFoto> hapusFoto(
            @Field("id_user") String id_user
    );

    @FormUrlEncoded
    @POST("RubahPassword.php")
    Call<ResponseRubahPw> UbahPw(
            @Field("id_user") String id_user,
            @Field("password") String password,
            @Field("password2") String password2
    );

    @FormUrlEncoded
    @POST("DataPesananUser.php")
    Call<ResponsePesananUser> getDataPesanan(
            @Field("id_pesanan") String id_pesanan
    );

    @FormUrlEncoded
    @POST("PesananCod.php")
    Call<ResponseCod> getCod(
            @Field("id_pesanan") String id_pesanan,
            @Field("waktu_penjemputan") String waktu
    );

    @FormUrlEncoded
    @POST("PesananSemua_Cari.php")
    Call<SemuaPesanan> getCariPesananSemua(
            @Field("id_user") String id_user,
            @Field("tanggal1") String tanggal_mulai,
            @Field("tanggal2") String tanggal_akhir
    );

    @FormUrlEncoded
    @POST("PesananSaatIni_Cari.php")
    Call<SemuaPesanan> getCariPesananSaatIni(
            @Field("id_user") String id_user,
            @Field("tanggal1") String tanggal_mulai,
            @Field("tanggal2") String tanggal_akhir
    );
    @FormUrlEncoded
    @POST("PesananSelesai_Cari.php")
    Call<SemuaPesanan> getCariPesananSelesai(
            @Field("id_user") String id_user,
            @Field("tanggal1") String tanggal_mulai,
            @Field("tanggal2") String tanggal_akhir
    );

    @FormUrlEncoded
    @POST("Pilih_alamat.php")
    Call<ResponseAlamatLain> getAlamatKirim(
            @Field("id_user") String id_user
    );


}
