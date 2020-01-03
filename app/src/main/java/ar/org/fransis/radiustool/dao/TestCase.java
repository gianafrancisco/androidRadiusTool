package ar.org.fransis.radiustool.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
