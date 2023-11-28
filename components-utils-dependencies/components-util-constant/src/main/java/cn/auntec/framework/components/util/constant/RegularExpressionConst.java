package cn.auntec.framework.components.util.constant;

/**
 * 正则表达式常量
 *
 * @author 蒋时华
 * @date 2020-05-20 14:37:41
 */
public interface RegularExpressionConst {

    /**
     * 整数或者小数
     */
    String DECIMAL = "^[0-9]+\\.{0,1}[0-9]{0,2}$";
    /**
     * 数字
     */
    String NUMBER = "^[0-9]*$";
    /**
     * 手机号
     */
    String PHONE = "^1\\d{10}$";

    /**
     * 邮箱
     */
    String EMAIL = "^[a-zA-Z0-9\\._-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 车牌号
     */
    String VCL_NO = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";

    /**
     * 日期时间 yyyy-MM-dd hh:mm:ss
     */
    String DATE_TIME = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";

    /**
     * 日期 yyyy-MM-dd
     */
    String DATE = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";

    /**
     * 时间 hh:mm:ss
     */
    String TIME = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";

    /**
     * HTTP url
     */
    String HTTP_URL = "^(https?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:\\/~+#]*[\\w\\-@?^=%&\\/~+#])?$";

    /**
     * ftp url
     */
    String FTP_URL = "^(ftps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:\\/~+#]*[\\w\\-@?^=%&\\/~+#])?$";

    /**
     * 正则表达式匹配中文汉字
     */
    String RE_CHINESE = "[\u4E00-\u9FFF]";
    /**
     * 正则表达式匹配中文字符串
     */
    String RE_CHINESES = RE_CHINESE + "+";

    /**
     * 正则中需要被转义的关键字
     */
    char[] RE_KEYS = new char[]{'$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'};

}
