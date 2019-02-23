package com.suji.ish.suji.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ish
 */
public class WordModel {

    private static final String TAG = "WordModel";

    /**
     * 先去自己库查
     * 查不到单词就去金山查和自己例句库查
     * 最后将结果插回数据库
     *
     * @param spell
     */
    @SuppressLint("CheckResult")
    public void getWordFromInternet(final String spell) {
        final InternetEvent event = new InternetEvent();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.sujiApi)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);

        //本地查词
        Observable<SujiJsonBean<Word>> observable0 = Observable.create(new ObservableOnSubscribe<SujiJsonBean<Word>>() {
            @Override
            public void subscribe(ObservableEmitter<SujiJsonBean<Word>> emitter) throws Exception {
                Word word = getWordWithSpell(spell);
                if (word != null) {
                    Log.d(TAG, "在本地数据库中查到单词" + spell);
                    SujiJsonBean<Word> sujiJsonBean = new SujiJsonBean<Word>();
                    sujiJsonBean.setCode(InternetEvent.SUCESS);
                    sujiJsonBean.setResult(word);
                    emitter.onNext(sujiJsonBean);
                } else {
                    emitter.onComplete();
                }
            }
        });

        //自己云端数据库表1查词
        Observable<SujiJsonBean<Word>> observable1 = wordService.getInSujiDb(spell);
        //金山查词
        final Observable<WordJson> observable2 = wordService.getJinShanWord("json", spell, Api.jinShanKey);
        final Observable<SujiJsonBean<String>> observable3 = wordService.getSentenceInSujiDb(spell);
        //金山接口和自己接口合并
        final Observable<Word> observableZip = Observable.zip(observable2, observable3, new BiFunction<WordJson, SujiJsonBean<String>, Word>() {
            @Override
            public Word apply(WordJson wordJson, SujiJsonBean<String> sujiJsonBean) throws Exception {
                Word word = new Word(wordJson);
                word.setSentence(sujiJsonBean.getResult());
                return word;
            }
        });


        Observable.concat(observable0, observable1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<SujiJsonBean<Word>>() {
                    @Override
                    public void accept(SujiJsonBean<Word> wordSujiJsonBean) throws Exception {
                        if (wordSujiJsonBean.getCode() == InternetEvent.SUCESS) {
                            Log.d(TAG, "ob1 sucess");
                            Word word = wordSujiJsonBean.getResult();
                            event.setCode(InternetEvent.SUCESS);
                            event.setMessage("success");
                            event.setWord(word);
                            InternetRxBus.getInstance().post(event);
                        } else {
                            Log.d(TAG, "ob1 fail");
                        }
                    }
                })

                //请求金山接口
                .observeOn(Schedulers.io())
                .flatMap(new Function<SujiJsonBean<Word>, ObservableSource<Word>>() {
                    @Override
                    public ObservableSource<Word> apply(SujiJsonBean<Word> wordSujiJsonBean) throws Exception {
                        if (wordSujiJsonBean.getCode() == 1) {
                            //不再进行后续请求
                            return Observable.empty();
                        }
                        return observableZip;
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Word>() {
                    @Override
                    public void accept(Word word) throws Exception {
                        event.setCode(InternetEvent.SUCESS);
                        event.setWord(word);
                        Log.d(TAG, "merge word: sucess!" + word.getSentence());
                        //新词插回自己服务器
                        insertInSujiDb(word);
                        InternetRxBus.getInstance().post(event);
                    }
                });
    }

    /**
     * 通过Rxbus来传递查询结果
     *
     * @param spell
     */
    public void getWordFromInternet2(final String spell) {

//        final InternetEvent eventMsg = new InternetEvent();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api.sujiApi)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        WordService wordService = retrofit.create(WordService.class);
//
//        DisposableObserver observer = new DisposableObserver<SujiJsonBean<Word>>() {
//
//            @Override
//            public void onNext(SujiJsonBean wordSujiJsonBean) {
//                SujiJsonBean wordJson = wordSujiJsonBean;
//                //查不到，去金山查
//                if (wordJson.getCode() == 0) {
//                    getWordFromJinShan(spell);
//                } else {
//                    SujiJsonBean<Word> jsonBean = wordSujiJsonBean;
//                    mWord = jsonBean.getResult();
//                    eventMsg.setCode(1);
//                    eventMsg.setMessage("success");
//                    eventMsg.setWord(mWord);
//                    InternetRxBus.getInstance().post(eventMsg);
//                    Log.d(TAG, "getWordFromInternet: " + mWord.getParts());
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//
//        wordService.getInSujiDb(spell)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);

        //retrofit 先去自己库搜索
//        Call<SujiJsonBean<Word>> call = wordService.getInSujiDb(spell);
//        call.enqueue(new Callback<SujiJsonBean<Word>>() {
//            @Override
//            public void onResponse(Call<SujiJsonBean<Word>> call, Response<SujiJsonBean<Word>> response) {
//                SujiJsonBean wordJson = response.body();
//                //查不到，去金山查
//                if (wordJson.getCode() == 0) {
//                    getWordFromJinShan(spell);
//                } else {
//                    SujiJsonBean<Word> jsonBean = response.body();
//                    mWord = jsonBean.getResult();
//                    eventMsg.setCode(1);
//                    eventMsg.setMessage("success");
//                    eventMsg.setWord(mWord);
//                    InternetRxBus.getInstance().post(eventMsg);
//                    Log.d(TAG, "getWordFromInternet: " + mWord.getParts());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SujiJsonBean<Word>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

    /**
     * 去金山查
     *
     * @param spell
     */
    public void getWordFromJinShan(String spell) {
//        final InternetEvent eventMsg = new InternetEvent();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api.jinShanSearch)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        WordService wordService = retrofit.create(WordService.class);
//        Call<WordJson> call = wordService.getJinShanWord("json", spell, Api.jinShanKey);
//
//        call.enqueue(new Callback<WordJson>() {
//            @Override
//            public void onResponse(Call<WordJson> call, Response<WordJson> response) {
//                WordJson wordJson = response.body();
//                mWord = new Word(wordJson);
//                getWordSentence();
//            }
//
//            @Override
//            public void onFailure(Call<WordJson> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

    /**
     * 去自己词库查例句
     */
    public void getWordSentence() {
//        final InternetEvent eventMsg = new InternetEvent();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api.sujiApi)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        WordService wordService = retrofit.create(WordService.class);
//
//        //搜索例句
//        Call<SujiJsonBean<String>> call = wordService.getSentenceInSujiDb(mWord.getSpell());
//        call.enqueue(new Callback<SujiJsonBean<String>>() {
//            @Override
//            public void onResponse(Call<SujiJsonBean<String>> call, Response<SujiJsonBean<String>> response) {
//                SujiJsonBean<String> sentenceResult = response.body();
//                String sentence = sentenceResult.getResult();
//                //例句查不到就为空
//                mWord.setSentence(sentence);
//                eventMsg.setCode(InternetEvent.SUCESS);
//                eventMsg.setWord(mWord);
//                Log.d(TAG, "merge word: sucess!" + mWord.getSentence());
//
//                //新词插回自己服务器
//                insertInSujiDb();
//                InternetRxBus.getInstance().post(eventMsg);
//            }
//
//            @Override
//            public void onFailure(Call<SujiJsonBean<String>> call, Throwable t) {
//
//            }
//
//        });
    }

    /**
     * 插回服务器数据库
     */
    public void insertInSujiDb(final Word word) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.sujiApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WordService wordService = retrofit.create(WordService.class);

        Call<SujiJsonBean<Integer>> call = wordService.insertInDb(word.getSpell(), word.getWordPast(),
                word.getWordDone(), word.getWordIng(), word.getWordPl(), word.getWordThird(),
                word.getWordEr(), word.getWordEst(), word.getPhEn(), word.getPhAm(), word.getPhOther(),
                word.getPhEnMp3(), word.getPhAmMp3(), word.getPhTtsMp3(), word.getParts(),
                word.getSentence());


        Log.d(TAG, "insertInSujiDb: " + word.getParts());
        call.enqueue(new Callback<SujiJsonBean<Integer>>() {
            @Override
            public void onResponse(Call<SujiJsonBean<Integer>> call, Response<SujiJsonBean<Integer>> response) {
                SujiJsonBean<Integer> result = response.body();
                if (result.getCode() == 1) {
                    int dbId = result.getResult();
                    word.setDbId(dbId);
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
        //先设置一次id 如果笔记本为空bookId会为0
        word.setBookId(noteBook.getId());

        long time = ToolsUtils.getInstance().getInstanceTime();
        String timeStr = ToolsUtils.getInstance().getDateFormat(time);
        word.setAddTime(time);
        word.setAddTimeString(timeStr);
        //初始化记忆参数
        word.initMemoInfo();

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

        //如果目前没有笔记本，那么得等到新建笔记本插入后再保存笔记，id才是对的
        word.setBookId(noteBook.getId());
        word.save();

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

    /**
     * 获取记忆词汇
     *
     * @return
     */
    public long getMinRememberDate() {
        return LitePal.min(Word.class, "nextTime", long.class);
    }

    public long getMaxRememberDate() {
        return LitePal.max(Word.class, "nextTime", long.class);
    }

    /**
     * 获取指定长度的单词，按计划时间逆序来排行
     * 取出的单词为已经背过单词
     *
     * @param size
     * @param currentTime
     * @return
     */
    public List<Word> getMemoWords(int size, String currentTime, int offset) {
        return LitePal
                //今天没有背过并且，
                .where("updateTimeStr is not ? and updateTimeStr is not null", currentTime)
                .order("nextTime")
                .limit(size)
                .offset(offset)
                .find(Word.class);
    }

    /**
     * 获取没背过的单词
     *
     * @param size
     * @return
     */
    public List<Word> getUnRememberWord(int size) {
        return LitePal
                .where("updateTimeStr is null")
                .order("nextTime")
                .limit(size)
                .find(Word.class);
    }


    public void update(Word word) {
        word.save();
    }

    public int getWordNum() {
        return LitePal.count(Word.class);
    }

    /**
     * 还未记忆过的单词数目
     *
     * @return
     */
    public int getUnRememberWordNum() {
        return LitePal.where("updateTimeStr is null").count(Word.class);
    }

    /**
     * 记过但是rate为0单词数
     *
     * @return
     */
    public int getUnClearWordNum() {
        int zeroNum = LitePal.where("rate = 0").count(Word.class);
        return zeroNum - getUnRememberWordNum();
    }

    public Word getWordWithSpell(String spell) {
        return LitePal.where("spell = ?", spell).findFirst(Word.class);
    }

    public int getKnowWordNum() {
        return LitePal.where("rate > 0 and rate < 4").count(Word.class);
    }

    public int getFamiliarWordNum() {
        return LitePal.where("rate >= 4").count(Word.class);
    }
}
