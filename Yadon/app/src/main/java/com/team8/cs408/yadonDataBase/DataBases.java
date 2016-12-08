package com.team8.cs408.yadonDataBase;

import android.provider.BaseColumns;

//Reference :
// http://arabiannight.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CAndroid-DB-%EC%83%9D%EC%84%B1-%EB%B0%8F-%EA%B4%80%EB%A6%AC-Cursor-Query

public final class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String GROUPNAME = "groupName";
        public static final String NAME = "name";
        public static final String PHONENUMBER = "phoneNumber";
        public static final String DEBT = "debt";
        public static final String ALARMSTART = "alarmStart";
        public static final String ALARMPERIOD = "alarmPeriod";
        public static final String _TABLENAME = "acountBook";
        public static final String _CREATE =
                "create table " + _TABLENAME + "("
                        + _ID + " Integer primary key autoincrement , "
                        + GROUPNAME + " text not null , "
                        + NAME + " text not null , "
                        + PHONENUMBER + " text not null , "
                        + ALARMSTART + " Integer not null , "
                        + ALARMPERIOD + " Integer not null , "
                        + DEBT + " Integer not null );";
    }
}
