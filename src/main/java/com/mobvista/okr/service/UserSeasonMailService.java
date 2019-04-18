package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.config.EmailProperties;
import com.mobvista.okr.domain.Season;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.enums.ProgressStatusEnum;
import com.mobvista.okr.enums.UserStatus;
import com.mobvista.okr.repository.SeasonRepository;
import com.mobvista.okr.repository.UserProfileRepository;
import com.mobvista.okr.repository.UserSeasonRepository;
import com.mobvista.okr.util.SeasonDateUtil;
import com.mobvista.okr.vm.UserReportDetailVM;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 * </p>
 *
 * @author 顾炜[GuWei]
 */
@Service
public class UserSeasonMailService {

    private final Logger log = LoggerFactory.getLogger(UserSeasonMailService.class);

    private final SpringTemplateEngine templateEngine;

    @Resource
    private EmailProperties emailProperties;
    @Resource
    private BaseEmailService baseEmailService;

    @Resource(name = "asyncExecutor")
    private Executor asyncExecutor;

    @Resource
    private AssessTaskService assessTaskService;
    @Resource
    private SeasonRepository seasonRepository;
    @Resource
    private UserSeasonRepository userSeasonRepository;
    @Resource
    private UserSeasonService userSeasonService;
    @Resource
    private UserProfileRepository userProfileRepository;


    public UserSeasonMailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * 发送图片格式通知邮件
     */
    @Async
    public void sendFeedBackEmail(String content, File jpgFile, UserInfoDTO userInfo) {
        //根据配置文件获取session
        Session session = Session.getDefaultInstance(baseEmailService.getProperties());
        MimeMessage mimeMessage = new MimeMessage(session);
        Transport transport = null;
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, CharEncoding.UTF_8);
            message.setTo(emailProperties.getReceiveMailAddress());
            message.setFrom(new InternetAddress(emailProperties.getSender(), emailProperties.getSenderName()));

            message.setSubject("用户反馈");

            Locale locale = Locale.getDefault();
            Context context = new Context(locale);
            context.setVariable("message", content);
            context.setVariable("userInfo", userInfo);
            content = templateEngine.process("feedBackEmail", context);

