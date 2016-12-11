package com.team8.cs408.yadonDataBase;
import android.provider.BaseColumns;

/**
 * Created by newwhite on 2016. 12. 9..
 */


// Name, Account, Message Sample,




public final class UserBasicInfoDB {
    public static final class CreateDB implements BaseColumns{
        public static final String USERNAME = "userName";
        public static final String USERBANK = "userBank";
        public static final String USERACCOUNT = "userAccount";
        public static final String ALARMSETTING = "alarmSetting";
        public static final String MESSAGESAMPLE = "messageSample";
        public static final String _TABLENAME = "userInfo";
        public static final String _CREATE =
                "create table " + _TABLENAME + "("
                        + _ID + " Integer primary key autoincrement , "
                        + USERNAME + " text not null , "
                        + USERBANK + " text not null , "
                        + USERACCOUNT + " text not null , "
                        + ALARMSETTING + " boolean not null , "
                        + MESSAGESAMPLE + " text not null );";
    }

}