package com.hasten;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Hasten
 */
public class Test {

    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    public void uploadChunk() {
        String chunkFolderPath = "D:\\develop\\upload\\chunk\\";
        File chunkFolder = new File(chunkFolderPath);
        //分块文件
        File[] files = chunkFolder.listFiles();

        /*这里是模拟了服务器怎么处理分块过来的数据*/
        //将分块文件上传至minio
        for (int i = 0; i < files.length; i++) {
            try {
                UploadObjectArgs uploadObjectArgs =
                        UploadObjectArgs.builder().bucket("testbucket").object("chunk/" + i).filename(files[i].getAbsolutePath()).build();
                minioClient.uploadObject(uploadObjectArgs);
                System.out.println("上传分块成功" + i);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void test_merge() throws Exception {
        List<ComposeSource> sources = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ComposeSource source = ComposeSource.builder()
                    .bucket("testbucket")
                    .object("chunk/" + i)
                    .build();
            sources.add(source);
        }

        ComposeObjectArgs composeObjectArgs =
                ComposeObjectArgs.builder().bucket("testbucket").object("merge01.mp4").sources(sources).build();

        minioClient.composeObject(composeObjectArgs);

    }

    //清除分块文件
    public void test_removeObjects() {
        //合并分块完成将分块文件清除
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(6)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs =
                RemoveObjectsArgs.builder().bucket("testbucket").objects(deleteObjects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(r -> {
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