            Multipart multipart = null;
            if (null != jpgFile) {
                //设置文本和图片布局
                multipart = new MimeMultipart("related");
                BodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(content + "\n<img src=\"cid:image\">", "text/html;charset=UTF-8");
                multipart.addBodyPart(htmlPart);
                BodyPart imgPart = new MimeBodyPart();
                DataSource fds = new FileDataSource(jpgFile);

                imgPart.setDataHandler(new DataHandler(fds));
                imgPart.setHeader("Content-ID", "<image>");
                multipart.addBodyPart(imgPart);
            } else {
                multipart = new MimeMultipart();
                BodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(content, "text/html;charset=UTF-8");
                multipart.addBodyPart(htmlPart);
            }
            mimeMessage.setContent(multipart);
            if (emailProperties.isEmailEnable()) {
                transport = session.getTransport();
                //使用stmp 用户名、密码
                transport.connect(emailProperties.getHost(), emailProperties.getUserName(), emailProperties.getPassword());
                //发送邮件
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
            if (null != jpgFile) {
                jpgFile.delete();
            }
            log.info("发送图片格式通知邮件-->邮件发送，邮件开关：{}，发送成功", emailProperties.isEmailEnable());
        } catch (Exception e) {
            log.error("Email could not be sent to user '{}'", JSON.toJSON(userInfo), e);
        } finally {
            try {
                if (null != transport) {
                    transport.close();
                }
            } catch (MessagingException e) {

            }
        }
    }


    /**
     * 邮件提醒未填写待评任务人员
     */
    @Async
    public void remindUnderwayAssessTaskUser(Long seasonId) {
        Season season = seasonRepository.selectByPrimaryKey(seasonId);
        if (null == season) {
            log.info("seasonId={}考核任务不存在!", seasonId);
            return;
        }
        if (season.getSecondStageEndTime().before(new Date())) {
            log.info("{}考核任务同事互评阶段已经结束!", season.getTitle());
            return;
        }
        List<UserInfoDTO> userInfoDTOList = assessTaskService.queryAssessTaskUserByStatusUnderway(seasonId);
        if (null == userInfoDTOList || userInfoDTOList.size() == 0) {
            log.warn("待评任务未评价任务邮件提醒，当前不存在未填写待评任务用户");
            return;
        }
        log.info("{}待评任务未评价任务邮件提醒，发送邮件", season.getTitle());
        for (UserInfoDTO userInfoDTO : userInfoDTOList) {
            //离职用户不发送邮件
            Byte userStatus = userInfoDTO.getStatus();
            if (null == userStatus || UserStatus.INVALID.getCode().equals(userStatus)) {
                continue;
            }
            asyncExecutor.execute(() -> sendAssessRemindEmail(season, userInfoDTO));
        }
    }


    /**
     * 邮件提醒未完成OKR制定的用户
     */
    @Async
    public void remindUnFinishFilledOKRUser(Long seasonId) {
        Season season = seasonRepository.selectByPrimaryKey(seasonId);
        if (null == season) {
            log.info("邮件提醒未完成OKR制定的用户失败，seasonId={}考核任务不存在!", seasonId);
            return;
        }

        ProgressStatusEnum status = SeasonDateUtil.getProcessStatus(season);
        if (!status.getCode().equals(ProgressStatusEnum.SECOND_IN.getCode())) {
            log.info("邮件提醒未完成OKR制定的用户失败，{}考核任务制定阶段为：{}!", season.getTitle(), status.getName());
            return;
        }
        List<UserInfoDTO> userInfoDTOList = userSeasonService.getUnFinishFilledOkrList(seasonId);
        if (null == userInfoDTOList || userInfoDTOList.size() == 0) {
            log.warn("邮件提醒未完成OKR制定的用户失败，当前不存在未填写待评任务用户");
            return;
        }
        log.info("{}待评任务未评价任务邮件提醒，发送邮件", season.getTitle());
        for (UserInfoDTO userInfoDTO : userInfoDTOList) {
            //离职用户不发送邮件
            Byte userStatus = userInfoDTO.getStatus();
            if (null == userStatus || UserStatus.INVALID.getCode().equals(userStatus)) {
                continue;
            }
            asyncExecutor.execute(() -> sendUnFinishFilledOKRRemindEmail(season, userInfoDTO));
        }
    }

    @Async
    public void assessTaskReportMail(UserReportDetailVM vm, Long userId, String sendMail, String templateName) {
        List<Long> userIdList = Lists.newArrayList(vm.getAssessTaskUserId(), vm.getReportUserId(), vm.getVerifyUserId());
        List<UserProfile> list = userProfileRepository.findAllByIdIn(userIdList);

        Map<Long, String> nameMap = Maps.newHashMap();
        Map<Long, UserProfile> userMap = Maps.newHashMap();
        list.forEach(user -> {
            nameMap.put(user.getId(), user.getRealName());
            userMap.put(user.getId(), user);
        });
        Context context = new Context();
        vm.setReportUserName(nameMap.get(vm.getReportUserId()));
        vm.setAssessTaskUserName(nameMap.get(vm.getAssessTaskUserId()));
        vm.setVerifyUserName(nameMap.get(vm.getVerifyUserId()));
        if (null != userId) {
            UserProfile user = userMap.get(userId);
            if (null == user) {
                return;
            }
            if (UserStatus.INVALID.getCode().equals(user.getStatus())) {
                log.info("发送邮件---->用户ID={},用户名:{} 已离职，邮件不发送！", user.getId(), user.getRealName());
                return;
            }
            sendMail = user.getEmail();
        }
        context.setVariable("vm", vm);
        baseEmailService.sendEmailSimple(context, sendMail, "评分举报", templateName);
    }


    @Async
    public void assessTaskReportMailToOKRAdmin(UserReportDetailVM vm) {
        assessTaskReportMail(vm, null, emailProperties.getSender(), "reportToAdminEmail");
    }

    @Async
    public void assessTaskReportMailToReportUser(UserReportDetailVM vm) {
        assessTaskReportMail(vm, vm.getReportUserId(), "", "reportAnswerToReportUserEmail");
    }


    @Async
    public void assessTaskReportMailToAssessUser(UserReportDetailVM vm) {
        Long userSeasonId = vm.getUserSeasonId();
        if (null != userSeasonId) {
            UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
            Season season = seasonRepository.selectByPrimaryKey(userSeason.getSeasonId());
            vm.setSeasonTitle(season.getTitle());
        }
        assessTaskReportMail(vm, vm.getAssessTaskUserId(), "", "reportAnswerToAssessUserEmail");
    }

    /**
     * 发送提醒邮件
     *
     * @param season
     * @param userInfoDTO
     */
    @Async
    public void sendAssessRemindEmail(Season season, UserInfoDTO userInfoDTO) {
        sendEmailSimple(season, userInfoDTO, "未评价任务提醒", "remindAssessTaskEmail");
    }

    /**
     * OKR制定的阶段邮件提醒
     *
     * @param season
     * @param userInfoDTO
     */
    @Async
    public void sendUnFinishFilledOKRRemindEmail(Season season, UserInfoDTO userInfoDTO) {
        sendEmailSimple(season, userInfoDTO, "OKR制定提醒", "remindORKUnFinishFilledEmail");
    }


    private void sendEmailSimple(Season season, UserInfoDTO userInfoDTO, String subject, String templateName) {
        Context context = new Context();
        context.setVariable("season", season);
        context.setVariable("userName", userInfoDTO.getRealName());
        baseEmailService.sendEmailSimple(context, userInfoDTO.getEmail(), subject, templateName);
    }


}
