package fransis.org.ar.radiustool;

public class AdSingleton {

    private boolean showStartUpAd = true;
    private int adCount = 0;

    private static AdSingleton instance = null;

    private AdSingleton(){

    }

    public static AdSingleton getInstance(){
        if(instance == null){
            instance = new AdSingleton();
        }
        return instance;
    }

    public boolean isShowStartUpAd() {
        return showStartUpAd;
    }

    public void setShowStartUpAd(boolean showStartUpAd) {
        this.showStartUpAd = showStartUpAd;
    }
    public boolean showAd(){
        adCount++;
        boolean show = false;
        if(adCount == 10){
            adCount = 0;
            show = true;
        }
        return show;
    }
}
