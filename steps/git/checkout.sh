#!/usr/bin/env bash

# script inputs - REPO, REV, MIRROR_PATH, WORKDIR
HASH=$(echo "$REPO" | md5sum | cut -d' ' -f1)
MIRROR="$MIRROR_PATH/$HASH"

# mirror update
mkdir -p "$MIRROR"
cd "$MIRROR" || exit

if [ -d .git ]; then
  git clone --mirror "$REPO" .
fi

git remote update

# workdir
cd "$WORKDIR" || exit

git clone "$MIRROR" .
git checkout "$REV"
