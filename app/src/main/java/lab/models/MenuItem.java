package lab.models;

import java.util.Objects;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lab.utils.MenuItemUtils;
import lab.utils.ValidationUtils;

public class MenuItem {

    @NotBlank(message = "name item must be stated")
    private String name;

    @Min(120)
    @DecimalMax(value = "500.00", message = "Price too high")
    private int price;

    @NotBlank(message = "Category must be stated")
    private String category;

    public static MenuItem createValidMenuItem(String name, int price, String category){
        MenuItem item = new MenuItem(name, price, category);
        ValidationUtils.validate(item);
        return item;
    }

    public MenuItem(){}

    public MenuItem(String name, int price, String category){

        setCategory(category);
        setName(name);
        setPrice(price);
    }

    public String getName(){

        return this.name;
    }

    public void setName(String name){

        if (MenuItemUtils.validName(name)){
            this.name = name;
        }

        else{

            throw new IllegalArgumentException("bad input name for name");
        }
    }

    public int getPrice(){

        return this.price;
    }

    public void setPrice(int price){

        if (MenuItemUtils.validPrice(price)){

            this.price = price;
        }

        else{

            throw new IllegalArgumentException("bad input for price");
        }
    }

    public String getCategory(){

        return this.category;
    }

    public void setCategory(String category){

        if (MenuItemUtils.validCategory(category)){

            this.category = category;
        }

        else{

            throw new IllegalArgumentException("bad input name for category");
        }
    }

    public static MenuItem createMenuItem(String name, int price, String category){

        if (MenuItemUtils.validCategory(category) && MenuItemUtils.validName(name) && MenuItemUtils.validPrice(price)){

            return new MenuItem(name, price, category);
        }

        else{

            return null;
        }
    }

    @Override
    public String toString(){

        return "MenuItem:\n" + this.name + "\n" + this.category + "\n" + Integer.toString(this.price) + "\n";
    }

    @Override
    public boolean equals(Object o){

        if (this == o){
            return true;
        }
        if (o == null || o.getClass() != getClass()){
            return false;
        }

        MenuItem item = (MenuItem) o;

        return Objects.equals(this.getName(), item.getName()) && Objects.equals(this.getCategory(), item.getCategory());
    }

    @Override
    public int hashCode(){
        return Objects.hash(name, category);
    }
}
