#!/usr/bin/env bash

set -e

mvn clean compile test-compile test \
    scalafix:scalafix mvn-scalafmt_2.12:format \
    -Dscalafix.mode=CHECK
