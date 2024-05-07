package fr.hoenheimsports.trainingservice.models;

public enum Gender {
    F("Féminin"),
    M("Masculin"),
    N("Mixte");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
