package com.mobvista.okr.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mobvista.okr.constants.AwsConstants;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.dto.*;
import com.mobvista.okr.dto.score.ScoreUserDTO;
import com.mobvista.okr.dto.score.ScoreUserDetailDTO;
import com.mobvista.okr.util.aws.AWS3ClientUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述：亚马逊图片获取工具类
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/20 10:32
 */
public class AwsPictureProcessUtil {


    /**
     * 封装用户图片信息
     *
     * @param list
     */
    public static void assembleUserInfoDTO(List<UserInfoDTO> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        //获取图片对应的aws路径
        Set<String> pictureSet = list.stream().map(UserInfoDTO::getProfilePhoto).collect(Collectors.toSet());
        pictureSet.remove(null);
        Map<String, String> stringMap = Maps.newHashMap();
        if (pictureSet.size() > 0) {
            stringMap = AWS3ClientUtils.getTempLinkUrlByKeys(Lists.newArrayList(pictureSet));
        }
        //获取默认图片
        String defaultPicture = getDefaultPicture();
        Map<String, String> finalStringMap = stringMap;
        list.forEach(dto -> {
            String oldPhoto = dto.getProfilePhoto();
            oldPhoto = null == oldPhoto ? StringUtils.EMPTY : oldPhoto;
            String newPhoto = finalStringMap.get(oldPhoto);
            if (StringUtils.isNotBlank(oldPhoto) && StringUtils.isNotBlank(newPhoto)) {
                dto.setProfilePhoto(newPhoto);
            } else {
                dto.setProfilePhoto(defaultPicture);
            }
        });
    }


    /**
     * 获取用户积分明细的图片
     *
     * @param list
     */
    public static final void assembleScoreUserDetailDTO(List<ScoreUserDetailDTO> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        //获取图片对应的aws路径
        Set<String> pictureSet = list.stream().map(ScoreUserDetailDTO::getAssessorProfilePhoto).collect(Collectors.toSet());
        pictureSet.remove(null);
        Map<String, String> stringMap = Maps.newHashMap();
        if (pictureSet.size() > 0) {
            stringMap = AWS3ClientUtils.getTempLinkUrlByKeys(Lists.newArrayList(pictureSet));
        }
        //获取默认图片
        String defaultPicture = getDefaultPicture();
        Map<String, String> finalStringMap = stringMap;
        list.forEach(dto -> {
            String oldPhoto = dto.getAssessorProfilePhoto();
            oldPhoto = null == oldPhoto ? StringUtils.EMPTY : oldPhoto;
            String newPhoto = finalStringMap.get(oldPhoto);
            if (StringUtils.isNotBlank(oldPhoto) && StringUtils.isNotBlank(newPhoto)) {
                dto.setAssessorProfilePhoto(newPhoto);
            } else {
                dto.setAssessorProfilePhoto(defaultPicture);
            }
        });
    }


    /**
     * 获取用户积分的图片
     *
     * @param list
     */
    public static void assembleScoreUserDTO(List<ScoreUserDTO> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        //获取图片对应的aws路径
        Set<String> pictureSet = list.stream().map(ScoreUserDTO::getAssessorProfilePhoto).collect(Collectors.toSet());
        pictureSet.remove(null);
        Map<String, String> stringMap = Maps.newHashMap();
        if (pictureSet.size() > 0) {
            stringMap = AWS3ClientUtils.getTempLinkUrlByKeys(Lists.newArrayList(pictureSet));
        }
        //获取默认图片
        String defaultPicture = getDefaultPicture();
        Map<String, String> finalStringMap = stringMap;
        list.forEach(dto -> {
            String oldPhoto = dto.getAssessorProfilePhoto();
            oldPhoto = null == oldPhoto ? StringUtils.EMPTY : oldPhoto;
            String newPhoto = finalStringMap.get(oldPhoto);
            if (StringUtils.isNotBlank(oldPhoto) && StringUtils.isNotBlank(newPhoto)) {
                dto.setAssessorProfilePhoto(newPhoto);
            } else {
                dto.setAssessorProfilePhoto(defaultPicture);
            }
        });
    }

    /**
     * 封装用户图片信息
     *
     * @param list
     */
    public static void assembleUserInfoLiteDTO(List<UserInfoLiteDTO> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        //获取图片对应的aws路径
        Set<String> pictureSet = list.stream().map(UserInfoLiteDTO::getProfilePhoto).collect(Collectors.toSet());
        pictureSet.remove(null);
        Map<String, String> stringMap = Maps.newHashMap();
        if (pictureSet.size() > 0) {
            stringMap = AWS3ClientUtils.getTempLinkUrlByKeys(Lists.newArrayList(pictureSet));
        }
        //获取默认图片
        String defaultPicture = getDefaultPicture();
        Map<String, String> finalStringMap = stringMap;
        list.forEach(dto -> {
            String oldPhoto = dto.getProfilePhoto();
            oldPhoto = null == oldPhoto ? StringUtils.EMPTY : oldPhoto;
            String newPhoto = finalStringMap.get(oldPhoto);
            if (StringUtils.isNotBlank(oldPhoto) && StringUtils.isNotBlank(newPhoto)) {
                dto.setProfilePhoto(newPhoto);
            } else {
                dto.setProfilePhoto(defaultPicture);
            }
        });
    }


