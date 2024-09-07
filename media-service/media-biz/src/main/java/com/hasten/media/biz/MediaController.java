package com.hasten.media.biz;

import com.hasten.common.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Hasten
 */

@Api(value = "handle media")
@RestController
//@RequestMapping("/media")
public class MediaController {


    @ApiOperation(value = "upload the small file")
    @PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload(
            @RequestPart("filedata") MultipartFile upload)
    {

        System.out.println("uploading a file.................................................................");

        return null;
    }

    @GetMapping(value="test")
    public Result<String> test(){
        System.out.println("test test test");

        return null;
    }


}
