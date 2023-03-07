# kibu-fabric-hooks
This module provides hooks for fabric-api events.
Hooks support unregistering listeners, which is required for plugin unloading.
Therefore, this mod introduces hooks equivalent to fabric events.

Defined hooks inject a listener into fabric api events, which then calls the hook invoker.
This could maybe be done using mixins / reflection, but for the sake of simplicity and compatibility,
the latter approach has been implemented.

## Credits
- event factories and javadoc from [fabric](https://github.com/FabricMC/fabric)