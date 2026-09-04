@echo off
setlocal
for /f "tokens=2 delims=:." %%x in ('chcp') do set _codepage=%%x
chcp 65001>nul
cd C:\Users\takoa\Desktop\modding\InfinityChestReborn\infChestSrc\neoforge\run
C:\Users\takoa\.jdks\graalvm-jdk-21.0.7\bin\java.exe @C:\Users\takoa\Desktop\modding\InfinityChestReborn\infChestSrc\neoforge\build\moddev\serverRunClasspath.txt @C:\Users\takoa\Desktop\modding\InfinityChestReborn\infChestSrc\neoforge\build\moddev\serverRunVmArgs.txt -Dfml.modFolders=infchest_reborn%%%%C:\Users\takoa\Desktop\modding\InfinityChestReborn\infChestSrc\neoforge\build\classes\java\main;infchest_reborn%%%%C:\Users\takoa\Desktop\modding\InfinityChestReborn\infChestSrc\neoforge\build\resources\main net.neoforged.devlaunch.Main @C:\Users\takoa\Desktop\modding\InfinityChestReborn\infChestSrc\neoforge\build\moddev\serverRunProgramArgs.txt
if not ERRORLEVEL 0 (  echo Minecraft failed with exit code %ERRORLEVEL%  pause)
chcp %_codepage%>nul
endlocal