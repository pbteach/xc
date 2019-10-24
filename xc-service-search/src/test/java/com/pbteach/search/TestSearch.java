package com.pbteach.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-08 8:51
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient client;

    //查询全部
    @Test
    public void testMathch_all() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        //设置搜索方式
        //全部搜索
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //分页参数
        searchSourceBuilder.from(0);//起始记录下标，从0开始
        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);

        }


    }

    //termquery
    @Test
    public void testTermQuery() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        //设置搜索方式
        //基本term query
//        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("name", "开发");
        String[] ids = new String[]{"1","2"};
        List<String> idList = Arrays.asList(ids);
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("_id",idList);
        searchSourceBuilder.query(termsQueryBuilder);
        //分页参数
        searchSourceBuilder.from(0);//起始记录下标，从0开始
        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);

        }


    }


    //mathchQuery
    @Test
    public void testMathchQuery() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        //设置matchQuery
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "spring开发");
        matchQueryBuilder.operator(Operator.OR);
        matchQueryBuilder.minimumShouldMatch("70%");//三个词至少匹配两个
        searchSourceBuilder.query(matchQueryBuilder);
        //分页参数
//        searchSourceBuilder.from(0);//起始记录下标，从0开始
//        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);

        }


    }

    //MultiMathchQuery
    @Test
    public void testMultiMathchQuery() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        //设置MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description");
//        multiMatchQueryBuilder.minimumShouldMatch("70%")
        multiMatchQueryBuilder.field("name",10);
        searchSourceBuilder.query(multiMatchQueryBuilder);


        //分页参数
//        searchSourceBuilder.from(0);//起始记录下标，从0开始
//        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);

        }


    }


    //boolQuery
    @Test
    public void testBoolQuery() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        //设置MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description");
//        multiMatchQueryBuilder.minimumShouldMatch("70%")
        multiMatchQueryBuilder.field("name",10);


        //基于termQuery
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("studymodel", "201002");

        //布尔查询builder
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termsQueryBuilder);


        //最终将布尔查询builder设置到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);


        //分页参数
//        searchSourceBuilder.from(0);//起始记录下标，从0开始
//        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);

        }


    }


    //filterQuery
    //针对termquery或rang范围查询，建议使用过虑器
    @Test
    public void testFilterQuery() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        //设置MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description");
//        multiMatchQueryBuilder.minimumShouldMatch("70%")
        multiMatchQueryBuilder.field("name",10);




        //布尔查询builder
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(multiMatchQueryBuilder);

        //设置过虑器
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("studymodel", "201001");
        boolQueryBuilder.filter(termsQueryBuilder);//根据studymodel过虑
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(60).lte(100);
        boolQueryBuilder.filter(rangeQueryBuilder); //根据价格范围过虑


        //最终将布尔查询builder设置到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);


        //分页参数
//        searchSourceBuilder.from(0);//起始记录下标，从0开始
//        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);

        }


    }

    //使用排序sort
    @Test
    public void testSort() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price"},new String[]{});
        //设置MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发框架", "name", "description");
//        multiMatchQueryBuilder.minimumShouldMatch("70%")
        multiMatchQueryBuilder.field("name",10);


        //布尔查询builder
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(multiMatchQueryBuilder);

        //设置过虑器
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(0).lte(100);
        boolQueryBuilder.filter(rangeQueryBuilder); //根据价格范围过虑

        //排序设置
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder("price").order(SortOrder.ASC);//根据价格升序
        searchSourceBuilder.sort(fieldSortBuilder);
//        searchSourceBuilder.sort()//可以设置多个排序字段

        //最终将布尔查询builder设置到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);


        //分页参数
//        searchSourceBuilder.from(0);//起始记录下标，从0开始
//        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);

        }


    }

    //高亮设置
    @Test
    public void testHighlighter() throws IOException {
        //创建搜索请求对象, 指定索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_a");
        searchRequest.types("doc");//指定类型

        //搜索builder
        SearchSourceBuilder searchSourceBuilder =new SearchSourceBuilder();
        //定义高亮设置参数
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag class='red'>");//高亮标签的前缀
        highlightBuilder.postTags("</tag>");//高亮标签的后缀
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //指定包括哪些字段，不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price"}, new String[]{});
        //设置MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发框架", "name", "description");
//        multiMatchQueryBuilder.minimumShouldMatch("70%")
        multiMatchQueryBuilder.field("name",10);


        //布尔查询builder
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(multiMatchQueryBuilder);

        //设置过虑器
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(0).lte(100);
        boolQueryBuilder.filter(rangeQueryBuilder); //根据价格范围过虑

        //排序设置
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder("price").order(SortOrder.ASC);//根据价格升序
        searchSourceBuilder.sort(fieldSortBuilder);
//        searchSourceBuilder.sort()//可以设置多个排序字段

        //最终将布尔查询builder设置到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);


        //分页参数
//        searchSourceBuilder.from(0);//起始记录下标，从0开始
//        searchSourceBuilder.size(1);//每页显示记录数
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        //解析搜索结果
        //匹配结果信息
        SearchHits hits = searchResponse.getHits();
        //搜索出来数据
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            //获取源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
            //取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //name字段高亮结果
            HighlightField highlightField = highlightFields.get("name");
            if(highlightField!=null){
                Text[] fragments = highlightField.getFragments();
                //高亮的name字段
                StringBuffer name  =new StringBuffer();
                for(Text text:fragments){
                    String string = text.string();
                    name.append(string);
                }
                System.out.println(name);
            }

        }


    }
}
