package fransis.org.ar.radiustool.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TestCase {
    @Query("SELECT * FROM testcase")
    List<fransis.org.ar.radiustool.model.TestCase> getAll();

    @Insert
    long insert(fransis.org.ar.radiustool.model.TestCase test);

    @Update
    void update(fransis.org.ar.radiustool.model.TestCase test);

    @Delete
    void delete(fransis.org.ar.radiustool.model.TestCase test);

}
