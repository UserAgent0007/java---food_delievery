package lab.Service.loader;

import lab.repository.*;
import lab.Service.LoadResult;

@FunctionalInterface
public interface LoadingStrategy {

    LoadResult load(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            DataLoader dataLoader
    );
}
