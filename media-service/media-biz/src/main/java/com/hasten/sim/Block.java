package com.hasten.sim;

import lombok.Data;

/**
 * TODO:
 *
 * a field called `isDone` should added to tell the server that all blks have been uploaded.
 *
 *
 * @author Hasten
 */
@Data
public class Block {
    private final String fileName;
    private final int blockId;
}
