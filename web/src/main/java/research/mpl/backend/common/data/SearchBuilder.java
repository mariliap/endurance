package research.mpl.backend.common.data;

/**
 * Created by Marilia Portela on 15/01/2017.
 */
public class SearchBuilder {

//    public static  Search<? extends GenericEntity> createAA(Class<? extends GenericEntity> clazz)
//           throws InstantiationException, IllegalAccessException {
//
//
//       Search<? extends GenericEntity> test = new Search<>();
//
//       return test.create(clazz);
//    }
//    public static void main(String[] args) {
//        try {
//            Search<EntityExample> x = SearchBuilder.createAA(EntityExample.class);
//            x.
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    public static <E extends GenericEntity> E createAA(Class<E> clazz)
            throws InstantiationException, IllegalAccessException {


        Search<? extends GenericEntity> test = new Search<>();

        return test.create(clazz);
    }
    public static void main(String[] args) {
        try {
            String a = SearchBuilder.createAA(EntityExample.class).getNumber() + " RESULTADO";
            System.out.println(a);

            String b = SearchBuilder.createAA(EntityExample.class).getStringVal() + " RESULTADO";
            System.out.println(a);

            //NewEntity a =SearchBuilder.createAA(EntityExample.class);


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
