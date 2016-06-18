echo  >1.txt
echo  >2.txt
echo  >3.txt
set type=m
set typename=man
for /l %%i in (1,1,9) do call :echo %%i
set type=p
set typename=pin
for /l %%i in (1,1,9) do call :echo %%i
set type=s
set typename=sou
for /l %%i in (1,1,9) do call :echo %%i
set type=h
set typename=hua
for /l %%i in (1,1,8) do call :echo %%i
set type=f
set typename=f
for /l %%i in (1,1,4) do call :echo %%i
set type=d
set typename=d
for /l %%i in (1,1,3) do call :echo %%i
goto :end

:echo
set i=%1
echo     public static Block blockMj%type%%i%;>>0.txt
echo     public static Item itemMj%type%%i%;>>0.txt
echo         blockMj%type%%i% = new BlockMj(Material.clay).setRegistryName(^"mj%type%%i%^").setUnlocalizedName(^"%i%%typename%^");>>1.txt
echo         itemMj%type%%i% = new ItemBlock(blockMj%type%%i%).setRegistryName(^"mj%type%%i%^").setUnlocalizedName(^"%i%%typename%^");>>1.txt
echo         GameRegistry.register(blockMj%type%%i%);>>2.txt
echo         GameRegistry.register(itemMj%type%%i%);>>2.txt
echo         registerRender(blockMj%type%%i%);>>3.txt
echo         registerRender(itemMj%type%%i%);>>3.txt


:end