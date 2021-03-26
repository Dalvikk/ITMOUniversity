#!/bin/bash -eu

if [[ -z "$2" ]] ; then
    echo Usage: $(basename "$0") [test class] [variant]
    exit 1
fi

OUT=__out
JS="$(dirname "$0")"
JAVA="$JS/../java"
CLASS="$1"
VARIANT="$2"

javac -d "$OUT" "--class-path=$JS:$JAVA" "$JS/${CLASS//\./\/}.java" \
  && java -ea "--module-path=$JS/graal" "--class-path=$OUT" "$CLASS" "$VARIANT"
