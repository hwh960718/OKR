package com.mobvista.okr;

import com.mobvista.okr.domain.ScoreOption;
import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.repository.ScoreOptionRepository;
import com.mobvista.okr.repository.UserSeasonRepository;
import com.mobvista.okr.service.MakeScoreAndRankService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 注释：
 * 作者：刘腾飞【liutengfei】
 * 时间：2018-04-12 17:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MakeScoreTest {

    @Resource
    private UserSeasonRepository userSeasonRepository;

    @Resource
    private ScoreOptionRepository scoreOptionRepository;

    @Resource
    private MakeScoreAndRankService makeScoreAndRankService;


    @Test
    public void makeScore() {
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(4897L);
        List<ScoreOption> optionList = scoreOptionRepository.findAllByType(null);
        Map<Long, ScoreOption> scoreOptionMap = optionList.stream().collect(Collectors.toMap(scoreOption -> scoreOption.getId(), scoreOption -> scoreOption));

//        makeScoreAndRankService.makeUserSeasonScore(userSeason, scoreOptionMap, null);
    }

}
