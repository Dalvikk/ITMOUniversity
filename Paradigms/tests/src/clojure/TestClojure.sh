#!/bin/bash
set -euo pipefail

if [[ -z "$2" ]] ; then
    echo Usage: $(basename "$0") [test class] [variant]
    exit 1
fi

CLASS="$1"
ARGS="$2 ${3-}"

OUT=__OUT
CLOJURE="$(dirname "$0")"
REPO="$CLOJURE/.."
LIB="$CLOJURE/lib/*"

javac \
    -d "$OUT" \
    "--class-path=$LIB:$REPO/java:$REPO/javascript:$REPO/clojure" \
    "$CLOJURE/${CLASS//\./\/}.java" \
 && java -ea "--class-path=$LIB:$OUT" "$CLASS" $ARGS
