package research.mpl.backend.common.data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marilia Portela on 29/12/2016.
 */
public class EntityDAO {
//
//
//    @Override
//    public List<T> createResultList(
//            Search<T> searchEntity) throws ServiceBusinessException {
//
//        return createPagedResultList(searchEntity, 0, 5);
//    }
//
//    public <E> E[] getArray(Class<E> clazz, int size) {
//        @SuppressWarnings("unchecked")
//        E[] arr = (E[]) Array.newInstance(clazz, size);
//        ArrayList.new
//        return arr;
//    }
//
//    @Override
//    public <T extends GenericEntity> List<T> createPagedResultList(
//            Search<T> searchEntity, int firstRow, int numberOfRows)
//            throws ServiceBusinessException {
//        try {
//            List<T> entitiesList = new ArrayList<T>();
//
//            List<T> entitiesToBeInserted = searchEntity.getEntitiesToBeInserted();
//            List<T> entitiesToBeDeleted = searchEntity.getEntitiesToBeDeleted();
//
//            int totalATrazer = numberOfRows;
//
//            if(entitiesToBeInserted != null && !entitiesToBeInserted.isEmpty()){
//                //Evaluate which page is being called
//                int pagina = (firstRow / numberOfRows) + 1;
//
//                //Calculate how many full pages are needed for new entities. The new pages will be the first ones,
//                // since new entities are added like a stack, on top of old entities
//                int numPaginasCheiasParaVincualcoesNovas = (entitiesToBeInserted.size() / numberOfRows);
//                //Calculate how many new entities are left to fill a non-full page
//                int numVincualcoesNovasRestantes = (entitiesToBeInserted.size() % numberOfRows);
//
//                if(pagina <= numPaginasCheiasParaVincualcoesNovas){
//                    int inicioIndexInclusivo = firstRow;
//                    int finalIndexExclusivo = firstRow + numberOfRows;
//                    entitiesList.addAll(entitiesToBeInserted.subList(inicioIndexInclusivo, finalIndexExclusivo));
//
//                    totalATrazer = 0;
//
//                }else if(pagina == (numPaginasCheiasParaVincualcoesNovas+1) && numVincualcoesNovasRestantes != 0){
//
//                    int inicioIndexInclusivo = entitiesToBeInserted.size() - numVincualcoesNovasRestantes;
//                    int finalIndexExclusivo = entitiesToBeInserted.size();
//                    entitiesList.addAll(entitiesToBeInserted.subList(inicioIndexInclusivo, finalIndexExclusivo));
//
//                    firstRow = firstRow - (numPaginasCheiasParaVincualcoesNovas*numberOfRows);
//                    totalATrazer = numberOfRows - numVincualcoesNovasRestantes;
//
//                }else if(pagina > numPaginasCheiasParaVincualcoesNovas){
//
//                    firstRow = firstRow - (numPaginasCheiasParaVincualcoesNovas*numberOfRows) - numVincualcoesNovasRestantes;
//                    totalATrazer = numberOfRows;
//
//                }
//            }
//
//            if(entitiesToBeDeleted != null && !entitiesToBeDeleted.isEmpty()){
//                vinculacaoCriteriaVisitor.applyFilterIdsDesconsiderados(entitiesToBeDeleted);
//            }
//
//            List<T> listaVinculacoes = find(searchEntity, "dataCadastro", false, firstRow, totalATrazer,
//                    vinculacaoCriteriaVisitor);
//
//            entitiesList.addAll(listaVinculacoes);
//
//            return entitiesList;
//
//        } catch (SGException e) {
//            throw new ServiceBusinessException(ApplicationMessages.ERROR_SALVAR, e);
//        }
//    }
//
//    @Override
//    public <T extends GenericEntity> Integer createResultCount(Search<T> searchEntity)
//            throws ServiceBusinessException {
//        try {
//            Integer totalVinculacoes = EBO.getDatasetSize(filter);
//            if(searchEntity.getEntitiesToBeInserted() != null && !searchEntity.getEntitiesToBeInserted().isEmpty()) {
//                totalVinculacoes = totalVinculacoes.intValue() + searchEntity.getEntitiesToBeInserted().size();
//            }
//            if(searchEntity.getEntitiesToBeDeleted() != null && !searchEntity.getEntitiesToBeDeleted().isEmpty()){
//                totalVinculacoes = totalVinculacoes.intValue() - searchEntity.getEntitiesToBeDeleted().size();
//            }
//            return totalVinculacoes;
//
//        } catch (SGException e) {
//            throw new ServiceBusinessException(ApplicationMessages.ERROR_SALVAR, e);
//        }
//    }
//
//
//
//
//    public List<T> find(Search<T> instance, String orderBy, boolean asc,
//                                    Integer firstResult, Integer maxResults,
//                                    MatchMode m, CriteriaVisitor visitor)
//            throws SGException {
//        Criteria criteria = createCriteria(instance, orderBy, asc, m, visitor);
//
//        if (firstResult != null) {
//            criteria.setFirstResult(firstResult);
//        }
//        if (maxResults != null) {
//            criteria.setMaxResults(maxResults);
//        }
//
//        if(visitor != null) {
//            visitor.visit(criteria);
//        }
//
//        return criteria.list();
//    }
//
//
//    private Criteria createCriteria(E instance, String orderBy, boolean asc,
//                                    MatchMode matchMode, CriteriaVisitor visitor) throws SGException {
//
//        Criteria criteria = getSession().createCriteria(instance.getClass());
//        if (instance.getId() != null) {
//            criteria.add(Restrictions.idEq(instance.getId()));
//        } else {
//            Example example = createExample(instance, matchMode);
//            criteria.add(example);
//            List<String> order = new ArrayList<String>();
//            if (orderBy != null) {
//                order = Arrays.asList(orderBy.split("\\."));
//            }
//            createSubCriteria(criteria, instance, order, asc, matchMode, false, visitor);
//        }
//        return criteria;
//    }
//
//    private void createSubCriteria(Criteria criteria, SGEntity instance,
//                                   List<String> order, boolean asc, MatchMode matchMode,
//                                   boolean leftJoin, CriteriaVisitor visitor) throws SGException {
//
//        boolean isLeftJoin = leftJoin;
//        /**
//         * Acrescentando no criterio ordena��o se existir
//         */
//        if (order.size() == 1) {
//            Order o = asc ? Order.asc(order.getSolutionAt(0)) : Order.desc(order.getSolutionAt(0));
//            criteria.addOrder(o.ignoreCase());
//        }
//
//        try {
//            PropertyDescriptor[] propertyDescriptors = PropertyUtils
//                    .getPropertyDescriptors(instance);
//
//            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//                Method method = propertyDescriptor.getReadMethod();
//                if (method != null
//                        && (method.isAnnotationPresent(ManyToOne.class) || method
//                        .isAnnotationPresent(OneToOne.class))) {
//
//                    String propertyName = propertyDescriptor.getName();
//                    Object sub = PropertyUtils.getProperty(instance,
//                            propertyName);
//                    /**
//                     * Se a subentidade existir e for do tipo SGEntity adicionar
//                     * como subcriteria.
//                     */
//                    if (sub != null && sub instanceof SGEntity) {
//                        Criteria subCriteria = null;
//                        // adicionar inner join apenas quando entidade notnull
//                        if (isLeftJoin
//                                || !method.isAnnotationPresent(NotNull.class)) {
//                            subCriteria = criteria.createCriteria(propertyName,
//                                    Criteria.LEFT_JOIN);
//                            isLeftJoin = true;
//                        } else {
//                            subCriteria = criteria.createCriteria(propertyName);
//                        }
//
//                        Long id = ((SGEntity) sub).getId();
//                        if (id != null) {
//                            subCriteria.add(Restrictions.idEq(id));
//                        } else {
//                            Example ex = createExample(sub, matchMode);
//                            subCriteria.add(ex);
//                            if(visitor != null) {
//                                visitor.visitSubCriteria(propertyName, subCriteria);
//                            }
//                            List<String> subOrder = new ArrayList<String>();
//                            if (order.size() > 1
//                                    && order.getSolutionAt(0).equals(propertyName)) {
//                                subOrder = order.subList(1, order.size());
//                            }
//                            createSubCriteria(subCriteria, (SGEntity) sub,
//                                    subOrder, asc, matchMode, isLeftJoin, visitor);
//                        }
//                    }
//
//                }
//            }
//
//        } catch (IllegalAccessException e) {
//            log.error(
//                    "S2GPR Framework: #0 criando criteria para classe #1 : ID [#2]",
//                    e, e.getClass(), instance.getClass(), instance.getId());
//            throw new SGException(e);
//        } catch (InvocationTargetException e) {
//            log.error(
//                    "S2GPR Framework: #0 criando criteria para classe #1 : ID [#2]",
//                    e, e.getClass(), instance.getClass(), instance.getId());
//            throw new SGException(e);
//        } catch (NoSuchMethodException e) {
//            log.error(
//                    "S2GPR Framework: #0 criando criteria para classe #1 : ID [#2]",
//                    e, e.getClass(), instance.getClass(), instance.getId());
//            throw new SGException(e);
//        }
//
//    }
//
//
//
}
