package com.codeBox.es.pool;

import com.codeBox.es.pool.model.EsClient;
import org.apache.commons.pool.impl.StackObjectPool;

/**
 * ClassName: es连接 <br/>
 * Function: <br/>
 * date: 2017/7/10 14:39 <br/>
 *
 * @author cyj
 * @since JDK 1.7
 */
public class EsClientPool {

    private String clusterName;

    private String ip;

    private String port;

    private int keepClienNum;

    private StackObjectPool<EsClient> pool;

    public EsClientPool(String clusterName, String ip, String port, int keepClienNum) {
        super();
        this.clusterName = clusterName;
        this.ip = ip;
        this.port = port;
        this.keepClienNum = keepClienNum;

        pool =  new StackObjectPool<EsClient>(new PoolableUserFactory(clusterName,ip,port), keepClienNum);
    }

    public String getClusterName() {
        return clusterName;
    }
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public int getKeepClienNum() {
        return keepClienNum;
    }
    public void setKeepClienNum(int keepClienNum) {
        this.keepClienNum = keepClienNum;
    }

    public EsClient getEsClient(){
        EsClient esClient = null;
        try {
            esClient = pool.borrowObject();
        } catch (Exception e) {
            System.out.println("create Client error!" );
            e.printStackTrace();
        }
        return esClient;
    }

    public EsClient removeEsClient(EsClient esClient){
        try {
            pool.returnObject(esClient);
        } catch (Exception e) {
            System.out.println("Client return to pool error!" );
            e.printStackTrace();
        }
        return esClient;
    }
}
