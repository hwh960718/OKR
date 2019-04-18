package com.mobvista.okr.util;

import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.dto.mbacs.MenuVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 注释：路由定义
 * 作者：刘腾飞【liutengfei】
 * 时间：2016-12-26 上午11:59
 *
 * @author guwei
 */
public class MenuUtil {

    /**
     * 级别 1
     */
    private static final int LEVEL_1 = 1;
    /**
     * 级别 2
     */
    private static final int LEVEL_2 = 2;

    /**
     * 菜单定义
     *
     * @return
     */
    public static List<MenuVo> allMenus() {
        List<MenuVo> menuVos = new ArrayList<>();

        //首页
        menuVos.add(new MenuVo("/home", "home", LocaleUtil.getLang("menu.home"), "home", "ios-home", "10000", "0", LEVEL_1));
        menuVos.add(new MenuVo("index", "home", LocaleUtil.getLang("menu.home"), "home_index", "ios-home", "10001", "10000", LEVEL_2));
        //我的考核
        menuVos.add(new MenuVo("/user_season", "user_season", LocaleUtil.getLang("menu.user.season"), "user_season", "ios-list", "11000", "0", LEVEL_1));
        menuVos.add(new MenuVo("index", "user_season", LocaleUtil.getLang("menu.user.season"), "user_season_index", "ios-list", "11001", "11000", LEVEL_2));
        //待评任务
        menuVos.add(new MenuVo("/assessment_list", "assessment_list", LocaleUtil.getLang("menu.assessment"), "assessment_list", "ios-toggle", "12000", "0", LEVEL_1));
        menuVos.add(new MenuVo("index", "assessment_list", LocaleUtil.getLang("menu.assessment"), "assess_list_index", "ios-toggle", "12001", "12000", LEVEL_2));
        //绩效排行
        //menuVos.add(new MenuVo("/ranking", "ranking", LocaleUtil.getLang("menu.ranking"), "ranking", "ribbon-a", "13000", "0", LEVEL_1));
        //menuVos.add(new MenuVo("index", "ranking", LocaleUtil.getLang("menu.ranking"), "ranking_index", "ribbon-a", "13001", "13000", LEVEL_2));
        //公司OKR预览
        //menuVos.add(new MenuVo("/company_overview", "company_overview", LocaleUtil.getLang("menu.company.overview"), "company_overview", "ios-world", "14000", "0", LEVEL_1));
        //menuVos.add(new MenuVo("index", "company_overview", LocaleUtil.getLang("menu.company.overview"), "company_overview_index", "ios-world", "14001", "14000", LEVEL_2));
        //下属评分任务概览
        //menuVos.add(new MenuVo("/branch_overview", "branch_overview", LocaleUtil.getLang("menu.branch.overview"), "branch_overview", "ios-color-filter", "15000", "0", LEVEL_1));
        //menuVos.add(new MenuVo("index", "branch_overview", LocaleUtil.getLang("menu.branch.overview"), "branch_overview_index", "ios-color-filter", "15001", "15000", LEVEL_2));

        //todo 用户管理模块提取到一级目录
        menuVos.add(new MenuVo("/user_manage", "user_manage", LocaleUtil.getLang("menu.user.manage"), "user_manage", "android-settings", "15100", "0", LEVEL_1));
        menuVos.add(new MenuVo("index", "user_manage", LocaleUtil.getLang("menu.user.manage"), "user-manage-index", "person", "15101", "15100", LEVEL_2));

        //系统管理
        menuVos.add(new MenuVo("/manage", "manage", LocaleUtil.getLang("menu.manage"), "manage", "android-settings", "16000", "0", LEVEL_1));
        //系统管理-公告管理
        menuVos.add(new MenuVo("notice_page", "notice", LocaleUtil.getLang("menu.notice"), "notice", "ios-bell", "16001", "16000", LEVEL_2));
        //系统管理-考核管理
        menuVos.add(new MenuVo("season", "season", LocaleUtil.getLang("menu.season"), "season", "android-checkbox", "16002", "16000", LEVEL_2));
        //系统管理-评分配置
        //menuVos.add(new MenuVo("score_option", "score_option", LocaleUtil.getLang("menu.option.score"), "score_option", "android-settings", "16003", "16000", LEVEL_2));
        //系统管理-用户管理
//        menuVos.add(new MenuVo("user_manage", "user_manage", LocaleUtil.getLang("menu.user.manage"), "user-manage", "person", "16004", "16000", LEVEL_2));
        //系统管理-用户职级权重配置
        //menuVos.add(new MenuVo("score_weight_option", "score_weight_option", LocaleUtil.getLang("menu.user.score.weight.option"), "score_weight_option", "android-settings", "16005", "16000", LEVEL_2));
        //系统管理-举报管理
        menuVos.add(new MenuVo("report_manage", "report_manage", LocaleUtil.getLang("menu.report.manage"), "report-manage", "person", "16006", "16000", LEVEL_2));

        //积分管理
        menuVos.add(new MenuVo("/integral", "score_manage", LocaleUtil.getLang("menu.score.manage"), "integral", "android-settings", "17000", "0", LEVEL_1));
        //系统管理-公告管理
        menuVos.add(new MenuVo("user_list", "score_user_list", LocaleUtil.getLang("menu.score.user.list"), "integral-user-list", "person", "17001", "17000", LEVEL_2));
        //系统管理-考核管理
        menuVos.add(new MenuVo("rule_list", "score_rule_list", LocaleUtil.getLang("menu.score.rule.list"), "integral-rule-list", "person", "17002", "17000", LEVEL_2));

        //商品管理
        menuVos.add(new MenuVo("/commodity_manage", "commodity_manage", LocaleUtil.getLang("menu.commodity.manage"), "commodity", "easel", "18000", "0", LEVEL_1));
        //商品
        menuVos.add(new MenuVo("index", "commodity_manage_list", LocaleUtil.getLang("menu.commodity.manage.list"), "commodity-manage-index", "easel", "18001", "18000", LEVEL_2));
        menuVos.add(new MenuVo("shelf", "commodity_shelf", LocaleUtil.getLang("menu.commodity.manage.shelf"), "commodity-shelf", "ios-game-controller-a", "18002", "18000", LEVEL_2));
        menuVos.add(new MenuVo("order", "commodity_order", LocaleUtil.getLang("menu.commodity.manage.order"), "commodity-order", "clipboard", "18002", "18000", LEVEL_2));
//零时
//        menuVos.add(new MenuVo("list", "commodity_shelf_list", LocaleUtil.getLang("menu.commodity.manage.shelf.list"), "commodity-list", "ios-cart-outline", "18003", "18000", LEVEL_2));

        //积分商城
        menuVos.add(new MenuVo("/commodity_manage", "commodity_shelf_list", LocaleUtil.getLang("menu.commodity.manage.shelf.list"), "commodity-list", "ios-cart-outline", "19000", "0", LEVEL_1));
        menuVos.add(new MenuVo("list", "commodity_shelf_list", LocaleUtil.getLang("menu.commodity.manage.shelf.list"), "commodity-list", "ios-cart-outline", "19001", "19000", LEVEL_2));

        //okr文档
        //menuVos.add(new MenuVo("/okr_doc", "", LocaleUtil.getLang("menu.okr.doc"), "okr_doc", "ios-book", "20000", "0", LEVEL_1, CommonConstants.URL_OKR_SYSTEM_DOC));
        //menuVos.add(new MenuVo("index", "", LocaleUtil.getLang("menu.okr.doc"), "okr_doc", "ios-book", "20001", "20000", LEVEL_2, CommonConstants.URL_OKR_SYSTEM_DOC));


        return menuVos;
    }


}
