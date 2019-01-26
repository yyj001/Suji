package com.suji.ish.suji.network;

import com.suji.ish.suji.json.SujiJsonBean;
import com.suji.ish.suji.json.WordJson;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WordService {

    @FormUrlEncoded
    @POST("search_word.php")
    Call<SujiJsonBean> getInSujiDb(@Field("word") String word);

    @GET("dictionary.php")
    Call<WordJson> getJinShanWord(@Query("type") String type, @Query("w")String word,@Query("key")String key);

    @FormUrlEncoded
    @POST("get_sentence.php")
    Call<SujiJsonBean> getSentenceInSujiDb(@Field("word") String word);

}
