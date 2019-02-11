package com.suji.ish.suji.utils;

//对databinding数据进行准换
public class Converter {

    public static String enPhToText(String phEn) {
        if (phEn == null ) {
            return null;
        }
        if(phEn.length()==0){
            return "暂无音标";
        }
        return "英[" + phEn + "]";
    }

    public static String amPhToText(String phEn) {
        if (phEn == null ) {
            return null;
        }
        if(phEn.length()==0){
            return "暂无音标";
        }
        return "美[" + phEn + "]";
    }

}
