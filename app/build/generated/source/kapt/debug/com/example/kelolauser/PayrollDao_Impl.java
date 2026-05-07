package com.example.kelolauser;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PayrollDao_Impl implements PayrollDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Payroll> __insertionAdapterOfPayroll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public PayrollDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPayroll = new EntityInsertionAdapter<Payroll>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `payroll` (`id`,`month`,`year`,`basicSalary`,`allowance`,`totalSalary`,`dateIssued`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Payroll entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getMonth() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getMonth());
        }
        if (entity.getYear() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getYear());
        }
        if (entity.getBasicSalary() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBasicSalary());
        }
        if (entity.getAllowance() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAllowance());
        }
        if (entity.getTotalSalary() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTotalSalary());
        }
        if (entity.getDateIssued() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDateIssued());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM payroll";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Payroll payroll) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPayroll.insert(payroll);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<Payroll> getAll() {
    final String _sql = "SELECT * FROM payroll";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
      final int _cursorIndexOfBasicSalary = CursorUtil.getColumnIndexOrThrow(_cursor, "basicSalary");
      final int _cursorIndexOfAllowance = CursorUtil.getColumnIndexOrThrow(_cursor, "allowance");
      final int _cursorIndexOfTotalSalary = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSalary");
      final int _cursorIndexOfDateIssued = CursorUtil.getColumnIndexOrThrow(_cursor, "dateIssued");
      final List<Payroll> _result = new ArrayList<Payroll>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Payroll _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpMonth;
        if (_cursor.isNull(_cursorIndexOfMonth)) {
          _tmpMonth = null;
        } else {
          _tmpMonth = _cursor.getString(_cursorIndexOfMonth);
        }
        final String _tmpYear;
        if (_cursor.isNull(_cursorIndexOfYear)) {
          _tmpYear = null;
        } else {
          _tmpYear = _cursor.getString(_cursorIndexOfYear);
        }
        final String _tmpBasicSalary;
        if (_cursor.isNull(_cursorIndexOfBasicSalary)) {
          _tmpBasicSalary = null;
        } else {
          _tmpBasicSalary = _cursor.getString(_cursorIndexOfBasicSalary);
        }
        final String _tmpAllowance;
        if (_cursor.isNull(_cursorIndexOfAllowance)) {
          _tmpAllowance = null;
        } else {
          _tmpAllowance = _cursor.getString(_cursorIndexOfAllowance);
        }
        final String _tmpTotalSalary;
        if (_cursor.isNull(_cursorIndexOfTotalSalary)) {
          _tmpTotalSalary = null;
        } else {
          _tmpTotalSalary = _cursor.getString(_cursorIndexOfTotalSalary);
        }
        final String _tmpDateIssued;
        if (_cursor.isNull(_cursorIndexOfDateIssued)) {
          _tmpDateIssued = null;
        } else {
          _tmpDateIssued = _cursor.getString(_cursorIndexOfDateIssued);
        }
        _item = new Payroll(_tmpId,_tmpMonth,_tmpYear,_tmpBasicSalary,_tmpAllowance,_tmpTotalSalary,_tmpDateIssued);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
