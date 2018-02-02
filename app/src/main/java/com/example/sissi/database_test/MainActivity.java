package com.example.sissi.database_test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


    private void dbOp(){
        EmployeeDbHelper employeeDbHelper = new EmployeeDbHelper(this);

        SQLiteDatabase db = employeeDbHelper.getWritableDatabase();

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
//        db.insert("employee", null, contentValues);
        // 法二
//        db.execSQL("insert into employee ('name','birth','nativePlace','address','phone','email','departmentId') " +
//                                  "values('zzb', 1962, 'anhui', 'songjiang', '12345678903', 'zzb@gmail.com', 1)");

        // 查询
        // 法一
//        db.query();
        // 法二
        Cursor cursor = db.rawQuery("select * from employee", null);
        int type;
//        for (int i=0; i<cursor.getColumnCount(); ++i){
//            type = cursor.getType(i);
//            if (Cursor.FIELD_TYPE_INTEGER == type){
//                cursor.getInt(i);
//            } else if (Cursor.FIELD_TYPE_STRING == type){
//                cursor.getString(i);
//            }
//        }
//        while (cursor.){
//
//        }

        cursor.close();
        employeeDbHelper.close();
    }

}
