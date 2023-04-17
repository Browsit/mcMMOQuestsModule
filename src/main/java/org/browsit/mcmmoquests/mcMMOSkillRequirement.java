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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.util.player.UserManager;

import me.blackvein.quests.CustomRequirement;

public class mcMMOSkillRequirement extends CustomRequirement {
	
	public mcMMOSkillRequirement() {
		setName("mcMMO Overhaul Skill Requirement");
		setAuthor("Browsit, LLC");
		setItem("DIAMOND_SWORD", (short)0);
		addStringPrompt("Skill Type", "- Available Skill Types -"
				+ mcMMOModule.getSuggestions(), "ANY");
		addStringPrompt("Skill Amount", "Enter the quantity of skill levels to need", "1");
	}

	@Override
	public String getModuleName() {
		return mcMMOModule.getModuleName();
	}

	@Override
	public Map.Entry<String, Short> getModuleItem() {
		return mcMMOModule.getModuleItem();
	}
	
	@Override
	public boolean testRequirement(final Player player, final Map<String, Object> data) {
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
				return false;
			}
			if (skillType.equalsIgnoreCase("ANY")) {
				for (final PrimarySkillType pst : PrimarySkillType.values()) {
					if (p.getProfile().getSkillLevel(pst) >= skillLevels) {
						return true;
					}
				}
			} else {
				if (mcMMO.p.getSkillTools().matchSkill(skillType) == null) {
					Bukkit.getLogger().severe("[mcMMO Overhaul Quests Module] Invalid skill type " + skillType);
					return false;
				}
				return p.getProfile().getSkillLevel(mcMMO.p.getSkillTools().matchSkill(skillType)) >= skillLevels;
			}
		}
		return false;
	}
}