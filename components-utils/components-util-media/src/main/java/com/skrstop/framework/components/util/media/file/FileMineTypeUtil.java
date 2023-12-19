package com.skrstop.framework.components.util.media.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * FileMineTypeUtil class
 *
 * @author 蒋时华
 * @date 2019/7/29
 */
@UtilityClass
public class FileMineTypeUtil {

    /**
     * 判断文件是否是应用程序文件
     *
     * @param in
     * @param mineType
     * @return
     * @throws IOException
     * @see com.skrstop.framework.components.util.constant.FileMineTypeConst
     */
    public static boolean equals(InputStream in, String mineType) throws IOException {
        if (StrUtil.isBlank(mineType)) {
            return false;
        }
        MediaType parse = MediaType.parse(new Tika().detect(in));
        if (ObjectUtil.isNotNull(parse) && StrUtil.isNotBlank(parse.getType())) {
            return parse.compareTo(MediaType.parse(mineType)) == 0;
        }
        return false;
    }

    /**
     * 是否包含指定的类型
     *
     * @param in
     * @param mineTypeList
     * @return
     * @throws IOException
     * @see com.skrstop.framework.components.util.constant.FileMineTypeConst
     */
    public static boolean contains(InputStream in, List<String> mineTypeList) throws IOException {
        if (CollectionUtil.isEmpty(mineTypeList)) {
            return false;
        }
        MediaType parse = MediaType.parse(new Tika().detect(in));
        if (ObjectUtil.isNotNull(parse) && StrUtil.isNotBlank(parse.getType())) {
            return mineTypeList.stream().anyMatch(mineType -> parse.compareTo(MediaType.parse(mineType)) == 0);
        }
        return false;
    }

}
