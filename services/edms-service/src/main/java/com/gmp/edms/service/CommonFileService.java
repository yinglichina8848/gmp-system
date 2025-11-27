package com.gmp.edms.service;

import com.gmp.edms.dto.CommonFileDTO;
import com.gmp.edms.dto.CommonFileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 通用文件管理服务接口
 * 提供跨模块的文件管理功能
 */
public interface CommonFileService {

    /**
     * 上传通用文件
     * @param file 上传的文件
     * @param module 文件所属模块
     * @param metadata 文件元数据
     * @return 文件信息DTO
     */
    CommonFileDTO uploadFile(MultipartFile file, String module, Map<String, Object> metadata) throws Exception;

    /**
     * 批量上传文件
     * @param files 上传的文件列表
     * @param module 文件所属模块
     * @param metadata 文件元数据
     * @return 文件信息DTO列表
     */
    List<CommonFileDTO> batchUploadFiles(List<MultipartFile> files, String module, Map<String, Object> metadata) throws Exception;

    /**
     * 下载文件
     * @param fileId 文件ID
     * @return 文件输入流
     */
    InputStream downloadFile(Long fileId) throws Exception;

    /**
     * 获取文件信息
     * @param fileId 文件ID
     * @return 文件信息DTO
     */
    CommonFileDTO getFileInfo(Long fileId) throws Exception;

    /**
     * 删除文件
     * @param fileId 文件ID
     */
    void deleteFile(Long fileId) throws Exception;

    /**
     * 批量删除文件
     * @param fileIds 文件ID列表
     */
    void batchDeleteFiles(List<Long> fileIds) throws Exception;

    /**
     * 生成文件访问的预签名URL
     * @param fileId 文件ID
     * @param expiration 过期时间（秒）
     * @return 预签名URL
     */
    String generatePresignedUrl(Long fileId, int expiration) throws Exception;

    /**
     * 查询模块下的文件
     * @param module 模块名称
     * @param filters 过滤条件
     * @param page 页码
     * @param size 每页大小
     * @return 文件列表
     */
    Map<String, Object> queryFiles(String module, Map<String, Object> filters, int page, int size);

    /**
     * 更新文件元数据
     * @param fileId 文件ID
     * @param metadata 新的元数据
     */
    void updateFileMetadata(Long fileId, Map<String, Object> metadata) throws Exception;

    /**
     * 获取文件统计信息
     * @param module 模块名称
     * @return 统计信息
     */
    Map<String, Object> getFileStatistics(String module);
}