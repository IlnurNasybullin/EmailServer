package org.example.app.repositories;

public interface Repository<T> {
    T select(Object ID);

    boolean update(T object);
}
