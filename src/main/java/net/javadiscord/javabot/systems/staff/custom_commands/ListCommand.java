package net.javadiscord.javabot.systems.staff.custom_commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.javadiscord.javabot.Bot;
import net.javadiscord.javabot.command.Responses;
import net.javadiscord.javabot.command.SlashCommandHandler;
import net.javadiscord.javabot.systems.staff.custom_commands.dao.CustomCommandRepository;

import java.sql.SQLException;

/**
 * Command that lists Custom Slash Commands.
 */
public class ListCommand implements SlashCommandHandler {

	@Override
	public ReplyAction handle(SlashCommandEvent event) {
		try (var con = Bot.dataSource.getConnection()) {
			var commands = new CustomCommandRepository(con).getCustomCommandsByGuildId(event.getGuild().getIdLong());
			StringBuilder sb = new StringBuilder();
			for (var c : commands) sb.append("/").append(c.getName()).append("\n");
			return Responses.success(event,
					"Custom Slash Command List",
					String.format("```\n%s\n```", (sb.length() > 0 ? sb : "No Custom Commands created yet."))
			);
		} catch (SQLException e) {
			e.printStackTrace();
			return Responses.error(event, "An Error occurred.");
		}
	}
}
