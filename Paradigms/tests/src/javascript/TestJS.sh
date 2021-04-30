#!/bin/bash
set -euo pipefail

if [[ -z "$2" ]] ; then
    echo Usage: $(basename "$0") [test class] [variant]
    exit 1
fi

CLASS="$1"
VARIANT="$2"

OUT=__out
JS="$(dirname "$0")"
REPO="$JS/.."

javac \
    -d "$OUT" \
    "--class-path=$REPO/java:$REPO/javascript" \
    "$JS/${CLASS//\./\/}.java" \
  && java -ea "--module-path=$JS/graal" "--class-path=$OUT" "$CLASS" "$VARIANT"
