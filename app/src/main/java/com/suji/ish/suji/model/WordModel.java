package com.suji.ish.suji.model;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.global.Api;
import com.suji.ish.suji.json.SujiJsonBean;
import com.suji.ish.suji.json.WordJson;
import com.suji.ish.suji.network.WordService;
import com.suji.ish.suji.rxjava.InternetEvent;
import com.suji.ish.suji.rxjava.InternetRxBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordModel {

    //返回用的word
    private Word mWord;
    /**
     * 通过Rxbus来传递查询结果
     * @param spell
     */
    public void getWordFromInternet(final String spell) {

        final InternetEvent eventMsg = new InternetEvent();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.sujiApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);

        //先去自己库搜索
        Call<SujiJsonBean> call = wordService.getInSujiDb(spell);
        call.enqueue(new Callback<SujiJsonBean>() {
            @Override
            public void onResponse(Call<SujiJsonBean> call, Response<SujiJsonBean> response) {
                SujiJsonBean wordJson = response.body();
                //查不到，去金山查
                if(wordJson.getCode()==0){
                    getWordFromJinShan(spell);
                }else{
                    SujiJsonBean<Word> jsonBean= response.body();
                    mWord = jsonBean.getResult();
                    eventMsg.setCode(1);
                    eventMsg.setMessage("success");
                    eventMsg.setWord(mWord);
                    InternetRxBus.getInstance().post(eventMsg);
                }
            }
            @Override
            public void onFailure(Call<SujiJsonBean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 去金山查
     * @param spell
     */
    public void getWordFromJinShan(String spell){
        final InternetEvent eventMsg = new InternetEvent();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.jinShanSearch)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);
        Call<WordJson> call = wordService.getJinShanWord("json", spell, Api.jinShanKey);

        call.enqueue(new Callback<WordJson>() {
            @Override
            public void onResponse(Call<WordJson> call, Response<WordJson> response) {
                WordJson wordJson = response.body();
                mWord = new Word(wordJson);
                getWordSentence();
            }

            @Override
            public void onFailure(Call<WordJson> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 去自己词库查例句
     */
    public void getWordSentence(){
        final InternetEvent eventMsg = new InternetEvent();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.sujiApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);

        //搜索例句
        Call<SujiJsonBean> call = wordService.getSentenceInSujiDb(mWord.getSpell());
        call.enqueue(new Callback<SujiJsonBean>() {
            @Override
            public void onResponse(Call<SujiJsonBean> call, Response<SujiJsonBean> response) {
                SujiJsonBean<String> sentenceResult = response.body();
                String sentence = sentenceResult.getResult();
                mWord.setSentence(sentence);
                eventMsg.setCode(InternetEvent.SUCESS);
                eventMsg.setWord(mWord);
                InternetRxBus.getInstance().post(eventMsg);
            }

            @Override
            public void onFailure(Call<SujiJsonBean> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
