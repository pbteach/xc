package com.pbteach.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.pbteach.framework.domain.media.MediaFile;
import com.pbteach.framework.domain.media.MediaFileProcess_m3u8;
import com.pbteach.framework.utils.HlsVideoUtil;
import com.pbteach.framework.utils.Mp4VideoUtil;
import com.pbteach.manage_media_process.dao.MediaFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:44
 **/
@Component
public class MediaProcessTask {

    private static  final Logger LOGGER = LoggerFactory.getLogger(MediaProcessTask.class);

    //ffmpeg绝对路径
    @Value("${xc-service-manage-media.ffmpeg-path}")
    String ffmpeg_path;

    //上传文件根目录
    @Value("${xc-service-manage-media.video-location}")
    String serverPath;

    @Autowired
    MediaFileRepository mediaFileRepository;

    /**
     * 接收视频处理消息进行视频处理,只处理avi
     * 1、解析消息，得到 媒资文件的id，查询数据库得到文件信息（文件路径）
     * 2、调用工具类将视频编码为h264格式（mp4文件）
     * 3、调用工具类将mp4文件转成m3u8及ts文件
     * 4、处理成功将m3u8文件url存储到数据库
     *
     * @param message
     */
    @RabbitListener(queues={"${xc-service-manage-media.mq.queue-media-video-processor}"},containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String message){
        //解析消息，｛“mediaId”:XXX｝
        Map map = JSON.parseObject(message, Map.class);
        //得到媒资文件的id
        String mediaId = (String) map.get("mediaId");

        //查询数据库xc_media
        MediaFile mediaFile = mediaFileRepository.findOne(mediaId);
        if(mediaFile == null){
            LOGGER.error("query mediaFile from xc_media is null,message is:{}",message);
            return ;
        }
        //处理之前判断是否需要处理
        String fileType = mediaFile.getFileType();
        if(!fileType.equals("avi")){
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            return ;
        }
        //初始状态为处理串
        mediaFile.setProcessStatus("303001");//处理中
        mediaFileRepository.save(mediaFile);

        //调用工具类将视频编码为h264格式（mp4文件）,原avi文件的路径
        String video_path = serverPath + mediaFile.getFilePath()+mediaFile.getFileName();
        String mp4_name = mediaFile.getFileId()+".mp4";
        String mp4folder_path = serverPath + mediaFile.getFilePath();
        //创建工具类对象
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        //生成mp4
        String result = mp4VideoUtil.generateMp4();
        if(!result.equals("success")){
            //记录错误信息
            LOGGER.error("generateMp4 error ,video_path is {},error msg is {}",video_path,result);
            //更新处理状态为失败
            mediaFile.setProcessStatus("303003");
            mediaFileRepository.save(mediaFile);
            return ;
        }


        //调用工具类将mp4文件转成m3u8及ts文件
        //String ffmpeg_path, String video_path, String m3u8_name,String m3u8folder_path
        String mp4_video_path = serverPath + mediaFile.getFilePath() + mp4_name;
        //m3u8_name
        String m3u8_name = mediaFile.getFileId() +".m3u8";
        //m3u8folder_path
        String m3u8folder_path = serverPath + mediaFile.getFilePath() + "hls/";
        //生成m3u8文件
        HlsVideoUtil hlsVideoUtil =new HlsVideoUtil(ffmpeg_path,mp4_video_path,m3u8_name,m3u8folder_path);
        String m3u8_result = hlsVideoUtil.generateM3u8();
        if(!m3u8_result.equals("success")){
            //更新处理状态为失败
            mediaFile.setProcessStatus("303003");
            mediaFileRepository.save(mediaFile);
            return ;
        }
        //将视频处理状态更新成功
        mediaFile.setProcessStatus("303002");
        //m3u8的url
        String m3u8Url = mediaFile.getFilePath()+"hls/"+m3u8_name;
        mediaFile.setFileUrl(m3u8Url);
        //将ts文件的列表明细存储
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        //保存
        mediaFileRepository.save(mediaFile);

    }
}
