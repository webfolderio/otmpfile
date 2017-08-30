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
#include <stdio.h>

#include <jni.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <linux/limits.h>

#include "io_webfolder_otmpfile_TempFile.h"

#ifndef __O_TMPFILE
#define __O_TMPFILE	020000000
#endif

#define O_TMPFILE (__O_TMPFILE | O_DIRECTORY)
#define O_TMPFILE_MASK (__O_TMPFILE | O_DIRECTORY | O_CREAT)

JNIEXPORT jint JNICALL Java_io_webfolder_otmpfile_TempFile_create(JNIEnv *env, jclass klass, jstring j_path) {
    const char *c_path = (*env)->GetStringUTFChars(env, j_path, 0);
    jint ret = open(c_path, O_RDWR | O_TMPFILE, S_IWUSR | S_IRUSR | S_IWGRP | S_IRGRP);
    (*env)->ReleaseStringUTFChars(env, j_path, c_path);
    return ret;
}

JNIEXPORT jobject JNICALL Java_io_webfolder_otmpfile_TempFile_toFileDescriptor(JNIEnv *env, jclass klass, jint fd) {
    if (fd < 0) {
        return NULL;
    }
    jclass class_fdesc = (*env)->FindClass(env, "java/io/FileDescriptor");
    if (class_fdesc == NULL) {
         return NULL;
    }
    jmethodID const_fdesc = (*env)->GetMethodID(env, class_fdesc, "<init>", "()V");
    if (const_fdesc == NULL) {
        return NULL;
    }
    jobject ret = (*env)->NewObject(env, class_fdesc, const_fdesc);
    if (ret == NULL) {
        return NULL;
    }
    jfieldID field_fd = (*env)->GetFieldID(env, class_fdesc, "fd", "I");
    if (field_fd == NULL) {
        return NULL;
    }
    if (field_fd == NULL) {
        return NULL;
    }
    (*env)->SetIntField(env, ret, field_fd, fd);
    return ret;
}

JNIEXPORT jint JNICALL Java_io_webfolder_otmpfile_TempFile_linkat(JNIEnv *env, jclass klass, jint fd, jstring j_new_path) {
	char old_path[PATH_MAX];
    if (fd < 0) {
        return -1;
    }
    snprintf(old_path, PATH_MAX, "/proc/self/fd/%d", fd);
    const char *c_new_path = (*env)->GetStringUTFChars(env, j_new_path, 0);
    int ret = linkat(AT_FDCWD, old_path, AT_FDCWD, c_new_path, AT_SYMLINK_FOLLOW);
    (*env)->ReleaseStringUTFChars(env, j_new_path, c_new_path);
    return ret;
}
