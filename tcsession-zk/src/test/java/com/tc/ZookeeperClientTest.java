
package com.tc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.tc.session.SessionClient;
import com.tc.session.SessionMetaData;
import com.tc.session.zookeeper.ZookeeperSessionClient;

/**
 * 測試類，描述你妹
 * 
 * @author gaofeng
 * @date Sep 17, 2013 2:59:00 PM
 * @id $Id$
 */
public class ZookeeperClientTest {
    
    private SessionClient sessionClient = ZookeeperSessionClient.getInstance();
    protected static Random random = new Random();
    
//    @Test
//    public void testCreateHomeNode() {
//    
//        sessionClient.createSession(null);
//    }
    
    @Test
    public void testGetSession(){
        long start = System.currentTimeMillis();
        SessionMetaData metadata = new SessionMetaData();
        String id = random.nextInt() + "";
        metadata.setId(id);
        metadata.setCreateTime(new Date().getTime());
        metadata.setLastAccessTime(new Date().getTime());
        metadata.setMaxIdle(5000);
//        metadata.setValid(true);
        metadata.setVersion(0);
        boolean flag = sessionClient.createSession(metadata);
        System.out.println("Total spends: " + (System.currentTimeMillis() - start) + " Result: " + flag);
        Assert.assertTrue(flag);
        
        metadata = sessionClient.getSession(id);
        Assert.assertNotNull(metadata);
    }
    
    @Test
    public void testGetAttribute() {
        long start = System.currentTimeMillis();
        SessionMetaData metadata = new SessionMetaData();
        String id = random.nextLong() + "";
        metadata.setId(id);
        metadata.setCreateTime(new Date().getTime());
        metadata.setLastAccessTime(new Date().getTime());
        metadata.setMaxIdle(5000);
//        metadata.setValid(true);
        metadata.setVersion(0);
        boolean flag = sessionClient.createSession(metadata);
        System.out.println("Total spends: " + (System.currentTimeMillis() - start) + " Result: " + flag);
        Assert.assertTrue(flag);
        
        String key = "mykey";
        String value = "HelloWorld!";
        flag = sessionClient.setAttribute(id, key, value);
        Assert.assertTrue(flag);
        
        String v = (String)sessionClient.getAttribute(id, key);
        Assert.assertEquals(v, value);
    }
    
    @Test
    public void testRemoveAttribute() {
        long start = System.currentTimeMillis();
        SessionMetaData metadata = new SessionMetaData();
        String id = random.nextLong() + "";
        metadata.setId(id);
        metadata.setCreateTime(new Date().getTime());
        metadata.setLastAccessTime(new Date().getTime());
        metadata.setMaxIdle(5000);
//        metadata.setValid(true);
        metadata.setVersion(0);
        boolean flag = sessionClient.createSession(metadata);
        System.out.println("Total spends: " + (System.currentTimeMillis() - start) + " Result: " + flag);
        Assert.assertTrue(flag);
        
        String key = "mykey";
        String value = "HelloWorld!";
        flag = sessionClient.setAttribute(id, key, value);
        Assert.assertTrue(flag);
        
        String v = (String)sessionClient.getAttribute(id, key);
        Assert.assertEquals(v, value);
        
        flag = sessionClient.removeAttribute(id, key);
        Assert.assertTrue(flag);
        Assert.assertTrue(sessionClient.getAttribute(id, key) == null);
    }
    
    @Test
    public void testGetAttributeNames(){
        long start = System.currentTimeMillis();
        SessionMetaData metadata = new SessionMetaData();
        String id = random.nextLong() + "";
        metadata.setId(id);
        metadata.setCreateTime(new Date().getTime());
        metadata.setLastAccessTime(new Date().getTime());
        metadata.setMaxIdle(5000);
//        metadata.setValid(true);
        metadata.setVersion(0);
        boolean flag = sessionClient.createSession(metadata);
        System.out.println("Total spends: " + (System.currentTimeMillis() - start) + " Result: " + flag);
        Assert.assertTrue(flag);
        
        String key = "mykey1";
        String value = "HelloWorld!";
        flag = sessionClient.setAttribute(id, key, value);
        Assert.assertTrue(flag);
        
        key = "mykey2";
        value = "Tesla";
        flag = sessionClient.setAttribute(id, key, value);
        Assert.assertTrue(flag);
        
        List<String> l = sessionClient.getAttributeNames(id);
        Assert.assertTrue(l.size() == 2);
        Assert.assertTrue(l.get(1).equals("mykey2"));;
    }
    
    @Test
    public void testRemoveSession(){
        long start = System.currentTimeMillis();
        SessionMetaData metadata = new SessionMetaData();
        String id = random.nextLong() + "";
        metadata.setId(id);
        metadata.setCreateTime(new Date().getTime());
        metadata.setLastAccessTime(new Date().getTime());
        metadata.setMaxIdle(5000);
//        metadata.setValid(true);
        metadata.setVersion(0);
        boolean flag = sessionClient.createSession(metadata);
        System.out.println("Total spends: " + (System.currentTimeMillis() - start) + " Result: " + flag);
        Assert.assertTrue(flag);
        
        metadata = sessionClient.getSession(id);
        Assert.assertNotNull(metadata);
        
        String key = "mykey1";
        String value = "HelloWorld!";
        flag = sessionClient.setAttribute(id, key, value);
        Assert.assertTrue(flag);
        
        key = "mykey2";
        value = "Tesla";
        flag = sessionClient.setAttribute(id, key, value);
        Assert.assertTrue(flag);
        
        Map<String, Object> m = sessionClient.removeSession(id);

        Assert.assertTrue(m.size() == 2);
        Assert.assertTrue(m.get("mykey2").equals("Tesla"));
        Assert.assertTrue(sessionClient.getSession(id) == null);
    }
    
    @Test
    public void testGetSessions(){
        List<String> sessionIds = sessionClient.getSessions();
        if(sessionIds == null){
            return;
        }
        System.out.println("Before retrieving data from zk. " + new Date());
        ArrayList<SessionMetaData> al = new ArrayList<SessionMetaData>();
        for(String sessionId : sessionIds){
            SessionMetaData metadata = sessionClient.getSession(sessionId);
//            if(metadata == null)continue;
//            if (metadata.getLastAccessTime() + 300000 < System.currentTimeMillis()) {
//                sessionClient.removeSession(sessionId);
//            }
            al.add(metadata);
        
        }
        System.out.println("Fetched data from zk successfully, prepare to sort" + new Date());
        Collections.sort(al, new Comparator<SessionMetaData>(){

            @Override
            public int compare(SessionMetaData o1, SessionMetaData o2) {
                return (int)(o1.getLastAccessTime() - o2.getLastAccessTime());
            }});
        
        for(SessionMetaData metadata : al){
            System.out.println(metadata.getId() + " : " + new Date(metadata.getLastAccessTime()) + " : " + metadata.getMaxIdle());
        }
        System.out.println("Total sessions: " + al.size());
        System.out.println("Finished processing." + new Date());
    }
}
