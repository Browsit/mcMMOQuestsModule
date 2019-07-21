package me.pikamug.mcMMO_OverhaulQuests;

import java.util.Map;

import org.bukkit.entity.Player;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.util.player.UserManager;

import me.blackvein.quests.CustomRequirement;

public class mcMMOSkillRequirement extends CustomRequirement {
	
	public mcMMOSkillRequirement() {
		setName("mcMMO Overhaul Skill Requirement");
		setAuthor("PikaMug");
		addStringPrompt("Skill Type", "Name of the skill type", "ANY");
		addStringPrompt("Skill Amount", "Enter the quantity of skill levels to need", "1");
	}
	
	@Override
	public boolean testRequirement(Player player, Map<String, Object> data) {
		if (data != null) {
			if (!UserManager.hasPlayerDataKey(player)) {
				UserManager.track(new McMMOPlayer(player, new PlayerProfile(player.getName(), player.getUniqueId())));
			}
			String skillType = (String)data.getOrDefault("Skill Type", "ANY");
			int skillLevels = 1;
			try {
				skillLevels = Integer.parseInt((String)data.getOrDefault("Skill Amount", "1"));
			} catch (NumberFormatException e) {
				// Default to 1
			}
			if (skillType.equalsIgnoreCase("ANY")) {
				for (PrimarySkillType pst : PrimarySkillType.values()) {
					if (UserManager.getPlayer(player).getProfile().getSkillLevel(pst) >= skillLevels) {
						return true;
					}
				}
			} else {
				if (UserManager.getPlayer(player.getName()).getProfile().getSkillLevel(PrimarySkillType.getSkill(skillType)) >= skillLevels) {
					return true;
				}
			}
		}
		return false;
	}
}