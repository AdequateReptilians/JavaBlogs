package dao;

import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by mik on 10.09.2016.
 */
public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {
    protected final Session session;
    protected final Class<T> type;

    public GenericDaoImpl(EntityManager em, Class<T> type) {
        HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
        this.session = hem.getSession();
        this.type = type;
    }

    public PK create(T newInstance){
        return (PK)session.save(newInstance);
    }

    /** Извлечь объект, предварительно сохраненный в базе данных, используя
     *   указанный id в качестве первичного ключа
     */
    public T findById(int id) {
        return session.get(type, id);
    }

    /** Сохранить изменения, сделанные в объекте.  */
    public void update(T transientObject){
        session.update(transientObject);
    }

    /** Удалить объект из базы данных */
    public void delete(T persistentObject){
        session.delete(persistentObject);
    }
}
