package lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.exceptions.AlreadyExistsException;
import lab.exceptions.DataSerializationException;
import lab.exceptions.InvalidDataException;
//import lab.models.Delivery;
import lab.models.Restaurant;
//import lab.repository.DeliveryRepository;
import lab.repository.RestaurantRepository;
import lab.serializer.JsonDataSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "RestaurantServlet", urlPatterns = {"/restaurant", "/restaurant/*"})
public class RestaurantServlet extends BaseServlet{
    // Implementation goes here
    private JsonDataSerializer<Restaurant> serializer;
    private RestaurantRepository restaurantRepository;

    @Override
    public void init() throws ServletException {
        logger.info("=== CourseServlet init() ===");

        serializer = new JsonDataSerializer<>();

        restaurantRepository = (RestaurantRepository) getServletContext()
                .getAttribute("restaurantRepository");

        if (restaurantRepository == null) {
            logger.error("RestaurantRepository not found in ServletContext");
            throw new ServletException("Application not properly initialized");
        }

        logger.info("RestaurantServlet initialized with {} restaurants", restaurantRepository.size());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE_JSON);

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleGetAll(resp);
            } else {
                String restaurantId = pathInfo.substring(1);
                handleGetById(restaurantId, resp);
            }
        } catch (DataSerializationException e) {
            logger.error("Serialization error in doGet", e);
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);

        try {
            String requestBody = getRequestBody(req);

            Restaurant restaurant = serializer.fromString(requestBody, Restaurant.class);;

            restaurantRepository.add(restaurant);
            logger.info("restaurant created: {}", restaurant.getName());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(serializer.toString(restaurant));

        } catch (AlreadyExistsException e) {
            logger.warn("Restaurant already exists: {}", e.getMessage());
            sendError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (InvalidDataException e) {
            logger.warn("Invalid Restaurant data: {}", e.getMessage());
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (DataSerializationException e) {
            logger.error("Serialization error in doPost", e);
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE_JSON);

        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "restaurant name is required");
            return;
        }

        String restaurantId = pathInfo.substring(1);

        try {
            String requestBody = getRequestBody(req);
            Restaurant updatedRestaurant = serializer.fromString(requestBody, Restaurant.class);

            boolean updated = restaurantRepository.update(updatedRestaurant);

            if (!updated) {
                logger.warn("Restaurant not found: {}", restaurantId);
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Restaurant not found: " + restaurantId);
                return;
            }

            logger.info("Restaurant updated: {}", restaurantId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(updatedRestaurant));

        } catch (DataSerializationException e) {
            logger.error("Serialization error in doPut", e);
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "restaurant name is required");
            return;
        }

        String restaurantId = pathInfo.substring(1);
        boolean removed = restaurantRepository.removeByIdentity(restaurantId);

        if (!removed) {
            logger.warn("Restaurant not found for deletion: {}", restaurantId);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Restaurant not found: " + restaurantId);
            return;
        }

        logger.info("Restaurant deleted: {}", restaurantId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException, DataSerializationException {
        List<Restaurant> restaurants = restaurantRepository.getAll();
        logger.info("Retrieved {} restaurant", restaurants.size());

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(serializer.listToString(restaurants));
    }

    private void handleGetById(String restaurantId, HttpServletResponse resp)
            throws IOException, DataSerializationException {
        Optional<Restaurant> restaurant = restaurantRepository.findByIdentity(restaurantId);

        if (restaurant.isPresent()) {
            logger.info("Found restaurant: {}", restaurantId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(restaurant.get()));
        } else {
            logger.warn("Restaurant not found: {}", restaurantId);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Restaurant not found: " + restaurantId);
        }
    }

//    private void handleGetByIdentity(String identity, HttpServletResponse resp)
//            throws IOException, DataSerializationException {
//        Optional<Delivery> course = restaurantRepository.findByIdentity(identity);
//
//        if (course.isPresent()) {
//            logger.info("Found course: {}", identity);
//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.getWriter().write(serializer.toString(course.get()));
//        } else {
//            logger.warn("Course not found: {}", identity);
//            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Course not found: " + identity);
//        }
//    }

    /**
     * Decode URL-encoded path parameter
     */
    private String decodePathParam(String param) {
        try {
            return java.net.URLDecoder.decode(param, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warn("Failed to decode path param: {}", param);
            return param;
        }
    }

    @Override
    public void destroy() {
        logger.info("=== CourseServlet destroy() ===");
        logger.info("Total requests processed: {}", getRequestCount());
        logger.info("Final course count: {}",
                restaurantRepository != null ? restaurantRepository.size() : 0);
    }
}

