package com.example.entity;

public class FileInfor {

    private String fileName;
    private String fileSize;
    private String fileType;
    
    private String fileURL;
    private double fileProgress;
    private int fileDownloadState;// 文件下载状态 0为已下载 1为正在下载 2为暂停

    public FileInfor() {
    }

    public FileInfor(String fileName, String fileSize, String fileType,
            String fileURL, double fileProgress, int fileDownloadState) {
        super();
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileURL = fileURL;
        this.fileProgress = fileProgress;
        this.fileDownloadState = fileDownloadState;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public double getFileProgress() {
        return fileProgress;
    }

    public void setFileProgress(double fileProgress) {
        this.fileProgress = fileProgress;
    }

    public int getFileDownloadState() {
        return fileDownloadState;
    }

    public void setFileDownloadState(int fileDownloadState) {
        this.fileDownloadState = fileDownloadState;
    }

}
