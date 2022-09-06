#!/bin/bash
dir=${PWD}
echo "Script executed from: ${PWD}"
if [[ $dir =~ "CCDAAgent" ]]; then
   echo "It's there!"
   nohup java -jar ${PWD}/ix-ccda-agent.war --spring.config.location=${PWD}/agent-config.properties --clientId=ccda-agent-client --clientSecret="28f5f3de-3f0c-4259-9d00-90e361cfd266"> ${PWD}/ix-ccda-agent-log.out 2>&1 &
else
	nohup java -jar ${PWD}/CCDAAgent/ix-ccda-agent.war --spring.config.location=${PWD}/CCDAAgent/agent-config.properties --clientId=ccda-agent-client --clientSecret="28f5f3de-3f0c-4259-9d00-90e361cfd266"> ${PWD}/CCDAAgent/ix-ccda-agent-log.out 2>&1 &
fi
