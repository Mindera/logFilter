//Copyright 2022 Mindera
//SPDX-License-Identifier: Apache-2.0

package mindera.logfilter.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

abstract class SpringlessUpdateMessageDigestInputStream extends InputStream {
    SpringlessUpdateMessageDigestInputStream() {
    }

    public void updateMessageDigest(MessageDigest messageDigest) throws IOException {
        int data;
        while ((data = this.read()) != -1) {
            messageDigest.update((byte) data);
        }

    }

    public void updateMessageDigest(MessageDigest messageDigest, int len) throws IOException {
        int data;
        for (int bytesRead = 0; bytesRead < len && (data = this.read()) != -1; ++bytesRead) {
            messageDigest.update((byte) data);
        }

    }
}