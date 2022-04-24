/*
 * Copyright (c) 2021 Browsit, LLC. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.browsit.mcmmoquests;

import java.util.AbstractMap;
import java.util.Map;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.util.skills.SkillTools;
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
        setAuthor("Browsit, LLC");
        setItem("DIAMOND_SWORD", (short)1562);
        setDisplay("%Skill Amount% %Skill Type% Skill Level(s)");
        addStringPrompt("Skill Type", "Name of the skill type", "ANY");
        addStringPrompt("Skill Amount", "Enter the quantity of skill levels to give", "1");
    }

    @Override
	public String getModuleName() {
		return "mcMMO Overhaul Quests Module";
	}

	@Override
	public Map.Entry<String, Short> getModuleItem() {
		return new AbstractMap.SimpleEntry<>("DIAMOND_SWORD", (short)1562);
	}
	
	@Override
	public void giveReward(final Player player, final Map<String, Object> data) {
		if (data != null) {
			if (!UserManager.hasPlayerDataKey(player)) {
				UserManager.track(new McMMOPlayer(player, new PlayerProfile(player.getName(), player.getUniqueId(),
						0)));
			}
			final String skillType = (String)data.getOrDefault("Skill Type", "ANY");
			int skillLevels = 1;
			try {
				skillLevels = Integer.parseInt((String)data.getOrDefault("Skill Amount", "1"));
			} catch (final NumberFormatException e) {
				// Default to 1
			}
			final McMMOPlayer p = UserManager.getPlayer(player);
			if (p == null) {
				return;
			}
			if (skillType.equalsIgnoreCase("ANY")) {
				mcMMO.p.getSkillTools();
				for (final PrimarySkillType pst : SkillTools.NON_CHILD_SKILLS) {
					p.getProfile().addLevels(pst, skillLevels);
				}
			} else {
				if (skillType.equalsIgnoreCase("SALVAGE") || skillType.equalsIgnoreCase("SMELTING")) {
					Bukkit.getLogger().severe("[mcMMO Overhaul Quests Module] Cannot add levels to child skill");
				} else {
					p.getProfile().addLevels(mcMMO.p.getSkillTools().matchSkill(skillType), skillLevels);
				}
			}
		}
	}
}