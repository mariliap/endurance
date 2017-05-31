package research.mpl.backend.smart.util;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Created by Marilia Portela on 14/12/2016.
 */
public class EntityManagerProducer {


    @Produces
    @ApplicationScoped
    @Default
    public EntityManagerFactory entityManagerFactory(){
        EntityManagerFactory emfA = Persistence.createEntityManagerFactory("hsqldb");
        return emfA;
    }


    @Produces
    @Default
    @RequestScoped
    protected EntityManager createEntityManager(EntityManagerFactory emfA)
    {
        return emfA.createEntityManager();
//        return this.entityManager;
    }

    protected void closeEntityManager( EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
    }

//    @Inject
//    @PersistenceUnitName("hsqldb")
//    private EntityManagerFactory emfA;
//
//    @Inject
//    @PersistenceUnitName("hsqldb")
//    private EntityManagerFactory entityManagerFactory;
//
//    @javax.enterprise.inject.Produces
//    @Default
//    @RequestScoped
//    protected EntityManager createEntityManager()
//    {
//        return entityManagerFactory.createEntityManager();
//    }


}
