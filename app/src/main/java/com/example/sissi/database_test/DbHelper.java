package com.example.sissi.database_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Sissi on 2/1/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    private List<String> sqls;

    public DbHelper(Context context, String name, int version, List<String> sqls){
        this(context, name, null, version, sqls);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, List<String> sqls){
        super(context, name, factory, version);
        this.sqls = sqls;
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
    public void onCreate(SQLiteDatabase db) {// 数据库创建
        PcTrace.p("-o->");
        //　创建表的合适时机是在数据库创建时，所以下面创建表．　
        for (String sql : sqls){
            db.execSQL(sql); // 类似SQLiteDatabase的其他接口如query，execSQL也支持占位符'?',然而在创建表时占位符的使用似乎受限，在check内和char(?)内使用均报错。
        }
//        String sql = "create table if not exists employee(" +
//                "id integer primary key autoincrement," +
//                "name text not null," +
//                "gender char(6) default 'male' check(gender='male' or gender='female'),"+
//                "birth int not null check(1960<=birth and birth<=2000)," +
//                "nativePlace char(128) not null," +
//                "address char(128) not null," +
//                "phone char(11) not null unique," +
//                "email char(32) unique," +
//                "departmentId int not null check(0<departmentId));"
//        ; // TODO 以一种更便捷的方式创建表　
//        db.execSQL(sql); // 类似SQLiteDatabase的其他接口如query，execSQL也支持占位符'?',然而在创建表时占位符的使用似乎受限，在check内和char(?)内使用均报错。
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PcTrace.p("-o-> oldVersion=%s, newVersion=%s", oldVersion, newVersion);
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

}
