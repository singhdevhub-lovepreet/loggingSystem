package logger.enums;

public enum Severity {
    LOW("low"),
    HIGH("high"),
    WARN("warn");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    Severity(String name){
        this.name = name;
    }

}
