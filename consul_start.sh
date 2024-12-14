#!/bin/bash
 
ip=$(hostname -I | awk '{print $1}')
consul agent -server -bootstrap-expect=1 -data-dir=consul-data -ui -bind="$ip"
