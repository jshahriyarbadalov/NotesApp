package dbmanager;

import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public interface Repository<T> {
    void add(T t);
    void update(T t);
    void remove(T t);
    List<T>findAll();
    List<T> find(Map<String, String> fieldList);
}
