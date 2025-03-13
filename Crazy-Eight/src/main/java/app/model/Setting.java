package app.model;

/**
 * The Setting class manages the state of two static boolean flags,
 * `enClicked` and `krClicked`, used to represent some settings or toggle states.
 * It provides static methods to retrieve and update these flags.
 */
public class Setting {
    private static boolean enClicked = true;
    private static boolean krClicked = false;

    /**
     * Checks if the 'enClicked' flag is set to true, indicating that a certain
     * setting or toggle related to 'en' has been activated.
     *
     * @return true if the 'enClicked' flag is set to true, otherwise false.
     */
    public static boolean isEnClicked() {
        return enClicked;
    }
    /**
     * Updates the value of the static `enClicked` flag in the Setting class.
     *
     * @param enClicked the new boolean value to set for the `enClicked` flag
     */
    public static void setEnClicked(boolean enClicked) {
        Setting.enClicked = enClicked;
    }
    /**
     * Checks if the 'krClicked' flag is set to true, indicating that a certain
     * setting or toggle related to 'kr' has been activated.
     *
     * @return true if the 'krClicked' flag is set to true, otherwise false.
     */
    public static boolean isKrClicked() {
        return krClicked;
    }
    /**
     * Updates the value of the static `krClicked` flag in the Setting class.
     *
     * @param krClicked the new boolean value to set for the `krClicked` flag
     */
    public static void setKrClicked(boolean krClicked) {
        Setting.krClicked = krClicked;
    }
}
