#!/bin/bash
kill $(ps aux | grep 'ix-ccda-agent.war' | grep -v grep | awk '{print $2}')