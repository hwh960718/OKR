package com.mobvista.okr.util.aws;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;

public class FileZip {

    /**
     * <P>FileZip.ZipFiles()<P>;
     * <P>Author :  MaYong </P>
     * <P>Date : 2017年3月14日 </P>
     *
     * @param srcfile
     * @param zipfile
     */
    public static void ZipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归压缩文件夹
     * <P>FileZip.ZipFile()<P>;
     * <P>Author :  MaYong </P>
     * <P>Date : 2017年3月14日 </P>
     *
     * @param inputStr ("D:/testzip";)
     */
    public static String ZipFile(String inputStr) {
        try {
            File input = new File(inputStr);
            String singleFile = null;
            if (input.isFile()) {
                int temp = input.getName().lastIndexOf(".");
                singleFile = input.getName().substring(0, temp);
            }
            String basepath = input.getName();
            String zipfilename = singleFile != null ? singleFile : input.getName() + "-" + System.currentTimeMillis();
            ZipOutputStream zos = new ZipOutputStream(new File(input.getParent(), zipfilename + ".zip"));
            zos.setEncoding("UTF-8");
            PutEntry(zos, input, basepath);
            zos.close();
            FileUtils.deleteFile(inputStr);
            return zipfilename + ".zip";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void PutEntry(ZipOutputStream zos, File file, String basepath) throws IOException, FileNotFoundException {
        FileInputStream is = null;
        byte[] b = new byte[1024 * 10];
        int tmp = 0;
        basepath += File.separator;
        if (file.isFile()) {
            zos.putNextEntry(new ZipEntry(basepath.substring(0, basepath.lastIndexOf(File.separator))));
            is = new FileInputStream(file);
            while ((tmp = is.read(b, 0, b.length)) != -1) {
                zos.write(b, 0, tmp);
            }
            is.close();
        } else {
            for (File f : file.listFiles()) {
                PutEntry(zos, f, basepath + f.getName());
            }
        }
        zos.flush();
    }
}