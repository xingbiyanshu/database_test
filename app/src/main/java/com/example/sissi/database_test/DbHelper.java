package com.example.sissi.database_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sissi on 2/1/2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "test_db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    /* 创建Helper对象时并不会触发下面任何一个回调（实际只是做了一些赋值操作），当通过Helper对象获取数据库对象时才会触发如下回调。
    * 首次获取数据库对象时会依次执行onConfigure, onCreate, onOpen。后续再获取时（此时数据库已创建）只会执行onConfigure, onOpen(onCreate不会再执行) .
    * 若版本号增大则获取数据库对象时执行onConfigure, onUpgrade, onOpen，若版本号减小则获取数据库对象时执行onConfigure, onDowngrade, onOpen。
    * */

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        PcTrace.p("-o->");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PcTrace.p("-o-> oldVersion=%s, newVersion=%s", oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        PcTrace.p("-o->");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PcTrace.p("-o->");
        String sql = "create table if not exists employee(" +
                "id integer primary key autoincrement," +
                "name text not null," +
                "gender char(6) default 'male' check(gender='male' or gender='female'),"+
                "birth int not null check(1960<=birth and birth<=2000)," +
                "nativePlace char(128) not null," +
                "address char(128) not null," +
                "phone char(11) not null unique," +
                "email char(32) unique," +
                "departmentId int not null check(0<departmentId));"
        ;
        db.execSQL(sql); // 类似SQLiteDatabase的其他接口如query，execSQL也支持占位符'?',然而在创建表时占位符的使用似乎受限，在check内和char(?)内使用均报错。
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PcTrace.p("-o-> oldVersion=%s, newVersion=%s", oldVersion, newVersion);
    }
}
