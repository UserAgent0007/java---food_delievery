package lab.Service;

public record LoadResult(
        int orderLoaded,
        int customersLoaded,

        long durationMs
) {
    public int getTotalItems() {
        return orderLoaded + customersLoaded;
    }

    @Override
    public String toString() {
        return String.format(
                "LoadResult{orders=%d, customers=%d, total=%d, duration=%dms}",
                orderLoaded, customersLoaded,
                getTotalItems(), durationMs
        );
    }
}
