package com.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class LoginDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "login_status.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "login_status";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IS_LOGGED_IN = "is_logged_in";

    private static final String SECRET_KEY = "your_secret_key_here";

    /**
     * 接收一个上下文对象，并调用父类的构造函数，指定数据库名称、版本号等参数，用于创建或打开数据库。
     * @param context
     */
    public LoginDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 在数据库第一次创建时调用，创建一个名为 login_status 的表，包含两个列：id 和 is_logged_in。
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_IS_LOGGED_IN + " TEXT)";
        db.execSQL(createTableQuery);
    }

    /**
     * 在数据库需要升级时调用，删除旧表并重新创建。
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 备份旧表数据
        String backupTableName = TABLE_NAME + "_backup";
        db.execSQL("ALTER TABLE " + TABLE_NAME + " RENAME TO " + backupTableName);
        // 创建新表
        onCreate(db);
        // 导入数据到新表
        String columns = COLUMN_ID + ", " + COLUMN_IS_LOGGED_IN;
        db.execSQL("INSERT INTO " + TABLE_NAME + " SELECT " + columns + " FROM " + backupTableName);
        // 删除备份表
        db.execSQL("DROP TABLE IF EXISTS " + backupTableName);
    }

    /**
     * 保存登录状态到数据库中。首先获取可写数据库实例，然后将登录状态加密后存储到 login_status 表中的 is_logged_in 列。
     * @param isLoggedIn
     */
    public void saveLoginStatus(boolean isLoggedIn) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IS_LOGGED_IN, encrypt(String.valueOf(isLoggedIn)));
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中读取登录状态。首先获取可读数据库实例，然后执行查询语句获取 is_logged_in 列的值。读取到的值是经过加密的，
     * 需要解密后返回给调用者。
     * @return
     */
    public boolean getLoginStatus() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                String encryptedStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IS_LOGGED_IN));
                String decryptedStatus = decrypt(encryptedStatus);
                return Boolean.parseBoolean(decryptedStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * 使用 AES 加密算法对数据进行加密。使用预设的密钥 SECRET_KEY，对给定的数据进行 AES 加密，并返回加密后的结果。
     * @param data
     * @return
     * @throws Exception
     */
    private String encrypt(String data) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    /**
     * 使用 AES 加密算法对数据进行解密。使用预设的密钥 SECRET_KEY，对给定的密文进行 AES 解密，并返回解密后的结果。
     * @param encryptedData
     * @return
     * @throws Exception
     */
    private String decrypt(String encryptedData) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));
        return new String(decryptedData);
    }
}

