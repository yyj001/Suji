package com.suji.ish.suji.network;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.json.SujiJsonBean;
import com.suji.ish.suji.json.WordJson;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Retrofit对单词调用api
 */
public interface WordService {

    @FormUrlEncoded
    @POST("search_word.php")
    Call<SujiJsonBean<Word>> getInSujiDb(@Field("word") String word);

    @GET("dictionary.php")
    Call<WordJson> getJinShanWord(@Query("type") String type, @Query("w")String word,@Query("key")String key);

    @FormUrlEncoded
    @POST("get_sentence.php")
    Call<SujiJsonBean> getSentenceInSujiDb(@Field("word") String word);

    @FormUrlEncoded
    @POST("insert_to_dic.php")
    Call<SujiJsonBean<Integer>> insertInDb(@Field("spell") String spell,@Field("wordPast") String wordPast,
                                           @Field("wordDone") String wordDone,@Field("wordIng") String wordIng,
                                           @Field("wordPl") String wordPl,@Field("wordThird") String wordThird,
                                           @Field("wordEr") String wordEr,@Field("wordEst") String wordEst,
                                           @Field("phEn") String phEn,@Field("phAm") String phAm,
                                           @Field("phOther") String phOther,@Field("phEnMp3") String phEnMp3,
                                           @Field("phAmMp3") String phAmMp3,@Field("phTtsMp3") String phTtsMp3,
                                           @Field("parts") String parts,@Field("sentence") String sentence);
}
