# Ender Crop Development

## Upgrading Gradle

1. Update the version in `distributionUrl` in [gradle-wrapper.properties](gradle/wrapper/gradle-wrapper.properties)
2. Run `gradle wrapper`

## Update Minecraft/NeoForge

1. In [gradle.properties](gradle.properties):
   1. Update `minecraft_version`
   2. Update `neoforged_version`, prefer a stable version, unless a dependency requires newer
   3. Update `mapping_version`, taking the most recently available release version for the new Minecraft version
      (https://ldtteam.jfrog.io/ui/native/parchmentmc-public/org/parchmentmc/data)
2. Refresh Gradle
