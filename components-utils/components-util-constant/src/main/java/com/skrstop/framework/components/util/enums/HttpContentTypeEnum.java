package com.skrstop.framework.components.util.enums;

import lombok.Getter;

/**
 * Http ContentType类型枚举类
 *
 * @author 蒋时华
 * @date 2019/6/17
 */
public enum HttpContentTypeEnum {

    /*** http contentType */
    APPLICATION_ALL("*/*"),
    APPLICATION_JSON_UTF8("application/json; charset=utf-8"),
    APPLICATION_JSON("application/json"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_PROBLEM_JSON("application/problem+json"),
    APPLICATION_PROBLEM_JSON_UTF8("application/problem+json;charset=UTF-8"),
    APPLICATION_PROBLEM_XML("application/problem+xml"),
    APPLICATION_RSS_XML("application/rss+xml"),
    APPLICATION_STREAM_JSON("application/stream+json"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_XML("application/xml"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_WEBP("image/webp"),
    IMAGE_SVG("image/svg+xml"),
    AUDIO_WAV("audio/wav"),
    AUDIO_M3U("audio/mpegurl"),
    AUDIO_MP3("audio/mpeg"),
    VIDEO_AVI("video/avi"),
    VIDEO_MP4("video/mp4"),
    VIDEO_MOV("video/quicktime"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_EVENT_STREAM("text/event-stream"),
    TEXT_HTML("text/htm"),
    TEXT_MARKDOWN("text/markdown"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    PROTOBUF("application/x-protobuf"),
    ;

    @Getter
    private final String contentType;

    HttpContentTypeEnum(String contentType) {
        this.contentType = contentType;
    }


    @Override
    public String toString() {
        return this.getContentType();
    }
}
