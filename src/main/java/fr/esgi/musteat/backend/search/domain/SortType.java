package fr.esgi.musteat.backend.search.domain;

public enum SortType {
    DISTANCE("distance"),
    PRICE("price"),
    RATIO("ratio");

    private final String text;

    SortType(String text) {
        this.text = text;
    }

    public static SortType fromString(String text) {
        if (text.equals("")) {
            return SortType.RATIO;
        }
        for (SortType sortType : SortType.values()) {
            if (sortType.text.equalsIgnoreCase(text)) {
                return sortType;
            }
        }
        throw new IllegalArgumentException("No enum constant " + SortType.class.getName() + "." + text);
    }
}
