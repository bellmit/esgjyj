package com.eastsoft.esgjyj.service.impl;

import com.eastsoft.esgjyj.dao.OfficeMapper;
import com.eastsoft.esgjyj.dao.UserMapper;
import com.eastsoft.esgjyj.dao.YjkhKhdxDao;
import com.eastsoft.esgjyj.dao.YjkhKhjgDao;
import com.eastsoft.esgjyj.domain.YjkhKhdxDO;
import com.eastsoft.esgjyj.domain.YjkhKhjgDO;
import com.eastsoft.esgjyj.vo.FgkpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<FgkpVO> listFgKp(String khid,String ofid) {
        List<FgkpVO> fgkpVOS = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("dxtype", "1");
        param.put("khid", khid);
        param.put("officeid", ofid);
        List<YjkhKhdxDO> khdxDOS = yjkhKhdxDao.list(param);
        for (YjkhKhdxDO khdxDO : khdxDOS) {
        	if ("0F000119".equals(khdxDO.getOfficeid())) continue;
//        	if ("0F000324".equals(khdxDO.getUserid())) continue;
            FgkpVO fgkpVO = new FgkpVO();
            Map<String, Object> param0 = new HashMap<>();
            param0.put("dxid", khdxDO.getId());
            if (null != khdxDO.getOfficeid()) {
                fgkpVO.setOffice(officeMapper.selectByPrimaryKey(khdxDO.getOfficeid()).getShortname());
            }
            if (null != khdxDO.getUserid()) {
                fgkpVO.setName(userMapper.selectByPrimaryKey(khdxDO.getUserid()).getUsername());
            }
            fgkpVO.setKhdx(khdxDO.getId());
            List<YjkhKhjgDO> khjgDOS = yjkhKhjgDao.list(param0);
            float total = 0;
            for (YjkhKhjgDO khjgDO : khjgDOS) {
                if (3 == khjgDO.getColIndex()) {
                    fgkpVO.setA3(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (4 == khjgDO.getColIndex()) {
                    fgkpVO.setA4(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (5 == khjgDO.getColIndex()) {
                    fgkpVO.setA5(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (6 == khjgDO.getColIndex()) {
                    fgkpVO.setA6(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (7 == khjgDO.getColIndex()) {
                    fgkpVO.setA7(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (8 == khjgDO.getColIndex()) {
                    fgkpVO.setA8(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (9 == khjgDO.getColIndex()) {
                    fgkpVO.setA9(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (10 == khjgDO.getColIndex()) {
                    fgkpVO.setA10(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (11 == khjgDO.getColIndex()) {
                    fgkpVO.setA11(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (12 == khjgDO.getColIndex()) {
                    fgkpVO.setA12(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (13 == khjgDO.getColIndex()) {
                    fgkpVO.setA13(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                } else if (14 == khjgDO.getColIndex()) {
                    fgkpVO.setA14(String.format("%.2f", khjgDO.getScore()));
                    total += khjgDO.getScore();
                }
                fgkpVO.setToScore(String.format("%.2f", total));
            }
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

}
