package com.liangdekai.bean;


public class ImageFolder {
    private String folderName ;
    private String firstImagePath ;
    private int fileCount ;
    private String folderDir ;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String path) {
        int lastIndex = path.lastIndexOf("/");
        this.folderName = path.substring(lastIndex+1);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public String getFolderDir() {
        return folderDir;
    }

    public void setFolderDir(String folderDir) {
        this.folderDir = folderDir;
    }
}
