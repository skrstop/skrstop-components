#!/bin/bash

# Copyright 1999-2018 Acmedcare Group Holding Ltd.
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

export BASE_DIR=$(
  cd $(dirname $0)/..
  pwd
)

pid=$(ps ax | grep -i ${BASE_DIR} | grep java | grep -v grep | awk '{print $1}')
if [ -z "$pid" ]; then
  echo "No server running."
  exit -1
fi

echo "The Application(${pid}) is running..."

kill ${pid}

echo "Send shutdown request to Application(${pid}) OK"
