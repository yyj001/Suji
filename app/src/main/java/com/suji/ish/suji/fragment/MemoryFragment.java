package com.suji.ish.suji.fragment;


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
import com.suji.ish.suji.json.WordJson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemoryFragment extends Fragment {

    private static String TAG = "MemoryFragment";

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

    private void initView(View view){
        Button btn = view.findViewById(R.id.test_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigation.createNavigateOnClickListener(R.id.action_memoryFragment_to_testFragment);
               toJson();
            }
        });
    }
    private void toJson(){
        String jsonStr = Api.json;
        Gson gson = new Gson();
        WordJson wordJson = gson.fromJson(jsonStr,WordJson.class);
        Word word = new Word(wordJson);
        Log.d(TAG, "toJson: " + word.getSpell() +"\n" + word.getWordEr()+"\n" + word.getWordIng() +"\n"+ word.getPhEn() + "\n" + word.getSentence() );
    }

    public void post(){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("word", "apple")
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.64.2:8080/suji/get_word_detail.php")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }
}
