# blindcat

originally made by me and @unleg1t like a month ago, i js decomped one of the jars and cleaned it

nocheat fork, with ncp skidded checks and bypasses ported from old hypixel clients (like around 80 checked)

## Dependencies

- java 8
- spigot
- protocollib

build w/ maven

## Commands

`/nocheat <subcommand>` is permission `nocheat.admin.commands`

| subcommand | desc |
|---|---|
| `permlist <player> [permission]` | list player perms permissions |
| `playerinfo <player>` | show data of player |
| `reload` | reload config (needs perm) |

## Permissions

| node | grants |
|---|---|
| `nocheat` | bypass all checks + all admin rights |
| `nocheat.admin` | all admin rights |
| `nocheat.admin.chatlog` | see log messages in chat |
| `nocheat.admin.commands` | use nocheat commands |
| `nocheat.admin.reload` | use `/nocheat reload` |
| `nocheat.checks` | bypass all checks |
| `nocheat.checks.<group>` | bypass check group |
| `nocheat.checks.<group>.<check>` | bypass single check |

groups: `combined`, `moving`, `blockbreak`, `blockplace`, `chat`, `fight`, `net`, `inventory`.
check plugin.yml


credits:

anticheat: Evenprime, sunsuke, unleg1t, asofold, Araykal

clients:
cedo (my goat)
billionare + alan wood
sigma devs
vanta (idk which one)
and like 20 more clients and client devs

old screenshot here (there was more later but still some of them):
https://media.discordapp.net/attachments/1552105171499360368/1556122292491395112/image.png?backend=b2&ex=6ae1fe91&is=6ae0ad11&hm=68f50cedf30be6e2043e0758ca8064dc9575da0a92b41c61bcad55f2f6536d77&=&format=webp&quality=lossless