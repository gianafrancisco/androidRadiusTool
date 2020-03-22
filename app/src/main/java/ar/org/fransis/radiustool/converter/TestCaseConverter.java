package ar.org.fransis.radiustool.converter;
import android.util.Log;

import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.org.fransis.radiustool.model.TestCase;

public class TestCaseConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @TypeConverter
    public static String toString(TestCase test) {

        String json = "";
        try {
            json = objectMapper.writeValueAsString(test);
        } catch (JsonProcessingException e) {
            Log.e("TestCaseConverter", e.toString());
        }
        return json;
    }

    @TypeConverter
    public static TestCase toTestCase(String json) {
        TestCase tc = null;
        try {
            tc = objectMapper.readValue(json, TestCase.class);
        } catch (JsonProcessingException e) {
            Log.e("TestCaseConverter", e.toString());
        }
        return tc;
    }
}
