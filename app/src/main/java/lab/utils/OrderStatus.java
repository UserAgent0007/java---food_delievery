package lab.utils;

public enum OrderStatus {
    PENDING ("Очікується"), 
    CONFIRMED ("Підтверджено"), 
    PREPARING ("Готується"), 
    DELIVERED ("Доставлено"), 
    CANCELED ("Відмінено");

    String status;

    private OrderStatus (String status){
        this.status = status;
    }

    public String toString(){
        return "The status is - " + status;
    }
}
