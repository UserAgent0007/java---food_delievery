package lab.models;

public enum testEnum {
    FIRST (1, "first"),
    SECOND (2, "second");

    public int number;
    public String text;

    private testEnum (int number, String text){
        this.number = number;
        this.text = text;
    }

    public String toString(){
        return String.valueOf(this.number) + this.text;
    }

}


