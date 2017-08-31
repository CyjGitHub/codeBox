package com.codeBox.es.pool;

import com.codeBox.es.pool.model.EsClient;
import org.apache.commons.pool.BasePoolableObjectFactory;

public class PoolableUserFactory extends BasePoolableObjectFactory<EsClient> {

	private String clusterName;

	private String ip;

	private String port;

	public PoolableUserFactory(String clusterName, String ip, String port) {
		super();
		this.clusterName = clusterName;
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public EsClient makeObject() throws Exception {
		// TODO Auto-generated method stub
		EsClient esClient = new EsClient(clusterName,ip,port);
		System.out.println("create EsClient! " + esClient);
		return esClient;
	}

	@Override
	public void destroyObject(EsClient esClient) throws Exception {
		esClient.close();
		System.out.println("destroyObject EsClient! " + esClient);
		super.destroyObject(esClient);
	}

	@Override
	public void passivateObject(EsClient obj) throws Exception {
		System.out.println("return to pool");
		super.passivateObject(obj);
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
}
