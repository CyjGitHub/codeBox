package com.codeBox.es.pool.model;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.optimize.OptimizeRequest;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

/**
 * 获取ES Client单例
 * 
 * @ClassName: ClusterClient
 * @Description: 
 * @author liuchen
 * @date 2013-4-25 上午10:20:35
 *
 */
public  class EsClient {

	private String clusterName;

	private String ip;

	private String port;

	//ES Client
	private Client client;
	
	protected EsClient(String clusterName, int numberOfShards, int numberOfReplicas) {
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", "es_cluster_test_5")
		        .put("number_of_shards", 3)
                .put("number_of_replicas", 0)
		        .build();
		client =  new TransportClient(settings);
	}
	
	protected EsClient addTransport(String host, int port) {
		((TransportClient) client).addTransportAddress(new InetSocketTransportAddress(host, port));
		return this;
	}
	
	
	private void buildClient(){
		
		Builder builder = ImmutableSettings.settingsBuilder();
		builder.put("cluster.name",  clusterName);
		builder.put("transport.tcp.compress", true);
		
		String[] arrIp =ip.split(",");
		String[] arrPort = port.split(",");
		
		TransportAddress[] addressArr = new TransportAddress[arrPort.length];
		for (int i = 0 ,size = arrIp.length; i < size; i++) {
			String objIp = arrIp[i];
			int port = 9300;
			try {
				port = Integer.valueOf(arrPort[i]);
			} catch (NumberFormatException e) {
				System.out.println("port trans error !");
			}
			addressArr[i] = new InetSocketTransportAddress(
					objIp, port);
		}
		
		client = new TransportClient(builder)
			.addTransportAddresses(addressArr);
		System.out.println("开辟集群连接,address:"+"\t连接对象"+client);
		
	}
	
	public Client getClient() {
		return client;
	}

	public void rebuildClient(){
		System.out.println("上次client连接发生错误,重新开辟连接!");
		if(client != null){
			close();
		}
		buildClient();
	}
	
	public EsClient(String clusterName, String ip, String port) {
		this.clusterName = clusterName;
		this.ip=ip;
		this.port = port;
		buildClient();
	}
	
	public BulkRequestBuilder getBulkRequestBuilder(){
		return client.prepareBulk();
	}
	
	public IndexRequestBuilder getIndexRequestBuilder(){
		return client.prepareIndex();
	}
	
	public SearchRequestBuilder getEsSearch(){
		return client.prepareSearch();
	}
	
	public void esRefresh(String... args){
		client.admin().indices().prepareRefresh(args).execute().actionGet();
	}
	
	public DeleteRequestBuilder getDeleteRequestBuilder(){
		return client.prepareDelete();
	}
	public DeleteByQueryRequestBuilder getDeleteByQueryRequestBuilder(){
		return client.prepareDeleteByQuery();
	}
	/**
	 * 获取ES服务器的所有打开的索引
	 * 
	 * @Description: 
	 * @return
	 */
	public ClusterHealthResponse getClusterHealthResponse(){
		return client.admin().cluster().health(Requests.clusterHealthRequest().waitForGreenStatus()).actionGet();
	}
	
	/**
	 * 获取ES服务器的所有索引（包括打开和关闭的索引）
	 * 
	 * @Description: 
	 * @return
	 */
	public MetaData getMetaData(){
		ClusterState state = client.admin().cluster().prepareState().execute().actionGet().getState();
		return state.getMetaData();
	}
	
	
	public boolean isExists(String indexName){
		return client.admin().indices().prepareExists(indexName).execute().actionGet().isExists();
	}
	
	public boolean closeIndex(String indexName){
		return client.admin().indices().prepareClose(indexName).execute().actionGet().isAcknowledged();
	}
	
	public boolean deleteIndex(String indexName){
		return client.admin().indices().prepareDelete(indexName).execute().actionGet().isAcknowledged();
	}
	
	public boolean createIndex(Settings settings, String indexName){
		return client.admin().indices().prepareCreate(indexName).setSettings(settings).execute().actionGet().isAcknowledged();
	}
	
	public void createAlias(String indexName, String aliasName){
		client.admin().indices().prepareAliases().addAlias(indexName, aliasName).execute().actionGet();
	}
	
	public boolean putMapping(PutMappingRequest mappingRequest){
		return client.admin().indices().putMapping(mappingRequest).actionGet().isAcknowledged();
	}
	
	public OptimizeResponse optimize(String indexName){
		
		OptimizeRequest optRequest = new OptimizeRequest(new String[] { indexName });
		// optRequest.flush(true);
		optRequest.maxNumSegments(1);
		OptimizeResponse optResponse = client.admin().indices().optimize(
					optRequest).actionGet();
		return optResponse;
	}
	
	
	
	/**
	 * 关闭ES客户端
	 * 
	 * @Description:
	 */
	public void close(){
		System.out.println("\t关闭连接对象"+client);
		client.close();
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
