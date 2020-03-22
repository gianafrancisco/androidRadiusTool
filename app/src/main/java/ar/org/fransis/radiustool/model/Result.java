package ar.org.fransis.radiustool.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ar.org.fransis.radiustool.converter.TestCaseConverter;

@Entity(tableName = "result")
public class Result {
    @TypeConverters(TestCaseConverter.class)
    private TestCase mTestCase;
    private String mReplyMessage;
    private long mResponseTime;
    private String mResponseType;
    @PrimaryKey(autoGenerate = true)
    private long mId;

    public Result(TestCase testCase, String mReplyMessage, String mResponseType, long mResponseTime) {
        this.mTestCase = testCase;
        this.mReplyMessage = mReplyMessage;
        this.mResponseTime = mResponseTime;
        this.mResponseType = mResponseType;
    }
    public String getTestName()
    {
        return mTestCase.getName();
    }
    public String getReplyMessage()
    {
        return mReplyMessage;
    }
    public long getResponseTime()
    {
        return mResponseTime;
    }
    public String getResponseType()
    {
        return this.mResponseType;
    }
    public long getId()
    {
        return mId;
    }
    public void setId(long id)
    {
         this.mId = id;
    }

    public TestCase getTestCase() {
        return mTestCase;
    }
}
