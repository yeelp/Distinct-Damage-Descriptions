![](images/DDDBanner.png)

# [Distinct Damage Descriptions](https://www.curseforge.com/minecraft/mc-mods/distinct-damage-descriptions)
[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/X8X5G4LPG)
[![Patreon](https://i.imgur.com/JkRflNx.png)](https://www.patreon.com/join/Yeelp)

(Supporting me on Kofi or Patreon helps me develop mods full time!)

[![](https://img.shields.io/modrinth/dt/jU1Kqlqz?style=flat&logo=modrinth&label=Modrinth%20Downloads&color=00af5c)](https://modrinth.com/mod/distinct-damage-descriptions) 
[![](https://img.shields.io/curseforge/dt/403617?style=flat&logo=curseforge&label=CurseForge%20Downloads&color=f16436)](https://www.curseforge.com/minecraft/mc-mods/distinct-damage-descriptions) 
[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/yeelp/Distinct-Damage-Descriptions/total?style=flat&logo=github&label=GitHub%20Downloads&color=white)](https://github.com/yeelp/Distinct-Damage-Descriptions/releases) 
[![Discord](https://img.shields.io/discord/750481601107853373?style=flat&logo=discord&logoColor=white&color=5662f6)](https://discord.gg/hwzWdXQ)
![](https://img.shields.io/github/v/release/yeelp/Distinct-Damage-Descriptions?include_prereleases)
[![](https://img.shields.io/github/issues/yeelp/Distinct-Damage-Descriptions)](https://github.com/yeelp/Distinct-Damage-Descriptions/issues) 

![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/jU1Kqlqz?style=flat&logo=modrinth&label=Available%20For&color=00af5c) ![CurseForge Game Versions](https://img.shields.io/curseforge/game-versions/403617?style=flat&logo=curseforge&label=Available%20For&color=f16436)


Adding physical D&amp;D-style damage types to Minecraft to deepen the combat!

If you'd like to learn more about Distinct Damage Descriptions (shortened to DDD), check out the [Modrinth](https://modrinth.com/mod/distinct-damage-descriptions) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/distinct-damage-descriptions) descriptions! This README is to document building and contributing

## Using Gradle with Distinct Damage Descriptions
This section assumes the following:
- You know how to set up a Forge MDK for the correct Minecraft version.
- You have the correct Java version installed and your `JAVA_HOME` system environment variable and points to an approriate JDK installation and is correctly prioritized in your `PATH` environment variable. (Note that from 1.12 until 1.18, Minecraft ran on Java 8.)
- You have your IDE of choice at hand.

Using Gradle with Distinct Damage Descriptions is the same as building with pretty much any other mod.

### Cloning
If you're more comfortable using something like Git GUI to clone a repo, then fair enough, you probably know what you're doing. 👍

Otherwise, create a directory for DDD, and clone the repository with
```
git clone https://github.com/yeelp/Distinct-Damage-Descriptions.git
```
then finish any additional Forge MDK setup that may be required.

### Building
Building is easy. First, navigate to where you cloned DDD with any shell and execute
```
gradlew build
```
from inside that directory. If successful, this will output `.jar` files to `./build/libs`. One is the actual jar, and the second, which will have `-sources` appended to the file name, is the source of what you just built.

If you'd rather not use the Gradle wrapper for whatever reasons, then you also probably already know what you're doing. 😉

## Adding Distinct Damage Descriptions as a dependency
If you want DDD as a dependency for a mod you're developing, you can use the Curse Maven plugin to add it as a dependency. In your `build.gradle`, under `dependencies`, use the following in your Gradle instruction:
```gradle
curse.maven:distinct-damage-descriptions-403617:<versionslug>
```
The exact Gradle instruction depends on the version of Gradle and ForgeGradle you are using and `<versionslug>` is the slug for the version you want as a dependency. Typically, you'd always want the latest version. This will add that version of DDD as dependency (the kind of dependency. For a required dependency, make sure that in your mod's `@Mod` annotation, you add the DDD version in the `dependencies` field. To have DDD v1.5.1 be a *required* dependency, you add `"required-after:distinctdamagedescriptions[1.5.1,)"` to the `dependencies` field. (This actually adds DDD as a required dependency that requires version v1.5.1 *or greater*.)

### Finding the version slug
To find the version slug, open up the CurseForge page for DDD and go to the Files tab. By default, the files should be sorted with the newest (and therefore latest version) at the top. Click the file corresponding to the version you want as a dependency. Then, if you check the URL of the page, you'll see the version slug at the end. For example, for DDD v1.5.1, navigating to its page on CurseForge will show the following URL in the address bar:
```
https://www.curseforge.com/minecraft/mc-mods/distinct-damage-descriptions/files/5027467
```
The numbers at the end is the version slug. So, `5027467` is the version slug for v1.5.1.

Bringing it all together, the dependency in your `build.gradle` script's `dependencies` section for DDD v1.5.1 would look like
```gradle
curse.maven:distinct-damage-descriptions-403617:5027467
```
Depending on your version of Gradle and ForgeGradle, the dependency instruction this is contained within will look different.

Check DDD's [contributing guidelines](https://github.com/yeelp/Distinct-Damage-Descriptions/blob/master/CONTRIBUTING.md) for specific contributing information.
