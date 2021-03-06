#!/bin/bash

###
# ============LICENSE_START=======================================================
# openECOMP : SDN-C
# ================================================================================
# Copyright (C) 2017 AT&T Intellectual Property. All rights
# 							reserved.
# ================================================================================
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ============LICENSE_END=========================================================
###

function enable_odl_cluster(){
  if [ -z $SDNC_REPLICAS ]; then
     echo "SDNC_REPLICAS is not configured in Env field"
     exit
  fi

  echo "Installing Opendaylight cluster features"
  mv $ODL_HOME/etc/org.apache.karaf.features.cfg $ODL_HOME/etc/org.apache.karaf.features.cfg.orig
  cat $ODL_HOME/etc/org.apache.karaf.features.cfg.orig | sed -e "\|featuresBoot=config|s|$|,odl-mdsal-clustering,odl-jolokia|" > $ODL_HOME/etc/org.apache.karaf.features.cfg
  #${ODL_HOME}/bin/client feature:install odl-mdsal-clustering
  #${ODL_HOME}/bin/client feature:install odl-jolokia

  echo "Update cluster information statically"
  hm=$(hostname)
  echo "Get current Hostname ${hm}"

  node=($(echo ${hm} | tr '-' '\n'))
  node_name=${node[0]}
  node_index=${node[1]}

  if [ -z $PEER_ODL_CLUSTER ]; then
    echo "This is a local cluster"
    node_list="${node_name}-0.sdnhost-cluster.onap.svc.cluster.local";

    for ((i=1;i<${SDNC_REPLICAS};i++));
    do
      node_list="${node_list} ${node_name}-$i.sdnhost-cluster.onap.svc.cluster.local"
    done
    /opt/opendaylight/current/bin/configure_cluster.sh $((node_index+1)) ${node_list}
  else
    echo "This is a Geo cluster"

    if $IS_PRIMARY_CLUSTER; then
       PRIMARY_NODE=${MY_ODL_CLUSTER}
       SECONDARY_NODE=${PEER_ODL_CLUSTER}
    else
       PRIMARY_NODE=${PEER_ODL_CLUSTER}
       SECONDARY_NODE=${MY_ODL_CLUSTER}
       member_offset=4
    fi

    node_list="${PRIMARY_NODE} ${SECONDARY_NODE}"
    /opt/onap/sdnc/bin/configure_geo_cluster.sh $((node_index+member_offset)) ${node_list}
  fi
}


# Install SDN-C platform components if not already installed and start container

ODL_HOME=${ODL_HOME:-/opt/opendaylight/current}
ODL_ADMIN_PASSWORD=${ODL_ADMIN_PASSWORD:-Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U}
SDNC_HOME=${SDNC_HOME:-/opt/onap/sdnc}
CCSDK_HOME=${CCSDK_HOME:-/opt/onap/ccsdk}
SLEEP_TIME=${SLEEP_TIME:-120}
MYSQL_PASSWD=${MYSQL_PASSWD:-openECOMP1.0}
ENABLE_ODL_CLUSTER=${ENABLE_ODL_CLUSTER:-false}
IS_PRIMARY_CLUSTER=${IS_PRIMARY_CLUSTER:-false}
MY_ODL_CLUSTER=${MY_ODL_CLUSTER:-127.0.0.1}

#
# Wait for database
#
echo "Waiting for mysql"
until mysql -h dbhost -u root -p${MYSQL_PASSWD} mysql &> /dev/null
do
  printf "."
  sleep 1
done
echo -e "\nmysql ready"

if [ ! -f ${SDNC_HOME}/.installed ]
then
	echo "Installing SDN-C database"
	${SDNC_HOME}/bin/installSdncDb.sh
	echo "Installing SDN-C keyStore"
	${SDNC_HOME}/bin/addSdncKeyStore.sh

	#${CCSDK_HOME}/bin/installOdlHostKey.sh

	if [ -x ${SDNC_HOME}/svclogic/bin/install.sh ]
	then
		echo "Installing directed graphs"
		${SDNC_HOME}/svclogic/bin/install.sh
	fi

    if $ENABLE_ODL_CLUSTER ; then enable_odl_cluster ; fi

	echo "Installed at `date`" > ${SDNC_HOME}/.installed
fi

exec ${ODL_HOME}/bin/karaf server
