package ar.org.fransis.radiustool.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;


@Dao
public interface Result {
    @Query("SELECT * FROM result ORDER BY mId DESC")
    List<ar.org.fransis.radiustool.model.Result> getAll();

    @Insert
    long insert(ar.org.fransis.radiustool.model.Result res);

    @Update
    void update(ar.org.fransis.radiustool.model.Result res);

    @Delete
    void delete(ar.org.fransis.radiustool.model.Result res);
}
