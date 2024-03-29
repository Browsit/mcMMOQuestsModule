package org.browsit.mcmmoquests;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.mcMMO;
import me.pikamug.quests.enums.ObjectiveType;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class mcMMOLevelUpObjective extends BukkitCustomObjective implements Listener {

    public mcMMOLevelUpObjective() {
        setName("mcMMO Overhaul Level Up Objective");
        setAuthor("Browsit, LLC");
        setItem("WOOD_STAIRS", (short)0);
        setShowCount(true);
        addStringPrompt("MMO Level Obj", "Set a name for the objective", "Level up skill");
        addStringPrompt("MMO Level Types", "- Available Skill Types -"
                + mcMMOModule.getSuggestions(), "ANY");
        setCountPrompt("Set the number of times to level up");
        setDisplay("%MMO Level Obj% %MMO Level Types%: %count%");
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
    public void onPlayerLevelUp(final McMMOPlayerLevelUpEvent event) {
        if (mcMMOModule.getQuests() == null) {
            return;
        }
        final Quester quester = mcMMOModule.getQuests().getQuester(event.getPlayer().getUniqueId());
        if (quester == null) {
            return;
        }
        for (final Quest quest : quester.getCurrentQuests().keySet()) {
            final Player p = quester.getPlayer();
            final Map<String, Object> dataMap = getDataForPlayer(p.getUniqueId(), this, quest);
            if (dataMap != null) {
                final String skillNames = (String)dataMap.getOrDefault("MMO Level Types", "ANY");
                if (skillNames == null) {
                    return;
                }
                final String[] spl = skillNames.split(",");
                for (final String str : spl) {
                    if (str.equals("ANY") || (mcMMO.p.getSkillTools().matchSkill(str) != null
                            && mcMMO.p.getSkillTools().matchSkill(str).equals(event.getSkill()))) {
                        incrementObjective(p.getUniqueId(), this, quest, 1);

                        quester.dispatchMultiplayerEverything(quest, ObjectiveType.CUSTOM,
                                (final Quester q, final Quest cq) -> {
                                    incrementObjective(q.getUUID(), this, quest, 1);
                                    return null;
                                });
                        break;
                    }
                }
            }
        }
    }
}
