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
echo     "parent": ^"mahjong:block/mj%type%%i%^",>>%name%
echo     "textures": {>>%name%
echo         "layer0": "mahjong:blocks/mj%type%%i%">>%name%
echo     },>>%name%
echo     "display": {>>%name%
echo         "thirdperson": {>>%name%
echo             "rotation": [ -90, 0, 0 ],>>%name%
echo             "translation": [ 0, 1, -3 ],>>%name%
echo             "scale": [ 0.55, 0.55, 0.55 ]>>%name%
echo         },>>%name%>>%name%
echo         "firstperson": {>>%name%
echo             "rotation": [ 0, -135, 25 ],>>%name%
echo             "translation": [ 0, 4, 2 ],>>%name%
echo             "scale": [ 1.7, 1.7, 1.7 ]>>%name%
echo         }>>%name%>>%name%
echo     }>>%name%
echo }>> %name%

:end
