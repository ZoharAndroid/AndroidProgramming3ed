package com.zohar.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuid = getString(getColumnIndex(CrimeDB.Col.UUID));
        String title = getString(getColumnIndex(CrimeDB.Col.TITLE));
        long date = getLong(getColumnIndex(CrimeDB.Col.DATE));
        int sovled = getInt(getColumnIndex(CrimeDB.Col.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuid));
        crime.setSolved(sovled != 0);
        crime.setDate(new Date(date));
        crime.setTitle(title);

        return crime;
    }
}
