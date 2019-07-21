package me.pikamug.mcMMOQuests;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.util.player.UserManager;

import me.blackvein.quests.CustomReward;

public class mcMMOSkillReward extends CustomReward {

	public mcMMOSkillReward() {
        setName("mcMMO Overhaul Skill Reward");
        setAuthor("PikaMug");
        setRewardName("%Skill Amount% %Skill Type% Skill Level(s)");
        addStringPrompt("Skill Type", "Name of the skill type", "ANY");
        addStringPrompt("Skill Amount", "Enter the quantity of skill levels to give", "1");
    }
	
	@Override
	public void giveReward(Player player, Map<String, Object> data) {
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
				for (PrimarySkillType pst : PrimarySkillType.NON_CHILD_SKILLS) {
					UserManager.getPlayer(player).getProfile().addLevels(pst, skillLevels);
				}
			} else {
				if (skillType.equalsIgnoreCase("SALVAGE") || skillType.equalsIgnoreCase("SMELTING")) {
					Bukkit.getLogger().severe("[mcMMO-OverhaulQuestsModule] Cannot add levels to child skill");
				} else {
					UserManager.getPlayer(player.getName()).getProfile().addLevels(PrimarySkillType.getSkill(skillType), skillLevels);
				}
			}
		}
	}
}