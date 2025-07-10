#!/usr/bin/env bash
set -euo pipefail
#set -euxo pipefail # use this during development

# ssh setup
echo "$GIT_KEY" > /key
chmod 600 /key
export GIT_SSH_COMMAND="ssh -i /key -o IdentitiesOnly=yes -o StrictHostKeyChecking=no -o LogLevel=error"

# repo constants
HASH=$(echo "$GIT_SSH" | md5sum | cut -d' ' -f1)
MIRROR="/storage/$HASH"
MIRROR_LAST_COMMIT="/storage/$HASH.last"

if [ -v IGNORE_LAST_COMMIT ]; then
  rm -rf "$MIRROR_LAST_COMMIT"
fi

# mirror update
if [ ! -d "$MIRROR" ]; then
  echo "Creating mirror directory $MIRROR"

  git clone --mirror "$GIT_SSH" "$MIRROR"
else
  echo "Mirror $MIRROR already exists"
fi

echo "Pulling all newest changes"
cd "$MIRROR" || exit
git remote update

# looking at what to checkout
if [ -v CHECKOUT ]; then
  echo "Explicitly checking out $CHECKOUT"
else
  echo "Looking for last checked out commit..."

  if [ -f "$MIRROR_LAST_COMMIT" ]; then
    LAST_CHECKOUT=$(cat "$MIRROR_LAST_COMMIT")

    echo "Last checkout $LAST_CHECKOUT"

    git log --all --pretty=format:"%h" > /tmp/commits.log
    NEXT_CHECKOUT=$(grep "$LAST_CHECKOUT" /tmp/commits.log -B 1 | head -n 1)

    if [ "$NEXT_CHECKOUT" = "$LAST_CHECKOUT" ]; then
      echo "NOOP=Already checked out"
      exit
    fi

    CHECKOUT="$NEXT_CHECKOUT"

    echo "Next commit to check out $CHECKOUT"
  else
    CHECKOUT=$(git log --all --pretty=format:"%h" -n1)

    echo "No last checked out commit! Checking out the newest $CHECKOUT"
  fi

  echo "$CHECKOUT" > "$MIRROR_LAST_COMMIT"
fi

# workdir
cd "/workdir" || exit

git clone "$MIRROR" .
git -c advice.detachedHead=false checkout "$CHECKOUT"

# get REVISIONS
REVISIONS=$(git log --decorate=short | head -n1 2>/dev/null || true)

echo "Raw git info $REVISIONS"

REVISIONS=${REVISIONS#*HEAD, }
REVISIONS=${REVISIONS%)}
REVISIONS=${REVISIONS//"tag: "/""}
REVISIONS=${REVISIONS//"origin/"/""}
REVISIONS=${REVISIONS//" "/""}

AUTHOR_NAME=$(git log --pretty=format:"%an" -n1)
AUTHOR_EMAIL=$(git log --pretty=format:"%ae" -n1)
AUTHOR_DATE=$(git log --pretty=format:"%aI" -n1)

# outputs
echo "CHECKOUT=$CHECKOUT"
echo "REVISIONS=$REVISIONS"
echo "AUTHOR_NAME=$AUTHOR_NAME"
echo "AUTHOR_EMAIL=$AUTHOR_EMAIL"
echo "AUTHOR_DATE=$AUTHOR_DATE"
