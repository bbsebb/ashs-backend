package fr.hoenheimsports.trainingservice.models;

public enum Category {
    U11("-11 ans"),
    U13("-13 ans"),
    U15("-15 ans"),
    U18("-18 ans"),
    SENIOR("Senior"),
    EDH("École de Hand");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
