#!/usr/bin/env bash

env

for n in {1..10}; do
  echo "stdout $n"

  sleep 1
done

for n in {1..10}; do
  echo "foo $n" >> /dev/stderr

  sleep 1
done

for n in {1..10}; do
  echo "stdout $n"

  sleep 1
done

for n in {1..10}; do
  echo "foo $n" >> /dev/stderr

  sleep 1
done

echo "GIT_ID=1322343453455"

echo "DONE"
