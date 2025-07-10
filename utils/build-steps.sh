#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

for dir in "$SCRIPT_DIR"/../steps/*; do
  echo "$dir"

  tag=$(basename "$dir")

  cd "$dir" || exit

  docker build -t "auto.$tag" .
done

echo "DONE"