    /**
     * 封装排名dto
     *
     * @param list
     */
    public static void assembleRankingDTO(List<RankingDTO> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        //获取图片对应的aws路径
        Set<String> pictureSet = list.stream().map(RankingDTO::getProfilePhoto).collect(Collectors.toSet());
        pictureSet.remove(null);
        Map<String, String> stringMap = Maps.newHashMap();
        if (pictureSet.size() > 0) {
            stringMap = AWS3ClientUtils.getTempLinkUrlByKeys(Lists.newArrayList(pictureSet));
        }
        //获取默认图片
        String defaultPicture = getDefaultPicture();
        Map<String, String> finalStringMap = stringMap;
        list.forEach(dto -> {
            String oldPhoto = dto.getProfilePhoto();
            oldPhoto = null == oldPhoto ? StringUtils.EMPTY : oldPhoto;
            String newPhoto = finalStringMap.get(oldPhoto);
            if (StringUtils.isNotBlank(oldPhoto) && StringUtils.isNotBlank(newPhoto)) {
                dto.setProfilePhoto(newPhoto);
            } else {
                dto.setProfilePhoto(defaultPicture);
            }
        });
    }

    /**
     * 封装用户图片信息
     *
     * @param list
     */
    public static void assembleAssessTaskDTO(List<AssessTaskDTO> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        //获取图片对应的aws路径
        Set<String> pictureSet = list.stream().map(AssessTaskDTO::getUserProfilePhoto).collect(Collectors.toSet());
        pictureSet.remove(null);
        Map<String, String> stringMap = Maps.newHashMap();
        if (pictureSet.size() > 0) {
            stringMap = AWS3ClientUtils.getTempLinkUrlByKeys(Lists.newArrayList(pictureSet));
        }
        //获取默认图片
        String defaultPicture = getDefaultPicture();
        Map<String, String> finalStringMap = stringMap;
        list.forEach(dto -> {
            String oldPhoto = dto.getUserProfilePhoto();
            oldPhoto = null == oldPhoto ? StringUtils.EMPTY : oldPhoto;
            String newPhoto = finalStringMap.get(oldPhoto);
            if (StringUtils.isNotBlank(oldPhoto) && StringUtils.isNotBlank(newPhoto)) {
                dto.setUserProfilePhoto(newPhoto);
            } else {
                dto.setUserProfilePhoto(defaultPicture);
            }
        });
    }


    /**
     * 封装用户图片信息
     *
     * @param list
     */
    public static void assembleSubordinateAssessTaskDTO(List<SubordinateAssessTaskDTO> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        //获取图片对应的aws路径
        Set<String> pictureSet = Sets.newHashSet();
        list.forEach(dto -> {
            pictureSet.add(dto.getUserProfilePhoto());
            pictureSet.add(dto.getAssessorProfilePhoto());
        });

        pictureSet.remove(null);
        Map<String, String> stringMap = Maps.newHashMap();
        if (pictureSet.size() > 0) {
            stringMap = AWS3ClientUtils.getTempLinkUrlByKeys(Lists.newArrayList(pictureSet));
        }
        Map<String, String> finalStringMap = stringMap;

        //默认图片路径
        String defaultPicture = getDefaultPicture();

        //设置图片
        list.forEach(dto -> {
            //设置被评价人图片
            String oldUserProfilePhoto = dto.getUserProfilePhoto();
            if (oldUserProfilePhoto == null || oldUserProfilePhoto.length() == 0) {
                dto.setUserProfilePhoto(defaultPicture);
            } else {
                dto.setUserProfilePhoto(finalStringMap.get(oldUserProfilePhoto));
            }

            //设置评价人图片
            String oldAssessorProfilePhoto = dto.getAssessorProfilePhoto();
            if (oldAssessorProfilePhoto == null || oldAssessorProfilePhoto.length() == 0) {
                dto.setAssessorProfilePhoto(defaultPicture);
            } else {
                dto.setAssessorProfilePhoto(finalStringMap.get(oldAssessorProfilePhoto));
            }

        });
    }


    /**
     * 获取图片
     *
     * @param photo
     * @return
     */
    public static String getPictureUrl(String photo) {
        if (StringUtils.isNotBlank(photo)) {
            return AWS3ClientUtils.getTempLinkUrl(photo);
        }
        return getDefaultPicture();
    }


    /**
     * 获取亚马逊默认图片
     *
     * @return
     */
    private static String getDefaultPicture() {
        return AWS3ClientUtils.getTempLinkUrl(AwsConstants.PICTURE_PATH_USER_DEFAULT);
    }


    /**
     * 获取所有默认图片
     *
     * @return
     */
    public static List<AWSResponseDTO> getAllDefaultPictureList() {
        List<String> list = Lists.newArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        for (int i = 1; i <= 10; i++) {
            String photo = "default_faces/Fruit-" + decimalFormat.format(i) + ".png";
            try {
                list.add(AwsConstants.PICTURE_PATH_USER + photo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AWS3ClientUtils.getTempLinkUrlDtoListByKeys(list);
    }


    /**
     * 删除历史文件
     *
     * @param oldPath
     * @param newPath
     */
    public static void deletePicture(String oldPath, String newPath) {
        //历史文件不存在
        if (StringUtils.isBlank(oldPath)) {
            return;
        }
        //新老文件一致
        if (oldPath.equals(newPath)) {
            return;
        }
        //需要删除的key
        List<String> deleteList = Lists.newArrayList();
        List<String> oldPathList = Lists.newArrayList(oldPath.split(CommonConstants.APPEND_COMMA));
        List<String> newPathList = Lists.newArrayList(newPath.split(CommonConstants.APPEND_COMMA));
        oldPathList.forEach(s -> {
            if (!newPathList.contains(s) && !s.contains("default_faces")) {
                deleteList.add(s);
            }
        });
        if (deleteList.size() > 0) {
            AWS3ClientUtils.deleteToBucket(deleteList.toArray(new String[deleteList.size()]));
        }
    }
}
