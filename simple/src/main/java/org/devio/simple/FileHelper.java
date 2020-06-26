package org.devio.simple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileHelper {

    /*  *
    创建文件
        /-- 在指定目录创建文件，
        -- 先判断目录是否存在，如果不存在，则递归创建，
        -- 再判断文件是否存在，如果不存在，则创建
        -- 最后，返回文件对象
        方法传参：目标路径、文件名称
     */
    public File newFile(String filePath, String fileName){
        if(filePath == null || filePath.length() == 0
                || fileName == null || fileName.length() == 0){
            return null;
        }
        try {
            //判断目录是否存在，如果不存在，递归创建目录
            File dir = new File(filePath);
            if(!dir.exists()){
                dir.mkdirs();
            }

            //组织文件路径
            StringBuilder sbFile = new StringBuilder(filePath);
            if(!filePath.endsWith("/")){
                sbFile.append("/");
            }
            sbFile.append(fileName);

            //创建文件并返回文件对象
            File file = new File(sbFile.toString());
            if(!file.exists()){
                file.createNewFile();
            }
            return file;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /*  *
    删除文件或目录
        需求：
        -- 可删除单个文件，也可删除整个目录，
        -- 先判断File类型是文件还是目录类型，
        -- 如果是文件，则直接删除
        -- 如果是目录，则获得目录信息，递归删除文件及文件夹
        方法传参：目标路径（文件或目录路径）
     */
    public void removeFile(String filePath) {
        if(filePath == null || filePath.length() == 0){
            return;
        }
        try {
            File file = new File(filePath);
            if(file.exists()){
                removeFile(file);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void removeFile(File file){
        //如果是文件直接删除
        if(file.isFile()){
            file.delete();
            return;
        }
        //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                removeFile(f);
            }
            file.delete();
        }
    }

    /*  *
    获得文件或目录大小（size）
        需求：
        -- 可获得单个文件大小，也可获得目录大小，
        -- 先判断File类型是文件还是目录类型，
        -- 如果是文件，则直接获取大小并返回
        -- 如果是目录，递归获得各文件大小累加，然后返回
        方法传参：目标路径（文件或目录路径）
     */
    float size = 0;
    public float getFileSize(String filePath) {
        if(filePath == null || filePath.length() == 0){
            return 0;
        }
        try {
            File file = new File(filePath);
            if(file.exists()){
                size = 0;
                return getSize(file);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    private float getSize(File file) {
        try {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                for (File f : children) {
                    size += getSize(f);
                }
                return size;
            }
            //如果是文件则直接返回其大小
            else {
                return (float) file.length();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return size;
    }

     /*  *
    拷贝文件
        需求：
        -- 将文件拷贝到指定目录
        -- 如果目录不存在，则递归创建
        -- 拷贝文件
        方法传参：文件路径、拷贝目标路径
     */
     public void copyFile(String filePath, String newDirPath,String newFileName) {
         if(filePath == null || filePath.length() == 0){
             return;
         }
         try {
             File file = new File(filePath);
             if(!file.exists()){
                 return;
             }
             //判断文件夹是否存在，如果不存在，则创建
             File newDir = new File(newDirPath);
             try {
                 if (!newDir.exists())
                     newDir.mkdir();
             }catch (Exception e){
                 e.printStackTrace();
             }

             //创建目录之后再创建文件
             File newfile = new File(newDirPath + newFileName);
             try{
                 if(!newfile.exists())
                     newfile.createNewFile();
             }catch (Exception e){
                 e.printStackTrace();
             }

             //创建目标文件
             InputStream is = new FileInputStream(file);
             FileOutputStream fos = new FileOutputStream(newfile);
             byte[] buffer = new byte[4096];
             int byteCount = 0;
             while ((byteCount = is.read(buffer)) != -1) {
                 fos.write(buffer, 0, byteCount);
             }
             fos.flush();
             is.close();
             fos.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }




     /*  *
    剪切文件
        需求：
        -- 将文件剪切到指定目录
        -- 如果目录不存在，则递归创建
        -- 拷贝文件，删除源文件
        方法传参：源文件路径、拷贝目标路径
     */
    public void moveFile(String filePath, String newDirPath,String newFileName) {
        if(filePath == null || filePath.length() == 0
                || newDirPath==null || newDirPath.length() == 0){
            return;
        }
        try {
            //拷贝文件
            copyFile(filePath, newDirPath,newFileName);
            //删除原文件
            removeFile(filePath);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
