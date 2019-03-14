package org.jboss.as.quickstarts.xa.client;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class LookupHelper {
    private static final String JNDI_PKG_PREFIXES = "org.jboss.ejb.client.naming";
    private static final String REMOTE_DEPLOYMENT_NAME = "tx-server";

    private LookupHelper() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class, cannot be instantiated");
    }

    public static <T> T lookupRemoteEJBOutbound(Class<? extends T> beanImplClass, Class<T> remoteInterface, boolean isStateful, Properties ejbProperties) throws NamingException {
        return lookupRemoteEJBOutbound(beanImplClass.getSimpleName(), remoteInterface, isStateful, ejbProperties);
    }

    public static <T> T lookupRemoteEJBOutbound(String beanImplName, Class<T> remoteInterface, Properties ejbProperties) throws NamingException {
        return lookupRemoteEJBOutbound(beanImplName, remoteInterface, false, ejbProperties);
    }

    public static <T> T lookupRemoteStatelessEJBOutbound(String beanImplName, Class<T> remoteInterface) throws NamingException {
        return lookupRemoteEJBOutbound(beanImplName, remoteInterface, false, null);
    }

    public static <T> T lookupRemoteStatefulEJBOutbound(String beanImplName, Class<T> remoteInterface) throws NamingException {
        return lookupRemoteEJBOutbound(beanImplName, remoteInterface, true, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookupRemoteEJBOutbound(String beanImplName, Class<T> remoteInterface, boolean isStateful, Properties ejbProperties) throws NamingException {
        final Properties jndiProperties = new Properties();
        if(ejbProperties != null) jndiProperties.putAll(ejbProperties);
        jndiProperties.put(Context.URL_PKG_PREFIXES, JNDI_PKG_PREFIXES);
        // jndiProperties.put("jboss.naming.client.ejb.context", "true"); // ?
        final Context context = new InitialContext(jndiProperties);
        
        return (T) context.lookup("ejb:/" + REMOTE_DEPLOYMENT_NAME + "/" + beanImplName + "!"
                + remoteInterface.getName() + (isStateful ? "?stateful" : ""));
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookupRemoteEJBDirect(String beanImplName, Class<T> remoteInterface, boolean isStateful, Properties ejbProperties) throws NamingException {
        String remoteServerHost = System.getProperty("tx.server.host", "tx-server"); // TODO: maybe change the way

        Properties jndiProperties = new Properties();
        jndiProperties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(javax.naming.Context.PROVIDER_URL,"http-remoting://" + remoteServerHost);
        jndiProperties.put(Context.URL_PKG_PREFIXES, JNDI_PKG_PREFIXES);
        // jndiProperties.put("jboss.naming.client.ejb.context", "true"); // ?

        if(ejbProperties != null) jndiProperties.putAll(ejbProperties);

        final Context context = new InitialContext(jndiProperties);

        return (T) context.lookup("ejb:/" + REMOTE_DEPLOYMENT_NAME + "/" + beanImplName + "!"
                + remoteInterface.getName() + (isStateful ? "?stateful" : ""));
    }

    public static <T> T lookupModuleEJB(Class<T> beanImplClass) {
        return lookupModuleEJB(beanImplClass, null);
    }

    public static <T> T lookupModuleEJB(Class<T> beanImplClass, Properties ejbProperties) {
        final Properties jndiProperties = new Properties();
        if(ejbProperties != null) jndiProperties.putAll(ejbProperties);
        jndiProperties.put(Context.URL_PKG_PREFIXES, JNDI_PKG_PREFIXES);

        try {
            final Context context = new InitialContext(jndiProperties);
            return beanImplClass.cast(context.lookup("java:module/" + beanImplClass.getSimpleName()));
        } catch (NamingException ne) {
            throw new IllegalStateException("Not possible to lookup bean " + beanImplClass.getName(), ne);
        }
    }
}
