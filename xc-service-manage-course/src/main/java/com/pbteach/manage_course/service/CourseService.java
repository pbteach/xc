package com.pbteach.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pbteach.framework.domain.cms.CmsPage;
import com.pbteach.framework.domain.cms.response.CmsPageResult;
import com.pbteach.framework.domain.course.*;
import com.pbteach.framework.domain.course.ext.CategoryNode;
import com.pbteach.framework.domain.course.ext.CourseInfo;
import com.pbteach.framework.domain.course.ext.CourseView;
import com.pbteach.framework.domain.course.ext.TeachplanNode;
import com.pbteach.framework.domain.course.request.CourseListRequest;
import com.pbteach.framework.domain.course.response.AddCourseResult;
import com.pbteach.framework.domain.course.response.CourseCode;
import com.pbteach.framework.domain.course.response.CoursePublishResult;
import com.pbteach.framework.exception.ExceptionCast;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.framework.model.response.QueryResult;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.manage_course.client.CmsPageClient;
import com.pbteach.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-30 11:47
 **/
@Service
public class CourseService {

    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CoursePubRepository coursePubRepository;

    @Autowired
    CmsPageClient cmsPageClient;

    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;



    //查询课程列表
   public QueryResponseResult<CourseInfo> findCourseList(String company_id,int page,int size,CourseListRequest courseListRequest){
        if(page<0){
            page = 1;
        }
        if(size<=0){
            size = 10;
        }
       //调用dao
       //设置分页参数
       PageHelper.startPage(page,size);
       //company_id传给 dao
       courseListRequest.setCompanyId(company_id);
       Page<CourseInfo> courseListPage = courseBaseMapper.findCourseListPage(courseListRequest);
       //列表
       List<CourseInfo> list = courseListPage.getResult();
       //总数
       long total = courseListPage.getTotal();
       QueryResult queryResult = new QueryResult();
       queryResult.setList(list);
       queryResult.setTotal(total);
       return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
   }

    //分类查询
    public CategoryNode findCategoryList(){
        CategoryNode categoryNode = categoryMapper.selectList();
        return categoryNode;
    }

