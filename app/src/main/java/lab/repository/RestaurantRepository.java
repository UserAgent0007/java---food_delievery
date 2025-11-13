package lab.repository;

import java.util.Comparator;
import java.util.List;

import lab.models.Restaurant;

public class RestaurantRepository extends GenericRepository<Restaurant>{
    
    public RestaurantRepository(){
        super(Restaurant::getName, "Restaurant");
    }

    public List<Restaurant> sortedRepository(){

        List<Restaurant> allItems = this.getAll();

        allItems.sort(
            Comparator.comparing(Restaurant::getName)
            .thenComparing(Restaurant::getCuisine)
        );

        return allItems;
    }

    public void showAllRestaurants(){

        this.getAll().stream().forEach(item->System.out.println(item));
    }
}
