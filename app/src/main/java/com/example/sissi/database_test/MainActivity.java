package com.example.sissi.database_test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
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

        Cursor cursor;

        final SQLiteDatabase db = employeeDbHelper.getWritableDatabase();
        final String colName = "name";
        String colBirth = "birth";
        String colNativePlace = "nativePlace";
        String colAddress = "address";
        String colPhone = "phone";
        String colEmail = "email";
        String colDepartmentId = "departmentId";

        final List<ContentValues> contentValuesList = new ArrayList<>();
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

//        db.beginTransaction();  // 批量插入时开启事务能显著提高效率　
        try {
//            for (ContentValues values : contentValuesList) {
            for (int i=0; i<contentValuesList.size(); ++i) {
                if (0==i%1000){
                    final int finalI = i;
                    new Thread(){ // 多线程方式插入
                        @Override
                        public void run() {
                            db.beginTransaction();  // 批量插入时开启事务能显著提高效率　
                            try {
                                int j;
                                for (j = finalI; j < finalI + 1000; ++j) {
//                                    DbUtils.updateOrInsert(db, "employee", contentValuesList.get(j),
//                                            "name=?", new String[]{contentValuesList.get(j).getAsString("name")});
                                }
                                if (10000==j) {
                                    Cursor cursor = db.query("employee", null, null, null,
                                            null, null, null, "9");
                                    PcTrace.p("count=%s", cursor.getCount());

                                    while (cursor.moveToNext()) {
                                        PcTrace.p("record name=%s", cursor.getString(cursor.getColumnIndex(colName)));
                                    }
                                    cursor.close();

                                    for (int k=0; k<3; ++k) { // 批量查询,第一次返回“满足条件的结果集里的”1到3号记录,第二次返回4到6，第三次返回7到9
                                        cursor = db.query("employee", null, null, null,
                                                null, null, null, k*3+","+"3"/*"3 offset "+k*3*/);
                                        PcTrace.p("count=%s", cursor.getCount());

                                        while (cursor.moveToNext()) {
                                            PcTrace.p("record name=%s", cursor.getString(cursor.getColumnIndex(colName)));
                                        }
                                        cursor.close();
                                    }
                                }

                                db.setTransactionSuccessful();
                            }finally {
                                db.endTransaction();
                            }
                        }
                    }.start();
                }
//                DbUtils.updateOrInsert(db, "employee", values, "name=?", new String[]{values.getAsString("name")});
            }
//            db.setTransactionSuccessful();
        }finally { // try finally的方式是事务的标准写法　
//            db.endTransaction();
        }
        PcTrace.p("<-- insert 10000 record");


//        // 插入记录
//        // 法一
//        db.insert("employee", null, contentValues);
//        // 法二
//        db.execSQL("insert into employee ('name','birth','nativePlace','address','phone','email','departmentId') " +
//                                  "values('zzb', 1962, 'anhui', 'songjiang', '12345678903', 'zzb@gmail.com', 1)");

        // 查询
        // 法一
//        cursor = db.query("employee", null, /*"name like '%name999%'"*/null, /*new String[]{"%name%"}*/null,
//                null, null, null, null);
//        PcTrace.p("count=%s", cursor.getCount());
//
//        while (cursor.moveToNext()){
//            PcTrace.p("record name=%s", cursor.getString(cursor.getColumnIndex(colName)));
//        }
//        if (cursor.moveToFirst()){
//            PcTrace.p("record name = %s already exists, update it", cursor.getString(0));
//        }else{
//
//        }
        // 法二
//        cursor = db.rawQuery("select id from employee where name=gf limit 1", null);


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

//        cursor.close();
//        employeeDbHelper.close();
    }

}
