#!/usr/bin/env bash

MIRROR=$(mktemp -d)
cd "$MIRROR" || exit

git clone --mirror "$REPO" .
git remote update
IMAGE=$(basename -s .git `git config --get remote.origin.url`)

CHECKOUT=$(mktemp -d)
cd "$CHECKOUT" || exit

git clone "$MIRROR" .
git checkout "$REV"

BRANCH=$(git rev-parse --abbrev-ref HEAD)
TAG=$(git tag --points-at HEAD | head -n1)

echo -e "IMAGE\t$IMAGE"
echo -e  "BRANCH\t$BRANCH"
echo -e "TAG\t$TAG"

rm -rf "$MIRROR"
rm -rf "$CHECKOUT"
