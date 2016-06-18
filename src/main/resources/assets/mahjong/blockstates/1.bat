set type=m
for /l %%i in (1,1,9) do call :echo %%i
set type=p
for /l %%i in (1,1,9) do call :echo %%i
set type=s
for /l %%i in (1,1,9) do call :echo %%i
set type=h
for /l %%i in (1,1,8) do call :echo %%i
set type=f
for /l %%i in (1,1,4) do call :echo %%i
set type=d
for /l %%i in (1,1,3) do call :echo %%i
goto :end

:echo
set i=%1
set name=mj%type%%i%.json
echo {>%name%
echo   "variants": {>>%name%
echo     "facing=down": {"model": "mahjong:mahjong_down"},>>%name%
echo     "facing=up": {"model": ^"mahjong:mj%type%%i%^"},>>%name%
echo     "facing=north": {"model": ^"mahjong:mj%type%%i%^", "x": 270, "y": 180},>>%name%
echo     "facing=south": {"model": ^"mahjong:mj%type%%i%^", "x": 270},>>%name%
echo     "facing=east": {"model": ^"mahjong:mj%type%%i%^", "x": 270, "y": 270},>>%name%
echo     "facing=west": {"model": ^"mahjong:mj%type%%i%^", "x": 270, "y": 90}>>%name%
echo   }>> %name%
echo }>> %name%

:end