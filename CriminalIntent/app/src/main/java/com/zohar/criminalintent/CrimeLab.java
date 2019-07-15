package com.zohar.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

//    private List<Crime> mCrimes;

    private Context mContext;
    private SQLiteDatabase mDBHelper;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDBHelper = new CrimeBaseHelper(mContext).getWritableDatabase();
//        mCrimes = new ArrayList<>();
//
//        // 生成数据
//        for (int i = 0; i < 100; i++){
//            Crime crime = new Crime();
//            crime.setTitle("crime #" + i);
//            crime.setSolved(i % 2== 0);
//            mCrimes.add(crime);
//        }
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeDB.Col.UUID, crime.getId().toString());
        values.put(CrimeDB.Col.TITLE, crime.getTitle());
        values.put(CrimeDB.Col.DATE, crime.getDate().toString());
        values.put(CrimeDB.Col.SOLVED, crime.isSolved());
        values.put(CrimeDB.Col.SUSPEND, crime.getMsuspend());
        return values;
    }

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    public List<Crime> getCrimes(){
        List<Crime> cirmes = new ArrayList<>();
        // 查询所有
        CrimeCursorWrapper cursorWrapper = queryCrimes(null,null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                cirmes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }

        return cirmes;
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(CrimeDB.Col.UUID + " = ?", new String[]{id.toString()});
        if (cursor.getCount() == 0){
            return null;
        }
        cursor.moveToFirst();
        Crime crime = cursor.getCrime();
        cursor.close();
        return crime;
    }

    /**
     * 添加
     *
     * @param crime crime
     */
    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        mDBHelper.insert(CrimeDB.NAME, null, values);
    }

    public void updateCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        mDBHelper.update(CrimeDB.NAME, values,  CrimeDB.Col.UUID + " = ?", new String[]{crime.getId().toString()});
    }

    private CrimeCursorWrapper queryCrimes(String selection, String[] selectionArgs){
        Cursor cursor = mDBHelper.query(CrimeDB.NAME,null,selection, selectionArgs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }

    /**
     * 删除
     *
     * @param crime crime
     */
    public void deleteCrime(Crime crime){
        mDBHelper.delete(CrimeDB.NAME, CrimeDB.Col.UUID + " = ?", new String[]{crime.getId().toString()});
    }

    /**
     * 获取图像文件
     *
     * @param crime
     * @return
     */
    public File getPhotoFile(Crime crime){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }
}
