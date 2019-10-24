package com.pbteach.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-08 8:53
 **/
@Configuration
public class ElasticsearchConfig {
    //配置了es的ip和端口，多个地址中间以逗号分隔
    @Value("${xuecheng.elasticsearch.hostlist}")
    private String hostlist;

    @Bean
    public RestHighLevelClient restHighLevelClient(){

        String[] hostlistarry = hostlist.split(",");
        HttpHost[] httpHosts  =new HttpHost[hostlistarry.length];
        int i=0;
        for(String host:hostlistarry){
            String[] server = host.split(":");
            HttpHost httpHost = new HttpHost(server[0],Integer.parseInt(server[1]));
            httpHosts[i] = httpHost;
            i++;
        }

        RestHighLevelClient restHighLevelClient= new RestHighLevelClient(RestClient.builder(httpHosts));
        return restHighLevelClient;

    }
}
