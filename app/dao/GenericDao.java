package dao;

import java.io.Serializable;

/**
 * Created by mik on 10.09.2016.
 */
public interface GenericDao <T, PK extends Serializable> {

    /** Сохранить объект newInstance в базе данных */
    T create(T newInstance);

    /** Извлечь объект, предварительно сохраненный в базе данных, используя
     *   указанный id в качестве первичного ключа
     */
    T findById(int id);

    /** Сохранить изменения, сделанные в объекте.  */
    void update(T transientObject);

    /** Удалить объект из базы данных */
    void delete(T persistentObject);
}
