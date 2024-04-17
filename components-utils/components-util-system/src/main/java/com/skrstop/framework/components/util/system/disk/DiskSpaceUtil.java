package com.skrstop.framework.components.util.system.disk;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.skrstop.framework.components.util.value.format.TextFormatUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2021-08-12 11:25:18
 */
@Slf4j
public class DiskSpaceUtil {

    /**
     * 检测磁盘容量并清理缓存文件 -- linux
     *
     * @param fileCachePath         路径
     * @param minSpaceLimit         空间限制, 单温：MB
     * @param ignoreDeleteException 是否忽略删除异常
     */
    public static synchronized void cleanLinuxDiskCapacity(String fileCachePath,
                                                           Long minSpaceLimit,
                                                           boolean ignoreDeleteException) {
        // 获取系统类型
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (!osInfo.isLinux()) {
            log.info("当前不是linux系统，不验证磁盘空间，跳过此流程");
            return;
        }
        File folder = new File(fileCachePath);
        long usableSpaceMb = getUsableSpace(fileCachePath);
        if (usableSpaceMb > minSpaceLimit) {
            return;
        }
        log.info("磁盘空间不足，当前：{} MB, 期望：{} MB, 开始清理", usableSpaceMb, minSpaceLimit);
        // 开始清理
        File[] files = folder.listFiles();
        if (ObjectUtil.isNull(files) || files.length == 0) {
            throw new SkrstopRuntimeException(TextFormatUtil.formatString("本地磁盘空间不足, 目录：{}，本地缓存文件所占空间为0, 请检查缓存目录是否正确", fileCachePath));
        }
        // 按文件创建时间排序
        List<File> sortFiles = CollectionUtil.newArrayList(files);
        sortFiles.sort(Comparator.comparingLong(file -> {
            long sortLong = -1;
            try {
                BasicFileAttributes attr = FileUtil.getAttributes(Paths.get(file.getPath()), false);
                Date createTimeDate = new Date(attr.creationTime().toMillis());
                sortLong = createTimeDate.getTime();
            } catch (IORuntimeException e) {
                log.error("清理文件失败：{}", ThrowableStackTraceUtil.getStackTraceStr(e));
                if (!ignoreDeleteException) {
                    throw new SkrstopRuntimeException(e);
                }
            }
            return sortLong;
        }));
        for (File sortFile : sortFiles) {
            boolean deleteResult = FileUtil.del(sortFile);
            if (!deleteResult) {
                // 记录警告信息
                log.error("磁盘清理，文件删除失败：{}", sortFile.getAbsolutePath());
            }
            usableSpaceMb = getUsableSpace(fileCachePath);
            if (usableSpaceMb > minSpaceLimit) {
                break;
            }
        }
        usableSpaceMb = getUsableSpace(fileCachePath);
        if (usableSpaceMb <= minSpaceLimit) {
            log.error("磁盘空间清理失败，当前：{} MB, 期望：{} MB", usableSpaceMb, minSpaceLimit);
            throw new SkrstopRuntimeException("本地磁盘空间不足，本地缓存文件所占空间为: " + usableSpaceMb);
        }
        log.info("磁盘空间清理完成，当前：{} MB, 期望：{} MB", usableSpaceMb, minSpaceLimit);
    }

    /**
     * 获取磁盘可用空间 MB
     *
     * @param fileCachePath
     * @return
     */
    private static long getUsableSpace(String fileCachePath) {
        File folder = new File(fileCachePath);
        long usableSpace = folder.getUsableSpace();
        return usableSpace / (1024 * 1024);
    }

}
