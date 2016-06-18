@echo off
set type=m
for /l %%i in (1,1,9) do call :set %%i
set type=p
for /l %%i in (1,1,9) do call :set %%i
set type=s
for /l %%i in (1,1,9) do call :set %%i
set type=h
for /l %%i in (1,1,8) do call :set %%i
set type=f
for /l %%i in (1,1,4) do call :set %%i
set type=d
for /l %%i in (1,1,3) do call :set %%i
goto :end

:set
set i=%1
set name=mj%type%%i%.json
echo %i%
echo {>%name%
echo   "variants": {>>%name%
echo     "facing=northd": {"model": "mahjong:mahjong_down", "y": 180},>>%name%
echo     "facing=southd": {"model": "mahjong:mahjong_down"},>>%name%
echo     "facing=eastd": {"model": "mahjong:mahjong_down", "y": 270},>>%name%
echo     "facing=westd": {"model": "mahjong:mahjong_down", "y": 90},>>%name%
echo     "facing=northu": {"model": ^"mahjong:mj%type%%i%^", "y": 180},>>%name%
echo     "facing=southu": {"model": ^"mahjong:mj%type%%i%^"},>>%name%
echo     "facing=eastu": {"model": ^"mahjong:mj%type%%i%^", "y": 270},>>%name%
echo     "facing=westu": {"model": ^"mahjong:mj%type%%i%^", "y": 90},>>%name%
echo     "facing=north": {"model": ^"mahjong:mj%type%%i%^", "x": 270, "y": 180},>>%name%
echo     "facing=south": {"model": ^"mahjong:mj%type%%i%^", "x": 270},>>%name%
echo     "facing=east": {"model": ^"mahjong:mj%type%%i%^", "x": 270, "y": 270},>>%name%
echo     "facing=west": {"model": ^"mahjong:mj%type%%i%^", "x": 270, "y": 90}>>%name%
echo   }>> %name%
echo }>> %name%
:end