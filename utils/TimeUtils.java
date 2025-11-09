package me.vados2343.wLagg.utils;

public class TimeUtils {
    public static long parseTimeString(String s){
        s=s.trim().toLowerCase();
        if(s.endsWith("s")){
            String num=s.substring(0,s.length()-1);
            return Long.parseLong(num);
        }else if(s.endsWith("m")){
            String num=s.substring(0,s.length()-1);
            return Long.parseLong(num)*60;
        }else if(s.endsWith("h")){
            String num=s.substring(0,s.length()-1);
            return Long.parseLong(num)*3600;
        }else{
            return Long.parseLong(s);
        }
    }
}
