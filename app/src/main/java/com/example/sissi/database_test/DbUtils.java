package com.example.sissi.database_test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Sissi on 2/6/2018.
 */

public class DbUtils {

    /**
     * 判断是否存在某条记录。
     * 该方法较通过“ query查询，然后使用cursor判断是否存在记录” 的方式高效很多，尤其在where子句中使用了like关键字的情形下。
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

    /**
     * 若存在则update,若不存在则insert
     * 此方法总是会先查询表中是否已存在记录.若表规模较大则耗时较多.
     * 若允许, 可以考虑先全部删除老记录再insert所有的新纪录(开启事务)的方式以提升效率.
     * */
    public static void updateOrInsert(SQLiteDatabase db, String table, ContentValues contentValues, String whereClause, String[] whereArgs){
        if (exists(db, table, whereClause, whereArgs)){
//            PcTrace.p("--update");
            db.update(table, contentValues, whereClause, whereArgs);
        } else {
//            PcTrace.p("--insert");
            db.insert(table, null, contentValues);
        }
    }

}
