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

        new Thread(){
            @Override
            public void run() {
                dbOp();
            }
        }.start();
    }


    private void dbOp(){
        EmployeeDbHelper employeeDbHelper = new EmployeeDbHelper(this);

        SQLiteDatabase db = employeeDbHelper.getWritableDatabase();
        String colName = "name";
        String colBirth = "birth";
        String colNativePlace = "nativePlace";
        String colAddress = "address";
        String colPhone = "phone";
        String colEmail = "email";
        String colDepartmentId = "departmentId";

        List<ContentValues> contentValuesList = new ArrayList<>();
        for (int i=0; i<10000; ++i){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", colName+i);
            contentValues.put("birth", 1960);
            contentValues.put("nativePlace", colNativePlace+i);
            contentValues.put("address", colAddress+i);
            contentValues.put("phone", ""+12345600000L+i);
            contentValues.put("email", colEmail+i+"@gmail.com");
            contentValues.put("departmentId", 1);
            contentValuesList.add(contentValues);
        }
        // 插入记录
        PcTrace.p("--> insert 10000 record");
        Cursor cursor1 = db.query("employee", null, null, null,
                null, null, null, null);
        PcTrace.p("record count=%s", cursor1.getCount());
        cursor1.close();
        for (int i=0; i<10000; ++i){
//            for (ContentValues values : contentValuesList){
//            Cursor cursor = db.query("employee", new String[]{"name"}, "id=?", new String[]{"3000"},
//                    null, null, null, "1");
            /*Cursor cursor = */db.query("employee", null, "id=? or name like ?", new String[]{""+i, "%na%"},
                    null, null, null, "1");
//            PcTrace.p("count=%s", cursor.getCount());
//            cursor.close();
//            Cursor cursor = db.query("employee", null, "id=?", new String[]{"1"},
//                    null, null, null, "1");
//            if (cursor.moveToFirst()) {
//                cursor.close();
//                continue;
//            }else{
//                PcTrace.p("-- insert ");
//            }
//            cursor.close();
//
//            db.insert("employee", null, values);
        }
        PcTrace.p("<-- insert 10000 record");

        // 判断记录是否存在
        Cursor cursor;
        PcTrace.p("--> tell if record exists, method 2");
        cursor = db.query("employee", null, "id=?", new String[]{"7000"},
                null, null, null, null);
        PcTrace.p("<-- tell if record exists, method 2");
        cursor.close();

        PcTrace.p("--> tell if record exists, method 1");
        cursor = db.query("employee", new String[]{"name"}, "id=?", new String[]{"7000"},
                null, null, null, "1");
        PcTrace.p("<-- tell if record exists, method 1");
        if (cursor.moveToFirst()){
            PcTrace.p("record id=1 exists, name=%s", cursor.getString(0));
        }else{
            PcTrace.p("record id=1 not exists");
        }


        // 插入记录
        // 法一
//        db.insert("employee", null, contentValues);
//        contentValues.clear();
//        contentValues.put("name", "cj");
//        contentValues.put("birth", 1961);
//        contentValues.put("nativePlace", "jiangsu");
//        contentValues.put("address", "songjiang");
//        contentValues.put("phone", "12345678902");
//        contentValues.put("email", "cj@gmail.com");
//        contentValues.put("departmentId", 1);
//        db.insert("employee", null, contentValues);
        // 法二
//        db.execSQL("insert into employee ('name','birth','nativePlace','address','phone','email','departmentId') " +
//                                  "values('zzb', 1962, 'anhui', 'songjiang', '12345678903', 'zzb@gmail.com', 1)");

        // 查询
        // 法一
//        Cursor cursor = db.query("test_db", new String[]{"name"}, "id=?", new String[]{"1"},
//                null, null, null, "1");
//        if (cursor.moveToFirst()){
//            PcTrace.p("record name = %s already exists, update it", cursor.getString(0));
//            db.update("test_db", contentValues, )
//        }else{
//
//        }
//        // 法二
//        Cursor cursor = db.rawQuery("select id from employee where name=gf limit 1", null);
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
