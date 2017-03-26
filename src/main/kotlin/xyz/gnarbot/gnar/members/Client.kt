package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.PrivateChannel
import net.dv8tion.jda.core.entities.VoiceChannel
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.servers.Servlet

/**
 * Bot's wrapper class for JDA's [Member].
 *
 * @see Member
 */
class Client(val servlet: Servlet, private val member: Member) : Member by member {
    val isBotMaster = Bot.admins.contains(id)

    val level: Level
        get() = when {
            // BOT
            isBot -> Level.BOT

            // BOT MASTER
            isBotMaster -> Level.BOT_CREATOR

            // SERVER OWNER
            member == servlet.owner -> Level.SERVER_OWNER

            // BOT COMMANDER
            hasRoleNamed("Bot Commander") -> Level.BOT_COMMANDER

            // DJ
            hasRoleNamed("DJ") -> Level.DJ

            // USERS
            else -> Level.USER
        }

    val voiceChannel: VoiceChannel?
        get() {
            return servlet.voiceChannels.firstOrNull { it.members.contains(member) }
        }

    fun requestPrivateChannel(): PrivateChannel {
        if (!user.hasPrivateChannel()) {
            openPrivateChannel().complete()
        }
        return privateChannel
    }

    /**
     * @return String representation of the member.
     */
    override fun toString(): String {
        return "Client(id=$id, name=\"$name\", guild=\"${servlet.name}\", level=$level)"
    }
}