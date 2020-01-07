package ar.org.fransis.radiustool.model;

public class Result {
    public static int id = 0;
    private TestCase testCase;
    private String mReplyMessage;
    private long mResponseTime;
    private String mResponseType;
    private int mId;

    public Result(TestCase testCase, String mReplyMessage, String mResponseType, long mResponseTime) {
        id++;
        mId = id;
        this.testCase = testCase;
        this.mReplyMessage = mReplyMessage;
        this.mResponseTime = mResponseTime;
        this.mResponseType = mResponseType;
    }
    public String getTestName()
    {
        return testCase.getName();
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
    public int getId()
    {
        return mId;
    }
}
