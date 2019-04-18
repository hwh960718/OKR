package com.mobvista.okr.job;

import com.google.common.collect.Lists;
import com.mobvista.okr.domain.Department;
import com.mobvista.okr.repository.DepartmentRepository;
import com.mobvista.okr.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步acs系统部门信息
 * cron = "0 0 1 * * ?"
 *
 * @author 顾炜[GuWei]
 */
@Component
public class SyncAcsDepartmentJob {
    private final Logger log = LoggerFactory.getLogger(SyncAcsDepartmentJob.class);

    @Resource
    private DepartmentService departmentService;

    @Resource
    private DepartmentRepository departmentRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    public void execute() {
        log.info("同步acs系统部门信息 start");
        syncDepartment();
        log.info("同步acs系统部门信息 end");
    }


    /**
     * 同步部门数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void syncDepartment() {
        List<Department> acsDepartmentList = departmentService.syncDepartmentFromAcs();
        if (null == acsDepartmentList || acsDepartmentList.size() == 0) {
            log.info("同步acs系统部门信息，需要同步的部门数据为空！");
            return;
        }

        Map<Long, Department> departmentMap = new HashMap<>();
        List<Department> list = departmentRepository.findAll();
        for (Department department : list) {
            departmentMap.put(department.getId(), department);
        }

        List<Department> insertList = Lists.newArrayList();
        List<Department> updateList = Lists.newArrayList();

        for (Department acsDept : acsDepartmentList) {
            Department department = departmentMap.get(acsDept.getId());
            if (department == null) {
                //如果不存在，需要插入新的部门信息
                insertList.add(acsDept);
            } else {
                //存在更新操作
                department.setLevel(acsDept.getLevel());
                department.setParentId(acsDept.getParentId());
                department.setName(acsDept.getName());
                if (acsDept.getLeaderCode() != null && acsDept.getLeaderCode() > 0) {
                    department.setLeaderCode(acsDept.getLeaderCode());
                }
                updateList.add(department);
            }
        }
        if (insertList.size() > 0) {
            departmentRepository.insertList(insertList);
        }
        if (updateList.size() > 0) {
            departmentRepository.updateList(updateList);
        }

    }
}
