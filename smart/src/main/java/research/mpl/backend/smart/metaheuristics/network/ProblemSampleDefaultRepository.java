package research.mpl.backend.smart.metaheuristics.network;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Marilia Portela on 01/03/2017.
 */
public class ProblemSampleDefaultRepository implements ProblemSampleRepository {
 
    @Inject
    private EntityManager entityManager;

    @Transactional
    @Override
    public ProblemSample create(ProblemSample sample) {
        entityManager.persist(sample);
        return sample;
    }

    @Override
    public ProblemSample find(Long id) {
        return entityManager.find(ProblemSample.class, id);
    }


    @Override
    public List<ProblemSample> findAll() {
        return entityManager.createQuery("SELECT i FROM ProblemSample i").getResultList();
    }

    @Override
    public void update(ProblemSample sample) {
        entityManager.merge(sample);
    }

    @Override
    public void delete(ProblemSample sample) {
        entityManager.remove(sample);
    }

}
