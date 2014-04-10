#!/bin/bash

mkdir -p ext
mkdir -p ext/include
mkdir -p ext/lib
mkdir -p ext/bin

git clone https://github.com/joakimkarlsson/bandit bandit_tmp
cp bandit_tmp/bandit ext/include/ -r
rm bandit_tmp -rf
