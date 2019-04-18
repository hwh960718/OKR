package com.mobvista.okr.util.aws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.dto.AWSResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @项目名称: CRM
 * @作者: MaYong
 * @描述: AWS服务S3实现文件上传
 * @创建时间: 2017年02月27日 16:17
 * @版本 : V1.0
 */
public class AWS3ClientUtils {

    //S3服务器的AccessKey
    private static String CRM_AWS_S3_ACCESS_KEY = "AKIAJ7YMSIPBGTNRG6NA";
    //S3服务器的SecretKey
    private static String CRM_AWS_S3_SECRET_KEY = "eZq9GJQNlnj59pygpqxmAp/QshlMly6JdARE2Ch1";
    //S3服务器的bucketName
    private static String CRM_AWS_S3_BUCKET_NAME = "bj-crm";

    //导出Excel服务器地址(Windows)
    private static String CRM_EXPORT_EXCEL_TEMP_FILE_FOR_WIN = "e:/data/temp/";
    //导出Excel服务器地址(Mac)
    private static String CRM_EXPORT_EXCEL_TEMP_FILE_FOR_MAC = "/Users/liutengfei/";
    //导出Excel服务器地址(Linux)
    private static String CRM_EXPORT_EXCEL_TEMP_FILE_FOR_LINUX = "/data/temp/";

    private static Logger LOGGER = LoggerFactory.getLogger(AWS3ClientUtils.class);

    static AmazonS3 s3 = null;
    static TransferManager tx = null;

    // 静态初始化
    static {
        if (s3 == null) {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private AWS3ClientUtils() {
    }


    private static void init() throws Exception {
        init_with_key();
        LOGGER.info("S3服务初始化完成！");
    }

    private static void init_with_key() throws Exception {
        AWSCredentials credentials = new BasicAWSCredentials(CRM_AWS_S3_ACCESS_KEY, CRM_AWS_S3_SECRET_KEY);
        s3 = new AmazonS3Client(credentials);
        tx = new TransferManager(s3);
    }


    /**
     * 上传文件
     *
     * @param inputStream
     * @param fileName
     * @return
     */
    public static AWSResponseDTO uploadFileToBucket(String path, InputStream inputStream, String fileName) {
        path = StringUtils.isBlank(path) ? CommonConstants.SYSTEM + CommonConstants.APPEND_SLASH : path;
        String key = path + UUID.randomUUID() + CommonConstants.APPEND_UNDER_LINE + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
        return uploadFileToBucketByKey(key, inputStream, fileName);
    }


    /**
     * 上传文件
     *
     * @param inputStream
     * @param fileName
     * @return
     */
    public static AWSResponseDTO uploadFileToBucketByKey(String key, InputStream inputStream, String fileName) {
        LOGGER.info("上传文件----文件KEY：" + key);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setExpirationTime(null);
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(CRM_AWS_S3_BUCKET_NAME, key);
        LOGGER.info("上传文件----开始上传...");
        s3.putObject(new PutObjectRequest(CRM_AWS_S3_BUCKET_NAME, key, inputStream, metadata));
        LOGGER.info("上传文件----上传结束...");
        return new AWSResponseDTO(s3.generatePresignedUrl(urlRequest).toString(), fileName, key);
    }

    /**
     * 删除文件
     *
     * @param keys
     * @return
     */
    public static void deleteToBucket(String... keys) {
        LOGGER.info("删除文件----文件KEY：" + JSON.toJSONString(keys));
        LOGGER.info("删除文件----开始删除...");
        DeleteObjectsRequest dor = new DeleteObjectsRequest(CRM_AWS_S3_BUCKET_NAME)
                .withKeys(keys);
        s3.deleteObjects(dor);
        LOGGER.info("删除文件----成功删除...");
    }


    /**
     * 下载文件
     *
     * @param response
     * @param jsonObject
     * @throws Exception
     */
    public static void downLoadAWS3(HttpServletResponse response, JSONObject jsonObject) throws Exception {
        GeneratePresignedUrlRequest httpRequest = null;
        String tempFile = null;

        String baseFile = CRM_EXPORT_EXCEL_TEMP_FILE_FOR_WIN;
        if (OSUtil.isMacOSX()) {
            baseFile = CRM_EXPORT_EXCEL_TEMP_FILE_FOR_MAC;
        }
        if (!OSUtil.isWindows() && !OSUtil.isMacOSX()) {
            baseFile = CRM_EXPORT_EXCEL_TEMP_FILE_FOR_LINUX;
        }
        tempFile = baseFile + (int) ((Math.random() * 9 + 1) * 100000) + "/";
        FileUtils.createPath(tempFile);
        String fileName = "";
        for (Iterator it2 = jsonObject.entrySet().iterator(); it2.hasNext(); ) {
            JSONObject.Entry je = (JSONObject.Entry) it2.next();
            InputStream is = null;
            OutputStream os = null;
            try {
                httpRequest = new GeneratePresignedUrlRequest(CRM_AWS_S3_BUCKET_NAME, String.valueOf(je.getKey()));
                fileName = String.valueOf(je.getValue());
                String url = s3.generatePresignedUrl(httpRequest).toString();//临时链接
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpget = new HttpGet(url);
                CloseableHttpResponse rs = httpClient.execute(httpget);
                HttpEntity entity = rs.getEntity();
                is = entity.getContent();

                os = new FileOutputStream(tempFile + String.valueOf(je.getValue()));
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
            } catch (IOException e) {
                throw new Exception(e.getMessage());
            } finally {
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (jsonObject.size() <= 1) {
            DownloadUtils.download(response, tempFile + fileName);
        } else {
            DownloadUtils.download(response, baseFile + FileZip.ZipFile(tempFile));
        }
    }

    /**
     * 获取临时URL
     *
     * @param key
     * @return
     */
    public static String getTempLinkUrl(String key) {
        GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(CRM_AWS_S3_BUCKET_NAME, key);
        //临时链接
        return s3.generatePresignedUrl(httpRequest).toString();

    }


    /**
     * 获取临时URL
     *
     * @param stringList 使用key批量获取URL
     * @return
     */
    public static Map<String, String> getTempLinkUrlByKeys(List<String> stringList) {
        Map<String, String> stringStringMap = Maps.newConcurrentMap();
        if (stringList != null && stringList.size() > 0) {
            stringList.forEach(s -> {
                GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(CRM_AWS_S3_BUCKET_NAME, s);
                stringStringMap.put(s, s3.generatePresignedUrl(httpRequest).toString());
            });
        }

        return stringStringMap;
    }

    /**
     * 获取临时URL
     *
     * @param stringList 使用key批量获取URL
     * @return
     */
    public static List<AWSResponseDTO> getTempLinkUrlDtoListByKeys(List<String> stringList) {
        List<AWSResponseDTO> awsResponseDTOList = Lists.newArrayList();
        if (stringList != null && stringList.size() > 0) {
            stringList.forEach(s -> {
                GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(CRM_AWS_S3_BUCKET_NAME, s);
                awsResponseDTOList.add(new AWSResponseDTO(s3.generatePresignedUrl(httpRequest).toString(), null, s));
            });
        }

        return awsResponseDTOList;
    }

}
