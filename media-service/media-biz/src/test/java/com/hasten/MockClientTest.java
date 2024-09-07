package com.hasten;

import org.junit.jupiter.api.Test;

import java.io.IOException;


class MockClientTest {

    public static final String TEST_FILE_NAME = "boltdb.pdf";

    @Test
    public void testMockClient() throws IOException {
        SingleFileUploader uploader = new SingleFileUploader(TEST_FILE_NAME);
        uploader.uploadFile();
        uploader.mergeChunk();
    }
}