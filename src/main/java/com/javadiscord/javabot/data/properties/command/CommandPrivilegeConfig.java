package com.javadiscord.javabot.data.properties.command;

import com.javadiscord.javabot.data.properties.config.BotConfig;
import com.javadiscord.javabot.data.properties.config.UnknownPropertyException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

@Data
@Slf4j
public class CommandPrivilegeConfig {
	private String type;
	private boolean enabled = true;
	private String id;

	public CommandPrivilege toData(Guild guild, BotConfig botConfig) {
		if (this.type.equalsIgnoreCase(CommandPrivilege.Type.USER.name())) {
			Member member = guild.getMemberById(id);
			if (member == null) throw new IllegalArgumentException("Member could not be found for id " + id);
			return new CommandPrivilege(CommandPrivilege.Type.USER, this.enabled, member.getIdLong());
		} else if (this.type.equalsIgnoreCase(CommandPrivilege.Type.ROLE.name())) {
			Long roleId = null;
			try {
				roleId = (Long) botConfig.get(guild).resolve(this.id);
			} catch (UnknownPropertyException e) {
				log.error("Unknown property while resolving role id.", e);
			}
			if (roleId == null) throw new IllegalArgumentException("Missing role id.");
			Role role = guild.getRoleById(roleId);
			if (role == null) throw new IllegalArgumentException("Role could not be found for id " + roleId);
			return new CommandPrivilege(CommandPrivilege.Type.ROLE, this.enabled, role.getIdLong());
		}
		throw new IllegalArgumentException("Invalid type.");
	}
}