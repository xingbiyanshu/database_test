package com.example.sissi.database_test;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Sissi on 2/6/2018.
 */

public class DbUtils {

    /**
     * 判断是否存在某条记录
     * 该方法较通过“ query查询，然后使用cursor判断是否存在记录” 的方式高效很多。
     * */
    public static boolean exists(SQLiteDatabase db, String table, String whereClause, String[] whereArgs){
        String sql = "select 1 from " + table + " where "+whereClause + " limit 1";

        SQLiteStatement sqLiteStatement = db.compileStatement(sql);

        sqLiteStatement.bindAllArgsAsStrings(whereArgs);
        try {
            long res = sqLiteStatement.simpleQueryForLong();
            return 1==res;
        }catch (SQLiteDoneException e){
            return false;
        }
    }

    /**
     * 判断是否存在某条记录。
     * 在大的循环体中使用该方法较上面方法高效，因为复用了预编译的SQLiteStatement
     * */
    public static boolean exists(SQLiteStatement sqLiteStatement, String[] whereArgs){
        if (null != whereArgs) {
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindAllArgsAsStrings(whereArgs);
        }
        try {
            sqLiteStatement.simpleQueryForLong();
            return true;
        }catch (SQLiteDoneException e){
            return false;
        }finally {
            sqLiteStatement.clearBindings();
        }
    }
}
