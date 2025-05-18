package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;

import java.util.List;
//class used build a railway line between two stations
public class BuildLineController {
    private final Player player;
    private final Map map;

    public BuildLineController(Player player, Map map) {
        this.player = player;
        this.map = map;
    }

    public List<Station> getAvailableStations() {
        return map.getStations();
    }

    public RailLine createRailLine(Station start, Station end, boolean isElectrified, boolean isDouble) {
        RailLine line = new RailLine(start, end, isElectrified, isDouble);
        if (!player.hasSufficientBudget(line.getCost())) {
            return null;
        }
        player.setBudget(player.getBudget() - line.getCost());
        map.addRailLine(line);
        return line;
    }
}
