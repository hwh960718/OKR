package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.Department;
import com.mobvista.okr.dto.DepartmentTreeNodeDTO;
import com.mobvista.okr.dto.mbacs.AccountDept;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.DepartmentRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.service.mbacs.AcsProxyService;
import com.mobvista.okr.util.OkHttpUtil;
import com.mobvista.okr.util.StringOkrUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mobvista.okr.constants.RedisConstants.USE_OKR_DEPARTMENT_IDS;

/**
 * Service class for managing Department.
 *
 * @author Weier Gu (顾炜)
 */
@Service
public class DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    @Resource
    private AcsProxyService acsProxyService;
    @Resource
    private RedisService redisService;
    @Resource
    private DepartmentRepository departmentRepository;

    /**
     * 获取部门树
     */
    public List<DepartmentTreeNodeDTO> getDepartmentTree() {
        List<Department> departments = departmentRepository.findAll();

        List<DepartmentTreeNodeDTO> result = Lists.newArrayList();
        departments
                .stream()
                .filter(department -> department.getLevel().equals(1))
                .forEach(department -> {
                    convertDepartMentTreeDTO(departments, result, department);
                });

        return result;
    }

    private void convertDepartMentTreeDTO(List<Department> departments, List<DepartmentTreeNodeDTO> result, Department department) {
        DepartmentTreeNodeDTO departmentTreeNodeDTO = new DepartmentTreeNodeDTO();
        departmentTreeNodeDTO.setId(department.getId());
        departmentTreeNodeDTO.setTitle(department.getName());
        makeDepartmentTree(departmentTreeNodeDTO, departments);

        result.add(departmentTreeNodeDTO);
    }

    /**
     * 获取leader下属部门树
     *
     * @return
     */
    public List<DepartmentTreeNodeDTO> getDepartmentTreeForLeader() {
        Long id = SecurityUtils.getCurrentUserId();
        List<Department> leaderDepartments = departmentRepository.findAllByLeaderCode(id);
        if (leaderDepartments.size() == 0) {
            return Lists.newArrayList();
        }

        List<Department> departments = departmentRepository.findAll();
        List<DepartmentTreeNodeDTO> result = Lists.newArrayList();
        leaderDepartments
                .forEach(department -> {
                    convertDepartMentTreeDTO(departments, result, department);
                });

        return result;
    }

    /**
     * 获取使用okr的部门id
     *
     * @return
     */
    public List<Long> getOkrDepartmentIds() {
        List<Long> depIds;
        String deptIds = redisService.get(USE_OKR_DEPARTMENT_IDS);
        if (deptIds != null && deptIds.length() != 0) {
            depIds = StringOkrUtil.convertStr2LongList(deptIds);
        } else {
            List<String> deptNames = new ArrayList<>();
            deptNames.add("技术中心");
            //deptNames.add("AI中心");
            List<Department> departmentList = departmentRepository.findAllByLevelAndNameIn(1, deptNames);
            depIds = departmentList.stream().map(dep -> dep.getId()).collect(Collectors.toList());
        }
        return depIds;
    }


    /**
     * 获取使用okr的所有部门id
     *
     * @return
     */
    public List<Long> getOkrALLDepartmentIds() {
        return getDepartmentIdAndChildrens(getOkrDepartmentIds());
    }

    /**
     * 从账号中心同步部门数据
     *
     * @return
     */
    public List<Department> syncDepartmentFromAcs() {
        List<AccountDept> accountDeptList = getAllAccountDeptFromAccount();
        List<Department> acsDepartmentList = accountDeptList.stream()
                .map(departmentResult -> {
                    Department department = new Department();
                    department.setId(departmentResult.getId());
                    department.setLevel(departmentResult.getLevel());
                    department.setParentId(departmentResult.getParentId());

                    /*String[] names = departmentResult.getName().split("/");
                    department.setName(names[names.length - 1]);*/
                    department.setName(departmentResult.getName());

                    if (departmentResult.getLeader() != null && departmentResult.getLeader().size() > 0) {
                        department.setLeaderCode(departmentResult.getLeader().get(0));
                    }

                    return department;
                })
                .collect(Collectors.toList());
        return acsDepartmentList;
    }


    /**
     * 获取账号中心所有部门
     *
     * @return
     */
    private List<AccountDept> getAllAccountDeptFromAccount() {
        try {
            String args = acsProxyService.getAcsRequestArgs();
            String response = OkHttpUtil.get(acsProxyService.deptUrl() + "?" + args);
            return JSON.parseArray(response, AccountDept.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取账号中心所有部门数据异常", e);
            return new ArrayList<>();
        }
    }


    private void makeDepartmentTree(DepartmentTreeNodeDTO node, List<Department> departments) {
        departments.forEach(childDepartment -> {
            if (node.getChildren() == null) {
                node.setChildren(Lists.newArrayList());
            }
            if (node.getId().equals(childDepartment.getParentId())) {
                DepartmentTreeNodeDTO dto = new DepartmentTreeNodeDTO();
                dto.setId(childDepartment.getId());
                dto.setTitle(childDepartment.getName());
                node.getChildren().add(dto);
            }
        });
        if (node.getChildren() != null) {
            node.getChildren().forEach(n -> {
                makeDepartmentTree(n, departments);
            });
        }
    }


    public List<Long> getDepartmentIdAndChildrens(List<Long> idList) {
        if (idList == null || idList.size() == 0) {
            return new ArrayList<>();
        }
        List<Department> secondDepartments = departmentRepository.findAllByParentIdIn(idList);
        if (secondDepartments != null && secondDepartments.size() != 0) {
            List<Long> childList = secondDepartments.stream().map(dep -> dep.getId()).collect(Collectors.toList());
            idList.addAll(getDepartmentIdAndChildrens(childList));
        }
        return idList;
    }


    /**
     * 根据一级部门获二级部门
     * Map<Long,List<Long>> key:一级部门id，value：二级部门id
     *
     * @return
     */
    public Map<Long, List<Long>> getSecondLevelDepartmentIdListByOneLevel() {

        Map<Long, List<Long>> map = Maps.newHashMap();
        //从redis获取指定一级部门id
        String oneLevelDepIdStr = redisService.get(RedisConstants.ASSESS_GROUP_ONE_LEVEL_DEPARTMENT_IDS);
        //校验是否制定一级部门
        ExceptionUtil.checkState(StringUtils.isNotBlank(oneLevelDepIdStr), "未指定一级部门");
        List<Long> oneLevelDepIdList = StringOkrUtil.convertStr2LongList(oneLevelDepIdStr);
        //根据一级部门获取二级部门
        List<Department> departmentList = departmentRepository.findAllByParentIdIn(oneLevelDepIdList);
        ExceptionUtil.checkState(null != departmentList && departmentList.size() > 0, "一级部门对应子部门不存在");

        departmentList.forEach(d -> {
            List<Long> list = map.get(d.getParentId());
            list = null == list ? Lists.newArrayList() : list;
            list.add(d.getId());
            map.put(d.getParentId(), list);
        });
        return map;
    }

}