    //新增课程
    public AddCourseResult add(CourseBase courseBase){
        //校验非空字段
        if(courseBase == null){
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }
        if(StringUtils.isEmpty(courseBase.getName())){
            //抛出异常..

        }
        //课程状态默认为制作中
        courseBase.setStatus("202001");
        CourseBase save = courseBaseRepository.save(courseBase);
        if(save!=null && StringUtils.isNotEmpty(save.getId())){
            return new AddCourseResult(CommonCode.SUCCESS,save.getId());
        }

        return new AddCourseResult(CommonCode.FAIL,null);
    }

    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId){
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }



    /**
     * 获取根结点,如果根结点找不到，创建根结点
     * @param courseId 课程id
     * @return 根结点id
     */
    private String getTeanplanRoot(String courseId){
        //根据课程id和parentid(0)查询teachplan
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if(teachplanList == null || teachplanList.size()==0){
            CourseBase one = courseBaseRepository.findOne(courseId);
            if(one == null){
                return null;
            }
            //创建根结点，向teachplan表添加一条记录
            Teachplan teachplan = new Teachplan();
            teachplan.setPname(one.getName());//根结点的名称为课程名称
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            Teachplan save = teachplanRepository.save(teachplan);
            return save.getId();

        }
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();

    }
    /**
     * 添加课程计划
     * 业务流程：
     * 1、添加一级结点
     * 1）确定根结点
     * 页面传入parentid为空， 处理根结点：
     *   找到一级结点所属的课程的根结点
     *   如果该课程为新课程那么根结点为空，自动创建课程根结点
     * 2）确定结点的层级 grade
     * 根据父结点来确定，如果该结点的grade为2，添加的新结点为3
     *
     * 2、添加二级结点
     * 1）确定根结点
     *   页面一定传入parentid
     * 2）确定结点的层级 grade
     *    根据父结点来确定，如果该结点的grade为2，添加的新结点为3；如果该结点的grade为1，添加的新结点为2
     *
     *
     * @param teachplan
     * @return
     */
    public ResponseResult addTeachplan(Teachplan teachplan){
        if(teachplan == null){
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }
        //得到课程 id
        String courseid = teachplan.getCourseid();

        //设置parentid
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(parentid)){
            //如果传入parentid为空说明要添加一级结点
            //先找到根结点 ，根据课程id和parentid查询teachplan表
            parentid = this.getTeanplanRoot(courseid);
        }
        if(StringUtils.isEmpty(parentid)){
            ExceptionCast.cast(CourseCode.COURSE_ADDTEACHPLAN_PARENTIDISNULL);
        }
        //根据parentid查询结点
        Teachplan parentNode = teachplanRepository.findOne(parentid);
        //父结点的层级
        String parent_grade = parentNode.getGrade();
        //子结点的层级
        String grade = null;
        if(parent_grade.equals("1")){
            grade = "2";
        }else  if(parent_grade.equals("2")){
            grade = "3";
        }
        teachplan.setParentid(parentid);
        teachplan.setGrade(grade);
        //状态为未发布
        teachplan.setStatus("0");
        teachplan.setCourseid(courseid);

        //向数据库保存课程计划
        Teachplan save = teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //保存课程图片到course_pic
    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        CoursePic one = coursePicRepository.findOne(courseId);

        if(one!=null){//保存图片
            one.setPic(pic);
            coursePicRepository.save(one);
        }else{
            CoursePic coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
            CoursePic save = coursePicRepository.save(coursePic);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //根据课程id查询课程图片
    public CoursePic findCoursepicList(String courseId) {
        CoursePic coursePic = coursePicRepository.findOne(courseId);
        return coursePic;
    }
    //删除课程图片
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        //删除课程图片信息，如果成功返回值大于0
        long result = coursePicRepository.deleteByCourseid(courseId);
        if(result>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            return new ResponseResult(CommonCode.FAIL);
        }

    }

    //根据课程id查询课程全部信息，此信息用于静态化
    public CourseView getCoruseView(String id) {
        CourseView courseView = new CourseView();
        CourseBase courseBase = courseBaseRepository.findOne(id);
        if(courseBase == null){
            return courseView;
        }
        //图片
        CoursePic coursePic = coursePicRepository.findOne(id);
        //营销信息
        CourseMarket courseMarket = courseMarketRepository.findOne(id);
        //课程计划
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setCourseBase(courseBase);
        courseView.setCourseMarket(courseMarket);
        courseView.setCoursePic(coursePic);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;


    }

    //课程预览

    /**
     * 业务流程
     * 1、前端请求课程预览
     * 2/此方法请求cms服务添加页面
     * 3/得到cms服务返回添加成功的页面id
     * 4、此方法根据页面id拼接成http://www.xuecheng.com/cms/preview/5b3469f794db44269cb2bff1地址
     * 5、将此页面预览的地址给前端返回
     * 6/前端在浏览器打开此页面预览地址
     * @param id
     * @return
     */
    public CoursePublishResult preview(String id) {
        CourseBase one = courseBaseRepository.findOne(id);
        //请求cms添加课程页面
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id+".html");//页面详情页面的名称为 "课程id.html"
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageAliase(one.getName());//页面别名就是课程名称
        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageCreateTime(new Date());
        //远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if(!cmsPageResult.isSuccess()){
            //抛出异常
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
        //获取新页面的id
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //构建一个页面预览的url
        String url = previewUrl+pageId;
        return new CoursePublishResult(CommonCode.SUCCESS,url);


    }


    //课程发布
    @Transactional
    public CoursePublishResult publish(String id) {
        CourseBase courseBase = courseBaseRepository.findOne(id);

        //准备页面信息
       /* CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id+".html");//页面详情页面的名称为 "课程id.html"
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageAliase(courseBase.getName());//页面别名就是课程名称
        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageCreateTime(new Date());*/
        //调用 cms的一键发布页面的接口
        /*CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_VIEWERROR);
        }*/
        //页面url
//        String pageUrl = cmsPostPageResult.getPageUrl();
        //更新课程状态,从数据字典中找课程发布状态
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);

        //将课程信息写到course_pub待索引表中，和更新课程状态属于一个事务。
        CoursePub coursePub = this.saveCoursePub(id);
        if(coursePub == null){
            ExceptionCast.cast(CommonCode.FAIL);
        }

        //将课程计划的媒资信息存储到teachplan_media_pub，将来由logstash采用此表的数据
        saveTeachplanMedia(id);


        return new CoursePublishResult(CommonCode.SUCCESS,"");
    }
    //保存teachplan_media_pub信息
    public void saveTeachplanMedia(String courseId){
        //由于根据课程id查询课程计划记录数是多个，所以准备采用先根据课程id删除teachplan_media_pub,再根据课程id查询teachplan_media，将查询到的记录添加到teachplan_media_pub
        //先根据课程id删除teachplanMediaPub
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        //再从teachplan_media查询
        List<TeachplanMedia> teachplanMedias = teachplanMediaRepository.findByCourseId(courseId);

        //定义list，最将将此list添加到TeachplanMediaPub表中
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for(TeachplanMedia teachplanMedia:teachplanMedias){
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            //将teachplanMedia数据拷贝到teachplanMediaPub
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            //存储时间戳
            teachplanMediaPub.setTimestamp(new Date());
            //加入list
            teachplanMediaPubList.add(teachplanMediaPub);

        }
        //向teachplanMediaPub添加数据
        teachplanMediaPubRepository.save(teachplanMediaPubList);
    }
    //向course_pub保存课程信息
    private CoursePub saveCoursePub(String courseId){

        //从数据库查询的对象
        CoursePub one = coursePubRepository.findOne(courseId);
        //创建coursePub对象
        CoursePub coursePub = createCoursePub(courseId);
        if(one == null){
            CoursePub save = coursePubRepository.save(coursePub);
            return save;
        }else{
            //将创建的新coursePub对象的值拷贝到one中
            BeanUtils.copyProperties(coursePub,one);
            //设置主键
            one.setId(courseId);
            //设置时间戳
            one.setTimestamp(new Date());
            CoursePub save = coursePubRepository.save(one);
            return save;
        }

    }

    //创建course_pub对象
    private CoursePub createCoursePub(String courseId){
        CoursePub coursePub = new CoursePub();
        //查询课程信息的各各表，组成一个coursePub对象
        CourseBase courseBase = courseBaseRepository.findOne(courseId);
        //将courseBase中属性拷贝到coursePub中
        //参数：原对象，目标对象，两者不为空
        BeanUtils.copyProperties(courseBase,coursePub);
        //copyProperties相当于 coursePub.setName(courseBase.getName());....

        //拼接营销信息
        CourseMarket courseMarket = courseMarketRepository.findOne(courseId);
        if(courseMarket!=null){
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        //图片信息
        CoursePic coursePic = coursePicRepository.findOne(courseId);
        if(coursePic!=null){
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        //课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        //将课程计划转成json存储到coursePub中
        String teachplanNodeString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachplanNodeString);

        //指定课程发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pubTime = simpleDateFormat.format(new Date());
        coursePub.setPubTime(pubTime);
        //时间戳,logstash根据此字段时间值决定是否索引
        coursePub.setTimestamp(new Date());

        return coursePub;
    }

    //保存课程计划相关联的视频
    @Transactional
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {

        if(teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())){
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }

        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();
        TeachplanMedia teachplanMedia_save = teachplanMediaRepository.findOne(teachplanId);
        if(teachplanMedia_save == null){
            teachplanMedia_save = new TeachplanMedia();
        }
        //校验只允许在第3级选择视频
        Teachplan teachplan = teachplanRepository.findOne(teachplanId);
        //获取层级
        String grade = teachplan.getGrade();
        if(!grade.equals("3")){
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_GRADEERROR);
        }
        teachplanMedia_save.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMedia_save.setMediaId(teachplanMedia.getMediaId());
        teachplanMedia_save.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        teachplanMedia_save.setTeachplanId(teachplanMedia.getTeachplanId());
        teachplanMedia_save.setCourseId(teachplanMedia.getCourseId());
        teachplanMediaRepository.save(teachplanMedia_save);
        return new ResponseResult(CommonCode.SUCCESS);

    }
}
