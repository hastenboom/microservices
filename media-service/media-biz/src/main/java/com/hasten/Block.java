package com.hasten;

import lombok.Data;

/**
 * @author Hasten
 */
@Data
public class Block {
    public static final int BLK_SIZE = 1024 * 1024 * 5;//5MB

    private final String fileName;
    private final long blockId;

}
