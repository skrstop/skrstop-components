#!/bin/bash
SERVICE_NAME=mixmicro-distribution-workshop
SERVICE_VERSION=1.0.0.BUILD-SNAPSHOT
# Copyright 1999-2018 Acmedcare+ Group Holding Ltd.
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

PATH_TO_JAR=${SERVICE_NAME}".jar"

error_exit() {
  echo "ERROR: $1 !!"
  exit 1
}

[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/opt/taobao/java
[ ! -e "$JAVA_HOME/bin/java" ] && error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"

export MODE="cluster"
export PROFILE="standalone"
export ENV="dev"
while getopts ":m:p:e:" opt; do
  case ${opt} in
  e)
    ENV=$OPTARG
    ;;
  m)
    #        MODE=$OPTARG
    ;;
  p)
    PROFILE=$OPTARG
    ;;
  ?)
    echo "未知参数"
    exit 1
    ;;
  esac
done

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=$(
  cd $(dirname $0)/..
  pwd
)
export DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
export CUSTOM_SEARCH_LOCATIONS="${DEFAULT_SEARCH_LOCATIONS},file:${BASE_DIR}/config/"
export LOG_DIR="${BASE_DIR}/logs"

#===========================================================================================
# JVM Configuration
#===========================================================================================

JAVA_OPT="${JAVA_OPT} -server -Xms512m -Xmx512m -Xmn256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
#JAVA_OPT="${JAVA_OPT} -Xdebug -Xrunjdwp:transport=dt_socket,address=9555,server=y,suspend=n"
JAVA_OPT="${JAVA_OPT} -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8  -XX:-UseParNewGC"
JAVA_OPT="${JAVA_OPT} -Djava.net.preferIPv4Stack=true -Dlogging.file=$LOG_DIR/$SERVICE_NAME.log -verbose:gc -Xloggc:${BASE_DIR}/logs/${SERVICE_NAME}_gc.log -XX:HeapDumpPath=$LOG_DIR/HeapDumpOnOutOfMemoryError/ -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintAdaptiveSizePolicy"
JAVA_OPT="${JAVA_OPT} -Dmixmicro.server.home=${BASE_DIR}"
JAVA_OPT="${JAVA_OPT} -Denv=${ENV}"
#if [[ "${MODE}" == "standalone" ]]; then
#    JAVA_OPT="${JAVA_OPT} -Dacmedcare.naming.standalone=true"
#    JAVA_OPT="${JAVA_OPT} -Dspring.config.location=${CUSTOM_SEARCH_LOCATIONS}"
#fi

#if [[ "${MODE}" != "standalone" && "${PROFILE}" != "standalone" ]]; then
# enabled monitor
#    JAVA_OPT="${JAVA_OPT} -javaagent:/acmedcare/applications/profile-env/perfino/agent/perfino.jar=server=jvm.perfino.acmedcare.com,pool=Remoting-Server-WSS/${PROFILE} "
#    JAVA_OPT="${JAVA_OPT} -Dspring.profiles.active=${PROFILE}"
#fi

JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"
JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"
JAVA_OPT="${JAVA_OPT} -Xbootclasspath/a:./:${BASE_DIR}/config/"
JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/${SERVICE_NAME}-${SERVICE_VERSION}.jar"
JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"
JAVA_OPT="${JAVA_OPT} --spring.config.location=${CUSTOM_SEARCH_LOCATIONS}"
JAVA_OPT="${JAVA_OPT} --logging.config=${BASE_DIR}/config/logback.xml"
if [ ! -d "${BASE_DIR}/logs" ]; then
  mkdir ${BASE_DIR}/logs
fi
if [ ! -f "${BASE_DIR}/logs/start.log" ]; then
  touch "${BASE_DIR}/logs/start.log"
fi

nohup ${JAVA} ${JAVA_OPT} >${BASE_DIR}/logs/start.log 2>&1 &
echo "Mixmicro+ Application Server is starting，you can check the ${BASE_DIR}/logs/start.log"
