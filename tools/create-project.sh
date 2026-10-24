#!/usr/bin/env bash
# Generates a business monorepo from the committed templates/multi-service.
# Usage: tools/create-project.sh <repo-name> <package-segment> [dest-dir]
#   repo-name        e.g. demo-c-contract  -> replaces "demo-c-template" everywhere
#   package-segment  e.g. contract         -> io.github.khkim6040.<segment>
#   dest-dir         default: $PWD/<repo-name>
# Only committed template files are used (git archive HEAD), so commit template edits first.
set -euo pipefail

USAGE="usage: tools/create-project.sh <repo-name> <package-segment> [dest-dir]"
NAME=${1:?$USAGE}
PKG=${2:?$USAGE}
DEST=${3:-$PWD/$NAME}
[[ $NAME =~ ^[a-z0-9-]+$ ]] || { echo "repo-name must match [a-z0-9-]+: $NAME" >&2; exit 1; }
[[ $PKG =~ ^[a-z][a-z0-9]*$ ]] || { echo "package-segment must match [a-z][a-z0-9]*: $PKG" >&2; exit 1; }
[[ -e $DEST ]] && { echo "destination already exists: $DEST" >&2; exit 1; }

PLATFORM=$(cd "$(dirname "$0")/.." && pwd)
SHA=$(git -C "$PLATFORM" rev-parse --short HEAD)

mkdir -p "$DEST"
git -C "$PLATFORM" archive --format=tar HEAD:templates/multi-service | tar -xf - -C "$DEST"

# 1. repo name (settings.gradle.kts, deploy/, README)
grep -rlI 'demo-c-template' "$DEST" | xargs -r perl -pi -e "s/demo-c-template/$NAME/g"
# 2. package: move source directories, then rewrite package/import lines
find "$DEST" -type d -path '*/io/github/khkim6040/sample' | while read -r d; do mv "$d" "$(dirname "$d")/$PKG"; done
grep -rlI 'io\.github\.khkim6040\.sample' "$DEST" | xargs -r perl -pi -e "s/io\\.github\\.khkim6040\\.sample/io.github.khkim6040.$PKG/g"

# self-check: fail loudly if any placeholder survived
if grep -rqIE 'demo-c-template|khkim6040[./]sample' "$DEST"; then
  echo "placeholders left behind:" >&2
  grep -rlIE 'demo-c-template|khkim6040[./]sample' "$DEST" >&2
  exit 1
fi

git -C "$DEST" init -q -b main
git -C "$DEST" add -A
git -C "$DEST" commit -q -m "Generate $NAME from demo-c-platform templates/multi-service@$SHA"
echo "generated $DEST (package io.github.khkim6040.$PKG, platform $SHA)"
echo "next: cd $DEST && git remote add origin https://github.com/khkim6040/$NAME.git && git push -u origin main"
