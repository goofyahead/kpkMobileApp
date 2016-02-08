package kpklib.models;

/**
 * Created by goofyahead on 12/8/15.
 */
public class Open {
    String morningOpen;
    String morningClose;
    String afternoonOpen;
    String afternoonClose;
    boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getMorningOpen() {
        return morningOpen;
    }

    public void setMorningOpen(String morningOpen) {
        this.morningOpen = morningOpen;
    }

    public String getMorningClose() {
        return morningClose;
    }

    public void setMorningClose(String morningClose) {
        this.morningClose = morningClose;
    }

    public String getAfternoonOpen() {
        return afternoonOpen;
    }

    public void setAfternoonOpen(String afternoonOpen) {
        this.afternoonOpen = afternoonOpen;
    }

    public String getAfternoonClose() {
        return afternoonClose;
    }

    public void setAfternoonClose(String afternoonClose) {
        this.afternoonClose = afternoonClose;
    }
}
