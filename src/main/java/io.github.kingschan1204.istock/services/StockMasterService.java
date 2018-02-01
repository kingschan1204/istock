package io.github.kingschan1204.istock.services;

import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.model.vo.StockMasterVo;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingschan on 2017/6/28.
 */
@Service
public class StockMasterService {

    private static Logger log = LoggerFactory.getLogger(StockMasterService.class);
    @Autowired
    private StockMasterRepository stockRepository;


    public StockMasterVo getStock(String code) {
        StockMasterEntity sme = stockRepository.findOne(code);
        if (null != sme) {
            StockMasterVo vo = new StockMasterVo();
            BeanUtils.copyProperties(sme, vo);
            return vo;
        }
        return null;
    }


    public void saveStock(String code) throws Exception {
        /*StockMasterEntity stock = stockRepository.findOne(code);
        if (null == stock) {
            //   throw new Exception("已存在，不要重复添加！");
            stock = new StockMasterEntity();
        }
        List<SinaStockPriceDto> lis = StockSpilderUtil.getStockPrice(new String[]{code});
        if (null == lis || lis.size() == 0) {
            throw new Exception("代码不存在！");
        }
        StockMasterDto smd = StockSpilderUtil.getStockInfo(code);
        List<ThsStockDividendRate> drs = StockSpilderUtil.getStockDividendRate(code);

        stock.setsDividendYear(-1);
        stock.setsDividendRate(BigDecimal.valueOf(-1d));
        if (null != drs) {
            for (int i = 0; i < drs.size(); i++) {
                ThsStockDividendRate item = drs.get(i);
                if (item.getPercent() != -1) {
                    stock.setsDividendYear(Integer.valueOf(item.getDividendYear().replaceAll("\\D+", "")));
                    stock.setsDividendRate(BigDecimal.valueOf(item.getPercent()));
                    break;
                }
            }
        }
        stock.setsCode(lis.get(0).getsCode());
        stock.setsStockName(lis.get(0).getsStockName());
        stock.setsCurrentPrice(lis.get(0).getsCurrentPrice());
        stock.setsYesterdayPrice(lis.get(0).getsYesterdayPrice());
        stock.setsRangePrice(lis.get(0).getsRangePrice());
        stock.setsMainBusiness(smd.getsMainBusiness());
        stock.setsIndustry(smd.getsIndustry());
        stock.setsPeDynamic(smd.getsPeDynamic());
        stock.setsPeStatic(smd.getsPeStatic());
        stock.setsPb(smd.getsPb());
        stock.setsTotalValue(smd.getsTotalValue());
        stock.setsRoe(smd.getsRoe());
        stockRepository.save(stock);*/
    }

