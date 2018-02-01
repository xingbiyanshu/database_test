package com.example.sissi.database_test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOp();
    }

    private String DB_NAME = "test_db";
    private int VERSION = 1;
    private String createEmployeeTableSql =
            "create table if not exists employee(" +
                    "id integer primary key autoincrement," +
                    "name text not null," +
                    "gender char(6) default 'male' check(gender='male' or gender='female'),"+
                    "birth int not null check(1960<=birth and birth<=2000)," +
                    "nativePlace char(128) not null," +
                    "address char(128) not null," +
                    "phone char(11) not null unique," +
                    "email char(32) unique," +
                    "departmentId int not null check(0<departmentId)" +
                    ");"
    ; // TODO 以一种更便捷的方式创建表　// 这个放在DbHelper中还是其上层有待考量。若按onUpgrade需要见到版本号来看应该放在DbHelper中，但SQLiteDatabase对象需要表名，它应该和DbHelper处在同一层次。

    private void dbOp(){
        List<String> sqls = new ArrayList<>();
        sqls.add(createEmployeeTableSql);
        DbHelper dbHelper = new DbHelper(this, DB_NAME, VERSION, sqls);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "gf");
        contentValues.put("birth", 1960);
        contentValues.put("nativePlace", "changde");
        contentValues.put("address", "songjiang");
        contentValues.put("phone", "12345678901");
        contentValues.put("email", "gf@gmail.com");
        contentValues.put("departmentId", 1);


        // 插入记录
        // 法一
        db.insert("employee", null, contentValues);
        contentValues.clear();
        contentValues.put("name", "cj");
        contentValues.put("birth", 1961);
        contentValues.put("nativePlace", "jiangsu");
        contentValues.put("address", "songjiang");
        contentValues.put("phone", "12345678902");
        contentValues.put("email", "cj@gmail.com");
        contentValues.put("departmentId", 1);
        db.insert("employee", null, contentValues);
        // 法二
        db.execSQL("insert into employee ('name','birth','nativePlace','address','phone','email','departmentId') " +
                                  "values('zzb', 1962, 'anhui', 'songjiang', '12345678903', 'zzb@gmail.com', 1)");

        // 查询
        // 法一
//        db.query();
        // 法二
        Cursor cursor = db.rawQuery("select * from employee", null);
//        cursor.get;
//        while (cursor.){
//
//        }

        dbHelper.close();
    }

}
