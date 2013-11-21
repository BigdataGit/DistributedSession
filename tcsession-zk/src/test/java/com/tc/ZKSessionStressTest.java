package com.tc;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.apache.catalina.connector.Request;
import org.junit.Test;

import com.tc.session.Configuration;
import com.tc.session.SessionMetaData;
import com.tc.session.TCSession;
import com.tc.session.TCSessionManager;
import com.tc.session.servlet.RemotableRequestWrapper;


/**
 * TCSession的压力测试
 *
 * @author gaofeng
 * @date Oct 16, 2013 7:05:48 PM
 * @id $Id$
 */
public class ZKSessionStressTest {
    
    private TCSessionManager sessionManager;
    
    @Test
    public void testStress(){
        HttpServletRequest req = new RemotableRequestWrapper(new Request(), sessionManager);
        sessionManager = new TCSessionManager();

        long now = System.currentTimeMillis();
        for(int j = 0; j < 10; j++){
            
        }
        for(int i = 0; i < 100; i++){
            String sessionId = "st_session_id";
            TCSession session = new TCSession(sessionManager, sessionId, req);
            SessionMetaData metadata = new SessionMetaData();
            metadata.setId(sessionId);
            int sessionTimeout = Configuration.getSessionTimeout();
            metadata.setMaxIdle(sessionTimeout * 60 * 1000); 
            sessionManager.getSessionClient().createSession(metadata);
            session.setAttribute("test_attr", "HelloWorld");
            Assert.assertTrue("HelloWorld".equals(session.getAttribute("test_attr")));
            session.invalidate();
        }
        System.out.println("Spent " + (System.currentTimeMillis() - now) + "ms");
    }
}