    public Page<StockMasterVo> stockMasterList(int pageindex, int pagesize, final String code, String orderfidld, String sort) {
        Pageable pageable = null;
        // 判断是否包含排序信息,生产对应的Pageable查询条件
        if (null != orderfidld && !orderfidld.isEmpty()&& null != sort) {
            Sort s = new Sort(
                    sort.equalsIgnoreCase("asc") ?
                            Sort.Direction.ASC : Sort.Direction.DESC
                    , orderfidld);
            pageable = new PageRequest(pageindex-1, pagesize, s);
        } else {
            Sort s = new Sort(Sort.Direction.DESC, "sCode");
            pageable = new PageRequest(pageindex-1, pagesize, s);
        }
        //pageable = new PageRequest(pageindex - 1, pagesize);
        // 获取包含分页信息和UserVo集合的Page<UserVo>对象
        Page<StockMasterVo> data = stockRepository.findAll(new Specification<StockMasterEntity>() {
                                                               @Override
                                                               public Predicate toPredicate(Root<StockMasterEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
               List<Predicate> predicates = new ArrayList<Predicate>();
               // 判断字段是否存在来决定添加的条件
               if (StringUtils.isNotBlank(code)) {
                   predicates.add(criteriaBuilder.like(root.<String>get("sCode"),  code + "%"));
               }
               if (predicates.size() == 0) return null;
               return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
           }
       }, pageable
        ).map(new Converter<StockMasterEntity, StockMasterVo>() {
            @Override
            public StockMasterVo convert(StockMasterEntity entity) {
                StockMasterVo vo = new StockMasterVo();
                BeanUtils.copyProperties(entity, vo);
                vo.setsCurrentPrice(entity.getsCurrentPrice().toString());
                vo.setsYesterdayPrice(entity.getsYesterdayPrice().toString());
                vo.setsRangePrice(String.format("%s%s",entity.getsRangePrice(),"%"));

                String ped =entity.getsPeDynamic().intValue()==-1?"--":entity.getsPeDynamic().toString();
                vo.setsPeDynamic(ped);

                String pes=entity.getsPeStatic().intValue()==-1?"--":entity.getsPeStatic().toString();
                vo.setsPeStatic(pes);

                String pb=entity.getsPb().intValue()==-1?"--":entity.getsPb().toString();
                vo.setsPb(pb);

                String tv=entity.getsTotalValue().intValue()==-1?"--":entity.getsTotalValue().intValue()+"亿";
                vo.setsTotalValue(tv);

                String roe =entity.getsRoe().intValue()==-1?"--":entity.getsRoe().toString().concat("%");
                vo.setsRoe(roe);

                String dy=entity.getsDividendYear()==-1?"--":entity.getsDividendYear().toString();
                vo.setsDividendYear(dy);

                String dr=entity.getsDividendRate().intValue()==-1?"--":entity.getsDividendRate().toString().concat("%");
                vo.setsDividendRate(dr);
                return vo;
            }
        });
        return data;
    }

    public void stockRefresh() throws Exception {
        /*String[] codes = stockRepository.getAllStockCode();
        List<SinaStockPriceDto> stocks = StockSpilderUtil.getStockPrice(codes);
        for (SinaStockPriceDto dto : stocks) {
            StockMasterDto smd = StockSpilderUtil.getStockInfo(dto.getsCode());
            StockMasterEntity stock = stockRepository.findOne(dto.getsCode());
            stock.setsCode(dto.getsCode());
            stock.setsStockName(dto.getsStockName());
            stock.setsCurrentPrice(dto.getsCurrentPrice());
            stock.setsYesterdayPrice(dto.getsYesterdayPrice());
            stock.setsRangePrice(dto.getsRangePrice());
            stock.setsMainBusiness(smd.getsMainBusiness());
            stock.setsIndustry(smd.getsIndustry());
            stock.setsPeDynamic(smd.getsPeDynamic());
            stock.setsPeStatic(smd.getsPeStatic());
            stock.setsPb(smd.getsPb());
            stock.setsTotalValue(smd.getsTotalValue());
            stock.setsRoe(smd.getsRoe());
            stockRepository.save(stock);
        }*/
    }


    /**
     * 根据新浪数据接口更新股票价格
     */
    @Transactional
    public void updateStockPriceBySina() throws Exception {
       /* log.info("update sina stock ipo");
        String[] codes = stockRepository.getAllStockCode();
        List<SinaStockPriceDto> stocks = StockSpilderUtil.getStockPrice(codes);
        for (SinaStockPriceDto dto : stocks) {
            *//*log.info("{} {} {} {} {}",
                    dto.getsStockName(),
                    dto.getsCurrentPrice().doubleValue(),
                    dto.getsYesterdayPrice().doubleValue(),
                    dto.getsRangePrice().doubleValue(),
                    dto.getsCode());*//*
            stockRepository.updateIPO(
                    dto.getsStockName(),
                    dto.getsCurrentPrice(),
                    dto.getsYesterdayPrice(),
                    dto.getsRangePrice(),
                    dto.getsCode());
        }*/
    }

    @Transactional
    public void delStock(String[] codes){
        for (String code:
             codes) {
            stockRepository.delete(code);
        }
    }

}
