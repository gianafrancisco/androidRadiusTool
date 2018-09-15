package ar.org.fransis.radiustool.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TestCase {
    @Query("SELECT * FROM testcase")
    List<ar.org.fransis.radiustool.model.TestCase> getAll();

    @Insert
    long insert(ar.org.fransis.radiustool.model.TestCase test);

    @Update
    void update(ar.org.fransis.radiustool.model.TestCase test);

    @Delete
    void delete(ar.org.fransis.radiustool.model.TestCase test);

}
