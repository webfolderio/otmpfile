/**
 * otmpfile
 * Copyright © 2017 WebFolder OÜ (support@webfolder.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.webfolder.otmpfile;

import static io.webfolder.otmpfile.TempFile.linkat;
import static io.webfolder.otmpfile.TempFile.toFileDescriptor;
import static java.io.File.separator;
import static java.lang.Long.toHexString;
import static java.lang.System.getProperty;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.Paths.get;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

public class SecureTempFile {

    private static final String TEMP_DIR = getProperty("java.io.tmpdir");

    private static final byte[] TEMP_FILE_TEST_CONTENT = "https://webfolder.io".getBytes();

    public static final boolean SUPPORT_O_TMPFILE = new SecureTempFile(TEMP_DIR).supportOtmpFile();

    private int fd;

    private FileDescriptor fileDescriptor;

    private final String path;

    public SecureTempFile() {
        this(TEMP_DIR);
    }

    public SecureTempFile(String path) {
        this.path = path;
    }

    private boolean supportOtmpFile() {
        try {
            int fd = TempFile.create(path);
            if (fd > 0) {
                FileDescriptor descriptor = toFileDescriptor(fd);
                if ( descriptor != null ) {
                    int linkat = -1;
                    String filePath = path + separator + toHexString(new Random().nextLong());
                    try (FileOutputStream fs = new FileOutputStream(descriptor)) {
                        fs.write(TEMP_FILE_TEST_CONTENT);
                        linkat = linkat(fd, filePath);
                    }
                    if (linkat > 0) {
                        Path tempFilePath = get(filePath);
                        if (exists(tempFilePath, NOFOLLOW_LINKS) && isRegularFile(tempFilePath, NOFOLLOW_LINKS)) {
                            boolean same = Arrays.equals(readAllBytes(tempFilePath), TEMP_FILE_TEST_CONTENT);
                            delete(tempFilePath);
                            return same && ! exists(tempFilePath, NOFOLLOW_LINKS);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            return false;
        }
        return false;        
    }

    public boolean create() {
        fd = TempFile.create(TEMP_DIR);
        if (fd > 0) {
            fileDescriptor = toFileDescriptor(fd);
            return fileDescriptor != null;
        }
        return false;
    }

    public int getFd() {
        return fd;
    }

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public boolean setName(String path) {
        if ( fd > 0 && fileDescriptor != null ) {
            int ret = linkat(fd, path);
            return ret != 0;
        }
        return false;
    }
}
