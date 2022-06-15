package fr.esgi.musteat.backend.search.domain;

public enum SortType {
    Distance("distance"),
    Price("price"),
    Both("both");

    private final String text;

    SortType(String text) {
        this.text = text;
    }

    public static SortType fromString(String text) {
        for (SortType sortType : SortType.values()) {
            if (sortType.text.equalsIgnoreCase(text)) {
                return sortType;
            }
        }
        throw new IllegalArgumentException("No enum constant " + SortType.class.getName() + "." + text);
    }
}
