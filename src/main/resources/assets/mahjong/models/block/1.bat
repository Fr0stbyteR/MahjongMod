for /l %%i in (1,1,9) do (
echo {>mjm%%i.json
echo     "parent":  "mahjong:block/mahjong_all",>>mjm%%i.json
echo     "textures": {>>mjm%%i.json
echo         ^"mjface": "mahjong:blocks/mjm%%i^">>mjm%%i.json
echo     }>>mjm%%i.json
echo } >> mjm%%i.json
)
for /l %%i in (1,1,9) do (
echo {>mjp%%i.json
echo     "parent":  "mahjong:block/mahjong_all",>>mjp%%i.json
echo     "textures": {>>mjp%%i.json
echo         ^"mjface": "mahjong:blocks/mjp%%i^">>mjp%%i.json
echo     }>>mjp%%i.json
echo } >> mjp%%i.json
)
for /l %%i in (1,1,9) do (
echo {>mjs%%i.json
echo     "parent":  "mahjong:block/mahjong_all",>>mjs%%i.json
echo     "textures": {>>mjs%%i.json
echo         ^"mjface": "mahjong:blocks/mjs%%i^">>mjs%%i.json
echo     }>>mjs%%i.json
echo } >> mjs%%i.json
)
for /l %%i in (1,1,4) do (
echo {>mjf%%i.json
echo     "parent":  "mahjong:block/mahjong_all",>>mjf%%i.json
echo     "textures": {>>mjf%%i.json
echo         ^"mjface": "mahjong:blocks/mjf%%i^">>mjf%%i.json
echo     }>>mjf%%i.json
echo } >> mjf%%i.json
)
for /l %%i in (1,1,3) do (
echo {>mjd%%i.json
echo     "parent":  "mahjong:block/mahjong_all",>>mjd%%i.json
echo     "textures": {>>mjd%%i.json
echo         ^"mjface": "mahjong:blocks/mjd%%i^">>mjd%%i.json
echo     }>>mjd%%i.json
echo } >> mjd%%i.json
)
for /l %%i in (1,1,8) do (
echo {>mjh%%i.json
echo     "parent":  "mahjong:block/mahjong_all",>>mjh%%i.json
echo     "textures": {>>mjh%%i.json
echo         ^"mjface": "mahjong:blocks/mjh%%i^">>mjh%%i.json
echo     }>>mjh%%i.json
echo } >> mjh%%i.json
)