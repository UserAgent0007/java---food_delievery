package lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.exceptions.AlreadyExistsException;
import lab.exceptions.DataSerializationException;
import lab.exceptions.InvalidDataException;
import lab.models.Delivery;
import lab.repository.DeliveryRepository;
import lab.serializer.JsonDataSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "DeliveryServlet", urlPatterns = {"/delivery", "/delivery/*"})
public class DeliveryServlet extends BaseServlet {
    private JsonDataSerializer<Delivery> serializer;
    private DeliveryRepository deliveryRepository;

    @Override
    public void init() throws ServletException {
        logger.info("=== CourseServlet init() ===");

        serializer = new JsonDataSerializer<>();

        deliveryRepository = (DeliveryRepository) getServletContext()
                .getAttribute("deliveryRepository");

        if (deliveryRepository == null) {
            logger.error("DeliveryRepository not found in ServletContext");
            throw new ServletException("Application not properly initialized");
        }

        logger.info("CourseServlet initialized with {} restaurants", deliveryRepository.size());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE_JSON);

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleGetAll(resp);
            } else {
                String deliveryId = pathInfo.substring(1);
                handleGetById(deliveryId, resp);
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
            Delivery delivery = serializer.fromString(requestBody, Delivery.class);

            deliveryRepository.add(delivery);
            logger.info("Delivery created: {}", delivery.getId());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(serializer.toString(delivery));

        } catch (AlreadyExistsException e) {
            logger.warn("Delivery already exists: {}", e.getMessage());
            sendError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (InvalidDataException e) {
            logger.warn("Invalid Delivery data: {}", e.getMessage());
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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Delivery ID is required");
            return;
        }

        String deliveryId = pathInfo.substring(1);

        try {
            String requestBody = getRequestBody(req);
            Delivery updatedDelivery = serializer.fromString(requestBody, Delivery.class);

            boolean updated = deliveryRepository.update(updatedDelivery);

            if (!updated) {
                logger.warn("Delivery not found: {}", deliveryId);
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Delivery not found: " + deliveryId);
                return;
            }

            logger.info("Delivery updated: {}", deliveryId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(updatedDelivery));

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
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Delivery ID is required");
            return;
        }

        String deliveryId = pathInfo.substring(1);
        boolean removed = deliveryRepository.removeByIdentity(deliveryId);

        if (!removed) {
            logger.warn("Delivery not found for deletion: {}", deliveryId);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Delivery not found: " + deliveryId);
            return;
        }

        logger.info("Delivery deleted: {}", deliveryId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException, DataSerializationException {
        List<Delivery> deliveries = deliveryRepository.getAll();
        logger.info("Retrieved {} delivery", deliveries.size());

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(serializer.listToString(deliveries));
    }

    private void handleGetById(String deliveryId, HttpServletResponse resp)
            throws IOException, DataSerializationException {
        Optional<Delivery> delivery = deliveryRepository.findByIdentity(deliveryId);

        if (delivery.isPresent()) {
            logger.info("Found delivery: {}", deliveryId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(serializer.toString(delivery.get()));
        } else {
            logger.warn("Delivery not found: {}", deliveryId);
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Delivery not found: " + deliveryId);
        }
    }

//    private void handleGetByIdentity(String identity, HttpServletResponse resp)
//            throws IOException, DataSerializationException {
//        Optional<Delivery> course = deliveryRepository.findByIdentity(identity);
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
        logger.info("=== DeliveryServlet destroy() ===");
        logger.info("Total requests processed: {}", getRequestCount());
        logger.info("Final course count: {}",
                deliveryRepository != null ? deliveryRepository.size() : 0);
    }
}
