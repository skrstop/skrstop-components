package com.skrstop.framework.components.util.compression.tar;

import java.io.*;

/**
 * @author 蒋时华
 */
public class TarUtils {

    private static final int BUFFER = 2048;

    /**
     * Determines the tar file size of the given folder/file path
     */
    public static long calculateTarSize(File path) {
        return tarSize(path) + TarConstants.EOF_BLOCK;
    }

    private static long tarSize(File dir) {
        long size = 0;

        if (dir.isFile()) {
            return entrySize(dir.length());
        } else {
            File[] subFiles = dir.listFiles();

            if (subFiles != null && subFiles.length > 0) {
                for (File file : subFiles) {
                    if (file.isFile()) {
                        size += entrySize(file.length());
                    } else {
                        size += tarSize(file);
                    }
                }
            } else {
                // Empty folder header
                return TarConstants.HEADER_BLOCK;
            }
        }

        return size;
    }

    private static long entrySize(long fileSize) {
        long size = 0;
        size += TarConstants.HEADER_BLOCK; // Header
        size += fileSize; // File size

        long extra = size % TarConstants.DATA_BLOCK;

        if (extra > 0) {
            size += (TarConstants.DATA_BLOCK - extra); // pad
        }

        return size;
    }

    public static String trim(String s, char c) {
        StringBuffer tmp = new StringBuffer(s);
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) != c) {
                break;
            } else {
                tmp.deleteCharAt(i);
            }
        }

        for (int i = tmp.length() - 1; i >= 0; i--) {
            if (tmp.charAt(i) != c) {
                break;
            } else {
                tmp.deleteCharAt(i);
            }
        }

        return tmp.toString();
    }

    public static void tarFolder(String directory, String destTarPath) throws Exception {
        // Output file stream
        FileOutputStream dest = new FileOutputStream(destTarPath);

        // Create a TarOutputStream
        TarOutputStream out = new TarOutputStream(new BufferedOutputStream(dest));

        tarFolder(null, directory, out);

        out.close();
    }

    private static void tarFolder(String parent, String path, TarOutputStream out)
            throws IOException {
        BufferedInputStream origin = null;
        File f = new File(path);
        String files[] = f.list();

        // is file
        if (files == null) {
            files = new String[1];
            files[0] = f.getName();
        }

        parent =
                ((parent == null) ? (f.isFile()) ? "" : f.getName() + "/" : parent + f.getName() + "/");

        for (String file : files) {
            System.out.println("Adding: " + path + "/" + file);
            File fe = f;
            byte data[] = new byte[BUFFER];

            if (f.isHidden()) {
                continue;
            }

            if (f.isDirectory()) {
                fe = new File(f, file);
            }

            if (fe.isDirectory()) {
                String[] fl = fe.list();
                if (fl != null && fl.length != 0) {
                    tarFolder(parent, fe.getPath(), out);
                } else {
                    TarEntry entry = new TarEntry(fe, parent + file + "/");
                    out.putNextEntry(entry);
                }
                continue;
            }

            FileInputStream fi = new FileInputStream(fe);
            origin = new BufferedInputStream(fi);
            TarEntry entry = new TarEntry(fe, parent + file);
            out.putNextEntry(entry);

            int count;

            while ((count = origin.read(data)) != -1) {
                out.write(data, 0, count);
            }

            out.flush();
            origin.close();
        }
    }
}
