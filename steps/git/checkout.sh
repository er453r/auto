#!/usr/bin/env bash
set -euxo pipefail

HASH=$(echo "$GIT_SSH" | md5sum | cut -d' ' -f1)
MIRROR="/storage/$HASH"

echo "$GIT_KEY" > /key
chmod 600 /key
export GIT_SSH_COMMAND="ssh -i /key -o IdentitiesOnly=yes -o StrictHostKeyChecking=no"

# mirror update
mkdir -p "$MIRROR"
cd "$MIRROR" || exit
git clone --mirror "$GIT_SSH" .
git remote update
git reflog

# workdir
cd "/workdir" || exit

git clone "$MIRROR" .
git checkout "master"

echo "CHECKOUT=MASTER"
