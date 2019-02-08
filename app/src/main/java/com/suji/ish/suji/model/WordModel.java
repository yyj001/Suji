package com.suji.ish.suji.model;

import android.util.Log;

import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.global.Api;
import com.suji.ish.suji.json.SujiJsonBean;
import com.suji.ish.suji.json.WordJson;
import com.suji.ish.suji.network.WordService;
import com.suji.ish.suji.rxjava.DataBaseEvent;
import com.suji.ish.suji.rxjava.InternetEvent;
import com.suji.ish.suji.rxjava.InternetRxBus;
import com.suji.ish.suji.utils.ToolsUtils;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordModel {

    //返回用的word
    private Word mWord;

    private static final String TAG = "WordModel";

    /**
     * 通过Rxbus来传递查询结果
     *
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
        Call<SujiJsonBean<Word>> call = wordService.getInSujiDb(spell);
        call.enqueue(new Callback<SujiJsonBean<Word>>() {
            @Override
            public void onResponse(Call<SujiJsonBean<Word>> call, Response<SujiJsonBean<Word>> response) {
                SujiJsonBean wordJson = response.body();
                //查不到，去金山查
                if (wordJson.getCode() == 0) {
                    getWordFromJinShan(spell);
                } else {
                    SujiJsonBean<Word> jsonBean = response.body();
                    mWord = jsonBean.getResult();
                    eventMsg.setCode(1);
                    eventMsg.setMessage("success");
                    eventMsg.setWord(mWord);
                    InternetRxBus.getInstance().post(eventMsg);
                    Log.d(TAG, "getWordFromInternet: " + mWord.getParts());
                }
            }

            @Override
            public void onFailure(Call<SujiJsonBean<Word>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 去金山查
     *
     * @param spell
     */
    public void getWordFromJinShan(String spell) {
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
    public void getWordSentence() {
        final InternetEvent eventMsg = new InternetEvent();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.sujiApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);

        //搜索例句
        Call<SujiJsonBean<String>> call = wordService.getSentenceInSujiDb(mWord.getSpell());
        call.enqueue(new Callback<SujiJsonBean<String>>() {
            @Override
            public void onResponse(Call<SujiJsonBean<String>> call, Response<SujiJsonBean<String>> response) {
                SujiJsonBean<String> sentenceResult = response.body();
                String sentence = sentenceResult.getResult();
                //例句查不到就为空
                mWord.setSentence(sentence);
                eventMsg.setCode(InternetEvent.SUCESS);
                eventMsg.setWord(mWord);
                Log.d(TAG, "merge word: sucess!" + mWord.getSentence());

                //新词插回自己服务器
                insertInSujiDb();
                InternetRxBus.getInstance().post(eventMsg);
            }

            @Override
            public void onFailure(Call<SujiJsonBean<String>> call, Throwable t) {

            }

        });
    }

    /**
     * 插回服务器数据库
     */
    public void insertInSujiDb() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.sujiApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);

        Call<SujiJsonBean<Integer>> call = wordService.insertInDb(mWord.getSpell(), mWord.getWordPast(),
                mWord.getWordDone(), mWord.getWordIng(), mWord.getWordPl(), mWord.getWordThird(),
                mWord.getWordEr(), mWord.getWordEst(), mWord.getPhEn(), mWord.getPhAm(), mWord.getPhOther(),
                mWord.getPhEnMp3(), mWord.getPhAmMp3(), mWord.getPhTtsMp3(), mWord.getParts(),
                mWord.getSentence());


        Log.d(TAG, "insertInSujiDb: " + mWord.getParts());
        call.enqueue(new Callback<SujiJsonBean<Integer>>() {
            @Override
            public void onResponse(Call<SujiJsonBean<Integer>> call, Response<SujiJsonBean<Integer>> response) {
                SujiJsonBean<Integer> result = response.body();
                if (result.getCode() == 1) {
                    int dbId = result.getResult();
                    mWord.setDbId(dbId);
                    Log.d(TAG, "insertInSujiDb: sucess!, dId=" + dbId);
                } else {
                    Log.d(TAG, "insertInSujiDb: fail!" + result.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SujiJsonBean<Integer>> call, Throwable t) {

            }
        });
    }

    /**
     * 本地数据库部分
     */

    public void insertWordToDb(Word word, NoteBook noteBook) {
        long time = ToolsUtils.getInstance().getInstanceTime();
        String timeStr = ToolsUtils.getInstance().getDateFormat(time);
        word.setAddTime(time);
        word.setAddTimeString(timeStr);
        //初始化记忆参数
        word.initMemoInfo();

        word.save();
        Log.d(TAG, "insertWordToDb: " + word.getId());

        NoteBookModel noteBookModel = new NoteBookModel();
        //当前还没有笔记本或者笔记本不存在
        NoteBook checkNoteBook = LitePal.find(NoteBook.class, word.getBookId());
        if (checkNoteBook == null) {
            noteBookModel.saveNoteBookMainThread(noteBook, DataBaseEvent.INSERT_SUCCESS, word);
        }
        //将笔记本数目+1
        noteBook.setEditTime(time);
        noteBook.setEditTimeString(timeStr);
        noteBook.setNoteNumber(noteBook.getNoteNumber() + 1);
        noteBookModel.saveNoteBookMainThread(noteBook, DataBaseEvent.ADD_WORD_SUCCESS, word);
    }

    /**
     * 获取该单词本所有单词
     *
     * @param bookId
     * @param callback
     */
    public void getAllWordForBook(int bookId, FindMultiCallback<Word> callback) {
        LitePal.where("bookId = ?", "" + bookId).findAsync(Word.class).listen(callback);
    }

    public void deleteByBookId(int id) {
        LitePal.deleteAll(Word.class, "bookId=?", id + "");
    }

    public Word getLocalWordById(int id) {
        return LitePal.find(Word.class, id);
    }

    public void deleteWord(Word word) {
        LitePal.delete(Word.class, word.getId());
        //让单词本数目减一
        new NoteBookModel().deleteWord(word);
    }
}
