package com.eastsoft.esgjyj.service.impl;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.dao.OfficeMapper;
import com.eastsoft.esgjyj.dao.UserMapper;
import com.eastsoft.esgjyj.dao.YjkhKhdxDao;
import com.eastsoft.esgjyj.dao.YjkhKhjgDao;
import com.eastsoft.esgjyj.domain.YjkhKhdxDO;
import com.eastsoft.esgjyj.domain.YjkhKhjgDO;
import com.eastsoft.esgjyj.vo.FgkpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
@Service
public class TjbbServiceImpl {
    @Autowired
    private YjkhKhdxDao yjkhKhdxDao;
    @Autowired
    private YjkhKhjgDao yjkhKhjgDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private BaseDao baseDao;

    public List<FgkpVO> listFgKp(String khid,String ofid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "1");
        param.put("khid", khid);
        param.put("officeid", ofid);
        FgkpVO fgkpVO = null;
        Map<String, Object> param0 = null;
        float total = 0;
        Map<String, Map<String, Object>> map = getScoreMap(khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        Map<String, Object> scoreMap = null;
        for (YjkhKhdxDO khdxDO : khdxDOS) {
        	if ("0F000119".equals(khdxDO.getOfficeid())) continue;
//        	if ("0F000324".equals(khdxDO.getUserid())) continue;
        	fgkpVO = new FgkpVO();
            param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            scoreMap = map.get(khdxDO.getId());
            if (scoreMap == null) continue;
            fgkpVO.setKhdx(khdxDO.getId());
            fgkpVO.setA3(String.format("%.2f", scoreMap.get("3") == null ? 0 : (Double)scoreMap.get("3")));
            fgkpVO.setA4(String.format("%.2f", scoreMap.get("4") == null ? 0 : (Double)scoreMap.get("4")));
            fgkpVO.setA5(String.format("%.2f", scoreMap.get("5") == null ? 0 : (Double)scoreMap.get("5")));
            fgkpVO.setA6(String.format("%.2f", scoreMap.get("6") == null ? 0 : (Double)scoreMap.get("6")));
            fgkpVO.setA7(String.format("%.2f", scoreMap.get("7") == null ? 0 : (Double)scoreMap.get("7")));
            fgkpVO.setA8(String.format("%.2f", scoreMap.get("8") == null ? 0 : (Double)scoreMap.get("8")));
            fgkpVO.setA9(String.format("%.2f", scoreMap.get("9") == null ? 0 : (Double)scoreMap.get("9")));
            fgkpVO.setA10(String.format("%.2f", scoreMap.get("10") == null ? 0 : (Double)scoreMap.get("10")));
            fgkpVO.setA11(String.format("%.2f", scoreMap.get("11") == null ? 0 : (Double)scoreMap.get("11")));
            fgkpVO.setA12(String.format("%.2f", scoreMap.get("12") == null ? 0 : (Double)scoreMap.get("12")));
            fgkpVO.setA13(String.format("%.2f", scoreMap.get("13") == null ? 0 : (Double)scoreMap.get("13")));
            fgkpVO.setA14(String.format("%.2f", scoreMap.get("14") == null ? 0 : (Double)scoreMap.get("14")));
            total = Float.parseFloat(fgkpVO.getA3()) + Float.parseFloat(fgkpVO.getA4()) + Float.parseFloat(fgkpVO.getA5())
            + Float.parseFloat(fgkpVO.getA6()) + Float.parseFloat(fgkpVO.getA7()) + Float.parseFloat(fgkpVO.getA8())
            + Float.parseFloat(fgkpVO.getA9()) + Float.parseFloat(fgkpVO.getA10()) + Float.parseFloat(fgkpVO.getA11())
            + Float.parseFloat(fgkpVO.getA12()) + Float.parseFloat(fgkpVO.getA13()) + Float.parseFloat(fgkpVO.getA14());
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }

    public List<FgkpVO> listTzKp(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "2");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);

        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            float total = 0;
            fgkpVO.setKhdx(khdxDO.getId());
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                } else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                }else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                } else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                }
                else if (8 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA8(String.format("%.2f", khjgDO.getScore()));
                }
            }
            fgkpVOS.add(fgkpVO);
            fgkpVO.setToScore(String.format("%.2f", total));
        }
        return fgkpVOS;
    }

    public List<FgkpVO> listZhKp(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>(16);
        param.put("dxtype", "3");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                }
                else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                }
                else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                }else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                }
            }
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }
    public List<FgkpVO> listZhfzr(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "8");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                }
                else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                }
                else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                }else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                }
            }
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }
    public List<FgkpVO> listZhzl(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "6");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                }else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                }
                else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                }else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                }
            }
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }
    public List<FgkpVO> listZhsjy(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "7");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                }else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                }else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                }else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                }
            }
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }


    public List<FgkpVO> listZlKp(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "4");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                } else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                } else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                } else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                } else if (15 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA15(String.format("%.2f", khjgDO.getScore()));
                }
                else if (15 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA15(String.format("%.2f", khjgDO.getScore()));
                }

            }
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }

    public List<FgkpVO> listSjyKp(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "5");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                } else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                } else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                } else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                } else if (8 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA8(String.format("%.2f", khjgDO.getScore()));
                } else if (15 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA15(String.format("%.2f", khjgDO.getScore()));
                } else if (9 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA9(String.format("%.2f", khjgDO.getScore()));
                } else if (10 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA10(String.format("%.2f", khjgDO.getScore()));
                }
                else if (15 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA15(String.format("%.2f", khjgDO.getScore()));
                }
            }
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }

    public List<FgkpVO> listPrsjy(String khid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "9");
        param.put("khid", khid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                } else if (4 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                } else if (5 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                } else if (6 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                } else if (7 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                } else if (8 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA8(String.format("%.2f", khjgDO.getScore()));
                } else if (15 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA15(String.format("%.2f", khjgDO.getScore()));
                } else if (9 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA9(String.format("%.2f", khjgDO.getScore()));
                }
                else if (10 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA10(String.format("%.2f", khjgDO.getScore()));
                }
                else if (15 == khjgDO.getColIndex()) {
                    total += khjgDO.getScore();
                    fgkpVO.setA15(String.format("%.2f", khjgDO.getScore()));
                }

            }
            fgkpVO.setToScore(String.format("%.2f", total));
            fgkpVOS.add(fgkpVO);
        }
        return fgkpVOS;
    }
    /**
     * 获取法官考核的得分
     * @param khid
     * @return
     */
    public Map<String, Map<String, Object>> getScoreMap(String khid) {
    	String sql = "select YJKH_KHJG.* from YJKH_KHJG, YJKH_KHDX where YJKH_KHJG.DXID = YJKH_KHDX.ID "
    			+ " and YJKH_KHDX.DXTYPE = '1' and YJKH_KHDX.KHID = '" + khid + "'";
    	List<Map<String, Object>> list = baseDao.queryForList(sql);
    	Map<String, Map<String, Object>> map = new HashMap<>();
    	Map<String, Object> scoreMap = null;
    	String khdxid;
    	double score;
    	int colIndex;
    	for(Map<String, Object> item : list) {
    		khdxid = (String)item.get("DXID");
    		score = ((BigDecimal)item.get("SCORE")).doubleValue();
    		colIndex = (Integer)item.get("COL_INDEX");
    		if (map.get(khdxid) == null) {
    			scoreMap = new HashMap<>();
    			scoreMap.put(colIndex + "", score);
    			map.put(khdxid, scoreMap);
			} else {
				scoreMap = map.get(khdxid);
				score = scoreMap.get(colIndex + "") == null ? score : (Integer)scoreMap.get(colIndex + "") + score;
				scoreMap.put(colIndex + "", score);
				map.put(khdxid, scoreMap);
			}
    	}
    	return map;
    }
}
