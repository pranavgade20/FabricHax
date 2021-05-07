
# FabricHax
A collection of Minecraft life hacks to automate repetitive stuff and make your life easier.

## Installation
Go to https://github.com/pranavgade20/FabricHax/releases and grab the latest jar asset. Drop it into a folder named mods in your default minecraft directory. You need to install https://fabricmc.net/ before this.  
You also need to get https://www.curseforge.com/minecraft/mc-mods/fabric-api in the mods folder.

## Usage
The mod has few modules active by default. You can access the full list of toggles using the keys `p+r`.  
The GUI also has tabs for configuring the modules.

## Included modules
* **Auto Hotbar**: Scrolls the correct tool in your hotbar when you try to mine a block.
* **Auto Sneak**: Stops you from falling over the edge. Especially useful while bridging(Shortcut `p+l`)
* **Scaffold**: Automatically places blocks you are holding below you wherever you walk.(Shortcut `p+o`)
* **Spawn Proofer**: Places blocks(like slabs or buttons) to prevent monster spawning. Just hold the blocks in your hand and move to where you want to place them(Shortcut `p+r`)
* **Farm Planter**: Places seeds on farmland. Just hold the seeds in your hand and go near farmland
* **Harvester**: Harvests mature crops near you. Just go near a mature crop to use
* **Digger**: Dig lots of blocks fast! (You can dig upto 50 blocks with one click) You just need a tool that can insta-mine the block(Shortcut `p+i`)
* **Fast Miner**: Mine blocks 30% faster! This allows you to insta-mine stone without haste effect.
* **AntiInvisibility**: Reveals entities/players having invisibility effect(Shortcut `p+u`)
* **Jesus**: Walk on water and lava(Shortcut `p+j`)
* **Freecam**: Spectator mode to look at your awesome builds!(Shortcut `p+z`)
* **Builder**: Place multiple blocks at once(Shortcut `p+b`)
* **Antifall**: Ignore fall damage(Shortcut: `p+y`)
* **Anti Fluid**: Replace the fluids around you with blocks in your hand(Shortcut: `p+g`)
* **Walker**: Walk in a straight line, useful on nether roof.(Shortcut: `p+w`)
* **Criticals**: Increases the damage you inflict by 50%(Shortcut: `p+c`)
* **Effects**: Gives you potion effects like speed, jump boost, etc
* **Fly**: Creative mode-like flight, even in survival.(Shortcut: `p+k`)
* **ElytraFly**: Same as Fly, but much more stable. You need an elytra to use this.(Shortcut: `p+h`)
* **Fullbright**: Disables darkness/makes everything completely bright.
* **Better Fluids**: Improves the way fluids(lava/water) are rendered.
* **No Fog**: Disable fog effect(including under water and lava)

P.S.: You can toggle the modules without a shortcut in setting(access using `p+r`)

## Development
### Setup
Download this project source, or `git clone https://github.com/pranavgade20/FabricHax.git`  
Navigate to the source directory  
To build the project jars, run `gradlew build`  
For detailed IDE-specific instructions please see [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup)

### Usage
Use `gradlew runClient` to launch a test environment.
To use it with your minecraft installation, copy the jar generated by `gradlew build` to .minecraft/mods/, with the  [fabric api](https://www.curseforge.com/minecraft/mc-mods/fabric-api) .

### TODO
This project is actively under development, we appreciate every bit of help :)

* Testers 
	* Don't know how to code? We still need testers! All you need to do is follow the installation and usage above. If you find bugs, please create an incident, and include ALL of the following:
		* Your computer's Operating System, and version
		* Java version (open a terminal and type `java -v`)
		* What version of MineCraft you are running
		* What you tried that caused an error
		* Any other info that can up pinpoint the error

* Coders
	* Good with Java/want to learn?
		* Minecraft mods are all the rage, help us by making new, clever modules!
		* Help format, document, comment, and reorganize code.
	* Groovy programmer?
		* There are more gradle commands we could implement, try adding new gradle commands, comment on current ones, and help new users get up to speed faster!
	* GitHub Actions?
		* If you know how to build GitHub Actions pipelines, we could use some help building a pipeline that creates a new release every time the file gradle.properties is changed(if it is even possible)

	* Others
		* We are constantly looking for new ideas to add to this repository. If you think of a module that could be useful, [create an issue](https://github.com/pranavgade20/FabricHax/issues/new). Thoroughly describe what the module should do, and why it could be helpful.

#### P.S.
This will not give you a "hacked" client. This project simply for automation and making repetitive tasks easier. This project does not, and never will, include any traditional hacks that, for example, make you better at pvp etc.
