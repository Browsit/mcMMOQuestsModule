package org.browsit.mcmmoquests;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.mcMMO;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class mcMMOXpGainObjective extends CustomObjective implements Listener {

    public mcMMOXpGainObjective() {
        setName("mcMMO Overhaul XP Gain Objective");
        setAuthor("Browsit, LLC");
        setItem("EXP_BOTTLE", (short)0);
        setShowCount(true);
        addStringPrompt("MMO XP Obj", "Set a name for the objective", "Gain skill XP");
        addStringPrompt("MMO XP Types", "- Available Skill Types -"
                + mcMMOModule.getSuggestions(), "ANY");
        setCountPrompt("Set the amount of XP to gain");
        setDisplay("%MMO XP Obj% in %MMO XP Types%: %count%");
    }

    @Override
    public String getModuleName() {
        return mcMMOModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return mcMMOModule.getModuleItem();
    }

    @EventHandler
    public void onPlayerXpGain(final McMMOPlayerXpGainEvent event) {
        if (mcMMOModule.getQuests() == null) {
            return;
        }
        final Quester quester = mcMMOModule.getQuests().getQuester(event.getPlayer().getUniqueId());
        if (quester == null) {
            return;
        }
        for (final Quest q : quester.getCurrentQuests().keySet()) {
            final Player p = quester.getPlayer();
            final Map<String, Object> dataMap = getDataForPlayer(p, this, q);
            if (dataMap != null) {
                final String skillNames = (String)dataMap.getOrDefault("MMO XP Types", "ANY");
                if (skillNames == null) {
                    return;
                }
                final String[] spl = skillNames.split(",");
                for (final String str : spl) {
                    if (str.equals("ANY") || (mcMMO.p.getSkillTools().matchSkill(str) != null
                            && mcMMO.p.getSkillTools().matchSkill(str).equals(event.getSkill()))) {
                        incrementObjective(p, this, 1, q);
                        break;
                    }
                }
            }
        }
    }
}
