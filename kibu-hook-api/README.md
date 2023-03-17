# kibu-hook-api
Hooks are very similar to Events from [fabric](https://github.com/FabricMC/fabric).
The biggest difference is, that hooks may be unregistered again.

Thus, the implementation is strongly inspired by Fabric's events.

## Why the name "hooks"?
In order to prevent confusion with regular Events from Fabric, 
I decided to name them differently, less ambiguous.

## Use kibu-hook-api in your project
In your `build.gradle` build script, add this repository and dependency:
```groovy
repositories {
    maven {
        url "https://repo.lclpnet.work/repository/internal"
    }
}

dependencies {
    modImplementation "work.lclpnet.mods.kibu:kibu-hooks:1.0.0"  // replace version if necessary
}
```
All available versions can be found [here](https://repo.lclpnet.work/#artifact/work.lclpnet.mods.kibu/kibu-hook-api).