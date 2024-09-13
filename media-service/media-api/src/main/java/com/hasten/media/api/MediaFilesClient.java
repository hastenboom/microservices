package com.hasten.media.api;

import com.hasten.common.domain.Result;
import com.hasten.media.domain.entity.MediaFiles;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Hasten
 */

@FeignClient(value = "media-service")
public interface MediaFilesClient {

    @GetMapping("/media/test/upload")
    Result<List<MediaFiles>> testUpload();

}
