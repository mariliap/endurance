package research.mpl.backend.common.data;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.FieldProxy;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;

import java.lang.annotation.Target;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by Marilia Portela on 29/12/2016.
 */
public class Search<E extends GenericEntity> implements Criterion {

    private E wrappedEntity;

    List<E> entitiesToBeInserted;

    List<E> entitiesToBeDeleted;


    interface FieldGetter {
        Object getValue();
    }

    interface FieldSetter {
        void setValue(Object value);
    }

    public class SetterInterceptor {
        @RuntimeType
        public  Object intercept(@FieldProxy("stringVal") FieldGetter accessor) {
            Object value = accessor.getValue();
            System.out.println("Invoked method with: " + value);
            return value;
        }
    }


//    private Search(E wrapped) {
//        wrappedEntity = wrapped;
//    }


    public <E extends GenericEntity> E create(Class<E> clazz) throws IllegalAccessException, InstantiationException {

//        E entity = clazz.newInstance();
//
//        PropertyDescriptor[] propertyDescriptors = PropertyUtils
//                .getPropertyDescriptors(entity);
//
//        DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition typeDefinition =  new ByteBuddy()
//                .subclass(getClass())
//                .method(ElementMatchers.named("toString"))
//                .intercept(FixedValue.value("Hello World!"));
//
//        for (PropertyDescriptor property : propertyDescriptors) {
//            typeDefinition.defineMethod(property.getName(), property.getPropertyType(), 1)
//                    .intercept(MethodCall.invoke(property.getReadMethod()));
//        }
//
//        Class<? extends Search<E>> dynamicType = typeDefinition
//                .make()
//                .load(getClass().getClassLoader())
//                .getLoaded();
//
//        return dynamicType.newInstance();




       // Method method = getClass().getMethod("getEntitiesToBeInserted");
        Class<? extends E> dynamicType = new ByteBuddy()
                .subclass(clazz)
                .name("NewEntity")
                .method(named("getNumber"))
                .intercept(FixedValue.value(100))
                .defineField("stringVal", String.class, Visibility.PRIVATE)
                .defineMethod("getStringVal", String.class, Visibility.PUBLIC)
                .intercept(FieldAccessor.ofBeanProperty())
                .make()
                .load(clazz.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        return dynamicType.newInstance();
    }

    public List<E> getEntitiesToBeInserted() {
        return entitiesToBeInserted;
    }

    public void setEntitiesToBeInserted(List<E> entitiesToBeInserted) {
        this.entitiesToBeInserted = entitiesToBeInserted;
    }

    public List<E> getEntitiesToBeDeleted() {
        return entitiesToBeDeleted;
    }

    public void setEntitiesToBeDeleted(List<E> entitiesToBeDeleted) {
        this.entitiesToBeDeleted = entitiesToBeDeleted;
    }

//    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException{
//        final StringBuilder buf = new StringBuilder().append( '(' );
//
//        PropertyDescriptor[] propertyDescriptors = PropertyUtils
//                .getPropertyDescriptors(wrappedEntity);
//
//        final EntityPersister meta = criteriaQuery.getFactory().getEntityPersister(
//                criteriaQuery.getEntityName( criteria )
//        );
//
//        final EntityPersister metaData = ((AbstractEntityPersister) sessionFactory.getClassMetadata(Hibernate.getClass(wrappedEntity)));
//
//        final String[] propertyNames = meta.getPropertyNames();
//        final Type[] propertyTypes = meta.getPropertyTypes();
//
//        final Object[] propertyValues = meta.getPropertyValues( exampleEntit);
//
//        for (PropertyDescriptor pd : Introspector.getBeanInfo(Foo.class).getPropertyDescriptors()) {
//            if (pd.getReadMethod() != null && !"class".equals(pd.getName()))
//                System.out.println(pd.getReadMethod().invoke(foo));
//        }
//
//        bject value = new PropertyDescriptor("name", Person.class).getReadMethod().invoke(person);
//
//        Object value = PropertyUtils.getProperty(person, "name");
//
//
//        return null;
//    }
//
//    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {
//            final StringBuilder buf = new StringBuilder().append( '(' );
//            final EntityPersister meta = criteriaQuery.getFactory().getEntityPersister(
//                    criteriaQuery.getEntityName( criteria )
//            );
//            final String[] propertyNames = meta.getPropertyNames();
//            final Type[] propertyTypes = meta.getPropertyTypes();
//
//            final Object[] propertyValues = meta.getPropertyValues( wrappedEntity );
//            for ( int i=0; i<propertyNames.length; i++ ) {
//                final Object propertyValue = propertyValues[i];
//                final String propertyName = propertyNames[i];
//
//                final boolean isVersionProperty = i == meta.getVersionProperty();
//                if ( ! isVersionProperty && isPropertyIncluded( propertyValue, propertyName, propertyTypes[i] ) ) {
//                    if ( propertyTypes[i].isComponentType() ) {
//                        appendComponentCondition(
//                            propertyName,
//                            propertyValue,
//                            (CompositeType) propertyTypes[i],
//                            criteria,
//                            criteriaQuery,
//                            buf
//                        );
//                    }
//                else {
//                        appendPropertyCondition(
//                            propertyName,
//                            propertyValue,
//                            criteria,
//                            criteriaQuery,
//                            buf
//                        );
//                }
//            }
//        }
//
//        List<Long> ignoredIds = new ArrayList<Long>();
//        for (E searchEntity: entitiesToBeDeleted){
//            ignoredIds.add(searchEntity.getId());
//        }
//        if (!ignoredIds.isEmpty()){
//            criteria.add(Restrictions.not(Restrictions.in("id", ignoredIds)));
//        }
//
//        if ( buf.length()==1 ) {
//            buf.append( "1=1" );
//      	}
//      	return buf.append( ')' ).toString();
//    }
//
//    protected void appendComponentCondition(String path, Object component, CompositeType type,
//                                            Criteria criteria, CriteriaQuery criteriaQuery, StringBuilder buf) {
//        if(component != null) {
//            String[] propertyNames = type.getPropertyNames();
//            Object[] values = type.getPropertyValues(component, this.getEntityMode(criteria, criteriaQuery));
//            Type[] subtypes = type.getSubtypes();
//
//            for(int i = 0; i < propertyNames.length; ++i) {
//                String subPath = StringHelper.qualify(path, propertyNames[i]);
//                Object value = values[i];
//                if(this.isPropertyIncluded(value, subPath, subtypes[i])) {
//                    Type subtype = subtypes[i];
//                    if(subtype.isComponentType()) {
//                        this.appendComponentCondition(subPath, value, (CompositeType)subtype, criteria, criteriaQuery, buf);
//                    } else {
//                        this.appendPropertyCondition(subPath, value, criteria, criteriaQuery, buf);
//                    }
//                }
//            }
//        }
//
//    }
//
//    protected void appendPropertyCondition(
//            String propertyName,
//            Object propertyValue,
//            Criteria criteria,
//            CriteriaQuery cq,
//            StringBuilder buf) {
//        final Criterion condition;
//        if ( propertyValue != null ) {
//            final boolean isString = propertyValue instanceof String;
//            if ( isLikeEnabled && isString ) {
//                condition = new LikeExpression(
//                        propertyName,
//                        (String) propertyValue,
//                        matchMode,
//                        escapeCharacter,
//                        isIgnoreCaseEnabled
//                );
//            }
//            else {
//                condition = new SimpleExpression( propertyName, propertyValue, "=", isIgnoreCaseEnabled && isString );
//
//            }
//        }
//        else {
//            condition = new NullExpression(propertyName);
//        }
//
//        final String conditionFragment = condition.toSqlString( criteria, cq );
//        if ( conditionFragment.trim().length() > 0 ) {
//            if ( buf.length() > 1 ) {
//                buf.append( " and " );
//            }
//            buf.append( conditionFragment );
//        }
//    }
//
    public TypedValue[] getTypedValues(Criteria var1, CriteriaQuery var2) throws HibernateException{
        return null;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException{
        return null;
    }
}
