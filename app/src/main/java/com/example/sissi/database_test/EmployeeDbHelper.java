package com.example.sissi.database_test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sissi on 2/1/2018.
 */

public class EmployeeDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "test_db";
//    private static int VERSION = 2; // 版本升级时递增该数字
    private static final String[][] versionedSqls = new String[][]{ // 不要修改已有的sql语句, 若需求有变更请添加对应的sql语句.
        // 版本1对应的sqls语句。创建员工表和部门表　
        {
            "create table if not exists employee(\n" +
                    "id integer primary key autoincrement,\n" +
                    "name text not null,\n" +
                    "gender char(6) default 'male' check(gender='male' or gender='female'),\n" +
                    "birth int not null check(1960<=birth and birth<=2000),\n" +
                    "nativePlace char(128) not null,\n" +
                    "address char(128) not null,\n" +
                    "phone char(11) not null unique,\n" +
                    "email char(32) unique,\n" +
                    "departmentId int not null check(0<departmentId)\n" +
                    ");\n"
                ,
            "create table if not exists department(\n" +
                    "id integer primary key check(0<id),\n" +
                    "name text not null,\n" +
                    "parentDepartmentId integer check(0<=parentDepartmentId)\n" +
                    ");\n"
        }, // TODO 以一种更便捷的方式创建表　// 这个放在DbHelper中还是其上层有待考量。若按onUpgrade需要见到版本号来看应该放在DbHelper中，但SQLiteDatabase对象需要表名，它应该和DbHelper处在同一层次。

        // 版本2新增的sqls语句。版本2会执行版本1和版本2的所有sql语句，版本3会执行版本1,2,3的所有sql语句，类推...
        {
            "alter table employee add column brief char(200);"
        },
        // 版本3。　
        {
            "create index if not exists nameIdx on employee(name);"
        },
    };

    private SQLiteDatabase database;

    public EmployeeDbHelper(Context context){
        super(context, DB_NAME, null, versionedSqls.length);
    }

    /* 创建Helper对象时并不会触发下面任何一个回调（实际只是做了一些赋值操作），当通过Helper对象获取数据库对象时才会触发如下回调。
    * 首次获取数据库对象时会依次执行onConfigure, onCreate, onOpen。后续再获取时（此时数据库已创建）只会执行onConfigure, onOpen(onCreate不会再执行) .
    * 若版本号增大则获取数据库对象时执行onConfigure, onUpgrade, onOpen，若版本号减小则获取数据库对象时执行onConfigure, onDowngrade, onOpen。
    * */

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
//        PcTrace.p("-->");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {// 数据库创建
//        PcTrace.p("-->");
        //　创建表的合适时机是在数据库创建时，所以下面创建表．
        // 注意，覆盖安装（或在线升级）后由于数据库已经存在（不应该删除，因为有用户数据）故不会再走该回调。
        // 走该回调时数据库版本总是0
        execSqls(db, 0, versionedSqls.length);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        PcTrace.p("--> oldVersion=%s, newVersion=%s", oldVersion, newVersion);
        execSqls(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        PcTrace.p("--> oldVersion=%s, newVersion=%s", oldVersion, newVersion);
        // 一般不会降级,若降级可按如下步骤处理
//        //第一、备份老表
//        String alt_table="alert table t_message rename to t_message_bak";
//        db.execSQL(alt_table);
//        //第二、建立新表
//        String crt_table = "create table t_message (id int primary key,,userName varchar(50),lastMessage varchar(50),datetime  varchar(50))";
//        db.execSQL(crt_table);
//        //第三、把老表中的数据挪到新表
//        String cpy_table="insert into t_message select id,userName,lastMessage,datetime from t_message_bak";
//        db.execSQL(cpy_table);
//        //第四、删除备份表
//        String drp_table = "drop table if exists t_message_bak";
//        db.execSQL(drp_table);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
//        PcTrace.p("-->");
        database = db;
    }

    // 执行(fromVersion, toVersion]区间内的sql语句
    private void execSqls(SQLiteDatabase db, int fromVersion, int toVersion){
//        if (toVersion <= fromVersion){
//            PcTrace.p(PcTrace.ERROR, "toVersion(%s) less than fromVersion(%s)", toVersion, fromVersion);
//            return;
//        }
        for (int i=fromVersion; i<toVersion; ++i) {
            for (int j = 0; j< versionedSqls[i].length; ++j) {
                db.execSQL(versionedSqls[i][j]);
            }
        }
    }

}
