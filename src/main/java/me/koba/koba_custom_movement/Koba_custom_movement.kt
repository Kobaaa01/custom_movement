package me.koba.koba_custom_movement

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.BlockFace
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Koba_custom_movement : JavaPlugin(), Listener, CommandExecutor
{
    private var index: Int = 0
    private val message: String = "Movement Effect: ON"
    override fun onEnable()
    {
        logger.info("Enabled!")
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable()
    {
        logger.info("Disabled.")
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent)
    {
        val player = event.player
        launchParticleEffect(player)
        sendActionBar(player, message)
        changeGround(player)
    }

    private fun sendActionBar(player: Player, message: String)
    {
        player.sendActionBar(
            Component.text()
                .content(message)
                .color(TextColor.color(255, 255, 85))
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, true)
                .build()
        )
    }

    private fun launchParticleEffect(player: Player)
    {
        val playerLocation = player.location
        playerLocation.y += 2
        val particle = Particle.SOUL_FIRE_FLAME
        player.world.spawnParticle(particle, playerLocation, 10, 0.5, 0.5, 0.5, 0.0)
    }

    private fun changeGround(player: Player)
    {
        val playerLocation = player.location
        val blockBelow = playerLocation.block.getRelative(BlockFace.DOWN)

        if (blockBelow.type != Material.AIR)
        {
            val originalType = blockBelow.type
            val blocks = listOf(
                Material.BLUE_WOOL,
                Material.RED_WOOL,
                Material.CYAN_WOOL,
                Material.YELLOW_WOOL
            )
            blockBelow.type = blocks[index]
            index += 1
            if (index == 4)
            {
                index = 0
            }
            object : BukkitRunnable()
            {
                override fun run()
                {
                    if (blockBelow.type in blocks)
                    {
                        blockBelow.type = originalType
                    }
                }
            }.runTaskLater(this, 100L)
        }
    }

}
