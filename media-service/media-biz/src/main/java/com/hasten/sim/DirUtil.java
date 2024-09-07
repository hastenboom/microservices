package com.hasten.sim;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hasten
 */
@Data
public class DirUtil {
    private File chunkDir;
    private String chunkDirPath;

    public DirUtil(String dirPath) {
        this.chunkDirPath = dirPath;
        this.chunkDir = new File(dirPath);
        if (!chunkDir.exists()) {
            chunkDir.mkdirs();
        }
    }

    public List<FileUtil> getRafList() throws IOException {
        File[] chunkFileList = chunkDir.listFiles();
        if (chunkFileList == null || chunkFileList.length == 0) {
            throw new IOException("No chunk files found in " + chunkDirPath);
        }

        Arrays.sort(chunkFileList, Comparator.comparing(File::getName));

        return Arrays.stream(chunkFileList).map((file) -> {
            try {
                return new FileUtil(file, "rw");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }


}
