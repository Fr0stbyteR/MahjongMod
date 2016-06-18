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
echo     "parent": ^"mahjong:block/mj%type%%i%^">>%name%
echo }>> %name%

:end
