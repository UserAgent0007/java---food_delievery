package lab.repository;

import java.util.*;
import java.util.logging.Logger;
// import java.util.logging.Level;

import lab.exceptions.AlreadyExistsException;
import lab.exceptions.InvalidDataException;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenericRepository<T> {
    private static final Logger logger = Logger.getLogger(GenericRepository.class.getName());

    private final List<T> items;
    private final IdentityExtractor<T> identityExtractor;
    private final String entityType;

    public GenericRepository(IdentityExtractor<T> identityExtractor, String entityType) {
//        this.items = new ArrayList<>();
        this.items = new CopyOnWriteArrayList<>();
        this.identityExtractor = identityExtractor;
        this.entityType = entityType;
//        logger.info("Created repository for " + entityType);
        logger.info("Created thread-safe repository for " + entityType);
    }

    public synchronized boolean add(T item) {
        if (item == null) {
            throw new InvalidDataException(entityType + " cannot be null");
        }

        String identity = identityExtractor.extractIdentity(item);
        if (findByIdentity(identity).isPresent()) {
            String errorMsg = String.format("%s already exists with identity: %s", entityType, identity);
            logger.warning(errorMsg);
            throw new AlreadyExistsException(errorMsg);
        }

        boolean added = items.add(item);
        if (added) {
            logger.info("Added " + entityType + ": " + identity);
        }
        return added;
    }

    public int addAll(Collection<T> newItems) {
        if (newItems == null || newItems.isEmpty()) {
            return 0;
        }

        int addedCount = 0;
        for (T item : newItems) {
            try {
                if (add(item)) {
                    addedCount++;
                }
            } catch (AlreadyExistsException e) {
                logger.config("Skipping duplicate: " + e.getMessage());
            }
        }
        logger.info("Bulk added " + addedCount + "of " + newItems.size() + " items to " +  entityType);
        return addedCount;
    }

    public synchronized boolean remove(T item) {
        if (item == null) {
            logger.warning("Attempted to remove null " + entityType);
            return false;
        }

        boolean removed = items.remove(item);
        if (removed) {
            logger.info("Removed " + entityType + ": " + identityExtractor.extractIdentity(item));
        } else {
            logger.warning("Failed to remove " + entityType + ": " + identityExtractor.extractIdentity(item));
        }
        return removed;
    }

    public synchronized boolean removeByIdentity(String identity) {
        if (identity == null) {
            logger.warning("Attempted to remove " + entityType + " with null identity");
            return false;
        }

//        Optional<T> itemToRemove = items.stream()
//                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
//                .findFirst();

        Optional<T> itemToRemove = findByIdentityInternal(identity);

        if (itemToRemove.isPresent()) {
            boolean removed = items.remove(itemToRemove.get());
            if (removed) {
                logger.info("Removed " + entityType + " by identity: " + identity);
            }
            return removed;
        } else {
            logger.warning("No " + entityType + " found with identity: " + identity + " to remove");
            return false;
        }
    }

    /**
     * Check if repository contains an item using equals()
     */
    public boolean contains(T item) {
        return items.contains(item);
    }

    /**
     * Check if repository contains an item with given identity
     */
    public boolean containsIdentity(String identity) {
        return findByIdentityInternal(identity).isPresent();
    }

    /**
     * Find an object by its unique identity field
     */
    public Optional<T> findByIdentity(String identity) {
        if (identity == null) {
            logger.warning("Attempted to find " + entityType + " with null identity");
            return Optional.empty();
        }

        Optional<T> result = items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();

        if (result.isPresent()) {
            logger.info("Found " + entityType + " with identity: " + identity);
        } else {
            logger.info("No " + entityType + " found with identity: " + identity);
        }

        return result;
    }

    private Optional<T> findByIdentityInternal(String identity) {
        return items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();
    }

    public List<T> getAll() {
        logger.info("Retrieved all " + entityType + " items. Count: " + items.size());
        return new ArrayList<>(items);
    }

    public T getElemmentByIndex(Integer index){
        
        if (index == null) {
            logger.warning("Attempted to find " + entityType + " with null index");
            return null;
        }

        try{
            T result = items.get(index);

            return result;
        }catch (IndexOutOfBoundsException i){
            logger.warning("Attempted to find " + entityType + " with wrong index");
            return null;
        }
        
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public synchronized void clear() {
        int sizeBefore = items.size();
        items.clear();
        logger.info("Cleared repository. Removed " + sizeBefore + " " + entityType + " items");
    }

    /**
     * Використано Comparator.compare
     * @param asc
     */

    public synchronized void sortByIdentity(boolean asc){

        List<T> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(identityExtractor::extractIdentity));
        if (!asc){
            Collections.reverse(sorted);
            
            logger.info("Sorted " + entityType +" by identity in descending order");
        }

        else{
            logger.info("Sorted " + entityType +" by identity in ascending order");
        }

        items.clear();
        items.addAll(sorted);
        
    }

    List<T> getItemsForTesting() {
        return new ArrayList<>(items);
    }

    public void addList(List<T> list1){

        for (var elem : list1){
            this.add(elem);
        }
    }
}
