# kibu
A modding library for FabricMC, intended to be used on servers.
Kibu provides capabilities useful for usage in [kibu-plugins](https://github.com/LCLPYT/kibu-plugins), 
e.g. event unregistration, which is needed when a plugin is unloaded.

## Gradle Dependency
You can install kibu via Gradle.

To use kibu in your project, modify your `build.gradle`:
```groovy
repositories {
    mavenCentral()
    
    maven {
        url "https://repo.lclpnet.work/repository/internal"
    }
}

dependencies {
    implementation 'work.lclpnet.mods.kibu:kibu:0.6.0'  // replace with your version
}
```
All available versions can be found [here](https://repo.lclpnet.work/#artifact/work.lclpnet.mods.kibu/kibu).

## Credits
- Gradle buildscript structure inspired by [fabric](https://github.com/FabricMC/fabric).
