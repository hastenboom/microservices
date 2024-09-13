package com.hasten.media.biz.controller;


import com.hasten.common.domain.Result;
import com.hasten.media.biz.service.IMediaFilesService;
import com.hasten.media.domain.entity.MediaFiles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author Hasten
 * @since 2024-09-11
 */
@Api(value = "handle media")
@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaFilesController {

    private final IMediaFilesService mediaFilesService;

    @ApiOperation(value = "upload the small file")
    @PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload(
            @RequestPart("filedata") MultipartFile upload)
    {

        System.out.println("uploading a file.................................................................");

        return null;
    }

    @GetMapping(value = "/test/upload")
    public Result<List<MediaFiles>> testUpload() {
        System.out.println("test upload..........");

        List<MediaFiles> list = mediaFilesService.list();

        return Result.ok(list);
    }


}