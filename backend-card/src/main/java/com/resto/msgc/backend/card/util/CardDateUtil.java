package com.resto.msgc.backend.card.util;

import com.alibaba.fastjson.JSON;
import com.resto.core.util.DateUtil;
import com.resto.msgc.backend.card.entity.WeekAndTimeDto;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bruce on 2017-12-21 10:50
 */
public class CardDateUtil {

    private static final String DETAULT_STR1 = "1970-01-01 ";
    private static final String DETAULT_STR2 = ":00";
    private static final String DETAULT_STR3 = "0";
    private static final String DELIMITER = ",";

    private static String getDateTimeString(String time) {
        if (time.length() == 4) {
            StringBuilder sb = new StringBuilder(DETAULT_STR1 + time + DETAULT_STR2);
            sb.insert(11,DETAULT_STR3);
            return sb.toString();
        }
        return DETAULT_STR1 + time + DETAULT_STR2;
    }

    /**
     * 比较当天的两个时间段的日期
     *
     * @param leftStartTime  第一个时间段的开始时间
     * @param leftEndTime    第一个时间段的结束时间
     * @param rightStartTime 第二个时间段的开始时间
     * @param rightEndTime   第二个时间段的结束时间
     * @return
     */
    public static ResultDto compareDate(String leftStartTime, String leftEndTime, String rightStartTime, String rightEndTime) {

        if (StringUtils.isBlank(leftStartTime) || StringUtils.isBlank(leftEndTime)
                || StringUtils.isBlank(rightStartTime) || StringUtils.isBlank(rightEndTime)) {
            return ResultDto.getError("时间不能为空");
        }

        if (leftStartTime.equals(leftEndTime) || rightStartTime.equals(rightEndTime)) {
            return ResultDto.getError("开始时间不跟结束时间能相同");
        }

        Date s1, e1, s2, e2;
        try {
            s1 = DateUtil.getDateFormString(getDateTimeString(leftStartTime), DateUtil.FORMAT_DEFAULT);
            e1 = DateUtil.getDateFormString(getDateTimeString(leftEndTime), DateUtil.FORMAT_DEFAULT);
            s2 = DateUtil.getDateFormString(getDateTimeString(rightStartTime), DateUtil.FORMAT_DEFAULT);
            e2 = DateUtil.getDateFormString(getDateTimeString(rightEndTime), DateUtil.FORMAT_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.getError("时间格式错误");
        }

        if (s1.getTime() > e1.getTime() || s2.getTime() > e2.getTime()) {
            return ResultDto.getError("开始时间不能大于结束时间");
        }

        if (((s1.getTime() >= s2.getTime()) && s1.getTime() < e2.getTime())
                || ((s1.getTime() > s2.getTime()) && s1.getTime() <= e2.getTime())
                || ((s2.getTime() >= s1.getTime()) && s2.getTime() < e1.getTime())
                || ((s2.getTime() > s1.getTime()) && s2.getTime() <= e1.getTime())) {
            return ResultDto.getError("时间不能有交集");
        }

        return ResultDto.getSuccess();
    }

    private static long compareDate(String date1, String date2) {
        Date s1, e1;
        try {
            s1 = DateUtil.parseDate(getDateTimeString(date1));
            e1 = DateUtil.parseDate(getDateTimeString(date2));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return s1.getTime() - e1.getTime();
    }

    /**
     * 判断多个时间段是否有重叠
     *
     * @param list
     * @return true有重叠 false没有重叠
     */
    public static boolean checkOverlap(List<String> list) {
        Collections.sort(list);
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            String[] iArray = list.get(i).split(DELIMITER);
            long detectTime = compareDate(iArray[0], iArray[1]);
            if (detectTime < 0) {
                for (int j = 0; j < list.size(); j++) {
                    if (j == i || j > i) {
                        continue;
                    }
                    String[] jArray = list.get(j).split(DELIMITER);
                    long compare = compareDate(iArray[0], jArray[1]);
                    if (compare <= 0) {
                        flag = true;
                        break;
                    }
                }
            } else {
                flag = true;
                break;
            }
            if (flag) break;
        }
        return flag;
    }

    public static boolean checkWeekAndTime(List<WeekAndTimeDto> list) {
        boolean flag = false;
        ok:
        for (WeekAndTimeDto item : list) {
            List<String> listA = new ArrayList<>(Arrays.asList(item.getWeekArray()));
            for (WeekAndTimeDto item2 : list) {
                List<String> listB = new ArrayList<>(Arrays.asList(item2.getWeekArray()));
                if (!(listA.containsAll(listB) && listB.containsAll(listA))) {
                    if (!Collections.disjoint(listA, listB)) {//返回true没有交集，false有交集
                        List<String> timeString = new ArrayList<>();
                        timeString.add(item.getTimeArray());
                        timeString.add(item2.getTimeArray());
                        if (checkOverlap(timeString)) {
                            flag = true;
                            break ok;
                        }
                    }
                }
            }
        }
        return flag;
    }

}
