package com.hasten;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class MediaBizApplicationTests {
    @Test
    public void testMerge() throws IOException {
        //块文件目录
        File chunkFolder = new File("d:/develop/bigfile_test/chunk/");

        //合并文件
        File mergeFile = new File("d:/develop/bigfile_test/nacos01.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
        //用于写文件
        RandomAccessFile rafMergeFile = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        rafMergeFile.seek(0);
        //缓冲区
        byte[] buf = new byte[1024];
        //分块列表
        File[] fileArray = chunkFolder.listFiles();
        // 转成集合，便于排序
        List<File> fileList = Arrays.asList(fileArray);
        // 从小到大排序
        Collections.sort(fileList, Comparator.comparingInt(o -> Integer.parseInt(o.getName())));
        //合并文件
        for (File chunkFile : fileList) {
            RandomAccessFile rafChuck = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = rafChuck.read(buf)) != -1) {
                rafMergeFile.write(buf, 0, len);
            }
            rafChuck.close();
        }
        rafMergeFile.close();













        //原始文件
        File originalFile = new File("d:/develop/bigfile_test/nacos.mp4");
        //校验文件
        try (

                FileInputStream fileInputStream = new FileInputStream(originalFile);
                FileInputStream mergeFileStream = new FileInputStream(mergeFile);

        )
        {
            //取出原始文件的md5
            String originalMd5 = DigestUtils.md5Hex(fileInputStream);
            //取出合并文件的md5进行比较
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileStream);
            if (originalMd5.equals(mergeFileMd5)) {
                System.out.println("合并文件成功");
            }
            else {
                System.out.println("合并文件失败");
            }

        }


    }

}
