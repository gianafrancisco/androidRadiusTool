package ar.org.fransis.radiustool.store;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ar.org.fransis.radiustool.model.TestCase;

@Database(entities = {TestCase.class}, version = 1)
public abstract class TestCaseDB extends RoomDatabase {
    public abstract ar.org.fransis.radiustool.dao.TestCase testCaseDao();
}
