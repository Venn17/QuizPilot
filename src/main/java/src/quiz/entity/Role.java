package src.quiz.entity;
public enum Role {
    USER("Basic", "user"),

    ADMINISTRATOR("Administrator", "adminstrator");

    public final String displayValue;
    public final String shortName; // arbitrary max lenght = 4

    Role(String display, String shortName) {
        this.displayValue = display;
        this.shortName = shortName;
    }

    public static Role fromShortName(String shortName) {

        switch (shortName) {
            case "user":
                return USER;
            case "adminstrator":
                return ADMINISTRATOR;
            default:
                throw new IllegalArgumentException("Short[" + shortName + "] not supported.");
        }
    }

}
