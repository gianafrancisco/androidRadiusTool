package ar.org.fransis.radiustool;

public enum Tab
{
    RADIUS(0, "Radius"),
    RESULTS(1, "Results"),
    DETAILS(2, "Details"),
    ABOUT(3, "About Me"),
    PREFERENCE(4, "Preferences");
    public final int value;
    public final String title;
    Tab(final int val, final String title)
    {
        this.value = val;
        this.title = title;
    }
}