#!/bin/bash
echo 'stop the app...'
./stop.sh
echo 'stopped'
sleep 2
echo 'start the app...'
./run.sh
echo 'started'