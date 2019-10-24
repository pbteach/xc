package com.pbteach.framework.domain.media.response;

import com.pbteach.framework.domain.media.MediaFile;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pbteach.com on 2018/3/31.
 */
@Data
@NoArgsConstructor
public class MediaFileResult extends ResponseResult {
    MediaFile mediaFile;
    public MediaFileResult(ResultCode resultCode, MediaFile mediaFile) {
        super(resultCode);
        this.mediaFile = mediaFile;
    }
}
