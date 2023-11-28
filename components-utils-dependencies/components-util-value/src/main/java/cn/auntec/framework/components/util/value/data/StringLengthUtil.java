package cn.auntec.framework.components.util.value.data;

import cn.auntec.framework.components.core.exception.defined.coding.EncodingException;
import cn.auntec.framework.components.util.constant.CharSetEnum;
import cn.hutool.core.util.ObjectUtil;

import java.io.UnsupportedEncodingException;

/**
 * 字符长度验证
 *
 * @author 蒋时华
 * @date 2018/10/30
 */
public class StringLengthUtil {

    private static final String DEFAULT_CHARSET = CharSetEnum.UTF8.getCharSet();

    /**
     * 字符串长度 == length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean eq(String str, Long length) {
        return StringLengthUtil.eq(str, length, DEFAULT_CHARSET);
    }

    /**
     * 字符串长度 == length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean eq(String str, Long length, String charset) {
        try {
            if (ObjectUtil.isNotNull(str) && str.getBytes(charset).length == length) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

    /**
     * 字符串长度 != length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean neq(String str, Long length) {
        return StringLengthUtil.neq(str, length, DEFAULT_CHARSET);
    }

    /**
     * 字符串长度 != length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean neq(String str, Long length, String charset) {
        try {
            if (ObjectUtil.isNotNull(str) && str.getBytes(charset).length != length) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

    /**
     * 字符串长度 < length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean lt(String str, Long length) {
        return StringLengthUtil.lt(str, length, DEFAULT_CHARSET);
    }

    /**
     * 字符串长度 < length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean lt(String str, Long length, String charset) {
        try {
            if (ObjectUtil.isNotNull(str) && str.getBytes(charset).length < length) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

    /**
     * 字符串长度 <= length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean elt(String str, Long length) {
        return StringLengthUtil.elt(str, length, DEFAULT_CHARSET);
    }

    /**
     * 字符串长度 <= length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean elt(String str, Long length, String charset) {
        try {
            if (ObjectUtil.isNotNull(str) && str.getBytes(charset).length <= length) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

    /**
     * 字符串长度 >= length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean egt(String str, Long length) {
        return StringLengthUtil.egt(str, length, DEFAULT_CHARSET);
    }

    /**
     * 字符串长度 >= length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean egt(String str, Long length, String charset) {
        try {
            if (ObjectUtil.isNotNull(str) && str.getBytes(charset).length >= length) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

    /**
     * 字符串长度 > length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean gt(String str, Long length) {
        return StringLengthUtil.gt(str, length, DEFAULT_CHARSET);
    }

    /**
     * 字符串长度 > length
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean gt(String str, Long length, String charset) {
        try {
            if (ObjectUtil.isNotNull(str) && str.getBytes(charset).length > length) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

    /**
     * minLength <= 字符串长度 <= maxLength
     *
     * @param str
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return
     */
    public static boolean gtAndlt(String str, Long minLength, Long maxLength) {
        return StringLengthUtil.gtAndlt(str, minLength, maxLength, DEFAULT_CHARSET);
    }

    /**
     * minLength <= 字符串长度 <= maxLength
     *
     * @param str
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return
     */
    public static boolean gtAndlt(String str, Long minLength, Long maxLength, String charset) {
        try {
            if (ObjectUtil.isNotNull(str)) {
                byte[] size = str.getBytes(charset);
                if (size.length >= minLength &&
                        size.length <= maxLength) {
                    return true;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

    /**
     * minLength < 字符串长度 < maxLength
     *
     * @param str
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return
     */
    public static boolean egtAndelt(String str, Long minLength, Long maxLength) {
        return StringLengthUtil.egtAndelt(str, minLength, maxLength, DEFAULT_CHARSET);
    }

    /**
     * minLength < 字符串长度 < maxLength
     *
     * @param str
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return
     */
    public static boolean egtAndelt(String str, Long minLength, Long maxLength, String charset) {
        try {
            if (ObjectUtil.isNotNull(str)) {
                byte[] size = str.getBytes(charset);
                if (size.length > minLength &&
                        size.length < maxLength) {
                    return true;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
        return false;
    }

}
