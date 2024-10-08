#!/usr/bin/env bash

cd "$WORKDIR" || exit

IMAGE=$(basename -s .git "$REPO")
BRANCH=$(git name-rev HEAD | cut -d' ' -f2)
TAG=$(git tag --points-at HEAD | head -n1)

echo -e "IMAGE\t$IMAGE"
echo -e "BRANCH\t$BRANCH"
echo -e "TAG\t$TAG"

DOCKER_TAG="$IMAGE:$BRANCH"

if [[ $BRANCH == *"~"* ]]; then
  echo "This is a commit-only build!"

  echo -e "Building commit $REV...\n"

  docker build --progress=plain -t "$DOCKER_REPO/$IMAGE:$REV" .
elif [ -n "$TAG" ]; then
  echo -e "Building tags $DOCKER_TAG, $IMAGE:$TAG...\n"

  docker build --progress=plain -t "$DOCKER_REPO/$DOCKER_TAG" -t "$DOCKER_REPO/$IMAGE:$TAG" .
else
  echo -e "Building tag $DOCKER_TAG...\n"

  docker build  --progress=plain -t "$DOCKER_REPO/$DOCKER_TAG" .
fi

echo -e "\nBuilding tag $DOCKER_TAG - DONE!"
