package com.suji.ish.suji.bean;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.android.databinding.library.baseAdapters.BR;
import com.suji.ish.suji.json.WordJson;
import com.suji.ish.suji.utils.ToolsUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Word extends LitePalSupport implements Observable {
    private PropertyChangeRegistry mRegistry = new PropertyChangeRegistry();
    private int id;
    private int dbId; //网络id
    private int bookId;
    private long addTime;
    private String addTimeString;
    private int addType; // 是否为词库里面的单词 1是，0，不是

    private int rate;//设置记忆度0-5
    private long nextTime;//下一次出现的时间
    private float er; //记忆系数
    private String updateTimeStr; //用来判断一天之内是否有重复单词,只有点击熟悉后才会写回

    private String spell;
    private String wordPl; //复数
    private String wordPast; //过去式
    private String wordDone; //完成式
    private String wordIng; //
    private String wordThird; // 第三人称单数
    private String wordEr;
    private String wordEst;
    private String phEn;
    private String phAm;

    private String phOther;
    private String phEnMp3;
    private String phAmMp3;
    private String phTtsMp3;

    private String parts; // 词义，json字符串拼接，用 \n 分割数组

    private String sentence; // 例句


    @Column(ignore = true)
    public static final int TYPE_PERSONAL = 0;
    @Column(ignore = true)
    public static final int TYPE_AUTHORITY = 1;

    public Word() {
    }

    public Word(WordJson wordJson) {
        this.addTime = ToolsUtils.getInstance().getInstanceTime();
        this.addTimeString = ToolsUtils.getInstance().getDateFormat(addTime);

        this.spell = wordJson.getWord_name();

        if (wordJson.getExchange() == null) {
            return;
        }

        //金山词霸后台接口没有类型判断
        // TODO 变形是个数组，可能有多种变形，以后这里得改
        if ((wordJson.getExchange().getWord_pl() instanceof List)) {
            this.wordPl = ((List<String>) wordJson.getExchange().getWord_pl()).get(0);
        }
        if ((wordJson.getExchange().getWord_third() instanceof List)) {
            this.wordThird = ((List<String>) wordJson.getExchange().getWord_third()).get(0);
        }
        if ((wordJson.getExchange().getWord_past() instanceof List)) {
            this.wordPast = ((List<String>) wordJson.getExchange().getWord_past()).get(0);
        }
        if ((wordJson.getExchange().getWord_done() instanceof List)) {
            this.wordDone = ((List<String>) wordJson.getExchange().getWord_done()).get(0);
        }
        if ((wordJson.getExchange().getWord_ing() instanceof List)) {
            this.wordIng = ((List<String>) wordJson.getExchange().getWord_ing()).get(0);
        }
        if ((wordJson.getExchange().getWord_er() instanceof List)) {
            this.wordEr = ((List<String>) wordJson.getExchange().getWord_er()).get(0);
        }
        if ((wordJson.getExchange().getWord_est() instanceof List)) {
            this.wordEst = ((List<String>) wordJson.getExchange().getWord_est()).get(0);
        }

        if (wordJson.getSymbols().size() > 0) {
            this.phEn = wordJson.getSymbols().get(0).getPh_en();
            this.phAm = wordJson.getSymbols().get(0).getPh_am();
            this.phOther = wordJson.getSymbols().get(0).getPh_other();
            this.phEnMp3 = wordJson.getSymbols().get(0).getPh_en_mp3();
            this.phAmMp3 = wordJson.getSymbols().get(0).getPh_am_mp3();
            this.phTtsMp3 = wordJson.getSymbols().get(0).getPh_tts_mp3();
            String meaning = null;
            List<WordJson.SymbolsBean.PartsBean> parts = wordJson.getSymbols().get(0).getParts();
            if (parts.size() > 0) {
                meaning = "";
            }
            for (WordJson.SymbolsBean.PartsBean part : parts) {
                meaning += part.getPart();
                for (String m : part.getMeans()) {
                    meaning += m;
                    meaning += " ";
                }
                meaning += ";\n";
            }
            this.parts = meaning;
        }
    }

    public void initMemoInfo() {
        this.rate = 0;
        this.nextTime = ToolsUtils.getInstance().getInstanceTime();
        this.er = 2f;
    }

    @Bindable
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
        mRegistry.notifyChange(this, BR.bookId);
    }

    @Bindable
    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
        mRegistry.notifyChange(this, BR.addTime);
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    @Bindable
    public String getAddTimeString() {
        return addTimeString;
    }

    public void setAddTimeString(String addTimeString) {
        this.addTimeString = addTimeString;
        mRegistry.notifyChange(this, BR.addTimeString);
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        mRegistry.notifyChange(this, BR.id);
    }

    @Bindable
    public int getAddType() {
        return addType;
    }

    public void setAddType(int addType) {
        this.addType = addType;
        mRegistry.notifyChange(this, BR.addType);
    }

    @Bindable
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
        mRegistry.notifyChange(this, BR.rate);
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public float getEr() {
        return er;
    }

    public void setEr(float er) {
        this.er = er;
    }

    @Bindable
    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
        mRegistry.notifyChange(this, BR.dbId);
    }

    @Bindable
    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        if (spell != null && spell.length() == 0) {
            return;
        }
        this.spell = spell;
        mRegistry.notifyChange(this, BR.spell);
    }

    @Bindable
    public String getWordPl() {
        return wordPl;
    }

    public void setWordPl(String wordPl) {
        if (wordPl != null && wordPl.length() == 0) {
            return;
        }
        this.wordPl = wordPl;
        mRegistry.notifyChange(this, BR.wordPl);
    }

    @Bindable
    public String getWordPast() {
        return wordPast;
    }

    public void setWordPast(String wordPast) {
        if (wordPast != null && wordPast.length() == 0) {
            return;
        }
        this.wordPast = wordPast;
        mRegistry.notifyChange(this, BR.wordPast);
    }

    @Bindable
    public String getWordDone() {
        return wordDone;
    }

    public void setWordDone(String wordDone) {
        if (wordDone != null && wordDone.length() == 0) {
            return;
        }
        this.wordDone = wordDone;
        mRegistry.notifyChange(this, BR.wordDone);
    }

    @Bindable
    public String getWordIng() {
        return wordIng;
    }

    public void setWordIng(String wordIng) {
        if (wordIng != null && wordIng.length() == 0) {
            return;
        }
        this.wordIng = wordIng;
        mRegistry.notifyChange(this, BR.wordIng);
    }

    @Bindable
    public String getWordThird() {
        return wordThird;
    }

    public void setWordThird(String wordThird) {
        if (wordThird != null && wordThird.length() == 0) {
            return;
        }
        this.wordThird = wordThird;
        mRegistry.notifyChange(this, BR.wordThird);
    }

    @Bindable
    public String getWordEr() {
        return wordEr;
    }

    public void setWordEr(String wordEr) {
        if (wordEr != null && wordEr.length() == 0) {
            return;
        }
        this.wordEr = wordEr;
        mRegistry.notifyChange(this, BR.wordEr);
    }

    @Bindable
    public String getWordEst() {
        return wordEst;
    }

    public void setWordEst(String wordEst) {
        if (wordEst != null && wordEst.length() == 0) {
            return;
        }
        this.wordEst = wordEst;
        mRegistry.notifyChange(this, BR.wordEst);
    }

    @Bindable
    public String getPhEn() {
        return phEn;
    }

    public void setPhEn(String phEn) {
        if (phEn != null && phEn.length() == 0) {
            return;
        }
        this.phEn = phEn;
        mRegistry.notifyChange(this, BR.phEn);
    }

    @Bindable
    public String getPhAm() {
        return phAm;
    }

    public void setPhAm(String phAm) {
        if (phAm != null && phAm.length() == 0) {
            return;
        }
        this.phAm = phAm;
        mRegistry.notifyChange(this, BR.phAm);
    }

    @Bindable
    public String getPhOther() {
        return phOther;
    }

    public void setPhOther(String phOther) {
        if (phOther != null && phOther.length() == 0) {
            return;
        }
        this.phOther = phOther;
        mRegistry.notifyChange(this, BR.phOther);
    }

    @Bindable
    public String getPhEnMp3() {
        return phEnMp3;
    }

    public void setPhEnMp3(String phEnMp3) {
        if (phEnMp3 != null && phEnMp3.length() == 0) {
            return;
        }
        this.phEnMp3 = phEnMp3;
        mRegistry.notifyChange(this, BR.phEnMp3);
    }

    @Bindable
    public String getPhAmMp3() {
        return phAmMp3;
    }

    public void setPhAmMp3(String phAmMp3) {
        if (phAmMp3 != null && phAmMp3.length() == 0) {
            return;
        }
        this.phAmMp3 = phAmMp3;
        mRegistry.notifyChange(this, BR.phAmMp3);
    }

    @Bindable
    public String getPhTtsMp3() {
        return phTtsMp3;
    }

    public void setPhTtsMp3(String phTtsMp3) {
        if (phTtsMp3 != null && phTtsMp3.length() == 0) {
            return;
        }
        this.phTtsMp3 = phTtsMp3;
        mRegistry.notifyChange(this, BR.phTtsMp3);
    }

    @Bindable
    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        if (parts != null && parts.length() == 0) {
            return;
        }
        this.parts = parts;
        mRegistry.notifyChange(this, BR.parts);
    }

    @Bindable
    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        if (sentence != null && sentence.length() == 0) {
            return;
        }
        this.sentence = sentence;
        mRegistry.notifyChange(this, BR.sentence);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mRegistry.remove(callback);
    }
}
