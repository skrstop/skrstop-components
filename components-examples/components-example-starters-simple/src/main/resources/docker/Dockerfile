# Dockerfile for mixmicro-distribution-workshop
# Build with:
# docker build -t mixmicro-distribution-workshop .
# Run with:
# docker run -p 19090:19090 -d -v /tmp/logs:/tmp/logs -v /tmp/logs/mixmicro-distribution-workshop:/mixmicro-distribution-workshop/logs --name mixmicro-distribution-workshop docker.apiacmed.com/library/mixmicro-distribution-workshop:1.0.0.BUILD-SNAPSHOT

FROM openjdk:8-jre-alpine
MAINTAINER Shihua.Jiang <4086824@qq.com>

ENV VERSION 1.0.0.BUILD-SNAPSHOT

RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD mixmicro-distribution-workshop-${VERSION}.zip /mixmicro-distribution-workshop/mixmicro-distribution-workshop-${VERSION}.zip

RUN unzip /mixmicro-distribution-workshop/mixmicro-distribution-workshop-${VERSION}.zip -d /mixmicro-distribution-workshop \
    && rm -rf /mixmicro-distribution-workshop/mixmicro-distribution-workshop-${VERSION}.zip \
    && sed -i '$d' /mixmicro-distribution-workshop/bin/startup.sh \
    && echo "tail -f /dev/null" >> /mixmicro-distribution-workshop/bin/startup.sh

EXPOSE 0

CMD ["/distribution-common/bin/startup.sh"]
