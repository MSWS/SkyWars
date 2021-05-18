package xyz.msws.skywars.utils;

/**
 * Utility method for futures
 *
 * @param <T>
 */
public interface Callback<T> {
    /**
     * Method to execute when the callback is called
     *
     * @param result given result, see {@link EmptyCallback} if no result is necessary
     */
    void execute(T result);
}
