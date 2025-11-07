package lab.models;
import java.util.Objects;

import lab.utils.RestaurantUtils;

public class Restaurant {
    private String name;
    private String cuisine;
    private String location;

    public Restaurant(){}

    public Restaurant(String name, String cuisine, String loacation){

        setCuisine(cuisine);
        setLocation(loacation);
        setName(name);
    }

    public String getName(){

        return this.name;
    }

    public void setName(String name){

        this.name = RestaurantUtils.capitalize(name);
    }

    public String getCuisine(){

        return this.cuisine;
    }

    public void setCuisine(String cuisine){

        this.cuisine = cuisine;
    }

    public String location(){

        return this.location;
    }

    public void setLocation(String location){

        if (RestaurantUtils.validLocation(location)){

            this.location = location;
        }

        else{
            
            throw new IllegalArgumentException("bad input location");
        }
    }

    public static Restaurant createRestaurant (String name, String cuisine, String location){

        if (RestaurantUtils.validLocation(location)){

            return new Restaurant(name, cuisine, location);
        }

        else{

            return null;
        }
    }

    @Override
    public String toString(){

        return "Restaurant:\n" + name + "\n" + location + "\n" + cuisine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Restaurant restaurant = (Restaurant) o;

        return Objects.equals(this.location, restaurant.location) && Objects.equals(this.name, restaurant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name + location);
    }
}
