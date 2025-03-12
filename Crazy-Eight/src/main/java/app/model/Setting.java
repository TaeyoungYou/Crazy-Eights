package app.model;

public class Setting {
    private static boolean enClicked = true;
    private static boolean krClicked = false;

    public static boolean isEnClicked() {
        return enClicked;
    }
    public static void setEnClicked(boolean enClicked) {
        Setting.enClicked = enClicked;
    }
    public static boolean isKrClicked() {
        return krClicked;
    }
    public static void setKrClicked(boolean krClicked) {
        Setting.krClicked = krClicked;
    }
}
