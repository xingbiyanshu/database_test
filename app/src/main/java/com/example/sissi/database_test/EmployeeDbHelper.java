package com.example.sissi.database_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sissi on 2/1/2018.
 */

public class EmployeeDbHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "test_db";
    private static int VERSION = 2;
    private String[][] versionedSqls = new String[][]{ // 不要直接修改已有的sql语句, 根据版本号添加对应的sql语句.
            {},// 版本0对应的sqls语句。仅用作占位，版本总是>=1

            // 版本1对应的sqls语句
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

            // 版本2新增的sqls语句。版本2会执行版本1和版本2的所有sql语句，其余版本类推。
            {
                "alter table employee add column brief char(200);"
            },
    };

    public EmployeeDbHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    /* 创建Helper对象时并不会触发下面任何一个回调（实际只是做了一些赋值操作），当通过Helper对象获取数据库对象时才会触发如下回调。
    * 首次获取数据库对象时会依次执行onConfigure, onCreate, onOpen。后续再获取时（此时数据库已创建）只会执行onConfigure, onOpen(onCreate不会再执行) .
    * 若版本号增大则获取数据库对象时执行onConfigure, onUpgrade, onOpen，若版本号减小则获取数据库对象时执行onConfigure, onDowngrade, onOpen。
    * */

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        PcTrace.p("-o->");
//        if (0==db.getVersion()) {
//            db.setVersion(1); // 创建数据库时即触发onUpgrade， 目的是让所有数据库创建及修改操作集中在onUpgrade内，避免误导后续维护者直接在onCreate回调内修改扩展数据库。
//        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {// 数据库创建
        PcTrace.p("-o->");
        //　创建表的合适时机是在数据库创建时，所以下面创建表．
        // 注意，覆盖安装（或在线升级）后由于数据库已经存在（不应该删除，因为有用户数据）故不会再走该回调。所以在初始表创建工作结束后，
        // 若随着需求的变更后续有修改数据库的操作（如添加修改删除表），则不应将相应的代码加在该回调内，而应该加在onUpgrade或onDowngrade内并修改版本号。
        // 所以其实在这里创建的只是数据库的初始形态而非当前的形态，当前形态要查看当前版本号，并对照onUpgrade或onDowngrade中的逻辑，这点很容易被忽略。
//        for (String sql : versionedSqls){
//            db.execSQL(sql); // 类似SQLiteDatabase的其他接口如query，execSQL也支持占位符'?',然而在创建表时占位符的使用似乎受限，在check内和char(?)内使用均报错。
//        }
        // 卸载后重新安装有可能直接从版本0跳到版本2，而且不会走onUpgrade回调，这样，如果版本1的改动在onUpgrade中做的话，就会被丢失了！
        // 走该回调时数据库版本总是0
        execSqls(db, 1, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PcTrace.p("-o-> oldVersion=%s, newVersion=%s", oldVersion, newVersion);
        execSqls(db, oldVersion, newVersion);
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

    // 执行从 fromVersion 到 toVersion(包含fromVersion和toVersion) 的sql语句
    private void execSqls(SQLiteDatabase db, int fromVersion, int toVersion){
        for (int i=fromVersion; i<=toVersion; ++i) {
            for (int j = 0; j< versionedSqls[i].length; ++j) {
                db.execSQL(versionedSqls[i][j]);
            }
        }
    }
}
