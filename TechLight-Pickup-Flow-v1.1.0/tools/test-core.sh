#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$ROOT/build/core-tests"
mkdir -p "$OUT/lib" "$OUT/classes"
BASE=https://repo.maven.apache.org/maven2
fetch() { [ -s "$OUT/lib/$2" ] || curl --fail --location --retry 2 "$BASE/$1" -o "$OUT/lib/$2"; }
fetch junit/junit/4.13.2/junit-4.13.2.jar junit.jar
fetch org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar hamcrest.jar
CP="$OUT/lib/junit.jar:$OUT/lib/hamcrest.jar"
S="$ROOT/app/src/main/java/sa/techlight/kitchen"
javac -encoding UTF-8 -source 8 -target 8 -cp "$CP" -d "$OUT/classes" "$S/LinkCrypto.java" "$ROOT/tests/PickupLinkCryptoTests.java"
java -cp "$OUT/classes:$CP" org.junit.runner.JUnitCore sa.techlight.kitchen.PickupLinkCryptoTests
