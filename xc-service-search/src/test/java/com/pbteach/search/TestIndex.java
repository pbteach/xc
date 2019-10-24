package com.pbteach.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-08 8:51
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    RestHighLevelClient client;

    /**
     {
     "settings":{
     "index":{
     "number_of_shards":1,#分片的数量
     "number_of_replicas":0#副本数量
     }
     }
     }

     */
    @Test
    public void testCreateIndex() throws IOException {
        //用于创建索引库的对象，指定索引库名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_a");

        //设置索引库的分片数量及副本的数量
        createIndexRequest.settings(
                Settings.builder().put("number_of_shards",1)//索引库的分片数量
                .put("number_of_replicas",0));
        //参数：String type, String source, XContentType xContentType
        String mapping_source = " {\n" +
                " \t\"properties\": {\n" +
                "           \"name\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"description\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"studymodel\": {\n" +
                "              \"type\": \"keyword\"\n" +
                "           },\n" +
                "           \"price\": {\n" +
                "              \"type\": \"float\"\n" +
                "           },\n" +
                "           \"timestamp\": {\n" +
                "          \t\t\"type\":   \"date\",\n" +
                "          \t\t\"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n" +
                "        \t}\n" +
                "        }\n" +
                "}";
        createIndexRequest.mapping("doc",mapping_source, XContentType.JSON);

        //创建IndicesClient对象向ES提交请求
        IndicesClient indices = client.indices();
        //创建索引库
        CreateIndexResponse  createIndexResponse= indices.create(createIndexRequest);
        //得到响应的结果
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);

    }

    @Test
    public void testDeleteIndex() throws IOException {
        //用于创建索引库的对象，指定索引库名称
       DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_a");

        //创建IndicesClient对象向ES提交请求
        IndicesClient indices = client.indices();
        //创建索引库
        DeleteIndexResponse deleteIndexResponse = indices.delete(deleteIndexRequest);
        //得到响应的结果
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    //创建文档（索引）
    @Test
    public void testAddDoc() throws IOException {
        //创建添加文档的对象
        IndexRequest indexRequest = new IndexRequest("xc_a","doc","2");

        //文档的内容，就是map格式
        //准备json数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);
        indexRequest.source(jsonMap);
        //创建client对象向ES提交请求
        //请求es添加文档
        IndexResponse indexResponse = client.index(indexRequest);
        //获取响应结果
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);

    }

    //查询文档
    @Test
    public void testGetDoc() throws IOException {
        //创建添加文档的对象
        GetRequest getRequest = new GetRequest("xc_a","doc","2");

        //创建client对象向ES提交请求
        //请求es查询文档
        GetResponse getResponse = client.get(getRequest);
        boolean exists = getResponse.isExists();
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }
    //更新文档
    @Test
    public void testUpdateDoc() throws IOException {
        //创建添加文档的对象
        GetRequest getRequest = new GetRequest("xc_a","doc","2");

        //创建client对象向ES提交请求
        //请求es查询文档
        GetResponse getResponse = client.get(getRequest);
        boolean exists = getResponse.isExists();
        if (exists) {
            //更新
            UpdateRequest updateRequest =new UpdateRequest("xc_a","doc","2");
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("name", "spring cloud基础入门");
            updateRequest.doc(jsonMap);
            UpdateResponse updateResponse = client.update(updateRequest);
            DocWriteResponse.Result result = updateResponse.getResult();
            System.out.println(result);
        }
    }

    //删除文档
    @Test
    public void testDeleteDoc() throws IOException {
        //删除文档对象
        DeleteRequest deleteRequest = new DeleteRequest("xc_a","doc","2");
        DeleteResponse deleteResponse = client.delete(deleteRequest);
        DocWriteResponse.Result result = deleteResponse.getResult();
        System.out.println(result);
    }
}
