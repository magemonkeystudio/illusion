Illusion (Formerly Holographic Displays)
===================

Bukkit Dev Page: http://dev.bukkit.org/bukkit-plugins/holographic-displays

API tutorial: https://github.com/filoghost/HolographicDisplays/wiki

Development Builds: https://ci.codemc.io/job/filoghost/job/HolographicDisplays

Updated links and renamed wikis will come shortly

## Maven

We currently do not have Illusion set up on a maven repository. When it's available there, it will be
available with the following information. For now, you can build the plugin and add it to your local
maven repository.

```xml
<repository>
    <id>sonatype</id>
    <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

```xml
<dependency>
    <groupId>studio.magemonkey</groupId>
    <artifactId>illusion-api</artifactId>
    <version>3.0.5-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

## Gradle
```groovy
maven {
  url "https://s01.oss.sonatype.org/content/repositories/snapshots"
}
```

```groovy
compileOnly 'studio.magemonkey:illusion-api:3.0.5'
```

## License
Illusion is free software/open source, and is distributed under the [GPL 3.0 License](https://opensource.org/licenses/GPL-3.0). It contains third-party code, see the included THIRD-PARTY.txt file for the license information on third-party code.
