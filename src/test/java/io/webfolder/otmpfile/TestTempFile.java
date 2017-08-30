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

import static io.webfolder.otmpfile.TempFile.create;
import static io.webfolder.otmpfile.TempFile.linkat;
import static io.webfolder.otmpfile.TempFile.toFileDescriptor;
import static java.io.File.separator;
import static java.lang.Math.abs;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTempFile {

    @Test
    public void test() throws Exception {
        int fd = create(getProperty("java.io.tmpdir"));
        assertTrue(fd > 0);
        FileDescriptor descriptor = toFileDescriptor(fd);
        String tempFile = getProperty("java.io.tmpdir") + separator + abs(new Random().nextInt());
        String content = valueOf(new Random().nextInt());
        try (FileOutputStream os = new FileOutputStream(descriptor)) {
            os.write(content.getBytes());
            int linkat = linkat(fd, tempFile);
            if ( linkat != 0 ) {
                throw new RuntimeException("Can not link file: " + tempFile);
            }
        }
        assertEquals(content, new String(readAllBytes(get(tempFile))));
    }
}
