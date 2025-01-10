#!/usr/bin/env bash

set -eux

echo "Test step"

env

echo derp

ls -la
pwd

export VAR="hurrdurr"

env > .env

ls -la

echo "Test step done"
