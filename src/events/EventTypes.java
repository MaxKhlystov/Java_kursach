package events;

public class EventTypes {
    // Client events
    public static final String CAR_ADDED = "CAR_ADDED";
    public static final String CAR_DELETED = "CAR_DELETED";
    public static final String PROFILE_UPDATED = "PROFILE_UPDATED";
    public static final String NOTIFICATIONS_VIEWED = "NOTIFICATIONS_VIEWED";

    // Mechanic events
    public static final String REPAIR_ADDED = "REPAIR_ADDED";
    public static final String REPAIR_STATUS_CHANGED = "REPAIR_STATUS_CHANGED";

    // Common events
    public static final String LOGOUT = "LOGOUT";
    public static final String VIEW_CARS = "VIEW_CARS";
    public static final String VIEW_REPAIRS = "VIEW_REPAIRS";
    public static final String SHOW_USER_GUIDE = "SHOW_USER_GUIDE";
    public static final String SHOW_ABOUT = "SHOW_ABOUT";
}