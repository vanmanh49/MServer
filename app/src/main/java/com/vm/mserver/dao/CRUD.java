package com.vm.mserver.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vm.mserver.model.Contact;
import com.vm.mserver.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VanManh on 04-Dec-17.
 */

public class CRUD {
    private static DBHelper dbHelper;
    private static CRUD crud;

    public static CRUD getInstants(Context context) {
        if (crud == null) {
            crud = new CRUD();
            dbHelper = new DBHelper(context);
        }
        return crud;
    }

    public List<Contact> getRegisterContact() {
        List<Contact> rs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constants.TB_PHONE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phoneNum = cursor.getString(2);
                rs.add(new Contact(id, name, phoneNum));
            } while (cursor.moveToNext());
        }
        db.close();
        return rs;
    }

    public List<Student> getAllStudents() {
        List<Student> rs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constants.TB_MARK_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int ms = cursor.getInt(0);
                String name = cursor.getString(1);
                int toan = cursor.getInt(2);
                int ly = cursor.getInt(3);
                int hoa = cursor.getInt(4);
                rs.add(new Student(ms, name, toan, ly, hoa));
            } while (cursor.moveToNext());
        }
        db.close();
        return rs;
    }

    public boolean isPhoneNumExist(String phoneNum) {
        List<Contact> lists = getRegisterContact();
        for (Contact c : lists) {
            if (c.getPhoneNum().equals(phoneNum)
                    || c.getPhoneNum().substring(1, c.getPhoneNum().length())
                    .equals(phoneNum.substring(3, phoneNum.length()))) {
                return true;
            }
        }
        return false;
    }

    public boolean registerContact(Contact p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.NAME_COLUMN, p.getName());
        values.put(Constants.PHONE_COLUMN, p.getPhoneNum());

        long rs = db.insert(Constants.TB_PHONE_NAME, null, values);
        db.close();
        return rs > 0 ? true : false;
    }

    public boolean deleteContact(Contact p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rs = db.delete(Constants.TB_PHONE_NAME, "ID = ?", new String[]{String.valueOf(p.getId())});
        db.close();
        return rs > 0 ? true : false;
    }

    public boolean deleteStudent(Student s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rs = db.delete(Constants.TB_MARK_NAME, Constants.MS + " = ?", new String[]{String.valueOf(s.getMs())});
        db.close();
        return rs > 0 ? true : false;
    }

    public boolean updateContact(Contact p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.NAME_COLUMN, p.getName());
        values.put(Constants.PHONE_COLUMN, p.getPhoneNum());
        int rs = db.update(Constants.TB_PHONE_NAME, values, "ID= ?", new String[]{String.valueOf(p.getId())});
        db.close();
        return rs > 0 ? true : false;
    }

    public boolean updateStudent(Student s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.S_NAME, s.getName());
        values.put(Constants.S_TOAN, s.getToan());
        values.put(Constants.S_LY, s.getLy());
        values.put(Constants.S_HOA, s.getHoa());
        int rs = db.update(Constants.TB_MARK_NAME, values, Constants.MS + " = ?", new String[]{String.valueOf(s.getMs())});
        db.close();
        return rs > 0 ? true : false;
    }

    public Student getStudentmarkByMSSV(int mssv) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constants.TB_MARK_NAME + " WHERE " + Constants.MS + " = " + mssv;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int s_mssv = cursor.getInt(0);
            String s_name = cursor.getString(1);
            Double s_toan = cursor.getDouble(2);
            Double s_ly = cursor.getDouble(3);
            Double s_hoa = cursor.getDouble(4);
            db.close();
            return new Student(s_mssv, s_name, s_toan, s_ly, s_hoa);
        } else {
            return null;
        }
    }

    public boolean addNewStudent(Student s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.MS, s.getMs());
        values.put(Constants.S_NAME, s.getName());
        values.put(Constants.S_TOAN, s.getToan());
        values.put(Constants.S_LY, s.getLy());
        values.put(Constants.S_HOA, s.getHoa());
        long rs = db.insert(Constants.TB_MARK_NAME, null, values);
        db.close();
        return rs > 0 ? true : false;
    }
}
