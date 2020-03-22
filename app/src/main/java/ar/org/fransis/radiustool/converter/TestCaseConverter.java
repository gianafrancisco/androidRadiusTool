package ar.org.fransis.radiustool.converter;

import androidx.room.TypeConverter;

import ar.org.fransis.radiustool.model.TestCase;

public class TestCaseConverter {
    @TypeConverter
    public static long toLong(TestCase test) {
        return test.getId();
    }

    @TypeConverter
    public static Long toTestCase(long id) {
        return null;
    }
}
