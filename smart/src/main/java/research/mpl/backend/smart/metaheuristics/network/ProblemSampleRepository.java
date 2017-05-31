package research.mpl.backend.smart.metaheuristics.network;


import java.util.List;

/**
 * Created by Marilia Portela on 01/03/2017.
 */
public interface ProblemSampleRepository {

    ProblemSample create(ProblemSample sample);

    void delete(ProblemSample item);

    ProblemSample find(Long id);

    List<ProblemSample> findAll();

    void update(ProblemSample sample);

}
