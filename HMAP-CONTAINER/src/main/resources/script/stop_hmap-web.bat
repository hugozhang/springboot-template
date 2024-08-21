@echo off

rem 获取监听指定端口（例如8080）的进程ID
for /F "tokens=5" %%P in ('netstat -ano ^| findstr :8088') do (
    rem 跳过空白行
    if not "%%P" == "" (
        rem 杀死找到的进程
        taskkill /F /PID %%P
        echo Process with PID %%P listening on port 8088 has been terminated.
    )
)

echo All instances of processes listening on port 8088 have been stopped or were not running.
pause