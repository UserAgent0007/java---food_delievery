package lab.Service.loader;

import lab.Service.LoadResult;
import lab.repository.DeliveryRepository;
import lab.repository.RestaurantRepository;

@FunctionalInterface
public interface LoadingStrategyNew {
    LoadResult load(
            DeliveryRepository deliveryRepository,
            RestaurantRepository restaurantRepository,
            DataLoader dataLoader
    );
}
