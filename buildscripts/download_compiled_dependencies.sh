mkdir -p ext/include
mkdir -p ext/lib
mkdir -p ext/bin

git clone https://github.com/MorleyDev/Emna emna_source --depth 1
mkdir -p emna_build
pushd emna_build
	../emna_source/buildscripts/download_header_only_dependencies.sh
	cmake ../emna_source -DCMAKE_BUILD_TYPE=$1 -DDISABLE_SDL=ON -DDISABLE_SFML=ON
	cmake --build .
popd

cp ./emna_source/src/Emna ./ext/include/ -r
cp ./emna_build/src/*.so* ./ext/lib/ -r

rm emna_source -rf
rm emna_build -rf

