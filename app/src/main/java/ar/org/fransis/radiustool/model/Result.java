package ar.org.fransis.radiustool.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ar.org.fransis.radiustool.converter.TestCaseConverter;

@Entity
public class Result {
    public static long id = 0;
    @TypeConverters(TestCaseConverter.class)
    private TestCase mTestCase;
    private String mReplyMessage;
    private long mResponseTime;
    private String mResponseType;
    @PrimaryKey
    private long mId;

    public Result(TestCase testCase, String mReplyMessage, String mResponseType, long mResponseTime) {
        id++;
        mId = id;
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

    public TestCase getTestCase() {
        return mTestCase;
    }
}
