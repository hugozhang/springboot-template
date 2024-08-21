@echo off
set JAR_FILE_PATH=hmap-web.jar
set LOG_FILE=startup.log

rem 后台运行并输出日志到指定文件 start /B "" "javaw" -jar %JAR_FILE_PATH% > %LOG_FILE% 2>&1

start /B "" "javaw" -Xmx4g -Xms4g -jar %JAR_FILE_PATH%

echo Service started.