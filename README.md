# otmpfile

[O_TMPFILE](https://kernelnewbies.org/Linux_3.11#head-8be09d59438b31c2a724547838f234cb33c40357) backed temporary files for Java.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/webfolderio/otmpfile/blob/master/LICENSE)

What is O_TMPFILE?
------------------

__O_TMPFILE__ is a Linux-specific flag for open(), that allows creation of already-unlinked temporary files, that don't need to be explicitly removed via unlink.
This means that you could create a temp file without name. This is wrapper library to use O_TMPFILE from Java.


How to Use?
-----------

```java
SecureTempFile tmp = new SecureTempFile();

// Create and write temp file without giving a name
try (FileOutputStream fs = new FileOutputStream(stf.getFileDescriptor())) {
  fs.write("foo".getBytes());
}

// Optionally set the file name
String tempFilePath = SecureTempFile.TEMP_DIR + File.separator + "my-temp-file.tmp";
tmp.setName(tempFilePath);
```

Articles
--------

[Automatic Deletion of Incomplete Output Files](https://nullprogram.com/blog/2016/08/07/)

[open() flags: O_TMPFILE and O_BENEATH](https://lwn.net/Articles/619146/)

[How to make temporary files](https://keens.github.io/blog/2015/11/08/ichijifairunotsukurikata/)

[O_TMPFILE flag linux - 3.11](https://www.oreilly.co.jp/community/blog/2014/09/file-open-and-new-flags.html)

Supported Java Versions
-----------------------

Oracle & OpenJDK Java 8, 11.

Both the JRE and the JDK are suitable for use with this library.

Supported Platforms
-------------------

otmpfile has been tested under Ubuntu, but should work on any platform where a __GNU/Linux 3.11__ Kernel available.

Integration with Maven
----------------------

To use the official release of otmpfile, please use the following snippet in your `pom.xml` file.

Add the following to your POM's `<dependencies>` tag:

```xml
<dependency>
    <groupId>io.webfolder</groupId>
    <artifactId>otmpfile</artifactId>
    <version>1.1.0</version>
</dependency>
```

License
------------

This project licensed under the [MIT](https://github.com/webfolderio/otmpfile/blob/master/LICENSE) License.

Download
--------

[otmpfile-1.1.0.jar](https://search.maven.org/remotecontent?filepath=io/webfolder/otmpfile/1.1.0/otmpfile-1.1.0.jar)

[otmpfile-1.1.0-sources.jar](https://search.maven.org/remotecontent?filepath=io/webfolder/otmpfile/1.1.0/otmpfile-1.1.0-sources.jar)


Author
------

[WebFolder OÜ](https://webfolder.io)