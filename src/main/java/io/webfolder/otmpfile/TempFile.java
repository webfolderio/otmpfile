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

import static java.lang.System.load;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class TempFile {

    static {
        ClassLoader cl = TempFile.class.getClassLoader();
        Path libFile;
        try (InputStream is = cl.getResourceAsStream("META-INF/libotmpfile.so")) {
            libFile = createTempFile("libotmpfile", ".so");
            copy(is, libFile, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        libFile.toFile().deleteOnExit();
        load(libFile.toAbsolutePath().toString());
    }

    public static native int create(String path);

    public static native FileDescriptor toFileDescriptor(int fd);

    public static native int linkat(int fd, String path);
}
