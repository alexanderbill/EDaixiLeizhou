// -*- Mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
//
// Copyright (C) 2013 Opera Software ASA.  All rights reserved.
//
// This file is an original work developed by Opera Software ASA

package com.example.edaixi;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class FileUtils {
    private static final int BUFFER_SIZE = 4096;
    private static final Set<String> DOUBLE_EXTENSIONS = new HashSet<String>();

    static {
        DOUBLE_EXTENSIONS.add(".tar.bz2");
        DOUBLE_EXTENSIONS.add(".tar.gz");
        DOUBLE_EXTENSIONS.add(".tar.xz");
        DOUBLE_EXTENSIONS.add(".tar.Z");
    }

    /**
     * Return extension of a file.
     *
     * @param file file to return extension for.
     * @return Extension, including the preceding '.', or the empty string if
     * the file name does not contain a '.'.
     */
    public static String getExtension(File file) {
        String name = file.getName();

        int lastDotPos = name.lastIndexOf('.');
        if (lastDotPos == -1) {
            return "";
        }

        int secondLastDotPos = name.lastIndexOf('.', lastDotPos - 1);
        if (secondLastDotPos != -1) {
            String tentativeExtension = name.substring(secondLastDotPos);
            if (DOUBLE_EXTENSIONS.contains(tentativeExtension)) {
                return tentativeExtension;
            }
        }

        return name.substring(lastDotPos);
    }

    public static String getExtensionWithoutDot(File file) {
        String extension = getExtension(file);
        return TextUtils.isEmpty(extension) ? "" : extension.substring(1);
    }

    /**
     * Verify md5 sum of a file.
     *
     * @param file file to verify md5 sum for.
     * @param md5 expected md5 sum for the file.
     * @return true if the file's md5 sum is consistent with expected one.
     */
    public static boolean verifyFileMD5(File file, String md5) {
        return TextUtils.equals(getFileMD5(file), md5.toLowerCase(Locale.US));
    }

    /**
     * Return md5 sum of a file.
     *
     * @param file file to return md5 for.
     * @return The md5 string in lower case whose format matches the output of
     * the UNIX md5sum command, or null on error.
     */
    public static String getFileMD5(File file) {
        InputStream is = null;

        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            is = new DigestInputStream(new FileInputStream(file), hash);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (is.read(buffer, 0, buffer.length) != -1) {
            }
            byte[] digest = hash.digest();
            StringBuilder sb = new StringBuilder();
            for (int b : digest) {
                sb.append(Integer.toHexString((b >> 4) & 0xf));
                sb.append(Integer.toHexString(b & 0xf));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    /**
     * Copies all the bytes from one file to another.
     * If to represents an existing file, that file will be overwritten with the
     * contents of from. If to equals from, then do nothing and return false.
     * If to and from refer to the same file, the behavior is uncertain.
     *
     * @param from the source file
     * @param to the destination file
     * @return true on success
     */
    public static boolean copy(File from, File to) {
        InputStream is = null;
        OutputStream os = null;
        boolean result = true;

        if (from.equals(to)) {
            return false;
        }

        try {
            is = new FileInputStream(from);
            os = new FileOutputStream(to);
            copy(is, os);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * Copies all the bytes from one stream to another.
     *
     * @param is the source stream
     * @param os the destination stream
     * @throws IOException
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = is.read(buffer)) != -1) {
            os.write(buffer, 0, read);
        }
    }

    /**
     * Reads all characters from a file into a String, using the given character
     * set.
     *
     * @param from the file to read from.
     * @param charset the charset used to decode the input stream.
     * @return a string containing all the characters from the file or null on error.
     */
    public static String toString(File from, Charset charset) {
        try {
            return toString(new FileInputStream(from), charset);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
    
    public static String toString(InputStream from, Charset charset) {
        Reader reader = null;

        try {
            reader = new InputStreamReader(from, charset);
            char[] buffer = new char[BUFFER_SIZE];
            int read;
            StringBuilder sb = new StringBuilder();
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }
    
    public static JSONObject openAssetFileJSONObject(String assetFile, String name) {
        JSONObject ret = null;
        try {
            String str = toString(SystemUtils.getActivity().getAssets().open(assetFile), Charset.defaultCharset());
            JSONObject json = new JSONObject(str);
            return json.getJSONObject(name);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }
    
    public static JSONArray openAssetFileJSONArray(String assetFile, String name) {
        JSONArray ret = null;
        try {
            String str = toString(SystemUtils.getActivity().getAssets().open(assetFile), Charset.defaultCharset());
            JSONObject json = new JSONObject(str);
            return json.getJSONArray(name);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Writes a character sequence (such as a string) to a file using the given
     * character set.
     *
     * @param from the character sequence to write.
     * @param to the destination file.
     * @param charset the charset used to encode the output stream.
     * @return true on success
     */
    public static boolean write(CharSequence from, File to, Charset charset) {
        Writer writer = null;
        boolean result = true;

        try {
            writer = new OutputStreamWriter(new FileOutputStream(to), charset).append(from);
        } catch (IOException e) {
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * Deletes file or directory (recursively).
     * @param file File or directory to be removed.
     * @return true if removed sucessfully.
     */
    public static boolean deleteR(File file) {
        boolean deleted = true;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (child.isDirectory()) {
                    if (!deleteR(child)) {
                        deleted = false;
                    }
                } else {
                    if (!child.delete()) {
                        deleted = false;
                    }
                }
            }
        }
        if (!file.delete()) {
            deleted = false;
        }
        return deleted;
    }

    public static File getUniqueDir(File dest) {
        File parentFile = dest.getAbsoluteFile().getParentFile();
        if (parentFile != null && parentFile.exists() && parentFile.isDirectory()) {
            return getUniqueDirInFolder(dest);
        } else {
            String path = dest.getAbsolutePath();
            String absolutePath = "/";
            File uniqueDir = null;
            for (String folderName : path.split("/")) {
                if ("".equals(folderName)) {
                    continue;
                }
                absolutePath += folderName;
                uniqueDir = getUniqueDirInFolder(new File(absolutePath));
                if (uniqueDir == null) {
                    return null;
                } else {
                    absolutePath = uniqueDir.getAbsolutePath() + "/";
                }
            }
            return uniqueDir;
        }
    }

    /**
     * Get a unique directory whose parent already exists
     */
    private static File getUniqueDirInFolder(File dest) {
        if (dest.exists()) {
            if (dest.isDirectory()) {
                return dest;
            } else {
                String path = dest.getAbsolutePath();
                for (int i = 1;; i++) {
                    dest = new File(path + i);
                    if (!dest.exists()) {
                        break;
                    } else if (dest.isDirectory()) {
                        return dest;
                    }
                }
            }
        }
        return dest.mkdir() ? dest : null;
    }

    /**
     * Check if "fileName" is a valid file name:
     * 1. Name length should be between 1 and 255
     * 2. Don't contain invalid character(\/:*?"<>|)
     * 3. Don't contain character "\s"(except space characters in the middle of string)
     */
    public static boolean isValidFileName(String fileName) {
        if (TextUtils.isEmpty(fileName) || fileName.length() > 255) {
            return false;
        } else {
            return fileName.matches("([^\\s\\\\/\\\"\\.:*?<>|](\\x20|[^\\s\\\\/:*?\\\"<>|])*[^\\s\\\\/\\\"\\.:*?<>|])|[^\\s\\\\/\\\"\\.:*?<>|]");
        }
    }

    /**
     * Delete files under the folder named @param folderPath
     * with the folders in @param except ignored.
     */
    public static void deleteFolder(String folderPath, String[] except) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    String directoryPath = f.getAbsolutePath();
                    boolean isExcept = false;
                    if (except != null) {
                        for (String comp : except) {
                            if (directoryPath.equals(comp)) {
                                isExcept = true;
                            }
                        }
                    }
                    if (isExcept) {
                        continue;
                    }
                    deleteFolder(f.getAbsolutePath(), except);
                }
                if (f.canWrite()) {
                    f.delete();
                }
            }
        }
    }

    /**
     * Create new folder (and its parents) if the "folder" does not exist or is
     * not a directory.
     *
     * Note: If there is a file whose path is the same as "folder", the file itself
     * will be removed in order to make folder.
     * @param folder the folder
     * @return if the folder exists now
     */
    public static boolean mkdirIfNeeded(File folder) {
        boolean existed = folder.exists();
        if (existed && !folder.isDirectory() && (existed = !folder.delete())) {
            return false;
        }
        if (!existed) {
            return folder.mkdirs();
        }
        return true;
    }
}
