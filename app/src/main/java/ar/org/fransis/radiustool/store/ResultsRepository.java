package ar.org.fransis.radiustool.store;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.org.fransis.radiustool.model.Result;

@Database(entities = {Result.class}, version = 1)
public abstract class ResultsRepository extends RoomDatabase {
    public abstract ar.org.fransis.radiustool.dao.Result resultDao();
}
