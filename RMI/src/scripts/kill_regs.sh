#!/usr/bin/env bash
kill `lsof -t -i:22460`
kill `lsof -t -i:22461`