#!/usr/bin/env bash

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
