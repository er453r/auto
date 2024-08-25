#!/usr/bin/env bash

echo -e "IMAGE\t$IMAGE"
echo -e "BRANCH\t$BRANCH"
echo -e "TAG\t$TAG"

DOCKER_TAG="$IMAGE:$BRANCH"

if [ ! -z "${TAG}" ]; then
  echo -e "Publishing tags $DOCKER_TAG, $IMAGE:$TAG...\n"

  docker push "$REPO/$DOCKER_TAG"
  docker push "$REPO/$IMAGE:$TAG"
else
  echo -e "Publishing tag $DOCKER_TAG...\n"

  docker push "$REPO/$DOCKER_TAG"
fi

echo -e "\Publishing tag $DOCKER_TAG - DONE!"
