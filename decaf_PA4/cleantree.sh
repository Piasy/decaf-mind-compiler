#!/bin/bash

set -e

ant clean

if [[ -f src/decaf/frontend/Lexer.l ]]; then
	rm -fv src/decaf/frontend/Lexer.java
fi
if [[ -f src/decaf/frontend/Parser.y ]]; then
	rm -fv src/decaf/frontend/Parser.java
	rm -fv src/decaf/frontend/Parser.output
fi

find -name 'decaf.jar' -print -delete

rm -fv TestCases/*/output/*.result
rm -fv TestCases/*/output/*.tac
rm -fv TestCases/*/output/*.*out
rm -fv TestCases/*/output/*.s

rm -f submit.zip
