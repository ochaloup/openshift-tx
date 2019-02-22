package org.jboss.as.quickstarts.xa.client;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.as.quickstarts.xa.client.resources.Utils;

@Path("ejb")
public class EJBTestCallerRestEndpoints {

    @GET
    @Path("stateless-pass")
    @Produces("text/plain")
    public String testToPass() {
        BeanTestToPass bean = Utils.lookupModuleEJB(BeanTestToPass.class, null);
        return bean.call();
    }

    @GET
    @Path("stateless-jvm-halt-business")
    @Produces("text/plain")
    public String testToHaltJVMOnBusinessMethod() {
        BeanTestToKillJVMBusiness bean = Utils.lookupModuleEJB(BeanTestToKillJVMBusiness.class, null);
        return bean.call();
    }
}
