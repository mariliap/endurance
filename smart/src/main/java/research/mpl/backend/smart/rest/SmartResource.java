package research.mpl.backend.smart.rest;

import research.mpl.backend.smart.metaheuristics.network.Execution;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Marilia Portela on 02/03/2017.
 */

@Path("/smart")
@ApplicationScoped
public class SmartResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Execution execution;

    @GET
    @Produces({"application/json"})
    public String run() {
        System.out.println("inicio");
        execution.start();
        System.out.println("fim");
        return "aaaa";
    }
}
