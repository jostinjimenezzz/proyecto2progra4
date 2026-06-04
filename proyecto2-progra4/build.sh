#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR/bolsaempleo-fe"
npm install
npm run build

cd "$ROOT_DIR/bolsaempleo-be"
rm -rf src/main/resources/static
mkdir -p src/main/resources/static
cp -r ../bolsaempleo-fe/dist/* src/main/resources/static/
mvn clean package -DskipTests
