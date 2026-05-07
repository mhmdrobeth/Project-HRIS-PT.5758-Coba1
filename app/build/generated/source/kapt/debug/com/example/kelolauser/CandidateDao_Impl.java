package com.example.kelolauser;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
public final class CandidateDao_Impl implements CandidateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Candidate> __insertionAdapterOfCandidate;

  public CandidateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCandidate = new EntityInsertionAdapter<Candidate>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `candidates` (`id`,`name`,`email`,`position`,`dateApplied`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Candidate entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPosition() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPosition());
        }
        if (entity.getDateApplied() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDateApplied());
        }
      }
    };
  }

  @Override
  public void insert(final Candidate candidate) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCandidate.insert(candidate);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Candidate> getAll() {
    final String _sql = "SELECT * FROM candidates";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
      final int _cursorIndexOfDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "dateApplied");
      final List<Candidate> _result = new ArrayList<Candidate>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Candidate _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        final String _tmpPosition;
        if (_cursor.isNull(_cursorIndexOfPosition)) {
          _tmpPosition = null;
        } else {
          _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
        }
        final String _tmpDateApplied;
        if (_cursor.isNull(_cursorIndexOfDateApplied)) {
          _tmpDateApplied = null;
        } else {
          _tmpDateApplied = _cursor.getString(_cursorIndexOfDateApplied);
        }
        _item = new Candidate(_tmpId,_tmpName,_tmpEmail,_tmpPosition,_tmpDateApplied);
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
