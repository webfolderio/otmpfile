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

import static io.webfolder.otmpfile.SecureTempFile.SUPPORT_O_TMPFILE;
import static java.io.File.separator;
import static java.lang.Math.abs;
import static java.lang.System.getProperty;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.Paths.get;

import java.io.FileOutputStream;
import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestTempFile {

    @Test
    public void test() throws Exception {
        assertTrue(SUPPORT_O_TMPFILE);

        SecureTempFile stf = new SecureTempFile();

        byte[] expecteds = "foo".getBytes();
        byte[] actuals = new byte[] { };

        if (stf.create()) {

            try (FileOutputStream fs = new FileOutputStream(stf.getFileDescriptor())) {
                fs.write("foo".getBytes());

                String tempFileName = getProperty("java.io.tmpdir") + separator + abs(new Random().nextInt());

                boolean renamed = stf.setName(tempFileName);
                assertTrue(renamed);

                if (renamed) {
                    assertNotNull(get(tempFileName));

                    assertTrue(exists(get(tempFileName), NOFOLLOW_LINKS));

                    actuals = readAllBytes(get(tempFileName));
                }
            }
        }

        assertArrayEquals(expecteds, actuals);
    }
}
