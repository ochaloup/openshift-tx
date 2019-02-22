package org.jboss.as.quickstarts.xa.client;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.transaction.UserTransaction;

import org.jboss.as.quickstarts.xa.client.resources.Utils;
import org.jboss.as.quickstarts.xa.server.StatelessRemote;
import org.jboss.logging.Logger;

@Stateless
public class BeanTestToKillJVMBusiness {
    private static final Logger log = Logger.getLogger(BeanTestToKillJVMBusiness.class);
    private static final String BEAN_NAME = "StatelessBeanKillJVMBusiness";

    @Resource
    private UserTransaction userTransaction;

    public String call() {
        try {
            StatelessRemote bean = Utils.lookupRemoteEJBOutbound(BEAN_NAME, StatelessRemote.class, null);
            int status = bean.call();
            log.debugf("Transaction status from 'call' is %s", status);
        } catch (Exception e) {
            throw new RuntimeException("Error on calling bean " + BEAN_NAME, e);
        }

        return "SUCCESS";
    }
}
