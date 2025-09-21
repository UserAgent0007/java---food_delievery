package lab.utils;

public enum CuisineType {
    ITALIAN("ITALIAN", 500, 1000),
    CHINESE("CHINESE", 100, 300),
    MEXICAN("MEXICAN", 200, 350),
    AMERICAN("AMERICAN", 600, 1200),
    INDIAN("INDIAN", 50, 100);

    int min, max;
    String title;

    private CuisineType(String title, int min, int max){
        this.min = min;
        this.max = max;
        this.title = title;
    }

    public String showPriceRange(){
        return "For " + title + " cuisine\n" + "Min price - " + String.valueOf(min) + "\n" + "Max price - " + String.valueOf(max);
    }
}
