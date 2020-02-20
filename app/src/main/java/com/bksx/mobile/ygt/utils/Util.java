package com.bksx.mobile.ygt.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bksx.mobile.ygt.R;
import com.wx.touchregion.TouchRegion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/17.
 */

public class Util {
    private static Toast toast;

    /**
     * 每次只会创建一个吐丝框
     * @param context
     * @param content
     */
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,content, Toast.LENGTH_SHORT);
            LayoutInflater inflater = LayoutInflater.from(context
                    .getApplicationContext());
            View view = inflater.inflate(R.layout.my_toast, null);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER, 0, -300);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    //扩大点击区域
    public static void expandTouchRegion(View view){
        TouchRegion tr = new TouchRegion((View) view.getParent());
        tr.expandViewTouchRegion(view, 100);
    }
    /**
     * 以最省内存的方式读取本地资源的图片 或者SDCard中的图片
     * @param imagePath
     * 图片在SDCard中的路径
     * @return
     */
    public static Bitmap getSDCardImg(String imagePath)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        return BitmapFactory.decodeFile(imagePath, opt);
    }
    /**
     * 身份证校验
     *
     * @param cardID
     *            身份证号码
     * @return String[2]
     * @0:是否校验通过(true/false)
     * @1:校验结果(18位身份证号码/错误信息)
     */
    public static String[] check_Card_ID(String cardID) {
        if (cardID != null && !"".equals(cardID)) { // 不为空
            cardID = cardID.toUpperCase();
            if (cardID.length() == 15 || cardID.length() == 18) { // 必须为15位或18位
                if (cardID.length() == 15) { // 如果为15位，必须为全数字
                    if (check_Num(cardID)) {
                        cardID = convert_cardID_15to18(cardID); // 15位转成18位
                    } else {
                        return new String[] { "false", "15位身份证号码必须全部为数字" };
                    }
                }
                if (check_Num(cardID.substring(0, 17))) { // 前17位必须为数字
                    if (check_Num(cardID.substring(17))
                            || "X".equals(cardID.substring(17))) {
                        String birthDay = cardID.substring(6, 14);
                        if (is_date(birthDay)) {
                            if (!regular(cardID)) {
                                return new String[] { "false", "身份证号码校验未通过" };
                            }
                        } else {
                            return new String[] { "false", "身份证号码的出生年月日不正确" };
                        }

                    } else {
                        return new String[] { "false", "身份证号码最后一位错误" };
                    }
                } else {
                    return new String[] { "false", "身份证号码前17位必须全部为数字" };
                }

            } else {
                return new String[] { "false", "身份证号码必须为15位或18位" };
            }
        } else {
            return new String[] { "false", "身份证号码不能为空" };
        }
        return new String[] { "true", cardID };
    }
    /**
     * 是否为数字(0~9)组成
     *
     * @param num
     * @return
     */
    public static boolean check_Num(String num) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(num);
        return m.find();
    }
    /**
     * 15位身份证转18位
     *
     * @param cardID
     * @return
     */
    private static String convert_cardID_15to18(String cardID) {
        int[] check_code = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10,
                5, 8, 4, 2, 1 };
        int total = 0;
        String last;
        {
            String cardID_ = cardID.substring(0, 6) + "19"
                    + cardID.substring(6, 15);
            for (int i = 0; i < cardID_.length(); i++) {
                int l_l_temp1 = Integer.parseInt(cardID_.charAt(i) + "")
                        * check_code[i];
                total += l_l_temp1;
            }
            total--;
            int lastnum = total % 11;// 最后一位
            if (lastnum == 0) {
                last = "0";
            } else {
                if (lastnum == 1) {
                    last = "X";
                } else {
                    last = (11 - lastnum) + "";
                }
            }
            cardID_ = cardID_ + last;
            return cardID_;
        }
    }

    /**
     * 身份证号码是否符合规则
     *
     * @param cardID
     * @return
     */
    private static boolean regular(String cardID) {
        int[] l_l_jym = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
                8, 4, 2, 1 };
        int l_l_total = 0;
        for (int i = 0; i < cardID.length() - 1; i++) {
            l_l_total += Integer.parseInt(cardID.charAt(i) + "") * l_l_jym[i];
        }
        if (check_Num(cardID.substring(17))) {
            l_l_total += Integer.parseInt(cardID.substring(17));
        } else if ("X".equals(cardID.substring(17))) {
            l_l_total += 10;
        }
        l_l_total--;
        if (!(l_l_total % 11 == 0)) {
            return false;
        }
        return true;
    }
    /**
     * 是否为日期
     *
     * @param date
     *            yyyyMMdd
     * @return
     */
    private static boolean is_date(String date) {
        int theYear = Integer.parseInt(date.substring(0, 4));
        int theMonth = Integer.parseInt(date.substring(4, 6));
        int theDay = Integer.parseInt(date.substring(6, 8));
        if (theMonth > 12) {
            return false;
        }
        if (theDay > 31) {
            return false;
        }
        switch (theMonth) {
            case 4:
            case 6:
            case 9:
            case 11:
                if (theDay == 31)
                    return false;
                else
                    break;
            case 2:
                if ((theYear % 4 == 0 || theYear % 100 == 0) && theYear % 400 != 0)// 润年2月份29天
                {
                    if (theDay > 29)
                        return false;
                } else {
                    if (theDay > 28)
                        return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpace(EditText editText, int length) {
        InputFilter filter1 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")
                        ||source.toString().contentEquals("\n")
                        ||source.toString().contentEquals("\r")
                        ||source.toString().equalsIgnoreCase("insert")
                        ||source.toString().equalsIgnoreCase("and")
                        ||source.toString().equalsIgnoreCase("or")
                        ||source.toString().equalsIgnoreCase("select")
                        ||source.toString().equalsIgnoreCase("delete")
                        ||source.toString().equalsIgnoreCase("drop")
                        ||source.toString().equalsIgnoreCase("update")
                        ||source.toString().equalsIgnoreCase("count")
                        ||source.toString().equalsIgnoreCase("master")
                        ||source.toString().equalsIgnoreCase("truncate")
                        ||source.toString().equalsIgnoreCase("declare")
                        ||source.toString().equalsIgnoreCase("--")
                        ||source.toString().equalsIgnoreCase("*/")
                        ||source.toString().equalsIgnoreCase("/*")
                        ||source.toString().equalsIgnoreCase("../")
                        ) {
                    return "";
                } else {
                    return null;
                }
            }
        };

        InputFilter filter2 = new InputFilter.LengthFilter(length);

        InputFilter filter3 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@$%^&*+=|{}\\[\\]<>/]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter1,filter2,filter3});
    }

    //Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
    public static String getDateStr(String day, int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    /***
     * 日期月份减一个月
     *
     * @param datetime
     * 日期(2014-11)
     * @return 2014-10
     */
    public static String dateFormat(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MONTH, -1);
        date = cl.getTime();
        return sdf.format(date);
    }
}
