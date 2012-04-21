BomberCraft Plugin For Minecraft(Bukkit)

Commands:
    /bombercraft create <X> <Y> - creates arena around you (safe creation)
    /bombercraft create X Y force - creates arena around you (destroys blocks forever)
    /bombercraft join <ID> - joins the game (you need to create arena before using this)
    /bombercraft spectate <ID> - teleports you above arena to spectate existing fight
    /bombercraft clear <ID> - removes the arena (arena transformed to air)
    /bombercraft start <ID> - starts the game
    /bombercraft list - list all arenas
    /bombercraft stop <ID> - stop game in arena and kick all players from it
    /bombercraft edit <ID> - enter edition mode for custom arena
    /bombercraft config <ID> - show configuration options of arena
    /bombercraft config <ID> <Option> <Value> - change configuration options of arena
    /bombercraft top - shows stats of all players
    /bombercraft help - list of all availble commands

    /bc XXX - alias for /bombercraft XXX


Permissions:
    bombercraft.admin.create - /bombercraft create
    bombercraft.admin.forcecreate - /bombercraft create X Y force
    bombercraft.admin.clear - /bombercraft clear
    bombercraft.admin.start - /bombercraft start <ID>
    bombercraft.admin.stop - /bombercraft stop
    bombercraft.admin.edit - /bombercraft edit and /bombercraft config
    bombercraft.play.join - /bombercraft join
    bombercraft.play.spectate - /bombercraft spectate
    bombercraft.play.start - /bombercraft start (only for current game after joining)
    bombercraft.play.list - /bombercraft list
    bombercraft.play.top - /bombercraft top
    bombercraft.allowcommand.XXX - allow usage of /XXX command in the arena


Dependances
    Permissions
    iConomy(not supported yet)


Changelog
    Version 0.6
        Fixed edit mode
    Version 0.5
        Commands locked in arena
        Updated edit mode
        Light option in arenas
        Stats system

    Version 0.4
        Custom arenas
        Fence on top of TNT (TNT block way again)
        New bonus item: redstone torch = detonator
        Help menu
        Stop command

    Version 0.3
        Permissions support (tested on permissions 2.7.3)
        MultiArena support
        Higher arenas
        Short command alias (/bc = /bombercraft)

    Version 0.2
        Game starting system (+autostart when 4 players join)
        Fixed TNT explosions bug
        Inventory storage
        Safe arena creation
        Arena regeneration after game
        Simple messages

    Version 0.1
        First release


TODO
    iConomy support
    winner transmision (arena chains/ tournament system)
    single player mode
    better stats system
