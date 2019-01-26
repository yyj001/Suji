package com.suji.ish.suji.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.global.Api;
import com.suji.ish.suji.json.SujiJsonBean;
import com.suji.ish.suji.json.WordJson;
import com.suji.ish.suji.network.WordService;
import com.suji.ish.suji.viewmodel.MemoryViewModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemoryFragment extends Fragment {

    private static String TAG = "MemoryFragment";

    private MemoryViewModel mViewModel;
    public MemoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memory, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);


        Button btn = view.findViewById(R.id.test_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mViewModel.loadData();
            }
        });
    }

    private void toJson() {
        String jsonStr = Api.json;
        Gson gson = new Gson();
        WordJson wordJson = gson.fromJson(jsonStr, WordJson.class);
        Word word = new Word(wordJson);
        Log.d(TAG, "toJson: " + word.getSpell() + "\n" + word.getWordEr() + "\n" + word.getWordIng() + "\n" + word.getPhEn() + "\n" + word.getSentence());
    }

//    public void post(){
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder()
//                .add("word", "apple")
//                .build();
//        Request request = new Request.Builder()
//                .url("http://192.168.64.2:8080/suji/get_word_detail.php")
//                .post(requestBody)
//                .build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
//                Headers headers = response.headers();
//                for (int i = 0; i < headers.size(); i++) {
//                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
//                }
//                Log.d(TAG, "onResponse: " + response.body().string());
//            }
//        });
//    }


    public void searchnSujiDb() {
        final String word = "super";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.sujiApi)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WordService wordService = retrofit.create(WordService.class);
                Call<SujiJsonBean> call = wordService.getInSujiDb(word);
                try {
                    //第一次请求
                    Response<SujiJsonBean> response = call.execute();
                    if(response.body().getCode()==1){
                    }else{
                        getJinShanWord(word);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        new Thread(runnable).start();
    }

    public void getJinShanWord(String word) {
        String BASE_URL = Api.jinShanSearch;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);
        Call<WordJson> call = wordService.getJinShanWord("json", word, Api.jinShanKey);

        call.enqueue(new Callback<WordJson>() {
            @Override
            public void onResponse(Call<WordJson> call, Response<WordJson> response) {
                WordJson wordJson = response.body();
                Word w = new Word(wordJson);
                Log.d(TAG, "onResponse: " + w.getParts());
            }

            @Override
            public void onFailure(Call<WordJson> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
