package research.mpl.backend.common.cdi.extension;

/**
 * Created by Marilia Portela on 16/12/2016.
 */
import research.mpl.backend.common.cdi.qualifiers.Startup;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;
import java.util.Set;

public class StartupBeanExtension implements Extension {

    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event,
                                          BeanManager beanManager) {

        Set<Bean<?>> beans =
                beanManager.getBeans(Object.class,
                        new AnnotationLiteral<Startup>() {

                        });

        for (Bean<?> bean : beans) {
            beanManager.getReference(
                    bean,
                    bean.getTypes().iterator().next(),
                    beanManager.createCreationalContext(bean)).toString();
        }
    }
}
