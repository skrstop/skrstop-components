package cn.auntec.framework.components.util.constant;

/**
 * 文件 MineType 常量池
 *
 * @author 蒋时华
 * @date 2019/7/29
 */
public interface FileMineTypeConst {

    /**
     * 纯文本
     */
    String TEXT_PLAN = "plain";
    String TEXT = "text";
    /**
     * html 文件
     */
    String TEXT_HTML = "html";
    /**
     * xml 文件
     */
    String TEXT_XML = "xml";
    /**
     * css 文件
     */
    String TEXT_CSS = "css";
    /**
     * java 源文件
     */
    String TEXT_JAVA = "x-java-source";
    /**
     * c 源文件
     */
    String TEXT_C = "x-csrc";
    /**
     * c++ 源文件
     */
    String TEXT_C_plus = "x-c++src";


    /**
     * 视频文件
     */
    String VIDEO_ALL = "*";
    String VIDEO = "video";
    /**
     * *.3gpp
     * *.3gp
     */
    String VIDEO_3GP = "3gpp";
    /**
     * *.mp4
     */
    String VIDEO_MP4 = "mp4";
    /**
     * *.mpeg
     * *.mpg
     */
    String VIDEO_MPEG = "mpeg";
    /**
     * *.flv
     */
    String VIDEO_FLV = "x-flv";
    /**
     * *.m4v
     */
    String VIDEO_M4V = "x-m4v";
    /**
     * *.wmv
     */
    String VIDEO_WMV = "x-ms-wmv";
    /**
     * *.mov
     */
    String VIDEO_MOV = "quicktime";
    /**
     * *.avi
     */
    String VIDEO_AVI = "x-msvideo";


    /**
     * 音频文件
     */
    String AUDIO_ALL = "*";
    String AUDIO = "audio";
    /**
     * *.au
     * *.snd
     */
    String AUDIO_SOUND = "basic";
    /**
     * *.mp2
     */
    String AUDIO_MP2 = "x-mpeg";
    /**
     * *.mid
     * *.midi
     * *.kar
     */
    String AUDIO_MIDI = "x-midi";
    /**
     * *.mp3
     */
    String AUDIO_MP3 = "mpeg";
    /**
     * *.wav
     */
    String AUDIO_WAV = "x-wav";
    /**
     * *.m4a
     */
    String AUDIO_M4A = "x-m4a";
    /**
     * *.ogg
     */
    String AUDIO_OGG = "ogg";
    /**
     * *.ra
     */
    String AUDIO_RA = "x-realaudio";


    /**
     * 图片文件
     */
    String IMAGE_ALL = "*";
    String IMAGE = "image";
    /**
     * *.jpeg
     */
    String IMAGE_JPEG = "jpeg";
    /**
     * *.gif
     */
    String IMAGE_GIF = "gif";
    /**
     * *.bmp
     */
    String IMAGE_BMP = "bmp";
    /**
     * *.png
     */
    String IMAGE_PNG = "png";
    /**
     * *.icon
     */
    String IMAGE_ICON = "x-icon";
    /**
     * *.jng
     */
    String IMAGE_JNG = "x-jng";
    /**
     * *.svg
     * *.svgz
     */
    String IMAGE_SVG = "svg+xml";
    /**
     * *.tif
     * *.tiff
     */
    String IMAGE_TIFF = "tiff";


    String APPLICATION = "application";
    /**
     * flash
     */
    String APPLICATION_FLASH = "x-shockwave-flash";
    String APPLICATION_JAR = "java-archive";
    String APPLICATION_WAR = "x-tika-java-web-archive";
    String APPLICATION_APK = "vnd.android.package-archive";
    String APPLICATION_IPA = "octet-stream.ipa";
    String APPLICATION_PLIST = "xml";
    /**
     * js 源文件
     */
    String APPLICATION_JS = "javascript";
    /**
     * .exe
     * .dll
     * .class
     */
    String APPLICATION_EXE_EXECUTABLE = "octet-stream";

    /**
     * rar
     */
    String APPLICATION_COMPRESS_RAR = "x-rar-compressed";
    /**
     * zip
     */
    String APPLICATION_COMPRESS_ZIP = "zip";
    /**
     * pdf
     */
    String APPLICATION_PDF = "pdf";

    /**
     * word (.doc)
     */
    String APPLICATION_MS_DOC = "msword";
    /**
     * word (.docx)
     */
    String APPLICATION_MS_DOCX = "vnd.openxmlformats-officedocument.wordprocessingml.document";
    /**
     * powerpoint (.ppt)
     */
    String APPLICATION_MS_PPT = "vnd.ms-powerpoint";
    /**
     * powerpoint (.pptx)
     */
    String APPLICATION_MS_PPTX = "vnd.openxmlformats-officedocument.presentationml.presentation";
    /**
     * excel (.xls)
     */
    String APPLICATION_MS_XLS = "vnd.ms-excel";
    /**
     * excel (.xlsx)
     */
    String APPLICATION_MS_XLSX = "vnd.openxmlformats-officedocument.spreadsheetml.sheet";

}
