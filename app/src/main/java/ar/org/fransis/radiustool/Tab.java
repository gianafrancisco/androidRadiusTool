package ar.org.fransis.radiustool;

public enum Tab
{
    RADIUS(0, "Radius"),
    DETAILS(2, "Details"),
    RESULTS(1, "Results"),
    ABOUT(3, "About Me"),
    PREFERENCE(4, "Preferences");
    public final int value;
    public final String title;
    private Tab(final int val, final String title)
    {
        this.value = val;
        this.title = title;
    }
}